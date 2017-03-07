package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e0899fb2-43c0-4905-a390-6c536094eade")
public class BonusTile extends Tile {
    @objid ("3e29d10d-6372-4dd9-bd6f-32bbedb5490d")
     BonusType bonusType;

    @objid ("2c6b3524-435a-414e-86f8-3a8b772005f9")
    public BonusTile() {
        bonusType = BonusType.Random;
    }

    @objid ("ef5a626b-54a8-41e3-833e-b15b09f2c6ba")
    public BonusTile(BonusType type) {
        this.bonusType = type;
    }

    @objid ("d6775c48-2c05-42c4-ad12-706ebc1b8ee8")
    @Override
    public TileType getType() {
        return TileType.Bonus;
    }

    @objid ("b391e578-677f-47b3-bf9c-8bd76328658c")
    public boolean isCollidable() {
        return false;
    }

    @objid ("6823a6b7-ebf3-44cf-8890-65bc440392e2")
    public BonusType getBonusType() {
        return this.bonusType;
    }

    @objid ("1277cdc7-164d-41c6-a297-9ad372607823")
    void setBonusType(BonusType value) {
        this.bonusType = value;
    }

    @objid ("55ed5042-47f7-40c9-95b1-ed6022b386c1")
    public Tile explode(int duration) {
        Tile emptyTile = new EmptyTile();
        emptyTile.explosionTimeRemaining = duration;
        return emptyTile;
    }

}
