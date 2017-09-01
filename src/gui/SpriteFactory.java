package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 * Classe qui s'occupe de charger et fournir les Sprites
 */
public class SpriteFactory {
     Map<String, BufferedImage> images;

    private SpriteFactory() {
        images = new ConcurrentHashMap<String, BufferedImage>();
    }

    /**
     * Lit l'image ressource demandée et renvoie sous forme de BufferedImage
     * @param name Nom de la ressource
     * @return Image demandée
     */
    private BufferedImage readRessource(String name) {
        try {
            InputStream stream = getClass().getResourceAsStream("/img/" + name + ".png");
            if (stream == null)
                stream = getClass().getResourceAsStream("/img/" + name + ".jpg");
            if (stream == null)
                stream = getClass().getResourceAsStream("/toolbarButtonGraphics/general/" + name + ".gif");
            
            if (stream == null)
                throw new FileNotFoundException("Can't read ressource : " + name);
            return ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return Instance de SpriteFactory
     */
    public static SpriteFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Charge si nécessaire l'image et la renvoie
     * @param name Nom de l'image
     * @return Image
     */
    public BufferedImage readImage(String name) {
        BufferedImage image = images.get(name);
        if (image == null) {
            image = readRessource(name);
            images.put(name, image);
        }
        return image;
    }

    /**
     * Renvoie une Sprite associée à l'image
     * @param name Nom de l'image
     * @return Sprite
     */
    public Sprite getSprite(String name) {
        return new Sprite(readImage(name));
    }

    /**
     * Renvoie une Sprite orientée associée à l'image
     * @param name Nom de l'image
     * @return Sprite orientée
     */
    public OrientedSprite getOrientedSprite(String name) {
        return new OrientedSprite(readImage(name));
    }

    /**
     * Renvoie une image à la taille indiquée
     * @param name Nom de l'image
     * @param size Taille désirée
     * @return Image dimensionnée
     */
    public Image getScaledImage(String name, int size) {
        return getSprite(name).getImage(size);
    }

    /**
     * Renvoie une ImageIcon de l'image à la taille indiquée
     * @param name Nom de l'image
     * @param size Taille désirée
     * @return ImageIcon
     */
    public ImageIcon getImageIcon(String name, int size) {
        return new ImageIcon(getScaledImage(name, size));
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
            return stringInSquare(256, 4, "");
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
    
    // http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    private static class SingletonHolder {
        private static final SpriteFactory instance = new SpriteFactory();

    }
}
