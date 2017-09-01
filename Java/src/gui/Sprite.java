package gui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Classe Sprite qui gère le bon dimensionnement de son image et sa mise en cache
 */
public class Sprite {
     BufferedImage src;

     BufferedImage cache;

    /**
     * Construit un sprite depuis une image source
     * @param src Image source
     */
    public Sprite(BufferedImage src) {
        this.src = src;
        this.cache = src;
    }

    /**
     * Crée une copie d'un sprite
     * @param copy Sprite à copier
     */
    public Sprite(Sprite copy) {
        this(copy.src);
    }

    public int getSize() {
        return cache.getWidth(null);
    }

    /**
     * Redimensionne si nécessaire l'image du sprite à la bonne taille
     * @param size Taille désirée
     */
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
    public Image getImage() {
        return cache;
    }

    /**
     * Redimensionne si nécessaire et renvoie une image du sprite à la taille voulue
     * @param size Taille désirée
     * @return Image dimensionnée
     */
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
    public static BufferedImage newCompatibleImage(int width, int height, int transparency) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleImage(width, height, transparency);
    }

}
