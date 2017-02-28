package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("0a4b4bbc-6c10-4d09-9d5f-ebe94f0193bd")
public abstract class Tile {
    @objid ("7ffae68f-5714-4e34-91d3-598f263115eb")
     int explosionTimeRemaining;

    @objid ("2f216b30-27c8-4022-ad19-dbd71146cfd8")
    protected List<Entity> entities = new ArrayList<Entity> ();

    @objid ("f4911fdc-1c7c-45f9-80e8-3af86c75d6bb")
     Map map;

    @objid ("eba8c794-3248-484e-8fdd-ec19b08a5c19")
    public abstract TileType getType();

    @objid ("20137d66-fdca-4295-90c9-d845540dcb82")
    public abstract boolean isCollidable();

    @objid ("afce3c9f-2a7e-43fa-b8fe-9183c2e26f63")
    public boolean isExploding() {
    }

    @objid ("a0f2cff1-fab0-4f53-b3e0-2666e4eb5b0b")
    public int getExplosionTimeRemaining() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.explosionTimeRemaining;
    }

    @objid ("2b2f5efa-8de9-4ab7-932c-ca4af3ebd86f")
    public List<Entity> getEntities() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.entities;
    }

    @objid ("43353fe2-84e4-465e-90e2-a96befea38d6")
    void setEntities(List<Entity> value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.entities = value;
    }

    @objid ("56948d0a-9630-4a04-8e0a-25aafbb43b4d")
    void update() {
    }

    @objid ("9f2c3dd7-e9e3-46b8-82e0-23ea933b9eda")
    Tile explode(int duration) {
    }

    @objid ("547b782f-b41e-4098-bd4a-5de3c5f1b0dd")
    void addEntity(Entity entity) {
    }

}
