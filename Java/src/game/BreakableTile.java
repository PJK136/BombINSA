package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("96719cc7-626b-4b48-9f90-aeab79c12c54")
public class BreakableTile extends Tile {
    @objid ("b437dbbe-08da-431d-a157-4f630122bbfa")
    @Override
    public TileType getType() {
    	return TileType.Breakable;
    }

}
