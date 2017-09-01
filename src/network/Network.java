package network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import game.Bomb;
import game.BonusType;
import game.Character;
import game.CharacterAbility;
import game.Controller;
import game.Direction;
import game.Entity;
import game.ExplosionType;
import game.GridCoordinates;
import game.Player;
import game.TileType;

/**
 * Cette classe contient toutes les classes "messages" que s'envoient le serveur et le client
 */
public class Network {
    public static final int tcpPort = 7373;

    public static final int udpPort = 7373;

// This registers objects that are going to be sent over the network.
    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Object.class);
        kryo.register(Object[].class);
        kryo.register(String.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(CommandMap.class);
        kryo.register(CommandMap.Name.class);
        kryo.register(Player.class);
        kryo.register(TileType.class);
        kryo.register(BonusType.class);
        kryo.register(Direction.class);
        kryo.register(ExplosionType.class);
        kryo.register(GridCoordinates.class);
        kryo.register(Entity.class);
        kryo.register(Character.class);
        kryo.register(CharacterAbility.class);
        kryo.register(Bomb.class);
        
        kryo.register(GameInfo.class);
        kryo.register(PlayerInfo.class);
        kryo.register(EntityPlayer.class);
        kryo.register(AddController.class);
        kryo.register(ControllerPlayer.class);
        kryo.register(ControllerUpdate.class);
        kryo.register(TimeRemaining.class);
        kryo.register(WarmupTimeRemaining.class);
        kryo.register(RoundEnded.class);
        kryo.register(NextRound.class);
        kryo.register(EntityToRemove.class);
        kryo.register(PlayerToRemove.class);
        kryo.register(EntityUpdateList.class);
    }

    private Network() {
    }

    public static class CommandMap {
        public Name name;

        public Object[] args;

        public CommandMap() {
        }

        public CommandMap(Name name, Object... objects) {
            this.name = name;
            args = objects;
        }

        public enum Name {
            loadMap,
            setTileType,
            setBonusType,
            setArrowDirection,
            setExplosion,
            setExplosionEnd;
        }

    }
    
    public static class PlayerInfo {
        public Player player;
        public String name;
        public int entityId;

        public PlayerInfo() {
        }

        public PlayerInfo(Player player, String name, int entityId) {
            this.player = player;
            this.name = name;
            this.entityId = entityId;
        }
    }
    
    public static class EntityPlayer {
        public int entityId;
        public int playerId;
        
        public EntityPlayer() {
        }
        
        public EntityPlayer(int entityId, int playerId) {
            this.entityId = entityId;
            this.playerId = playerId;
        }
    }

    public static class AddController {
        public String name;

        public AddController() {
        }

        public AddController(String name) {
            this.name = name;
        }

    }
    
    public static class ControllerPlayer {
        public int controllerId;

        public int playerId;

        public ControllerPlayer() {
        }

        public ControllerPlayer(int controllerId, int playerId) {
            this.controllerId = controllerId;
            this.playerId = playerId;
        }

    }

    public static class ControllerUpdate {
        public int id;

        public Direction direction;

        public boolean bombing;

        public ControllerUpdate() {
        }

        public ControllerUpdate(int id, Controller controller) {
            this.id = id;
            this.direction = controller.getDirection();
            this.bombing = controller.isPlantingBomb();
        }

    }    

    public static class TimeRemaining {
        public int timeRemaining;

        public TimeRemaining() {
        }

        public TimeRemaining(int timeRemaining) {
            this.timeRemaining = timeRemaining;
        }

    }

    public static class WarmupTimeRemaining {
        public int warmupTimeRemaining;

        public WarmupTimeRemaining() {
        }

        public WarmupTimeRemaining(int warmupTimeRemaining) {
            this.warmupTimeRemaining = warmupTimeRemaining;
        }

    }

    public static class EntityToRemove {
        public ArrayList<Integer> toRemove;

        public EntityToRemove() {
            this.toRemove = new ArrayList<Integer>();
        }

        public EntityToRemove(int capacity) {
            this.toRemove = new ArrayList<Integer>(capacity);
        }

        public EntityToRemove(ArrayList<Integer> toRemove) {
            this.toRemove = toRemove;
        }

    }
    
    public static class PlayerToRemove {
        public ArrayList<Integer> toRemove;

        public PlayerToRemove() {
            this.toRemove = new ArrayList<Integer>();
        }

        public PlayerToRemove(int capacity) {
            this.toRemove = new ArrayList<Integer>(capacity);
        }

        public PlayerToRemove(ArrayList<Integer> toRemove) {
            this.toRemove = toRemove;
        }

    }

    public static class RoundEnded {
        public String winnerName;
        public int winnerID;
        
        public RoundEnded() {
        }
        
        public RoundEnded(String winnerName, int winnerID) {
            this.winnerName = winnerName;
            this.winnerID = winnerID;
        }
    }

    public static class NextRound {
    }

    public static class EntityUpdateList {
        public int timestamp;
        public ArrayList<Entity> entities;
        
        public EntityUpdateList() {
        }
        
        public EntityUpdateList(int timestamp, List<Entity> entities) {
            this.timestamp = timestamp;
            this.entities = new ArrayList<>(entities);
        }
    }

}
