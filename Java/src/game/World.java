package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e1a6de2f-aa96-47de-99e8-3e7cc01deb8e")
public class World {
    @objid ("8e41547f-656f-4424-a2bd-84858b306af6")
    protected int timeRemaining;

    @objid ("07bb6a22-8f20-4538-8ae1-222ef290cc0b")
    protected int tileSize;

    @objid ("5fdd0d6a-029c-45dc-be4b-410e35086c6b")
    protected Tile[][] map;

    @objid ("3aa3c966-0090-417b-ad5f-c33f8b326884")
    protected List<Entity> entities = new ArrayList<Entity> ();

    @objid ("2bde8c8b-9971-411e-b712-639b6958ebbc")
    public List<Controller> controllers = new ArrayList<Controller> ();

    @objid ("923cc1fb-737c-4f7b-83c3-924ec5e5a43f")
    int getTimeRemaining() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.timeRemaining;
    }

    @objid ("3bf2bf63-99ba-400f-80fa-91732427a985")
    void setTimeRemaining(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.timeRemaining = value;
    }

    @objid ("a31f56b5-b516-4d6a-97cd-8b368b1426ff")
    List<Entity> getEntities() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.entities;
    }

    @objid ("ccbdf7a9-5ffe-4062-98d5-e818988c731f")
    void setEntities(List<Entity> value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.entities = value;
    }

    @objid ("f013395c-2338-405d-93a9-5e2cfd129f7d")
    public void update() {
    }

    @objid ("6fe4c974-9b8e-4849-8ae3-a2b90eaed94f")
    public void loadMap(String filename) {
    }

    @objid ("1cb0ec56-8dda-4050-867f-eb323a28d995")
    public void saveMap(String filename) {
    }

    @objid ("926ea609-9992-495c-bebe-367793405e44")
    public TileType getTileType(GridCoordinates position) {
    }

    @objid ("0a9395f3-e04f-45e7-8422-36d828bebc92")
    public TileType getTileType(double x, double y) {
    }

    @objid ("84c30152-c3c0-4a9b-a6c7-850c7d07e7a7")
    int getTileSize() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.tileSize;
    }

    @objid ("a9d3987f-c55a-48c8-b329-77d78a0dfff3")
    void setTileSize(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.tileSize = value;
    }

    @objid ("66c18a38-78da-420d-98ea-bcd545e07595")
    void createExplosion(double x, double y, int fire) {
    }

    @objid ("ae05a042-fdb6-46ba-830b-02fa363392ba")
    void plantBomb(double x, double y) {
    }

    @objid ("c9469926-88e7-4f1b-98ea-156eac4e30a8")
    public Controller newController() {
    }

}
