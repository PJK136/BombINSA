package gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

public class SpriteFactory {

    Map<String, BufferedImage> images;
    
    private SpriteFactory() {
        images = new ConcurrentHashMap<String, BufferedImage>();
    }
    
    private BufferedImage readRessource(String name) {
        try {
            return ImageIO.read(Sprite.class.getResourceAsStream("/img/" + name + ".png"));
        } catch (IOException e) {
            System.err.println("Can't read ressource at " + "/img/" + name + ".png");
            return null;
        }
    }
    
    // http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    private static class SingletonHolder
    {       
        /** Instance unique non préinitialisée */
        private final static SpriteFactory instance = new SpriteFactory();
    }
 
    /** Point d'accès pour l'instance unique du singleton */
    public static SpriteFactory getInstance() {
        return SingletonHolder.instance;
    }
    
    public BufferedImage readImage(String name) {
        BufferedImage image = images.get(name);
        if (image == null) {
            image = readRessource(name);
            images.putIfAbsent(name, image);
        }
        
        return image;
    }
    
    public Sprite getSprite(String name) {
        return new Sprite(readImage(name));
    }
    
    public OrientedSprite getOrientedSprite(String name) {
        return new OrientedSprite(readImage(name));
    }
    
    /* For test purposes

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
            BufferedImage tile = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = tile.createGraphics();
            g.setColor(toColor(type));
            g.fillRect(0, 0, 256, 256);
            g.dispose();
            return tile;
        }
    }
    
    public static void drawCenteredString(Graphics g, String str, int centerX, int centerY) {
        int height = g.getFontMetrics().getHeight();
        int width = g.getFontMetrics().stringWidth(str);
        g.drawString(str, centerX-width/2, centerY+height/4);
    } */
}
