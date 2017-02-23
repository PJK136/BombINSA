package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("7c0be07f-9851-49c1-9084-d21acfdf1a57")
public class Arrow extends Tile {
    @objid ("e9081624-7d7d-411c-9fae-43e3570502a5")
    protected Direction direction;

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
