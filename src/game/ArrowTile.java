package game;


/**
 * Tuile flèche qui projette toute bombe qui passe dessus dans une direction donnée 
 */
public class ArrowTile extends ExplodableTile {
     Direction direction;

    /**
     * Construit une flèche dirigée vers le haut
     */
    public ArrowTile() {
        direction = Direction.Up;
    }

    /**
     * Construit une flèche dans la direction indiquée
     * @param direction Direction désirée
     */
    public ArrowTile(Direction direction) {
        this.direction = direction;
    }

    @Override
    public TileType getType() {
        return TileType.Arrow;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public Direction getDirection() {
        return this.direction;
    }

    void setDirection(Direction value) {
        this.direction = value;
    }

}
