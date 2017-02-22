package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("0a4b4bbc-6c10-4d09-9d5f-ebe94f0193bd")
public class Tile {
    @objid ("e3c50532-1270-4fc6-9609-abbe4a39d444")
    protected TileType type;

    @objid ("387e8faa-5c7a-4e45-a991-70ce30b58c6f")
    TileType getType() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.type;
    }

    @objid ("90821bed-67e2-4780-899a-8e22ae5de4a5")
    void setType(TileType value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.type = value;
    }

    @objid ("56948d0a-9630-4a04-8e0a-25aafbb43b4d")
    void update() {
    }

}
