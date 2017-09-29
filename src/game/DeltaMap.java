package game;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import network.Network.CommandMap;

/**
 * Carte avec historique
 */
public class DeltaMap extends Map {
     List<CommandMap> deltas = new LinkedList<CommandMap>();

    /**
     * Construit une carte avec historique
     * @param tileSize Taille des tuiles
     */
    public DeltaMap(int tileSize) {
        super(tileSize);
    }

    /**
     * Construit une carte avec historique
     * @param columns Nombre de colonnes
     * @param rows Nombre de lignes
     * @param tileSize Taille des tuiles
     */
    public DeltaMap(int columns, int rows, int tileSize) {
        super(tileSize);
        setSize(columns, rows);
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        deltas.add(new CommandMap(CommandMap.Name.setName, name));
    }

    @Override
    void loadMap(Scanner sc) throws InputMismatchException {
        super.loadMap(sc);
        deltas.add(new CommandMap(CommandMap.Name.loadMap, saveMap()));
    }

    @Override
    public void setTileType(TileType type, GridCoordinates gc) {
        super.setTileType(type, gc);
        deltas.add(new CommandMap(CommandMap.Name.setTileType, type, new GridCoordinates(gc)));
    }

    @Override
    public void setBonusType(BonusType type, GridCoordinates gc) {
        super.setBonusType(type, gc);
        deltas.add(new CommandMap(CommandMap.Name.setBonusType, type, new GridCoordinates(gc)));
    }

    @Override
    public void setArrowDirection(Direction direction, GridCoordinates gc) {
        super.setArrowDirection(direction, gc);
        deltas.add(new CommandMap(CommandMap.Name.setArrowDirection, direction, new GridCoordinates(gc)));
    }

    @Override
    void setExplosion(int duration, ExplosionType type, Direction direction, Character owner, GridCoordinates gc) {
        super.setExplosion(duration, type, direction, owner, gc);
        deltas.add(new CommandMap(CommandMap.Name.setExplosion, duration, type, direction, new GridCoordinates(gc)));
    }

    @Override
    void setExplosionEnd(GridCoordinates gc) {
        super.setExplosionEnd(gc);
        deltas.add(new CommandMap(CommandMap.Name.setExplosionEnd, new GridCoordinates(gc)));
    }

    @Override
    void setTile(Tile tile, GridCoordinates gc) {
        if (tiles[gc.x][gc.y] != tile) {
            deltas.add(new CommandMap(CommandMap.Name.setTileType, tile.getType(), new GridCoordinates(gc)));
            if (tile.getType() == TileType.Bonus)
                deltas.add(new CommandMap(CommandMap.Name.setBonusType, ((BonusTile)tile).getBonusType(), new GridCoordinates(gc)));
            else if  (tile.getType() == TileType.Arrow)
                deltas.add(new CommandMap(CommandMap.Name.setArrowDirection, ((ArrowTile)tile).getDirection(), new GridCoordinates(gc)));
        }
        super.setTile(tile, gc);
    }

    /**
     * Applique une commande sur la map
     * @param command Commande à exécuter
     * @param map Map sur laquelle exécuter
     */
    public static void executeDelta(CommandMap command, Map map) {
        switch (command.name) {
        case setName:
            map.setName((String)command.args[0]);
            break;
        case loadMap:
            map.loadMap((String)command.args[0]);
            break;
        case setTileType:
            map.setTileType((TileType)command.args[0], (GridCoordinates)command.args[1]);
            break;
        case setBonusType:
            map.setBonusType((BonusType)command.args[0], (GridCoordinates)command.args[1]);
            break;
        case setArrowDirection:
            map.setArrowDirection((Direction)command.args[0], (GridCoordinates)command.args[1]);
            break;
        case setExplosion:
            map.setExplosion((int)command.args[0], (ExplosionType)command.args[1], (Direction)command.args[2], null, (GridCoordinates)command.args[3]);
            break;
        case setExplosionEnd:
            map.setExplosionEnd((GridCoordinates)command.args[0]);
            break;
        default:
            break;
        }
    }

    /**
     * Charge l'historique sur la carte
     * @param deltas Historique à charger
     * @param map Carte sur laquelle exécuter
     */
    public static void executeDeltas(List<CommandMap> deltas, Map map) {
        for (CommandMap command : deltas) {
            executeDelta(command, map);
        }
    }

}
