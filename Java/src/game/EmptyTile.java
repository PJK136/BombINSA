package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("2ee20b8d-0ca1-45dc-9c2f-d6f9a8dae45c")
public class EmptyTile extends ExplodableTile {
    @objid ("1e514f2f-2677-4b09-90c5-7770342f7a45")
    public TileType getType() {
        return TileType.Empty;
    }

    @objid ("81346f4a-1c14-472f-9623-a660ddd6b6d9")
    public boolean isCollidable() {
        return false;
    }

}
