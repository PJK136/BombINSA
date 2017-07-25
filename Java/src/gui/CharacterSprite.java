package gui;

import java.awt.Image;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Direction;

/**
 * Classe gérant les sprites du joueur
 */
@objid ("56494f5a-2022-4bf5-8c12-90f1f369f286")
public class CharacterSprite {
    @objid ("e1fa58d6-7096-4685-acc5-1978632e9877")
    public static int TOTAL_MOVES = 2;

    @objid ("61e26157-550d-4b0f-8e8e-f13c4b032603")
    private OrientedSprite standing;

    @objid ("14e01b6c-246d-4858-8535-a92a150cbfa8")
    private OrientedSprite[] moving;

    /**
     * Construit les sprites du joueur
     * @param color Couleur du jouer
     */
    @objid ("bb1fdbc9-a556-40ca-9ecb-b2a1f19280a4")
    public CharacterSprite(PlayerColor color) {
        SpriteFactory factory = SpriteFactory.getInstance();
        this.standing = factory.getOrientedSprite("bomberMan_"+color.name().toLowerCase());
        this.moving = new OrientedSprite[TOTAL_MOVES];
        for (int i = 0; i < TOTAL_MOVES; i++)
            this.moving[i] = factory.getOrientedSprite("bomberMan_"+color.name().toLowerCase()+"_moving"+i);
    }

    /**
     * Renvoie l'image du joueur qui ne se déplace pas suivant une direction
     * @param direction Direction du joueur
     * @return Image du joueur orientée
     */
    @objid ("db6566db-c05c-44f6-8c8c-7c4a3309a6c5")
    public Image getStandingPlayer(Direction direction) {
        return standing.getOrientedImage(direction);
    }

    /**
     * Renvoie une des images du joueur se déplaçant suivant une direction
     * @param direction Direction du joueur
     * @param i Index de l'image
     * @return Image du joueur orientée
     */
    @objid ("ee7755ed-5441-4fb1-893d-bc21c4b5efa5")
    public Image getMovingPlayer(Direction direction, int i) {
        return moving[i].getOrientedImage(direction);
    }

    /**
     * Redimensionne les sprites du joueur à la taille indiquée
     * @param size Taille désirée
     */
    @objid ("0500a8af-8c88-49e5-b0ec-52e8ab6d78c7")
    public void setSize(int size) {
        this.standing.setSize(size);
        for (int i = 0; i < TOTAL_MOVES; i++)
            this.moving[i].setSize(size);
    }

}
