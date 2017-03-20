package gui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class Sprite {
    BufferedImage src;
    BufferedImage cache;
    
    public Sprite(BufferedImage src) {
        this.src = src;
        this.cache = src;
    }
    
    public Sprite(Sprite copy) {
        this(copy.src);
    }
    
    public int getSize() {
        return cache.getWidth(null);
    }
    
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
    
    public Image getImage() {
        return cache;
    }
    
    public Image getImage(int size) {
        setSize(size);
        return cache;
    }
    
    public static BufferedImage newCompatibleImage(int width, int height, int transparency) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleImage(width, height, transparency);
    }
}
