package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import game.Character;
import game.TileType;
import game.WorldView;

/**
 * JPanel d'affichage de la carte et des entitiés
 */
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

    private double scale;
    
    private Object lock;
    
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
    private CharacterSprite[] players;

    @objid ("0e692d65-5a5c-4ade-a4a0-b946e0a2bb73")
    private Sprite[] bombs;

    @objid ("c42cdd12-fa87-40c1-88d9-da86b61d8224")
    private Sprite[] explosions;
    
    private Sprite wallpaper;

    /**
     * Construit un panneau d'affichage de la carte et des entitiés
     */
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
        
        players = new CharacterSprite[PlayerColor.values().length];
        for (PlayerColor color : PlayerColor.values())
            players[color.ordinal()] = new CharacterSprite(color);
        
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
        
        wallpaper = SpriteFactory.getInstance().getSprite("wallpaper");
        
        showSpawningLocations = false;
        
        cacheTileSize = 0;
        
        lock = new Object();
        
        setFocusable(true);
        setDoubleBuffered(true);
    }

    /**
     * @return Les sprites des tuiles utilisées par GameViewer
     */
    @objid ("583f3996-abed-471d-8101-b04b30a43ab8")
    public Sprite[] getTileSprites() {
        return Arrays.copyOf(tiles, tiles.length);
    }

    /**
     * Définit l'affichage ou non des emplacements d'apparition
     * @param showSpawningLocations Affichage ou non
     */
    @objid ("2586ef32-e39f-4586-9357-595b4ee6a16f")
    public void setShowSpawningLocations(boolean showSpawningLocations) {
        this.showSpawningLocations = showSpawningLocations;
    }

    /**
     * Met à jour le buffer draw si nécessaire en fonction de la carte
     * @param map Carte à dessiner
     */
    @objid ("83213c12-5c54-444e-a97b-5c1c6b3696a4")
    private void updateDrawImage(MapView map) {
        scale = (double)Math.min(getWidth()/map.getColumnCount(), getHeight()/map.getRowCount())/map.getTileSize();
        final int width = scale(map.getColumnCount()*map.getTileSize());
        final int height = scale(map.getRowCount()*map.getTileSize());
        
        if (draw == null || draw.getWidth() != width || draw.getHeight() != height) {
            GraphicsConfiguration gc = getGraphicsConfiguration();
            if (gc == null)
                gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            draw = gc.createCompatibleVolatileImage(Math.max(1, width), Math.max(1, height), Transparency.OPAQUE);
        }
    }

    private int scale(int n) {
        return (int)(n*scale);
    }
    
    private double scale(double n) {
        return n*scale;
    }
    
    private Font scale(Font font, MapView map) {
        return font.deriveFont((float)scale(font.getSize()*map.getTileSize()/32));
    }
    
    /**
     * Dessine la partie
     * @param worldView Vue de la partie
     */
    @objid ("18e3e04f-dec2-45e2-a3f5-dbabb34447b4")
    public void drawWorld(WorldView worldView) {
        MapView map = worldView.getMap();
        updateDrawImage(map);
        do {
            Graphics2D g = draw.createGraphics();
            g.setFont(scale(g.getFont(), map));
            drawMap(g, map);
            drawWorld(g, worldView);
            g.dispose();
        } while (draw.contentsLost());
        updateDisplay();
    }

    /**
     * Dessine la carte
     * @param map Carte à dessiner
     */
    @objid ("15b604df-463d-46e1-a427-f4b1fe7216df")
    public void drawMap(MapView map) {
        updateDrawImage(map);
        do {
            Graphics2D g = draw.createGraphics();
            g.setFont(scale(g.getFont(), map));
            drawMap(g, map);
            g.dispose();
        } while (draw.contentsLost());
        updateDisplay();
    }

    /**
     * Dessine la carte sur un Graphics2D
     * @param g Graphics2D sur lequel dessiner
     * @param map Carte à dessiner
     */
    @objid ("f390cb1f-6299-4095-b357-460eec272041")
    private void drawMap(Graphics2D g, MapView map) {
        int size = scale(map.getTileSize());
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
                
                drawCenteredString(g, String.valueOf(i+1), (int)scale(map.toCenterX(gc)), (int)scale(map.toCenterY(gc)));
            }
        }
    }

    //http://answers.unity3d.com/questions/150347/what-exactly-does-timetime-do-in-mathfpingpong.html
    @objid ("a2ce16fa-379a-4c02-be51-8541e80d6bac")
    private static double pingPong(double time, double length) {
        double l = 2 * length;
        double t = time % l;
        
        if (0 <= t && t < length)
            return t;
        else
            return l - t;
    }

    /**
     * http://answers.unity3d.com/questions/678855/trying-to-get-object-to-blink-speed-acording-to-ti.html
     * @param remaining nombre restant
     * @param duration durée
     * @param intervalMin intervalle minimum
     * @param intervalMax intervalle maximum
     * @return alternance de vrai et de faux à intervalle dynamique
     */
    @objid ("dd75a61f-a8f2-4638-b49e-8fc4e1a0c499")
    public static boolean oscillate(double remaining, double duration, double intervalMin, double intervalMax) {
        double interval = intervalMin + remaining*(intervalMax - intervalMin)/duration;
        return pingPong(duration - remaining, interval) > interval/2; //Ping pong sur la durée écoulée
    }
    
    /**
     * Dessine la partie sur un Graphics2D
     * @param g Graphics2D sur lequel dessiner
     * @param worldView Vue de la partie
     */
    @objid ("755fc65e-0758-44c2-9985-aa3b99a9ab0c")
    private void drawWorld(Graphics2D g, WorldView worldView) {
        for (Entity entity : worldView.getEntities()) {
            if (entity instanceof Bomb) {
                if (oscillate(((Bomb)entity).getTimeRemaining(),
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
            if (entity instanceof Character) {
                int invulnerability = ((Character)entity).getInvulnerability();
                if (worldView.getWarmupTimeRemaining() != 0 || invulnerability == 0 ||
                    invulnerability % (2*HIT_BLINK_INTERVAL*worldView.getFps()) >= HIT_BLINK_INTERVAL*worldView.getFps()) {
                    int color = ((Character)entity).getPlayerID() % colorCount;
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
                if (entity instanceof Character) {
                    drawCenteredString(g,
                                       ((Character)entity).getController().getName(),
                                       (int)scale(entity.getX()),
                                       (int)scale(entity.getBorderTop()-worldView.getMap().getTileSize()/5));
                }
            }
        }
    }

    /**
     * Dessine une entitié
     * @param g Graphics2D sur lequel dessiner
     * @param entity Entité à dessiner
     * @param image Image de l'entité
     */
    @objid ("74f9ac1f-3b1a-4b47-b048-85518b178ca9")
    private void drawEntity(Graphics2D g, Entity entity, Image image) {
        g.drawImage(image, (int)scale(entity.getBorderLeft()), (int)scale(entity.getBorderTop()), null);
    }

    /**
     * Met à jour l'affichage
     */
    @objid ("3d9b90b7-9585-42ab-b70f-bddea28da889")
    private void updateDisplay() {        
        synchronized (lock) {
            VolatileImage cache = wait;
            wait = draw;
            draw = cache;
            updatePending = true;
        }
    
        repaint();
    }

    /**
     * Met à jour les images en cache
     * @param size Nouvelle taille
     */
    @objid ("a3eb5366-b80a-427e-baf9-06b3886893f0")
    private void updateCaches(int size) {
        for (Sprite[] sprites : new Sprite[][]{tiles, bonuses, bombs, explosions}) {
            for (Sprite sprite : sprites)
                sprite.setSize(size);
        }
        
        for (CharacterSprite sprite : players)
            sprite.setSize(size);
        
        cacheTileSize = size;
    }

    /**
     * Dessine un chaîne de caractère centrée
     * @param g Graphics sur lequel dessiner
     * @param str Chaîne de caractère à dessiner
     * @param centerX Position horizontale du centre de la chaîne de caractère
     * @param centerY Position verticale du centre de la chaîne de caractère
     */
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
        if (updatePending) {
            synchronized (lock) {
                VolatileImage cache = wait;
                wait = world;
                world = cache;
                updatePending = false;
            }
        }
        
        int wallPaperSize = Math.max(getHeight(), getWidth());
        g.drawImage(wallpaper.getImage(wallPaperSize), 0, 0, null);
        
        if (world != null)
            g.drawImage(world, (getWidth()-world.getWidth())/2, (getHeight()-world.getHeight())/2, null);
        
        g.dispose();
    }
    
    public double getScaleFactor() {
        return scale;
    }

    public int getOffsetX() {
        return (getWidth()-world.getWidth())/2;
    }
    
    public int getOffsetY() {
        return (getHeight()-world.getHeight())/2;
    }
}
