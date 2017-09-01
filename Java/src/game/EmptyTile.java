package game;


/**
 * Tuile vide
 */
public class EmptyTile extends ExplodableTile {
    public TileType getType() {
        return TileType.Empty;
    }

    /**
     * impose que la tuile ne soit pas percutable
     */
    public boolean isCollidable() {
        return false;
    }

}
