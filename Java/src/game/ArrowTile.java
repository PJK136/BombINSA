package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("7c0be07f-9851-49c1-9084-d21acfdf1a57")
public class ArrowTile extends Tile {
    @objid ("e9081624-7d7d-411c-9fae-43e3570502a5")
     Direction direction;

    @objid ("3ffd6d9c-5051-4269-9ead-1aa727152d09")
    public ArrowTile(Direction direction) {
    }

    @objid ("b485a2c1-70f6-4414-94d3-9a06cf720033")
    @Override
    public TileType getType() {
    }

    @objid ("626cbd74-5143-4b0e-9b2e-40d5e7c8c616")
    public Direction getDirection() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.direction;
    }

    @objid ("d94a5ee2-e6d7-40dd-b30c-3259db5fbaf0")
    void setDirection(Direction value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.direction = value;
    }

}
