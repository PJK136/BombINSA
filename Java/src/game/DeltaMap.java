package game;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import network.Network.CommandMap;

@objid ("12a42e28-45db-4083-a2d6-44f180711817")
public class DeltaMap extends Map {
    @objid ("8513eb13-92ef-4f66-b5dc-d2b6de90c84b")
     List<CommandMap> deltas;

    @objid ("ba393edb-4bdc-404e-a892-60d26b6d4fe2")
    public DeltaMap(int tileSize) {
        super(tileSize);
        deltas = new LinkedList<CommandMap>();
    }

    @objid ("b61d4be9-c0fa-4d56-b0c0-99997a3fd7c5")
    public DeltaMap(int columns, int rows, int tileSize) {
        super(columns, rows, tileSize);
        deltas = new LinkedList<CommandMap>();
    }

    @objid ("e7f0c674-34e5-4026-9500-3e7d5d9dd3e4")
    @Override
    void loadMap(Scanner sc) throws InputMismatchException {
        super.loadMap(sc);
        deltas.add(new CommandMap(CommandMap.Name.loadMap, saveMap()));
    }

    @objid ("f28abebd-0aeb-4aa5-9764-636b33d519a4")
    @Override
    public void setTileType(TileType type, GridCoordinates gc) {
        super.setTileType(type, gc);
        deltas.add(new CommandMap(CommandMap.Name.setTileType, type, new GridCoordinates(gc)));
    }

    @objid ("ccfd115d-f352-4a10-8455-4f0a790be20a")
    @Override
    public void setBonusType(BonusType type, GridCoordinates gc) {
        super.setBonusType(type, gc);
        deltas.add(new CommandMap(CommandMap.Name.setBonusType, type, new GridCoordinates(gc)));
    }

    @objid ("f5a3e8cc-5512-4f72-9c72-44f3c04f8b5e")
    @Override
    public void setArrowDirection(Direction direction, GridCoordinates gc) {
        super.setArrowDirection(direction, gc);
        deltas.add(new CommandMap(CommandMap.Name.setArrowDirection, direction, new GridCoordinates(gc)));
    }

    @objid ("f54dd2fd-cb50-4922-af2f-1b03b84d701a")
    @Override
    void setExplosion(int duration, ExplosionType type, Direction direction, GridCoordinates gc) {
        super.setExplosion(duration, type, direction, gc);
        deltas.add(new CommandMap(CommandMap.Name.setExplosion, duration, type, direction, new GridCoordinates(gc)));
    }

    @objid ("0447ca0d-ad8a-4004-ac97-ea09fd18b70e")
    @Override
    void setExplosionEnd(GridCoordinates gc) {
        super.setExplosionEnd(gc);
        deltas.add(new CommandMap(CommandMap.Name.setExplosionEnd, new GridCoordinates(gc)));
    }

    @objid ("110a446a-967c-4b04-9d81-3dccc8738f46")
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

    @objid ("37f96f5b-ab0d-4d97-bf2c-f25c7efdb432")
    public static void executeDelta(CommandMap command, Map map) {
        switch (command.name) {
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
            map.setExplosion((int)command.args[0], (ExplosionType)command.args[1], (Direction)command.args[2], (GridCoordinates)command.args[3]);
            break;
        case setExplosionEnd:
            map.setExplosionEnd((GridCoordinates)command.args[0]);
            break;
        default:
            break;
        }
    }

    @objid ("a389a7b1-e2a5-445c-b765-4950785c76f5")
    public static void executeDeltas(List<CommandMap> deltas, Map map) {
        for (CommandMap command : deltas) {
            executeDelta(command, map);
        }
    }

}
