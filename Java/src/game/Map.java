package game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("828d0eae-528b-43df-a33e-799c03f4c5af")
public class Map implements MapView {
    @objid ("07880cea-125c-44c4-b8e2-fa231ec26a91")
     int tileSize;

    @objid ("cc22a393-49b0-4bbb-9a9b-435c2804b924")
     Tile[][] tiles;

    @objid ("01eb7be5-4c79-4697-b0b8-0c701c3ff114")
     List<GridCoordinates> spawningLocations;

    @objid ("207bd3eb-53bc-4cf3-96e2-f4df05fd2714")
    public Map(int tileSize) {
        this.tileSize = tileSize;
        this.spawningLocations = new ArrayList<GridCoordinates>();
    }

    @objid ("a4436290-1f67-4588-b3bc-fa90f4f58cfa")
    public Map(int columns, int rows, int tileSize) {
        this(tileSize);
        tiles = new Tile[columns][rows];
        GridCoordinates gc = new GridCoordinates();
        for (gc.x = 0; gc.x < getColumnCount(); gc.x++) {
            for (gc.y = 0; gc.y < getRowCount(); gc.y++) {
                setTileType(TileType.Empty, gc);
            }
        }
    }

    @objid ("64ccc174-db72-453d-bdcb-2e3112bac24a")
    public int getColumnCount() {
        return tiles.length;
    }

    @objid ("1e8f0325-d975-49b2-92d6-e878e9254352")
    public int getRowCount() {
        return tiles[0].length;
    }

    @objid ("b23f3caa-604b-4104-bb57-0dd034b39302")
    public int getWidth() {
        return getColumnCount() * tileSize;
    }

    @objid ("59f30df4-4a7a-40d0-84f6-2dd01811ef45")
    public int getHeight() {
        return getRowCount() * tileSize;
    }
    
    public boolean isInsideMap(GridCoordinates gc) {
        return gc.x >= 0 && gc.x < getColumnCount() && gc.y >= 0 && gc.y < getRowCount();
    }
    
    public boolean isInsideMap(double x, double y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    @objid ("48cfdc63-3e7d-49fd-9aa2-20e0e732aea2")
    public int getTileSize() {
        return tileSize;
    }

    @objid ("30af5e79-8af8-4b1e-b7d3-b1f7764177ed")
    public GridCoordinates toGridCoordinates(double x, double y) {
        int X = (int) (x/tileSize);
        int Y = (int) (y/tileSize);
        return new GridCoordinates(X, Y);
    }
    
    @Override
    public double toCenterX(GridCoordinates gc) {
        return (gc.x+0.5)*tileSize;
    }
    
    @Override
    public double toCenterY(GridCoordinates gc) {
        return (gc.y+0.5)*tileSize;
    }
    
    public boolean isCollidable(GridCoordinates gc) {
        if (!isInsideMap(gc))
            return true;

        return tiles[gc.x][gc.y].isCollidable();
    }

    @objid ("1731df9e-c870-4d85-8b30-64347ac9b64e")
    public boolean isCollidable(double x, double y) {
        if (!isInsideMap(x,y)) //La vérification dans la version GC n'est pas suffisante
            return true;       //car si x= -31, gc.x = 0 donc pas de détection 
        
        GridCoordinates gc = toGridCoordinates(x, y);
        return tiles[gc.x][gc.y].isCollidable();
    }
    
    public boolean isExplodable(GridCoordinates gc) {
        return tiles[gc.x][gc.y] instanceof ExplodableTile;
    }
    @Override
    public boolean isExploding(GridCoordinates gc) {
        return isExplodable(gc) && ((ExplodableTile)tiles[gc.x][gc.y]).isExploding();
    }

    @objid ("2d3a7680-da99-4351-813b-efa9981cf8ea")
    public boolean isExploding(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        return isExploding(gc);
    }

    @Override
    public ExplosionType getExplosionType(GridCoordinates gc) {
        return ((ExplodableTile)tiles[gc.x][gc.y]).getExplosionType();
    }
    
    
    @Override
    public Direction getExplosionDirection(GridCoordinates gc) {
        return ((ExplodableTile)tiles[gc.x][gc.y]).getExplosionDirection();
    }
    
    public TileType getTileType(GridCoordinates gc) {
        return tiles[gc.x][gc.y].getType();
    }
    
    @objid ("8591210d-fb9c-463e-b84f-c0fa935ec9c0")
    public TileType getTileType(double x, double y) {
        return getTileType(toGridCoordinates(x, y));
    }

    public BonusType getBonusType(GridCoordinates gc) {
        return(((BonusTile)(tiles[gc.x][gc.y])).getBonusType());
    }
    
    @objid ("47c42a8c-d080-4066-9144-20a8fe58cd67")
    public BonusType getBonusType(double x, double y) {
        return getBonusType(toGridCoordinates(x, y));
    }

    @objid ("5cbfa265-5ee9-4a71-afc5-0d371f6efe4e")
    public Direction getArrowDirection(GridCoordinates gc) {
        return(((ArrowTile)(tiles[gc.x][gc.y])).getDirection());
    }
    
    public Direction getArrowDirection(double x, double y) {
        return getArrowDirection(toGridCoordinates(x, y));
    }

    
    @objid ("d303d2cc-982c-4eb4-bb9f-9cead2bfc564")
    public List<GridCoordinates> getSpawningLocations() {
        return Collections.unmodifiableList(spawningLocations);
    }

    @objid ("63d25698-a5f4-4701-a7ed-10309284ddb6")
    public void addSpawningLocation(GridCoordinates gc) {
        if (!isInsideMap(gc))
            throw new RuntimeException("Coordonées du lieu de spawn invalides : " + gc);
        spawningLocations.add(gc);
    }

    @objid ("3e625452-8ef8-40e1-ae3b-de5858f727c6")
    public void removeSpawningLocation(GridCoordinates gc) {
        spawningLocations.remove(gc);
    }

    @objid ("7f54516a-ad04-4987-b239-f3408e868759")
    public void setTileSize(int value) {
        if (value <= 1)
            throw new RuntimeException("Taille des tuiles inférieure à 1 :" + value);
        
        this.tileSize = value;
        GridCoordinates gc = new GridCoordinates();
        for(gc.x= 0; gc.x < getColumnCount(); gc.x++){
            for(gc.y= 0; gc.y < getRowCount(); gc.y++){
                updateEntities(gc);
            }
        }
    }
    
    public void setsize(int columns, int rows) {
        if (columns <= 0)
            throw new RuntimeException("Nombre de colonnes négatif ou nul : " + columns);
        else if (rows <= 0)
            throw new RuntimeException("Nombre de lignes négatif ou nul : " + rows);
        
        Tile[][] newTiles = new Tile[columns][rows];
        
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (i < getColumnCount() && j < getRowCount()) {
                    newTiles[i][j] = tiles[i][j];
                }
                else
                    newTiles[i][j] = new EmptyTile();
            }
        }
        
        Iterator<GridCoordinates> iterator = spawningLocations.iterator();
        while (iterator.hasNext()) {
            if (!isInsideMap(iterator.next()))
                iterator.remove();
        }
        
        tiles = newTiles;
    }

    public void loadMap(String map) throws InputMismatchException {
        loadMap(new Scanner(map));
    }
    
    public void loadMap(Path path) throws InputMismatchException, IOException {
        loadMap(new Scanner(path));
    }
    
    public void loadMap(InputStream map) throws InputMismatchException {
        loadMap(new Scanner(map));
    }
    
    @objid ("81317f24-599b-4128-9480-5c1f6c0859f2")
    private void loadMap(Scanner sc) throws InputMismatchException {
        int columnCount = sc.nextInt();
        int rowCount = sc.nextInt();
        
        tiles = new Tile[columnCount][rowCount];
        
        int spawningLocationsCount = sc.nextInt();
        spawningLocations = new ArrayList<GridCoordinates>(spawningLocationsCount);
        for (int i = 0; i < spawningLocationsCount; i++)
            spawningLocations.add(new GridCoordinates(sc.nextInt(), sc.nextInt()));
        
        TileType[] types = TileType.values();
        Direction[] directions = Direction.values();
        
        GridCoordinates grid = new GridCoordinates();
        
        for (grid.x = 0; grid.x < columnCount; grid.x++) {
            for (grid.y = 0; grid.y < rowCount; grid.y++) {
                TileType type = types[sc.nextInt()];
                setTileType(type, grid);
                
                if (type == TileType.Arrow)
                    ((ArrowTile)tiles[grid.x][grid.y]).setDirection(directions[sc.nextInt()]);
            }
        }
        
        sc.close();
    }

    @objid ("8960c7a3-87e7-4b1b-8f9a-1858808c9349")
    public String saveMap() {
        StringJoiner content = new StringJoiner(" ");
        content.add(String.valueOf(getColumnCount()));
        content.add(String.valueOf(getRowCount()));
        content.add(String.valueOf(spawningLocations.size()));
        
        for (GridCoordinates coordinates : spawningLocations)
            content.add(String.valueOf(coordinates.x)).add(String.valueOf(coordinates.y));
        
        for (int i = 0; i < getColumnCount(); i++) {
            for (int j = 0; j < getRowCount(); j++) {
                TileType type = tiles[i][j].getType();
                content.add(String.valueOf(type.ordinal()));
                
                if (type == TileType.Arrow)
                    content.add(String.valueOf(((ArrowTile)tiles[i][j]).getDirection().ordinal()));
            }
        }
        return content.toString();
    }

    @objid ("78a5a0ad-2342-4f09-8a8b-06386dbe2797")
    public void setTileType(TileType type, double x, double y) {
        setTileType(type, toGridCoordinates(x, y));
    }

    @objid ("33e1123b-fd64-4e1d-96ea-0813d9ffac7d")
    public void setTileType(TileType type, GridCoordinates gc) {
        Tile newTile = null;
        switch(type){
        case Empty:
            newTile = new EmptyTile();
            break;
        case Breakable:
            newTile = new BreakableTile();
            break;
        case Unbreakable:
            newTile = new UnbreakableTile();
            break;
        case Bonus:
            newTile = new BonusTile();
            break;
        case Arrow:
            newTile = new ArrowTile();
            break;
        }
        
        if (tiles[gc.x][gc.y] != null)
            newTile.setEntities(tiles[gc.x][gc.y].getEntities());
        tiles[gc.x][gc.y] = newTile;
    }
    

    @objid ("af2aa240-e83a-4b08-9641-56c0dfe48630")
    public void setArrowDirection(Direction direction, double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        ((ArrowTile)(tiles[gc.x][gc.y])).setDirection(direction);
    }
    
    public boolean hasBomb(double x, double y) {
        List<Entity> entities = getEntities(x, y);
        for (Entity entity : entities) {
            if (entity instanceof Bomb)
                return true;
        }
        return false;
    }

    @objid ("3197c6a7-683c-41b2-8d78-dc1d8b6b0f8a")
    void setExplosion(int duration, ExplosionType type, Direction direction, GridCoordinates gc) {
        ((ExplodableTile)tiles[gc.x][gc.y]).explode(duration, type, direction);
    }
    
    void setExplosionCenter(GridCoordinates gc) {
        ((ExplodableTile)tiles[gc.x][gc.y]).setExplosionType(ExplosionType.Center);
    }
    
    void setExplosionEnd(GridCoordinates gc) {
        if (((ExplodableTile)tiles[gc.x][gc.y]).getExplosionType() != ExplosionType.Center)
            ((ExplodableTile)tiles[gc.x][gc.y]).setExplosionType(ExplosionType.End);
    }

    @objid ("3ec5e6c1-3f55-4edd-8324-02193ad30b88")
    void setBonusType(BonusType type, double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        ((BonusTile)(tiles[gc.x][gc.y])).setBonusType(type);
    }

    @objid ("c15e0882-3eff-4467-ac1f-4152e69db4f1")
    public List<Entity> getEntities(double x, double y) {
        GridCoordinates gc = toGridCoordinates(x, y);
        return Collections.unmodifiableList(tiles[gc.x][gc.y].getEntities());
    }

    @objid ("e0d1831e-0655-4b4d-a0d8-d282ff461427")
    Tile[][] getTiles() {
        return this.tiles;
    }

    @objid ("6b4bc03d-7c89-4fd5-9ce3-be22a1dd04ba")
    void addEntity(Entity entity) {
        GridCoordinates gc = toGridCoordinates(entity.getX(), entity.getY());
        tiles[gc.x][gc.y].addEntity(entity);
    }
    
    @objid ("0812769a-5d2b-489d-9bf4-f782cb16fbef")
    void update() {
        GridCoordinates gc = new GridCoordinates();
        for(gc.x=0; gc.x<getColumnCount(); gc.x++){
            for(gc.y=0; gc.y<getRowCount(); gc.y++){
                //on parcours le tableau et on update les cases
               tiles[gc.x][gc.y] = tiles[gc.x][gc.y].update();
               updateEntities(gc);
            }
        }
        
    }
    
    private void updateEntities(GridCoordinates gc){
        Iterator<Entity> iterator = tiles[gc.x][gc.y].entities.iterator();
        while(iterator.hasNext()){
            //parcours les entités pour virer ceux qui sont à remove
            Entity entity = iterator.next();
            if(entity.isToRemove()){
                iterator.remove();
            } else {
                GridCoordinates pos = toGridCoordinates(entity.x, entity.y);
                if((gc.x != pos.x) || (gc.y != pos.y)){
                    //parcours les entités et déplace puis remove celles qui sont pas dans la bonne case
                    tiles[pos.x][pos.y].addEntity(entity);
                    iterator.remove();
                }
            }
            
            //TODO : Mettre à jour les "bouts" des explosions
        }
    }
}
