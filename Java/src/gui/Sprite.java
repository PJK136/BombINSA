package gui;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprite {
    BufferedImage src;
    BufferedImage cache;
    
    public Sprite(BufferedImage src) {
        this.src = src;
    }
    
    public Sprite(Sprite copy) {
        this.src = copy.src;
    }
    
    public int getSize() {
        return cache.getWidth(null);
    }
    
    public void setSize(int size) {
        if (src == null)
            return;
        
        if (cache == null || cache.getWidth(null) != size || cache.getHeight(null) != size) {
            // http://stackoverflow.com/questions/2245869/resize-jcomponent-for-file-export/2246484#2246484
            cache = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            double w = src.getWidth();
            double h = src.getHeight();
            AffineTransform scaleTransform = new AffineTransform();
            scaleTransform.scale(size/w, size/h);
            AffineTransformOp scaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);
            scaleOp.filter(src, cache);
        }
    }
    
    public Image getImage() {
        return cache;
    }
    
    public Image getImage(int size) {
        setSize(size);
        return cache;
    }
}
