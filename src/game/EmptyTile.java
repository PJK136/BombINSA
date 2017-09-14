package game;


/**
 * Tuile vide
 */
public class EmptyTile extends ExplodableTile {
    @Override
    public TileType getType() {
        return TileType.Empty;
    }

    /**
     * impose que la tuile ne soit pas percutable
     */
    @Override
    public boolean isCollidable() {
        return false;
    }

}
