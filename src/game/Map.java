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


/** Cette classe gère la carte dans laquelle évoluent les entités et les tuiles */
public class Map implements MapView {
     String name;

     int tileSize;

     Tile[][] tiles;

     List<GridCoordinates> spawningLocations;

    /**
     * Crée une carte de jeu vierge
     * @param tileSize La taille des cases de grille dans cette carte
     */
    public Map(int tileSize) {
        this.tileSize = tileSize;
        this.spawningLocations = new ArrayList<GridCoordinates>();
        this.tiles = new Tile[0][0];
    }

    /**
     * Crée une carte de jeu
     * @param columns Le nombre de colonnes dans la grille de case de la carte
     * @param rows Le nombre de lignes dans la grille de case de la carte
     * @param tileSize La taille des cases de grille dans cette carte
     */
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColumnCount() {
        return tiles.length;
    }

    @Override
    public int getRowCount() {
        if (getColumnCount() > 0)
            return tiles[0].length;
        else
            return 0;
    }

    @Override
    public int getWidth() {
        return getColumnCount() * tileSize;
    }

    @Override
    public int getHeight() {
        return getRowCount() * tileSize;
    }

    @Override
    public int getTileSize() {
        return tileSize;
    }

    @Override
    public GridCoordinates toGridCoordinates(double x, double y) {
        int X = (int) (x/tileSize);
        int Y = (int) (y/tileSize);
        return new GridCoordinates(X, Y);
    }

    @Override
    public boolean isInsideMap(GridCoordinates gc) {
        return gc.x >= 0 && gc.x < getColumnCount() && gc.y >= 0 && gc.y < getRowCount();
    }

    @Override
    public boolean isInsideMap(double x, double y) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
    }

    @Override
    public double toCenterX(GridCoordinates gc) {
        return (gc.x+0.5)*tileSize;
    }

    @Override
    public double toCenterY(GridCoordinates gc) {
        return (gc.y+0.5)*tileSize;
    }

    @Override
    public double toCenterX(double x) {
        return (((int)(x/tileSize))+0.5)*tileSize;
    }

    @Override
    public double toCenterY(double y) {
        return (((int)(y/tileSize))+0.5)*tileSize;
    }

    @Override
    public boolean isCollidable(GridCoordinates gc) {
        if (!isInsideMap(gc))
            return true;
        return tiles[gc.x][gc.y].isCollidable();
    }

    @Override
    public boolean isCollidable(double x, double y) {
        if (!isInsideMap(x,y)) //La vérification dans la version GC n'est pas suffisante
            return true;       //car si x= -31, gc.x = 0 donc pas de détection

        GridCoordinates gc = toGridCoordinates(x, y);
        return tiles[gc.x][gc.y].isCollidable();
    }

    @Override
    public boolean isExplodable(GridCoordinates gc) {
        return tiles[gc.x][gc.y] instanceof ExplodableTile;
    }

    @Override
    public boolean isExploding(GridCoordinates gc) {
        return isExplodable(gc) && ((ExplodableTile)tiles[gc.x][gc.y]).isExploding();
    }

    @Override
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

    @Override
    public int getExplosionTimeRemaining(GridCoordinates gc) {
        return ((ExplodableTile)tiles[gc.x][gc.y]).getExplosionTimeRemaining();
    }

    @Override
    public TileType getTileType(GridCoordinates gc) {
        return tiles[gc.x][gc.y].getType();
    }

    @Override
    public TileType getTileType(double x, double y) {
        return getTileType(toGridCoordinates(x, y));
    }

    @Override
    public BonusType getBonusType(GridCoordinates gc) {
        return(((BonusTile)(tiles[gc.x][gc.y])).getBonusType());
    }

    @Override
    public BonusType getBonusType(double x, double y) {
        return getBonusType(toGridCoordinates(x, y));
    }

    @Override
    public Direction getArrowDirection(GridCoordinates gc) {
        return(((ArrowTile)(tiles[gc.x][gc.y])).getDirection());
    }

    @Override
    public Direction getArrowDirection(double x, double y) {
        return getArrowDirection(toGridCoordinates(x, y));
    }

    @Override
    public List<GridCoordinates> getSpawningLocations() {
        return Collections.unmodifiableList(spawningLocations);
    }

    /**
     * Ajoute une zone d'apparition pour les joueurs sur la carte
     * @param gc Les coordonnées de la case qui deviendra une zone d'apparition
     */
    public void addSpawningLocation(GridCoordinates gc) {
        if (!isInsideMap(gc))
            throw new RuntimeException("Coordonnées du lieu de spawn invalides : " + gc);
        spawningLocations.add(gc);
    }

    public void addSpawningLocation(int index, GridCoordinates gc) {
        if (!isInsideMap(gc))
            throw new RuntimeException("Coordonnées du lieu de spawn invalides : " + gc);
        spawningLocations.add(index, gc);
    }

    public void removeSpawningLocation(GridCoordinates gc) {
        spawningLocations.remove(gc);
    }

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

    public void setSize(int columns, int rows) {
        if (columns <= 0)
            throw new RuntimeException("Nombre de colonnes négatif ou nul : " + columns);
        else if (rows <= 0)
            throw new RuntimeException("Nombre de lignes négatif ou nul : " + rows);

        Tile[][] newTiles = new Tile[columns][rows];

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (i < getColumnCount() && j < getRowCount())
                    newTiles[i][j] = tiles[i][j];
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

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Charge une carte
     * @param sc Scanner support du chargement de la carte
     * @throws InputMismatchException Erreur liée au chargement de la carte
     */
    void loadMap(Scanner sc) throws InputMismatchException {
        int columnCount = sc.nextInt();
        int rowCount = sc.nextInt();

        Tile[][] newTiles = new Tile[columnCount][rowCount];

        int spawningLocationsCount = sc.nextInt();
        spawningLocations = new ArrayList<GridCoordinates>(spawningLocationsCount);
        for (int i = 0; i < spawningLocationsCount; i++)
            spawningLocations.add(new GridCoordinates(sc.nextInt(), sc.nextInt()));

        TileType[] types = TileType.values();
        BonusType[] bonuses = BonusType.values();
        Direction[] directions = Direction.values();

        GridCoordinates grid = new GridCoordinates();

        for (grid.x = 0; grid.x < columnCount; grid.x++) {
            for (grid.y = 0; grid.y < rowCount; grid.y++) {
                TileType type = types[sc.nextInt()];
                setTileType(newTiles, type, grid);

                if (type == TileType.Bonus)
                    ((BonusTile)newTiles[grid.x][grid.y]).setBonusType(bonuses[sc.nextInt()]);
                else if (type == TileType.Arrow)
                    ((ArrowTile)newTiles[grid.x][grid.y]).setDirection(directions[sc.nextInt()]);
            }
        }

        sc.close();

        tiles = newTiles;
    }

    /**
     * Charge une carte
     * @param map Entrée depuis un String
     * @throws InputMismatchException Erreur liée au chargement de la carte
     */
    public void loadMap(String map) throws InputMismatchException {
        loadMap(new Scanner(map));
    }

    /**
     * Charge une carte
     * @param path Entrée depuis un Path
     * @throws IOException Erreur lors de l'ouverture du fichier
     * @throws InputMismatchException Erreur liée au chargement de la carte
     */
    public void loadMap(Path path) throws IOException, InputMismatchException {
        loadMap(new Scanner(path));
    }

    /**
     * Charge une carte
     * @param map Entrée depuis un flux entrant
     * @throws InputMismatchException Erreur liée au chargement de la carte
     */
    public void loadMap(InputStream map) throws InputMismatchException {
        loadMap(new Scanner(map));
    }

    /**
     * Sauvegarde une carte sous forme de String
     * @return Le String correspondant la carte
     */
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

                if (type == TileType.Bonus)
                    content.add(String.valueOf(((BonusTile)tiles[i][j]).getBonusType().ordinal()));
                else if (type == TileType.Arrow)
                    content.add(String.valueOf(((ArrowTile)tiles[i][j]).getDirection().ordinal()));
            }
        }
        return content.toString();
    }

    Tile newTile(TileType type) {
        return type.newTile();
    }

    public void setTileType(TileType type, GridCoordinates gc) {
        setTileType(tiles, type, gc);
    }

    public void setTileType(TileType type, double x, double y) {
        setTileType(type, toGridCoordinates(x, y));
    }

    private void setTileType(Tile[][] tiles, TileType type, GridCoordinates gc) {
        Tile newTile = newTile(type);

        if (tiles[gc.x][gc.y] != null)
            newTile.setEntities(tiles[gc.x][gc.y].getEntities());
        tiles[gc.x][gc.y] = newTile;
    }

    public void setBonusType(BonusType type, GridCoordinates gc) {
        ((BonusTile)(tiles[gc.x][gc.y])).setBonusType(type);
    }

    public void setBonusType(BonusType type, double x, double y) {
        setBonusType(type, toGridCoordinates(x, y));
    }

    public void setArrowDirection(Direction direction, GridCoordinates gc) {
        ((ArrowTile)(tiles[gc.x][gc.y])).setDirection(direction);
    }

    public void setArrowDirection(Direction direction, double x, double y) {
        setArrowDirection(direction, toGridCoordinates(x, y));
    }

    void setExplosion(int duration, ExplosionType type, Direction direction, GridCoordinates gc) {
        ((ExplodableTile)tiles[gc.x][gc.y]).explode(duration, type, direction);
    }

    void setExplosionEnd(GridCoordinates gc) {
        ((ExplodableTile)tiles[gc.x][gc.y]).setLastExplosionEnd();
    }

    @Override
    public List<Entity> getEntities(GridCoordinates gc) {
        return Collections.unmodifiableList(tiles[gc.x][gc.y].getEntities());
    }

    @Override
    public List<Entity> getEntities(double x, double y) {
        return getEntities(toGridCoordinates(x, y));
    }

    @Override
    public boolean hasBomb(GridCoordinates gc) {
        List<Entity> entities = getEntities(gc);
        for (Entity entity : entities) {
            if (entity instanceof Bomb)
                return true;
        }
        return false;
    }

    @Override
    public boolean hasBomb(double x, double y) {
        return hasBomb(toGridCoordinates(x, y));
    }

    @Override
    public Bomb getFirstBomb(GridCoordinates gc) {
        List<Entity> entities = getEntities(gc);
        for (Entity entity : entities) {
            if (entity instanceof Bomb)
                return (Bomb)entity;
        }
        return null;
    }

    @Override
    public Bomb getFirstBomb(double x, double y) {
        return getFirstBomb(toGridCoordinates(x, y));
    }

    Tile[][] getTiles() {
        return this.tiles;
    }

    void addEntity(Entity entity) {
        GridCoordinates gc = toGridCoordinates(entity.getX(), entity.getY());
        tiles[gc.x][gc.y].addEntity(entity);
    }

    /**
     * Met à jour la carte
     */
    void update() {
        GridCoordinates gc = new GridCoordinates();
        for(gc.x=0; gc.x<getColumnCount(); gc.x++){
            for(gc.y=0; gc.y<getRowCount(); gc.y++){
                //on parcours le tableau et on update les cases
               setTile(tiles[gc.x][gc.y].update(), gc);
               updateEntities(gc);
            }
        }
    }

    void setTile(Tile tile, GridCoordinates gc) {
        tiles[gc.x][gc.y] = tile;
    }

    /**
     * Met à jour les entités contenues dans une case de grille
     * @param gc les coordonnées de la case étudiée
     */
    private void updateEntities(GridCoordinates gc) {
        Iterator<Entity> iterator = tiles[gc.x][gc.y].entities.iterator();
        while(iterator.hasNext()){
            //parcours les entités pour virer ceux qui sont à remove
            Entity entity = iterator.next();
            if(entity.isToRemove()){
                iterator.remove();
            } else {
                GridCoordinates pos = toGridCoordinates(entity.x, entity.y);
                if (!gc.equals(pos)) {
                    //parcours les entités et déplace puis remove celles qui sont pas dans la bonne case
                    tiles[pos.x][pos.y].addEntity(entity);
                    iterator.remove();
                }
            }
        }
    }

}
