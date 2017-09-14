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

import game.Bomb;
import game.BonusType;
import game.Character;
import game.Entity;
import game.ExplosionType;
import game.GridCoordinates;
import game.MapView;
import game.TileType;
import game.WorldView;

/**
 * JPanel d'affichage de la carte et des entitiés
 */
public class GameViewer extends JPanel {
    private boolean updatePending;

    private int cacheTileSize;

     boolean showSpawningLocations;

    public static final double BOMB_BLINK_INTERVAL_MIN = 0.1;

    public static final double BOMB_BLINK_INTERVAL_MAX = 0.75;

    public static final double HIT_BLINK_INTERVAL = 0.05;

    private GameSettings settings;

    private double scale;
    
    private Object lock;
    
    private VolatileImage world;

    private VolatileImage wait;

    private VolatileImage draw;

    private Sprite[] tiles;

    private Sprite[] bonuses;
   
    private CharacterSprite[] players;
    
    private CharacterSprite defaultPlayer;

    private Sprite[] bombs;

    private Sprite[] explosions;
    
    private Sprite wallpaper;

    /**
     * Construit un panneau d'affichage de la carte et des entitiés
     */
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
        
        defaultPlayer = new CharacterSprite(null);
        
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
    public Sprite[] getTileSprites() {
        return Arrays.copyOf(tiles, tiles.length);
    }

    /**
     * Définit l'affichage ou non des emplacements d'apparition
     * @param showSpawningLocations Affichage ou non
     */
    public void setShowSpawningLocations(boolean showSpawningLocations) {
        this.showSpawningLocations = showSpawningLocations;
    }

    /**
     * Met à jour le buffer draw si nécessaire en fonction de la carte
     * @param map Carte à dessiner
     */
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
    private void drawMap(Graphics2D g, MapView map) {
        int size = scale(map.getTileSize());
        if (size <= 0)
            return;
            
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
    public static boolean oscillate(double remaining, double duration, double intervalMin, double intervalMax) {
        double interval = intervalMin + remaining*(intervalMax - intervalMin)/duration;
        return pingPong(duration - remaining, interval) > interval/2; //Ping pong sur la durée écoulée
    }
    
    /**
     * Dessine la partie sur un Graphics2D
     * @param g Graphics2D sur lequel dessiner
     * @param worldView Vue de la partie
     */
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
                    
                    CharacterSprite sprite = defaultPlayer;
                    if (((Character)entity).getPlayer() != null && ((Character)entity).getPlayer().getID() >= 0)
                        sprite = players[((Character)entity).getPlayer().getID() % colorCount];
                    
                    if (entity.getSpeed() == 0.)
                        drawEntity(g, entity, sprite.getStandingPlayer(entity.getDirection()));
                    else
                        drawEntity(g, entity, sprite.getMovingPlayer(entity.getDirection(), 10*Math.abs(worldView.getTimeRemaining())/worldView.getFps()%2));
                }
            }
        }
        
        if (settings.tags) {
            //Dessine les noms en dernier
            for (Entity entity : worldView.getEntities()) {
                if (entity instanceof Character) {
                    if (((Character)entity).getController() != null) {
                        drawCenteredString(g,
                                           ((Character)entity).getController().getName(),
                                           (int)scale(entity.getX()),
                                           (int)scale(entity.getBorderTop()-worldView.getMap().getTileSize()/5));
                    }
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
    private void drawEntity(Graphics2D g, Entity entity, Image image) {
        g.drawImage(image, (int)scale(entity.getBorderLeft()), (int)scale(entity.getBorderTop()), null);
    }

    /**
     * Met à jour l'affichage
     */
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
    private void updateCaches(int size) {
        for (Sprite[] sprites : new Sprite[][]{tiles, bonuses, bombs, explosions}) {
            for (Sprite sprite : sprites)
                sprite.setSize(size);
        }
        
        for (CharacterSprite sprite : players)
            sprite.setSize(size);
        
        defaultPlayer.setSize(size);
        
        cacheTileSize = size;
    }

    /**
     * Dessine un chaîne de caractère centrée
     * @param g Graphics sur lequel dessiner
     * @param str Chaîne de caractère à dessiner
     * @param centerX Position horizontale du centre de la chaîne de caractère
     * @param centerY Position verticale du centre de la chaîne de caractère
     */
    public static void drawCenteredString(Graphics g, String str, int centerX, int centerY) {
        if (str == null)
            return;
        
        int height = g.getFontMetrics().getHeight();
        int width = g.getFontMetrics().stringWidth(str);
        g.drawString(str, centerX-width/2, centerY+height/4);
    }
    
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
        if (world != null)
            return (getWidth()-world.getWidth())/2;
        return 0;
    }
    
    public int getOffsetY() {
        if (world != null)
            return (getHeight()-world.getHeight())/2;
        return 0;
    }
}
