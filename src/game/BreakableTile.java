package game;


/**
 * Tuile cassable qui va libérer un bonus ou seulement devenir une case vide après explosion
 */
public class BreakableTile extends ExplodableTile {
    public static final double LOOT_RATE = 0.40;

    @Override
    public TileType getType() {
        return TileType.Breakable;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    /**
     * Renvoie aléatoirement une tuile bonus ou une case vide 
     * d'après les taux d'apparitions
     */
    @Override
    Tile postExplosion() {
        Tile ret;
        double drop = (Math.random());
        if (drop < LOOT_RATE) {
            ret = new BonusTile(BonusTile.randomBonus());
        } else {
            ret = new EmptyTile();
        }
         
        ret.setEntities(entities);
        return ret;
    }

}
