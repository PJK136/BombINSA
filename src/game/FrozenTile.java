package game;

public class FrozenTile extends ExplodableTile {
    @Override
    public TileType getType() {
        return TileType.Frozen;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

}
