package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import game.Bomb;
import game.BonusType;
import game.Entity;
import game.ExplosionType;
import game.GridCoordinates;
import game.MapView;
import game.Player;
import game.TileType;
import game.WorldView;

@objid ("932712f7-e00f-431a-abd5-e4322b7407bd")
public class GameViewer extends JPanel {
    @objid ("12584c94-a9fc-48b1-bdf6-42c3345b8404")
    private BufferedImage world;

    private Sprite[] tiles;
    private Sprite[] bonuses;
    private PlayerSprite[] players;
    private Sprite[] bombs;
    private Sprite[] explosions;
    
    private int cacheTileSize;
    
    boolean showSpawningLocations;
    
    public static final double BOMB_BLINK_INTERVAL = 0.325;
    
    @objid ("e8b05c80-1463-4060-8ffd-82157c92adb5")
    public GameViewer() {
        SpriteFactory factory = SpriteFactory.getInstance();
        tiles = new Sprite[TileType.values().length];
        for (TileType type : TileType.values()) {
            if (type == TileType.Arrow)
                tiles[type.ordinal()] = factory.getOrientedSprite(type.name().toLowerCase());
            else
                tiles[type.ordinal()] = factory.getSprite(type.name().toLowerCase());
        }
        
        bonuses = new Sprite[BonusType.values().length];
        for (BonusType type : BonusType.values()) {
            bonuses[type.ordinal()] = factory.getSprite("bonus" + type.ordinal()); 
        }
        
        players = new PlayerSprite[PlayerColor.values().length];
        for (PlayerColor color : PlayerColor.values())
            players[color.ordinal()] = new PlayerSprite(color);
        
        bombs = new Sprite[2];
        {
            bombs[0] = factory.getSprite("bomb");
            bombs[1]= factory.getSprite("bomb_exploding");
        }
        
        explosions = new Sprite[ExplosionType.values().length];
        for (ExplosionType type : ExplosionType.values()) {
            if (type == ExplosionType.Center)
                explosions[type.ordinal()] = factory.getSprite("explosion_"+type.name().toLowerCase());
            else
                explosions[type.ordinal()] = factory.getOrientedSprite("explosion_"+type.name().toLowerCase());
        }
        
        showSpawningLocations = false;
        
        cacheTileSize = 0;
        
        setFocusable(true);
    }
    
    public Sprite[] getTileSprites() {
        return Arrays.copyOf(tiles, tiles.length);
    }
    
    public void setShowSpawningLocations(boolean showSpawningLocations) {
        this.showSpawningLocations = showSpawningLocations;
    }
    
    @objid ("18e3e04f-dec2-45e2-a3f5-dbabb34447b4")
    public void drawWorld(WorldView worldView) {
        MapView map = worldView.getMap();
        BufferedImage newWorld = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB); //ARGB ?
        Graphics2D g = newWorld.createGraphics();
        drawMap(g, map);
        drawWorld(g, worldView);
        updateDisplay(newWorld);
    }
    
    public void drawMap(MapView map) {
        BufferedImage newWorld = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB); //ARGB ?
        drawMap(newWorld.createGraphics(), map);
        updateDisplay(newWorld);
    }
    
    private void drawMap(Graphics2D g, MapView map) {
        if (cacheTileSize != map.getTileSize())
            updateCaches(map.getTileSize());
        
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
                        image = bonuses[map.getBonusType(gc).ordinal()].getImage(); 
                    } else if (map.getTileType(gc) == TileType.Arrow) {
                        image = ((OrientedSprite)tiles[TileType.Arrow.ordinal()]).getOrientedImage(map.getArrowDirection(gc));
                    } else
                        image = tiles[map.getTileType(gc).ordinal()].getImage();
                    
                    g.drawImage(image, gc.x*map.getTileSize(), gc.y*map.getTileSize(), this);
                    
                    if (map.isExploding(gc)) {
                        if (map.getExplosionType(gc) != ExplosionType.Center)
                            g.drawImage(((OrientedSprite)explosions[map.getExplosionType(gc).ordinal()]).getOrientedImage(map.getExplosionDirection(gc)),
                                        gc.x*map.getTileSize(), gc.y*map.getTileSize(), this);
                        else
                            g.drawImage(explosions[ExplosionType.Center.ordinal()].getImage(),
                                        gc.x*map.getTileSize(), gc.y*map.getTileSize(), this);
                    }
                }
            }
        }
        
        if (showSpawningLocations) {
            g.setColor(Color.blue);
            List<GridCoordinates> spawningLocations = map.getSpawningLocations();
            for (int i = 0; i < spawningLocations.size(); i++) {
                GridCoordinates gc = spawningLocations.get(i);
                g.drawOval(gc.x*map.getTileSize(), gc.y*map.getTileSize(), map.getTileSize(), map.getTileSize());
                
                drawCenteredString(g, String.valueOf(i), (int)map.toCenterX(gc), (int)map.toCenterY(gc));
            }
        }
    }
    
    private void drawWorld(Graphics2D g, WorldView worldView) {
        for (Entity entity : worldView.getEntities()) {
            if (entity instanceof Bomb) { 
                if (((Bomb)entity).getTimeRemaining() % (2*BOMB_BLINK_INTERVAL*worldView.getFps()) >= BOMB_BLINK_INTERVAL*worldView.getFps())
                    g.drawImage(bombs[0].getImage(), (int)entity.getBorderLeft(), (int)entity.getBorderTop(), this);
                else
                    g.drawImage(bombs[1].getImage(), (int)entity.getBorderLeft(), (int)entity.getBorderTop(), this);
            }
        }
        
        final int colorCount = PlayerColor.values().length;
        //Dessine les joueurs en dernier
        for (Entity entity : worldView.getEntities()) {
            if (entity instanceof Player) {
                int color = ((Player)entity).getPlayerID() % colorCount;
                if (entity.getSpeed() == 0.)
                    g.drawImage(players[color].getStandingPlayer(entity.getDirection()),
                               (int)entity.getBorderLeft(), (int)entity.getBorderTop(), null);
                else
                    g.drawImage(players[color].getMovingPlayer(entity.getDirection(), 10*Math.abs(worldView.getTimeRemaining())/worldView.getFps()%2),
                               (int)entity.getBorderLeft(), (int)entity.getBorderTop(), null);
            }
        }
    }

    
    private void updateDisplay(BufferedImage newWorld) {
        if (world == null || (world.getWidth() != newWorld.getWidth() || world.getHeight() != newWorld.getHeight())) {
            world = newWorld;
            revalidate();
            repaint();
        } else {
            world = newWorld;
            repaint();
        }
    }
    
    private void updateCaches(int size) {
        for (Sprite[] sprites : new Sprite[][]{tiles, bonuses, bombs, explosions}) {
            for (Sprite sprite : sprites)
                sprite.setSize(size);
        }
        
        for (PlayerSprite sprite : players)
            sprite.setSize(size);
        
        cacheTileSize = size;
    }
    
    public static void drawCenteredString(Graphics g, String str, int centerX, int centerY) {
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
