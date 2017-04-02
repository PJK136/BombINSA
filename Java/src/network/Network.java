package network;

import java.util.ArrayList;
import java.util.LinkedList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import game.Bomb;
import game.BonusType;
import game.Controller;
import game.Direction;
import game.Entity;
import game.ExplosionType;
import game.GridCoordinates;
import game.Player;
import game.PlayerAbility;
import game.TileType;

public class Network {
    static public final int tcpPort = 7373;
    static public final int udpPort = 7373;

    // This registers objects that are going to be sent over the network.
    static public void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Object.class);
        kryo.register(Object[].class);
        kryo.register(String.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(CommandMap.class);
        kryo.register(CommandMap.Name.class);
        kryo.register(TileType.class);
        kryo.register(BonusType.class);
        kryo.register(Direction.class);
        kryo.register(ExplosionType.class);
        kryo.register(GridCoordinates.class);
        kryo.register(Entity.class);
        kryo.register(Player.class);
        kryo.register(PlayerAbility.class);
        kryo.register(Bomb.class);
        
        kryo.register(GameInfo.class);
        kryo.register(AddController.class);
        kryo.register(ControllerUpdate.class);
        kryo.register(PlayerName.class);
        kryo.register(ControllerPlayer.class);
        kryo.register(TimeRemaining.class);
        kryo.register(WarmupTimeRemaining.class);
        kryo.register(RoundEnded.class);
        kryo.register(Restart.class);
        kryo.register(ToRemove.class);
    }
    
    public static class CommandMap {

        public enum Name {
            loadMap,
            setTileType,
            setBonusType,
            setArrowDirection,
            setExplosion,
            setExplosionEnd,
        }
        
        public Name name;
        public Object[] args;
        
        public CommandMap() {
            
        }
        
        public CommandMap(Name name, Object...objects) {
            this.name = name;
            args = objects;
        }
    }
    
    public static class AddController {
        String name;
        
        public AddController() {}
        public AddController(String name) { this.name = name; }
    }
    
    public static class ControllerUpdate {
        public int id;
        public Direction direction;
        public boolean bombing;
        
        public ControllerUpdate() { }
        public ControllerUpdate(int id, Controller controller) {
            this.id = id;
            this.direction = controller.getDirection();
            this.bombing = controller.isPlantingBomb();
        }
    }
    
    public static class PlayerName {
        public int entityId;
        public String name;
        
        public PlayerName() {}
        public PlayerName(int entityId, String name) {
            this.entityId = entityId;
            this.name = name;
        }
    }
    
    public static class ControllerPlayer {
        public int controllerId;
        public int entityId;
        
        public ControllerPlayer() {}
        public ControllerPlayer(int controllerId, int entityId) {
            this.controllerId = controllerId;
            this.entityId = entityId;
        }
    }
    
    public static class TimeRemaining {
        public int timeRemaining;
        
        public TimeRemaining() {}
        public TimeRemaining(int timeRemaining) { this.timeRemaining = timeRemaining; }
    }
    
    public static class WarmupTimeRemaining {
        public int warmupTimeRemaining;
        
        public WarmupTimeRemaining() {}
        public WarmupTimeRemaining(int warmupTimeRemaining) { this.warmupTimeRemaining = warmupTimeRemaining; }
    }
    
    public static class ToRemove {
        public ArrayList<Integer> toRemove;
        
        public ToRemove() { this.toRemove = new ArrayList<Integer>(); }
        public ToRemove(int capacity) { this.toRemove = new ArrayList<Integer>(capacity); }
        public ToRemove(ArrayList<Integer> toRemove) { this.toRemove = toRemove; }
    }
    
    public static class RoundEnded {
        
    }

    public static class Restart {
        
    }
    
    private Network() {
        // TODO Auto-generated constructor stub
    }

}
