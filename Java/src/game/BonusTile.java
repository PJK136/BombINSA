package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e0899fb2-43c0-4905-a390-6c536094eade")
public class BonusTile extends ExplodableTile {
    @objid ("3e29d10d-6372-4dd9-bd6f-32bbedb5490d")
     BonusType bonusType;

    @objid ("ef5a626b-54a8-41e3-833e-b15b09f2c6ba")
    public BonusTile(BonusType type) {
    }

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

    @objid ("c5183470-d76f-4e5e-9d39-c2b0e4c4eb53")
    void postExplosion() {
    }

}
