package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid("828d0eae-528b-43df-a33e-799c03f4c5af")
public class Map implements MapView {
    @objid("07880cea-125c-44c4-b8e2-fa231ec26a91")
    int tileSize;

    @objid("cc22a393-49b0-4bbb-9a9b-435c2804b924")
    Tile[][] tiles;

    @objid("01eb7be5-4c79-4697-b0b8-0c701c3ff114")
    List<GridCoordinates> spawningLocations = new ArrayList<GridCoordinates>();

    @objid("207bd3eb-53bc-4cf3-96e2-f4df05fd2714")
    public Map(int columns, int rows, int tileSize) {
        this.tileSize = tileSize;
        tiles = new Tile[columns][rows];
        // TODO voir quoi faire de l'attribut spawningLocations
    }

    @objid("b23f3caa-604b-4104-bb57-0dd034b39302")
    public int getWidth() {
        return (tiles.length) * (tileSize);
    }//faite

    @objid("59f30df4-4a7a-40d0-84f6-2dd01811ef45")
    public int getHeight() {
        if (tiles.length > 0) {
            return (tiles[0].length) * (tileSize);
        } else {
            throw new RuntimeException("Terrain de taille ZERO !!!");
        }
    }//faite

    @objid("48cfdc63-3e7d-49fd-9aa2-20e0e732aea2")
    public int getTileSize() {
        return tileSize;
    }//faite

    @objid("30af5e79-8af8-4b1e-b7d3-b1f7764177ed")
    public GridCoordinates toGridCoordinates(double x, double y) {
        int X = (int) (x/tileSize);
        int Y = (int) (y/tileSize);
        return new GridCoordinates(X, Y);
    }//faite

    @objid("1731df9e-c870-4d85-8b30-64347ac9b64e")
    public boolean isCollidable(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        return tiles[gc.x][gc.y].isCollidable();
    }//faite

    @objid("2d3a7680-da99-4351-813b-efa9981cf8ea")
    public boolean isExploding(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        return tiles[gc.x][gc.y].isExploding();
    }//faite

    @objid("8591210d-fb9c-463e-b84f-c0fa935ec9c0")
    public TileType getTileType(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        return tiles[gc.x][gc.y].getType();
    }//faite

    @objid("47c42a8c-d080-4066-9144-20a8fe58cd67")
    public BonusType getBonusType(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        if (tiles[gc.x][gc.y] instanceof BonusTile){
        return(((BonusTile)(tiles[gc.x][gc.y])).getBonusType());
        } else {
            throw new RuntimeException("Recup le type de bonus d'une case qui n'en est pas une");
        }
    }//faite

    @objid("5cbfa265-5ee9-4a71-afc5-0d371f6efe4e")
    public Direction getArrowDirection(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        if (tiles[gc.x][gc.y] instanceof ArrowTile){
        return(((ArrowTile)(tiles[gc.x][gc.y])).getDirection());
        } else {
            throw new RuntimeException("Recup la dir d'une fleche qui n'en est pas une");
        }
    }//faite

    @objid("d303d2cc-982c-4eb4-bb9f-9cead2bfc564")
    public List<GridCoordinates> getSpawningLocations() {
    }//TODO

    @objid("7f54516a-ad04-4987-b239-f3408e868759")
    public void setTileSize(int value) {
        this.tileSize = value;
    }//faite

    @objid("81317f24-599b-4128-9480-5c1f6c0859f2")
    public void loadMap(String map) {
    }//TODO

    @objid("8960c7a3-87e7-4b1b-8f9a-1858808c9349")
    public String saveMap() {
    }//TODO

    @objid("78a5a0ad-2342-4f09-8a8b-06386dbe2797")
    public void setTileType(TileType type, double x, double y) {
        setTileType(type, toGridCoordinates(x, y));
    }
        
    public void setTileType(TileType type, GridCoordinates gc) {
        switch(type){
        case Empty:
            tiles[gc.x][gc.y] = new EmptyTile();
            break;
        case Breakable:
            tiles[gc.x][gc.y] = new BreakableTile();
            break;
        case Unbreakable:
            tiles[gc.x][gc.y] = new UnbreakableTile();
            break;
        case Bonus:
            tiles[gc.x][gc.y] = new BonusTile();
            break;
        case Arrow:
            tiles[gc.x][gc.y] = new ArrowTile();
            break;
        }
    }//faite

    @objid("af2aa240-e83a-4b08-9641-56c0dfe48630")
    public void setArrowDirection(Direction direction, double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        if (tiles[gc.x][gc.y] instanceof ArrowTile){
            ((ArrowTile)(tiles[gc.x][gc.y])).setDirection(direction);
            } else {
                throw new RuntimeException("Change la direction d'une fleche qui n'en est pas une");
            }
    }//faite

    @objid("3197c6a7-683c-41b2-8d78-dc1d8b6b0f8a")
    void setExplosion(int duration, double x, double y) {
    }//doing

    @objid("3ec5e6c1-3f55-4edd-8324-02193ad30b88")
    void setBonusType(BonusType type, double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        if (tiles[gc.x][gc.y] instanceof BonusTile){
        ((BonusTile)(tiles[gc.x][gc.y])).setBonusType(type);
        } else {
            throw new RuntimeException("Change le type de bonus d'une case qui n'en est pas une");
        }
    }//faite

    @objid("c15e0882-3eff-4467-ac1f-4152e69db4f1")
    List<Entity> getEntities(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        return tiles[gc.x][gc.y].getEntities();
    }//faite

    @objid("e0d1831e-0655-4b4d-a0d8-d282ff461427")
    Tile[][] getTiles() {
        return this.tiles;
    } //faite

    @objid("6b4bc03d-7c89-4fd5-9ce3-be22a1dd04ba")
    void addEntity(Entity entity, double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        tiles[gc.x][gc.y].addEntity(entity);
    }//faite

    @objid("0812769a-5d2b-489d-9bf4-f782cb16fbef")
    void update() {
    }//TODO

}
