package gui;

import java.awt.Image;

import game.Direction;

public class PlayerSprite {
    public static int TOTAL_MOVES = 2;
    private OrientedSprite standing;
    private OrientedSprite[] moving;
    
    public PlayerSprite(PlayerColor color) {
        this.standing = new OrientedSprite(Sprite.readRessource("bomberMan_"+color.name().toLowerCase()));
        this.moving = new OrientedSprite[TOTAL_MOVES];
        for (int i = 0; i < TOTAL_MOVES; i++)
            this.moving[i] = new OrientedSprite(Sprite.readRessource("bomberMan_"+color.name().toLowerCase()+"_moving"+i));
    }
    
    public Image getStandingPlayer(Direction direction) {
        return standing.getOrientedImage(direction);
    }
    
    public Image getMovingPlayer(Direction direction, int i) {
        return moving[i].getOrientedImage(direction);
    }
    
    public void setSize(int size) {
        this.standing.setSize(size);
        for (int i = 0; i < TOTAL_MOVES; i++)
            this.moving[i].setSize(size);
    }
}
