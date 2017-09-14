package game;


/**
 * Tuile incassable
 */
public class UnbreakableTile extends Tile {
    @Override
    public TileType getType() {
        return TileType.Unbreakable;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

}
