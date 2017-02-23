package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("0a4b4bbc-6c10-4d09-9d5f-ebe94f0193bd")
public class Tile {
    @objid ("e3c50532-1270-4fc6-9609-abbe4a39d444")
    protected TileType type;

    @objid ("2f216b30-27c8-4022-ad19-dbd71146cfd8")
    protected List<Entity> entities = new ArrayList<Entity> ();

    @objid ("387e8faa-5c7a-4e45-a991-70ce30b58c6f")
    public TileType getType() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.type;
    }

    @objid ("90821bed-67e2-4780-899a-8e22ae5de4a5")
    void setType(TileType value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.type = value;
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

    @objid ("547b782f-b41e-4098-bd4a-5de3c5f1b0dd")
    void addEntity(Entity entity) {
    }

}
