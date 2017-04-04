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
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Classe qui s'occupe de charger et fournir les Sprites
 */
@objid ("188ae9da-b54c-4e1e-a3ff-948915c69a2e")
public class SpriteFactory {
    @objid ("be66be87-2fc4-4602-af56-316d8d408437")
     Map<String, BufferedImage> images;

    @objid ("2e147b15-6089-4727-b68f-3d835d53a20f")
    private SpriteFactory() {
        images = new ConcurrentHashMap<String, BufferedImage>();
    }

    /**
     * Lit l'image ressource demandée et renvoie sous forme de BufferedImage
     * @param name Nom de la ressource
     * @return Image demandée
     */
    @objid ("1a5a9bd4-95f9-44cd-9c99-16d2baddd8af")
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
    @objid ("a4bcd1c1-4528-4332-afd4-bc75e9f86b4d")
    public static SpriteFactory getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Charge si nécessaire l'image et la renvoie
     * @param name Nom de l'image
     * @return Image
     */
    @objid ("ca0717cc-da7f-438c-b675-029d0ab2969f")
    public BufferedImage readImage(String name) {
        BufferedImage image = images.get(name);
        if (image == null) {
            image = readRessource(name);
            images.putIfAbsent(name, image);
        }
        return image;
    }

    /**
     * Renvoie une Sprite associée à l'image
     * @param name Nom de l'image
     * @return Sprite
     */
    @objid ("af178005-1f6f-439a-9e68-9277f4c5b3bf")
    public Sprite getSprite(String name) {
        return new Sprite(readImage(name));
    }

    /**
     * Renvoie une Sprite orientée associée à l'image
     * @param name Nom de l'image
     * @return Sprite orientée
     */
    @objid ("cb5b5f88-34d1-4661-857c-efe9233732bb")
    public OrientedSprite getOrientedSprite(String name) {
        return new OrientedSprite(readImage(name));
    }

    /**
     * Renvoie une image à la taille indiquée
     * @param name Nom de l'image
     * @param size Taille désirée
     * @return Image dimensionnée
     */
    @objid ("1012fe74-b517-4161-ae30-4bda5f5d534f")
    public Image getScaledImage(String name, int size) {
        return getSprite(name).getImage(size);
    }

    /**
     * Renvoie une ImageIcon de l'image à la taille indiquée
     * @param name Nom de l'image
     * @param size Taille désirée
     * @return ImageIcon
     */
    @objid ("64de7ee9-2eb1-483b-b339-60e0150d68b8")
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
    @objid ("04ec226d-8f5c-485e-b881-d2853dcd4fd9")
    private static class SingletonHolder {
        @objid ("9ff0d2e2-a060-4051-80e5-3f4790836aa0")
        private static final SpriteFactory instance = new SpriteFactory();

    }
}
