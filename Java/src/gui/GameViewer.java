package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
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
    @objid ("ed779ebb-3be4-48e3-b95b-734fd05f7722")
    private boolean updatePending;

    @objid ("d28faab6-a0b2-4d48-babd-321c55a2329c")
    private int cacheTileSize;

    @objid ("54289806-23b6-484d-a05f-5753b28d66cb")
     boolean showSpawningLocations;

    @objid ("6f14574d-25ab-4e23-be63-75a029c045ef")
    public static final double BOMB_BLINK_INTERVAL_MIN = 0.1;

    @objid ("fdfb3581-6042-4ba2-a627-d6f8757d3daf")
    public static final double BOMB_BLINK_INTERVAL_MAX = 0.75;

    @objid ("a21a8f32-edef-4b44-84c4-5bd84063c892")
    public static final double HIT_BLINK_INTERVAL = 0.05;

    @objid ("b2236fa3-c898-483c-910b-80e165542deb")
    private GameSettings settings;

    @objid ("113a9a2b-d75f-48f0-9ef6-969b7a21874b")
    private VolatileImage world;

    @objid ("ca0554e4-6bd2-49ba-aba2-0c176d3b72d0")
    private VolatileImage wait;

    @objid ("1d5e84ab-7ea8-4b04-aca0-c67adc47e1e0")
    private VolatileImage draw;

    @objid ("142c9113-4c57-4ca4-87b6-5f4480139c7d")
    private Sprite[] tiles;

    @objid ("ac1bb551-5adb-46d2-b6aa-a0d3b8f441a7")
    private Sprite[] bonuses;

    @objid ("26411124-731f-4b6b-8936-3798689dc93a")
    private PlayerSprite[] players;

    @objid ("0e692d65-5a5c-4ade-a4a0-b946e0a2bb73")
    private Sprite[] bombs;

    @objid ("c42cdd12-fa87-40c1-88d9-da86b61d8224")
    private Sprite[] explosions;

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

    @objid ("583f3996-abed-471d-8101-b04b30a43ab8")
    public Sprite[] getTileSprites() {
        return Arrays.copyOf(tiles, tiles.length);
    }

    @objid ("2586ef32-e39f-4586-9357-595b4ee6a16f")
    public void setShowSpawningLocations(boolean showSpawningLocations) {
        this.showSpawningLocations = showSpawningLocations;
    }

    @objid ("83213c12-5c54-444e-a97b-5c1c6b3696a4")
    private void updateDrawImage(MapView map) {
        int width = settings.scale(map.getWidth());
        int height = settings.scale(map.getHeight());
        
        if (draw == null || draw.getWidth() != width || draw.getHeight() != height) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            if (gc == null)
                gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            draw = gc.createCompatibleVolatileImage(Math.max(1, width), Math.max(1, height), Transparency.OPAQUE);
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

    @objid ("15b604df-463d-46e1-a427-f4b1fe7216df")
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

    @objid ("f390cb1f-6299-4095-b357-460eec272041")
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

    @objid ("755fc65e-0758-44c2-9985-aa3b99a9ab0c")
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
                if (worldView.getWarmupTimeRemaining() != 0 || invulnerability == 0 ||
                    invulnerability % (2*HIT_BLINK_INTERVAL*worldView.getFps()) >= HIT_BLINK_INTERVAL*worldView.getFps()) {
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

    @objid ("74f9ac1f-3b1a-4b47-b048-85518b178ca9")
    private void drawEntity(Graphics2D g, Entity entity, Image image) {
        g.drawImage(image, (int)settings.scale(entity.getBorderLeft()), (int)settings.scale(entity.getBorderTop()), null);
    }

    @objid ("3d9b90b7-9585-42ab-b70f-bddea28da889")
    private void updateDisplay() {
        if (wait == null) {
            wait = draw;
            draw = null;
            updatePending = true;
            revalidate();
            repaint();
        }
        else {
            boolean toRevalidate = false;
            
            synchronized (wait) {
                toRevalidate = wait.getWidth(null) != draw.getWidth(null) || wait.getHeight(null) != draw.getHeight(null);
            
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

    @objid ("a3eb5366-b80a-427e-baf9-06b3886893f0")
    private void updateCaches(int size) {
        for (Sprite[] sprites : new Sprite[][]{tiles, bonuses, bombs, explosions}) {
            for (Sprite sprite : sprites)
                sprite.setSize(size);
        }
        
        for (PlayerSprite sprite : players)
            sprite.setSize(size);
        
        cacheTileSize = size;
    }

    @objid ("6739bd4e-1437-4897-b494-45f4309b8971")
    public static void drawCenteredString(Graphics g, String str, int centerX, int centerY) {
        if (str == null)
            return;
        
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

    @objid ("92370fd0-5611-46a4-af23-f82fe2a73fc9")
    @Override
    public Dimension getPreferredSize() {
        if (wait != null)
            return new Dimension(wait.getWidth(null), wait.getHeight(null));
        else
            return super.getPreferredSize();
    }

}
