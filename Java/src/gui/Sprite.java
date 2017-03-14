package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Sprite {
    BufferedImage src;
    Image cache;
    
    public Sprite(BufferedImage src) {
        this.src = src;
    }
    
    public int getSize() {
        return cache.getWidth(null);
    }
    
    public void setSize(int size) {
        if (cache == null || cache.getWidth(null) != size || cache.getHeight(null) != size) {
            cache = src.getScaledInstance(size, size, Image.SCALE_SMOOTH);
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
