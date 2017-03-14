package gui;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import game.Direction;

public class OrientedSprite extends Sprite {

    Image directions[];
            
    public OrientedSprite(BufferedImage src) {
        super(src);
        directions = new Image[Direction.values().length];
    }
    
    public Image getOrientedImage(Direction direction) {
        return directions[direction.ordinal()];
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
        
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == null || directions[i].getWidth(null) != size || directions[i].getHeight(null) != size) {
                // http://stackoverflow.com/questions/2245869/resize-jcomponent-for-file-export/2246484#2246484
                double w = src.getWidth();
                double h = src.getHeight();
                AffineTransform scaleTransform = new AffineTransform();
                // last-in-first-applied: rotate, scale
                scaleTransform.scale(size/w, size/h);
                scaleTransform.rotate(-i*Math.PI/2, w/2, h/2);
                AffineTransformOp scaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);
                directions[i] = scaleOp.filter(src, null);
            }
        }
    }
}
