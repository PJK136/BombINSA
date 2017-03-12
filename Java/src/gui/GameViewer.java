package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import game.Bomb;
import game.BonusType;
import game.Direction;
import game.Entity;
import game.GridCoordinates;
import game.MapView;
import game.TileType;
import game.WorldView;

@objid ("932712f7-e00f-431a-abd5-e4322b7407bd")
public class GameViewer extends JPanel {
    @objid ("12584c94-a9fc-48b1-bdf6-42c3345b8404")
    private BufferedImage world;

    private ArrayList<BufferedImage> tiles;
    private ArrayList<BufferedImage> bonuses;
    private ArrayList<BufferedImage> players;
    private ArrayList<BufferedImage> bombs;
    
    private int cacheTileSize;
    private Image cacheTiles[];
    private Image cacheBonuses[];
    private Image cacheArrows[];
    private Image cacheBombs[];
    
    boolean showSpawningLocations;
    
    //http://stackoverflow.com/questions/2768054/how-to-get-the-first-non-null-value-in-java
    private static <T> T coalesce(T a, T b) {
        return a == null ? b : a;
    }
    
    @objid ("e8b05c80-1463-4060-8ffd-82157c92adb5")
    public GameViewer() {
        tiles = new ArrayList<BufferedImage>(TileType.values().length);
        for (TileType type : TileType.values()) {
            tiles.add(coalesce(readRessource(type.name().toLowerCase()),
                      getDefaultTileImage(type)));
        }
        
        bonuses = new ArrayList<BufferedImage>(BonusType.values().length);
        for (BonusType type : BonusType.values()) {
            bonuses.add(coalesce(readRessource("bonus" + type.ordinal()), 
                                 stringInSquare(256, 4, "B" + String.valueOf(type.ordinal())))); 
        }
        
        bombs = new ArrayList<BufferedImage>(2);
        {
            bombs.add(coalesce(readRessource("bomb"), stringInSquare(256, 0, "💣")));
            bombs.add(coalesce(readRessource("bomb_exploding"), stringInSquare(256, 0, "💣", Color.red)));
        }
        
        showSpawningLocations = false;
        
        cacheTileSize = 0;
        cacheTiles = new Image[tiles.size()];
        cacheBonuses = new Image[bonuses.size()];
        cacheArrows = new Image[Direction.values().length];
        cacheBombs = new Image[bombs.size()];
        
        setFocusable(true);
    }
    
    private BufferedImage readRessource(String name) {
        try {
            return ImageIO.read(new File("img/" + name + ".png"));
        } catch (IOException e) {
            System.err.println("Can't read : " + "img/" + name + ".png");
            return null;
        }
    }
    public List<BufferedImage> getTileImages() {
        return Collections.unmodifiableList(tiles);
    }

    private Color toColor(TileType type) {
        switch (type) {
        case Arrow:
            return Color.GREEN;
        case Bonus:
            return Color.YELLOW;
        case Breakable:
            return Color.GRAY;
        case Empty:
            return Color.WHITE;
        case Unbreakable:
            return Color.BLACK;
        default:
            return Color.RED;
        }
    }
    
    private BufferedImage stringInSquare(int size, int thickness, String str, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillRect(0, 0, size, size);
        for (int i = 0; i < thickness; i++)
            g.drawRect(i, i, size-i, size-i);
        g.setColor(color);
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, (int) (((size-2*(thickness+1)))*0.8))); 
        drawCenteredString(g, str, size/2, size/2);
        return image;
    }
    
    private BufferedImage stringInSquare(int size, int thickness, String str) {
        return stringInSquare(size, thickness, str, Color.black);
    }
    
    private BufferedImage getDefaultTileImage(TileType type) {
        if (type == TileType.Bonus) {
            return stringInSquare(256, 4, "B?");
        }
        else if (type == TileType.Arrow) {
            return stringInSquare(256, 4, "🡺");
        }
        else {
            BufferedImage tile = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = tile.createGraphics();
            g.setColor(toColor(type));
            g.fillRect(0, 0, 256, 256);
            g.dispose();
            return tile;
        }
    }
    
    public void setShowSpawningLocations(boolean showSpawningLocations) {
        this.showSpawningLocations = showSpawningLocations;
    }
    
    @objid ("18e3e04f-dec2-45e2-a3f5-dbabb34447b4")
    public void drawWorld(WorldView worldView) {
        drawMap(worldView.getMap(), worldView.getEntities());
        //TODO : À enlever ou garder ?
    }
    
    public void drawMap(MapView map) {
        drawMap(map, null);
    }
    
    public void drawMap(MapView map, List<Entity> entities) {
        if (cacheTileSize != map.getTileSize()) {
            //TODO : iterator
            for (int i = 0; i < tiles.size(); i++) {
                cacheTiles[i] = tiles.get(i).getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
            }
            
            for (int i = 0; i < bonuses.size(); i++) {
                cacheBonuses[i] = bonuses.get(i).getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
            }
            
            for (int i = 0; i < cacheArrows.length; i++) {
                // http://stackoverflow.com/questions/2245869/resize-jcomponent-for-file-export/2246484#2246484
                BufferedImage image = tiles.get(TileType.Arrow.ordinal());
                double w = image.getWidth();
                double h = image.getHeight();
                AffineTransform scaleTransform = new AffineTransform();
                // last-in-first-applied: rotate, scale
                scaleTransform.scale(map.getTileSize()/w, map.getTileSize()/h);
                scaleTransform.rotate(-i*Math.PI/2, w/2, h/2);
                AffineTransformOp scaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
                cacheArrows[i] = scaleOp.filter(image, null);
            }
            
            for (int i = 0; i < bombs.size(); i++) {
                cacheBombs[i] = bombs.get(i).getScaledInstance(map.getTileSize(), map.getTileSize(), Image.SCALE_SMOOTH);
            }
            
            cacheTileSize = map.getTileSize();
        }

        BufferedImage newWorld = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB); //ARGB ?
        Graphics2D g = newWorld.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        {
            g.setColor(Color.black);
            g.fillRect(0, 0, map.getWidth(), map.getHeight());
            
            GridCoordinates gc = new GridCoordinates();
            for (gc.x = 0; gc.x < map.getColumnCount(); gc.x++) {
                for (gc.y = 0; gc.y < map.getRowCount(); gc.y++) {
                    Image image;
                    if (map.getTileType(gc) == TileType.Bonus) {
                        image = cacheBonuses[map.getBonusType(gc).ordinal()]; 
                    } else if (map.getTileType(gc) == TileType.Arrow) {
                        image = cacheArrows[map.getArrowDirection(gc).ordinal()];
                    } else
                        image = cacheTiles[map.getTileType(gc).ordinal()];
                    
                    g.drawImage(image, gc.x*map.getTileSize(), gc.y*map.getTileSize(), this);
                }
            }
        }
        
        if (showSpawningLocations) {
            g.setColor(Color.blue);
            List<GridCoordinates> spawningLocations = map.getSpawningLocations();
            for (int i = 0; i < spawningLocations.size(); i++) {
                GridCoordinates gc = spawningLocations.get(i);
                g.drawOval(gc.x*map.getTileSize(), gc.y*map.getTileSize(), map.getTileSize(), map.getTileSize());
                
                drawCenteredString(g, String.valueOf(i), (int)((gc.x+0.5)*map.getTileSize()), (int)((gc.y+0.5)*map.getTileSize()));
            }
        }
        
        if (entities != null) {
            for (Entity entity : entities) {
                if (entity instanceof Bomb) {
                    g.drawImage(cacheBombs[0], (int)(entity.getX()-map.getTileSize()/2), (int)(entity.getY()-map.getTileSize()/2), this);
                }
                else
                    g.fillOval((int)(entity.getX()-map.getTileSize()/2.), (int)(entity.getY()-map.getTileSize()/2.), map.getTileSize(), map.getTileSize());
            }
        }
        
        if (world == null || (world.getWidth() != newWorld.getWidth() || world.getHeight() != newWorld.getHeight())) {
            world = newWorld;
            revalidate();
            repaint();
        } else {
            world = newWorld;
            repaint();
        }
    }
    
    private void drawCenteredString(Graphics g, String str, int centerX, int centerY) {
        int height = g.getFontMetrics().getHeight();
        int width = g.getFontMetrics().stringWidth(str);
        g.drawString(str, centerX-width/2, centerY+height/4);
        
    }

    @objid ("8a85e92f-ba76-4ae7-8d93-ab5ea648949a")
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(world, 0, 0, this);
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (world != null)
            return new Dimension(world.getWidth(), world.getHeight());
        else
            return super.getPreferredSize();
    }

}
