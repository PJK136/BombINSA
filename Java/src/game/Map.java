package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("828d0eae-528b-43df-a33e-799c03f4c5af")
public class Map implements MapView {
    @objid ("07880cea-125c-44c4-b8e2-fa231ec26a91")
    protected int tileSize;

    @objid ("cc22a393-49b0-4bbb-9a9b-435c2804b924")
    protected List<Tile> tiles = new ArrayList<Tile> ();

    @objid ("48cfdc63-3e7d-49fd-9aa2-20e0e732aea2")
    public int getTileSize() {
    }

    @objid ("30af5e79-8af8-4b1e-b7d3-b1f7764177ed")
    public GridCoordinates toGridCoordinates(double x, double y) {
    }

    @objid ("1731df9e-c870-4d85-8b30-64347ac9b64e")
    public boolean isCollidableBy(Entity entity, double x, double y) {
    }

    @objid ("8591210d-fb9c-463e-b84f-c0fa935ec9c0")
    public TileType getTileType(double x, double y) {
    }

    @objid ("47c42a8c-d080-4066-9144-20a8fe58cd67")
    public BonusType getBonusType(double x, double y) {
    }

    @objid ("5cbfa265-5ee9-4a71-afc5-0d371f6efe4e")
    public Direction getArrowDirection(String x, String y) {
    }

    @objid ("7f54516a-ad04-4987-b239-f3408e868759")
    public void setTileSize(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.tileSize = value;
    }

    @objid ("81317f24-599b-4128-9480-5c1f6c0859f2")
    public void loadMap(String map) {
    }

    @objid ("8960c7a3-87e7-4b1b-8f9a-1858808c9349")
    public String saveMap() {
    }

    @objid ("78a5a0ad-2342-4f09-8a8b-06386dbe2797")
    public void setTileType(TileType type, double x, double y) {
    }

    @objid ("af2aa240-e83a-4b08-9641-56c0dfe48630")
    public void setArrowDirection(Direction direction, double x, double y) {
    }

    @objid ("436f4565-82f9-449f-9e09-7280be5fb916")
    void setExplosion(double x, double y, int explosionTime) {
    }

    @objid ("c15e0882-3eff-4467-ac1f-4152e69db4f1")
    List<Entity> getEntities(double x, double y) {
    }

    @objid ("e0d1831e-0655-4b4d-a0d8-d282ff461427")
    List<Tile> getTiles() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.tiles;
    }

    @objid ("6b4bc03d-7c89-4fd5-9ce3-be22a1dd04ba")
    void addEntity(Entity entity, double x, double y) {
    }

    @objid ("0812769a-5d2b-489d-9bf4-f782cb16fbef")
    void update() {
    }

}
