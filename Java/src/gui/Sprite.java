package gui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Classe Sprite qui gère le bon dimensionnement de son image et sa mise en cache
 */
@objid ("6b279ea4-9b46-45ac-8a4f-20fcdff328ef")
public class Sprite {
    @objid ("5e629bac-12aa-4096-a9b0-2cf873e7d8bd")
     BufferedImage src;

    @objid ("648f361a-4c39-42ee-8142-f41c9b2ab4da")
     BufferedImage cache;

    /**
     * Construit un sprite depuis une image source
     * @param src Image source
     */
    @objid ("c91172af-251e-4f23-9178-1d4ffbf411a0")
    public Sprite(BufferedImage src) {
        this.src = src;
        this.cache = src;
    }

    /**
     * Crée une copie d'un sprite
     * @param copy Sprite à copier
     */
    @objid ("b486f284-2ec8-4c32-9329-447c5519b80d")
    public Sprite(Sprite copy) {
        this(copy.src);
    }

    @objid ("0d055000-5b5a-438e-bd86-87607dcd116e")
    public int getSize() {
        return cache.getWidth(null);
    }

    /**
     * Redimensionne si nécessaire l'image du sprite à la bonne taille
     * @param size Taille désirée
     */
    @objid ("551bdb92-dbe6-4f77-a992-64e90d6365d7")
    public void setSize(int size) {
        if (src == null)
            return;
        
        if (cache == null || cache.getWidth(null) != size || cache.getHeight(null) != size) {
            BufferedImage newCache = newCompatibleImage(size, size, Transparency.BITMASK);
            double w = src.getWidth();
            double h = src.getHeight();
            Graphics2D g = newCache.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.scale(size/w, size/h);
            g.drawImage(src, 0, 0, null);
            g.dispose();
            cache = newCache;
        }
    }

    /**
     * @return L'image dimensionnée précédemment avec setSize ou getImage(size)
     */
    @objid ("2eabedc2-8e44-474b-b7a7-410bd41dedbb")
    public Image getImage() {
        return cache;
    }

    /**
     * Redimensionne si nécessaire et renvoie une image du sprite à la taille voulue
     * @param size Taille désirée
     * @return Image dimensionnée
     */
    @objid ("2c0c8ed9-9494-4791-8365-88b2717c9d18")
    public Image getImage(int size) {
        setSize(size);
        return cache;
    }

    /**
     * Crée une image compatible avec l'environnement actuel
     * @param width Largeur
     * @param height Hauteur
     * @param transparency Paramètre de transparence
     * @return Image dimensionnée et compatible
     */
    @objid ("18d73b3c-e767-48df-b154-553c0a4c7863")
    public static BufferedImage newCompatibleImage(int width, int height, int transparency) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleImage(width, height, transparency);
    }

}
