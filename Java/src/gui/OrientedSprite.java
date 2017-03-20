package gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import game.Direction;

public class OrientedSprite extends Sprite {

    BufferedImage directions[];
            
    public OrientedSprite(BufferedImage src) {
        super(src);
        directions = new BufferedImage[Direction.values().length];
    }
    
    public Image getOrientedImage(Direction direction) {
        return directions[direction.ordinal()];
    }

    @Override
    public void setSize(int size) {
        if (src == null)
            return;
        
        super.setSize(size);
        
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == null || directions[i].getWidth(null) != size || directions[i].getHeight(null) != size) {
                BufferedImage newCache = newCompatibleImage(size, size, Transparency.BITMASK);
                // http://stackoverflow.com/questions/2245869/resize-jcomponent-for-file-export/2246484#2246484
                double w = src.getWidth();
                double h = src.getHeight();
                Graphics2D g = newCache.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.scale(size/w, size/h);
                g.rotate(-i*Math.PI/2, w/2, h/2);
                g.drawImage(src, 0, 0, null);
                g.dispose();
                directions[i] = newCache;
            }
        }
    }
}
