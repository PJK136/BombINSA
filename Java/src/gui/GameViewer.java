package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.Graphics;
import java.awt.image.VolatileImage;
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
import game.World;
import game.WorldView;

@objid ("932712f7-e00f-431a-abd5-e4322b7407bd")
public class GameViewer extends JPanel {
    private GameSettings settings;
    
    @objid ("12584c94-a9fc-48b1-bdf6-42c3345b8404")
    private boolean updatePending;
    private VolatileImage world;
    private VolatileImage wait;
    private VolatileImage draw;

    private Sprite[] tiles;
    private Sprite[] bonuses;
    private PlayerSprite[] players;
    private Sprite[] bombs;
    private Sprite[] explosions;
    
    private int cacheTileSize;
    
    boolean showSpawningLocations;
    
    public static final double BOMB_BLINK_INTERVAL_MIN = 0.1;
    public static final double BOMB_BLINK_INTERVAL_MAX = 0.75;
    public static final double HIT_BLINK_INTERVAL = 0.05;
    
    @objid ("e8b05c80-1463-4060-8ffd-82157c92adb5")
    public GameViewer() {
        settings = GameSettings.getInstance();
        
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
        setDoubleBuffered(true);
    }
    
    public Sprite[] getTileSprites() {
        return Arrays.copyOf(tiles, tiles.length);
    }
    
    public void setShowSpawningLocations(boolean showSpawningLocations) {
        this.showSpawningLocations = showSpawningLocations;
    }
    
    private void updateDrawImage(MapView map) {
        int width = settings.scale(map.getWidth());
        int height = settings.scale(map.getHeight());
        
        if (draw == null || draw.getWidth() != width || draw.getHeight() != height) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            if (gc == null)
                gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            draw = gc.createCompatibleVolatileImage(width, height, Transparency.OPAQUE);
        }
    }
    
    @objid ("18e3e04f-dec2-45e2-a3f5-dbabb34447b4")
    public void drawWorld(WorldView worldView) {
        MapView map = worldView.getMap();
        updateDrawImage(map);
        do {
            Graphics2D g = draw.createGraphics();
            g.setFont(settings.scale(g.getFont()));
            drawMap(g, map);
            drawWorld(g, worldView);
            g.dispose();
        } while (draw.contentsLost());
        updateDisplay();
    }
    
    public void drawMap(MapView map) {
        updateDrawImage(map);
        do {
            Graphics2D g = draw.createGraphics();
            g.setFont(settings.scale(g.getFont()));
            drawMap(g, map);
            g.dispose();
        } while (draw.contentsLost());
        updateDisplay();
    }
    
    private void drawMap(Graphics2D g, MapView map) {
        int size = settings.scale(map.getTileSize());
        if (cacheTileSize != size)
            updateCaches(size);
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        {
            GridCoordinates gc = new GridCoordinates();
            for (gc.x = 0; gc.x < map.getColumnCount(); gc.x++) {
                for (gc.y = 0; gc.y < map.getRowCount(); gc.y++) {
                    Image image;
                    if (map.getTileType(gc) == TileType.Bonus) {
                        image = bonuses[map.getBonusType(gc).ordinal()].getImage(); 
                    } else if (map.getTileType(gc) == TileType.Arrow) {
                        g.drawImage(tiles[TileType.Empty.ordinal()].getImage(), gc.x*size, gc.y*size, null);
                        image = ((OrientedSprite)tiles[TileType.Arrow.ordinal()]).getOrientedImage(map.getArrowDirection(gc));
                    } else
                        image = tiles[map.getTileType(gc).ordinal()].getImage();
                    
                    g.drawImage(image, gc.x*size, gc.y*size, null);
                    
                    if (map.isExploding(gc)) {
                        if (map.getExplosionType(gc) != ExplosionType.Center)
                            g.drawImage(((OrientedSprite)explosions[map.getExplosionType(gc).ordinal()]).getOrientedImage(map.getExplosionDirection(gc)),
                                        gc.x*size, gc.y*size, null);
                        else
                            g.drawImage(explosions[ExplosionType.Center.ordinal()].getImage(),
                                        gc.x*size, gc.y*size, null);
                    }
                }
            }
        }
        
        if (showSpawningLocations) {
            g.setColor(Color.blue);
            List<GridCoordinates> spawningLocations = map.getSpawningLocations();
            for (int i = 0; i < spawningLocations.size(); i++) {
                GridCoordinates gc = spawningLocations.get(i);
                g.drawOval(gc.x*size, gc.y*size, size, size);
                
                drawCenteredString(g, String.valueOf(i), settings.scale((int)map.toCenterX(gc)), settings.scale((int)map.toCenterY(gc)));
            }
        }
    }
    
    private void drawWorld(Graphics2D g, WorldView worldView) {
        for (Entity entity : worldView.getEntities()) {
            if (entity instanceof Bomb) {
                if (World.oscillate(((Bomb)entity).getTimeRemaining(),
                                    ((Bomb)entity).getDuration(),
                                    (int)(BOMB_BLINK_INTERVAL_MIN*worldView.getFps()),
                                    (int)(BOMB_BLINK_INTERVAL_MAX*worldView.getFps()))) {
                    drawEntity(g, entity, bombs[0].getImage());
                } else {
                    drawEntity(g, entity, bombs[1].getImage());
                }
            }
        }
        
        final int colorCount = PlayerColor.values().length;
        //Dessine les joueurs ensuite
        for (Entity entity : worldView.getEntities()) {
            if (entity instanceof Player) {
                int invulnerability = ((Player)entity).getInvulnerability();
                if (invulnerability == 0 || invulnerability % (2*HIT_BLINK_INTERVAL*worldView.getFps()) >= HIT_BLINK_INTERVAL*worldView.getFps()) {
                    int color = ((Player)entity).getPlayerID() % colorCount;
                    if (entity.getSpeed() == 0.)
                        drawEntity(g, entity, players[color].getStandingPlayer(entity.getDirection()));
                    else
                        drawEntity(g, entity, players[color].getMovingPlayer(entity.getDirection(), 10*Math.abs(worldView.getTimeRemaining())/worldView.getFps()%2));
                }
            }
        }
        
        if (settings.tags) {
            //Dessine les noms en dernier
            for (Entity entity : worldView.getEntities()) {
                if (entity instanceof Player) {
                    drawCenteredString(g,
                                       ((Player)entity).getController().getName(),
                                       (int)settings.scale(entity.getX()),
                                       (int)settings.scale(entity.getBorderTop()-worldView.getMap().getTileSize()/5));
                }
            }
        }
    }
    
    private void drawEntity(Graphics2D g, Entity entity, Image image) {
        g.drawImage(image, (int)settings.scale(entity.getBorderLeft()), (int)settings.scale(entity.getBorderTop()), null);
    }

    
    private void updateDisplay() {
        if (wait == null) {
            wait = draw;
            draw = null;
            updatePending = true;
            revalidate();
            repaint();
        }
        else {
            boolean toRevalidate = wait.getWidth(null) != draw.getWidth(null) || wait.getHeight(null) != draw.getHeight(null);
            synchronized (wait) {
                VolatileImage cache = wait;
                wait = draw;
                draw = cache;
                updatePending = true;
            }

            if (toRevalidate)
                revalidate();
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
        if (updatePending && wait != null) {
            synchronized (wait) {
                VolatileImage cache = wait;
                wait = world;
                world = cache;
                updatePending = false;
            }
        }
        
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(world, 0, 0, null);
        g.dispose();
    }
    
    @Override
    public Dimension getPreferredSize() {
        if (wait != null)
            return new Dimension(wait.getWidth(null), wait.getHeight(null));
        else
            return super.getPreferredSize();
    }

}
