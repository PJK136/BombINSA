package gui;

import java.awt.Image;

import game.Direction;

/**
 * Classe gérant les sprites du joueur
 */
public class CharacterSprite {
    public static int TOTAL_MOVES = 2;

    private OrientedSprite standing;

    private OrientedSprite[] moving;

    /**
     * Construit les sprites du joueur
     * @param color Couleur du jouer
     */
    public CharacterSprite(PlayerColor color) {
        String colorName = "gray";
        if (color != null)
            colorName = color.name().toLowerCase();
        SpriteFactory factory = SpriteFactory.getInstance();
        this.standing = factory.getOrientedSprite("bomberMan_"+colorName);
        this.moving = new OrientedSprite[TOTAL_MOVES];
        for (int i = 0; i < TOTAL_MOVES; i++)
            this.moving[i] = factory.getOrientedSprite("bomberMan_"+colorName+"_moving"+i);
    }

    /**
     * Renvoie l'image du joueur qui ne se déplace pas suivant une direction
     * @param direction Direction du joueur
     * @return Image du joueur orientée
     */
    public Image getStandingPlayer(Direction direction) {
        return standing.getOrientedImage(direction);
    }

    /**
     * Renvoie une des images du joueur se déplaçant suivant une direction
     * @param direction Direction du joueur
     * @param i Index de l'image
     * @return Image du joueur orientée
     */
    public Image getMovingPlayer(Direction direction, int i) {
        return moving[i].getOrientedImage(direction);
    }

    /**
     * Redimensionne les sprites du joueur à la taille indiquée
     * @param size Taille désirée
     */
    public void setSize(int size) {
        this.standing.setSize(size);
        for (int i = 0; i < TOTAL_MOVES; i++)
            this.moving[i].setSize(size);
    }

}
