package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("448c0a60-8029-4a03-b5df-bb399dc51ac3")
public class UnbreakableTile extends Tile {
    @objid ("ab038780-15fe-4b06-9a5f-a46daa173c09")
    @Override
    public TileType getType() {
        return TileType.Unbreakable;
    }

    @objid ("014635fc-153f-46b5-9704-509b3100c351")
    public boolean isCollidable() {
        return true;
    }
    
    public Tile explode(int duration){
        return this;
    }

}
