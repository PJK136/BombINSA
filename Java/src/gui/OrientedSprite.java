package gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Direction;

/**
 * Sprite qui fournit une image dans 4 directions
 */
@objid ("47c0fb7b-39d7-4731-8dc9-b81d822dacd9")
public class OrientedSprite extends Sprite {
    @objid ("dace6ab3-4bbd-46df-b5f8-1c9bd7f9ad21")
     BufferedImage[] directions;

    /**
     * Construit un sprite orienté
     * @param src Image source
     */
    @objid ("abd2faad-3b09-4e47-bb6d-b1715ef19243")
    public OrientedSprite(BufferedImage src) {
        super(src);
        directions = new BufferedImage[Direction.values().length];
    }

    /**
     * Renvoie une image du sprite orientée dans la direction voulue
     * @param direction Direction
     * @return Image orientée
     */
    @objid ("58f5b97c-d080-44e8-8123-4eb7b219918e")
    public Image getOrientedImage(Direction direction) {
        return directions[direction.ordinal()];
    }

    @objid ("cac040e9-9951-41e5-aa43-b3905aee6246")
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
