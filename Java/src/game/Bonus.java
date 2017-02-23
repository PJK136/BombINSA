package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e0899fb2-43c0-4905-a390-6c536094eade")
public class Bonus extends Tile {
    @objid ("3e29d10d-6372-4dd9-bd6f-32bbedb5490d")
    protected BonusType bonusType;

    @objid ("6823a6b7-ebf3-44cf-8890-65bc440392e2")
    public BonusType getBonusType() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.bonusType;
    }

    @objid ("1277cdc7-164d-41c6-a297-9ad372607823")
    void setBonusType(BonusType value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.bonusType = value;
    }

}
