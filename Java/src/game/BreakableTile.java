package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("96719cc7-626b-4b48-9f90-aeab79c12c54")
public class BreakableTile extends ExplodableTile {
    @objid ("0f97109a-7ddc-4f5f-916d-aa2c1e51cc55")
    public static final double LOOT_RATE = 0.40;

    @objid ("b437dbbe-08da-431d-a157-4f630122bbfa")
    @Override
    public TileType getType() {
        return TileType.Breakable;
    }

    @objid ("0f60bf8a-4b36-487b-9fe4-ea8ec57dcaa7")
    public boolean isCollidable() {
        return true;
    }

    @objid ("0fab6260-7a9c-4a9e-adda-5a7b5f0d0098")
    @Override
    Tile postExplosion() {
        Tile ret;
        double drop = (Math.random());
        if(drop<LOOT_RATE){
            ret = new BonusTile(BonusTile.randomBonus());
        } else {
            ret = new EmptyTile();
        }
         
        ret.setEntities(entities);
        return ret;
    }

}
