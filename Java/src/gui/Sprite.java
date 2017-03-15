package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
        if (src == null)
            return;
        
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
    
    public static BufferedImage readRessource(String name) {
        try {
            return ImageIO.read(new File("img/" + name + ".png"));
        } catch (IOException e) {
            System.err.println("Can't read : " + "img/" + name + ".png");
            return null;
        }
    }
}
