package network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

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

/**
 * Cette classe contient toutes les classes "messages" que s'envoient le serveur et le client
 */
@objid ("9c7be3f0-b12b-4d48-bdb0-4cf286ca0add")
public class Network {
    @objid ("739b2928-14e9-43fe-9402-0c6b02ea8899")
    public static final int tcpPort = 7373;

    @objid ("dce5e286-19dc-41ee-ac67-8d3daaaf7dd9")
    public static final int udpPort = 7373;

// This registers objects that are going to be sent over the network.
    @objid ("df26e6f8-19a3-4113-8bf7-3cb784324d65")
    public static void register(EndPoint endPoint) {
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
        kryo.register(NextRound.class);
        kryo.register(ToRemove.class);
        kryo.register(EntityUpdateList.class);
    }

    @objid ("0b43c40b-21a6-4743-a9fa-9c109b61369c")
    private Network() {
    }

    @objid ("7e47a4ec-478d-44d5-af9b-d630535a6c76")
    public static class CommandMap {
        @objid ("9781aca1-2704-4485-b6a5-68928c38ed9f")
        public Name name;

        @objid ("7c2a1c62-0a46-497c-ace1-83e51222de81")
        public Object[] args;

        @objid ("c43393e3-534d-4770-b1e2-f9e6afa79ebb")
        public CommandMap() {
        }

        @objid ("f049e688-e395-47c7-b042-da27490f98f4")
        public CommandMap(Name name, Object... objects) {
            this.name = name;
            args = objects;
        }

        @objid ("83b2a48c-cd66-44d4-9f6b-aeaead64bfb2")
        public enum Name {
            loadMap,
            setTileType,
            setBonusType,
            setArrowDirection,
            setExplosion,
            setExplosionEnd;
        }

    }

    @objid ("04f0f570-85ae-4081-ae1f-8505cbd85c74")
    public static class AddController {
        @objid ("2aabf79f-7bfb-4e1a-a23c-2a019d7eb701")
         String name;

        @objid ("b1adaae9-8773-4cfe-b26b-5e6bfd85a6c2")
        public AddController() {
        }

        @objid ("5622d5e5-272a-4233-889d-be5a90f26ad0")
        public AddController(String name) {
            this.name = name;
        }

    }

    @objid ("ca56f542-84ac-42a0-893c-c3e1a2c85301")
    public static class ControllerUpdate {
        @objid ("84b297ec-db6c-4740-951c-12c0d8ca5251")
        public int id;

        @objid ("587e543c-51cf-4412-a75c-404bb216e552")
        public Direction direction;

        @objid ("34f4a42f-d880-4699-befc-66f42767895b")
        public boolean bombing;

        @objid ("9a1d128d-4e9c-4a63-a714-eddebe6dee2d")
        public ControllerUpdate() {
        }

        @objid ("d5a6d7ad-a65e-41ba-95e0-b8c42608535a")
        public ControllerUpdate(int id, Controller controller) {
            this.id = id;
            this.direction = controller.getDirection();
            this.bombing = controller.isPlantingBomb();
        }

    }

    @objid ("c46ca67a-b355-43ff-9bae-963528b54129")
    public static class PlayerName {
        @objid ("906902b3-3782-440c-8866-27ba3c708d72")
        public int entityId;

        @objid ("301d5c35-373d-4613-8fe4-ff56b26a645f")
        public String name;

        @objid ("94b1413e-8966-4794-b0ed-b069c23044b0")
        public PlayerName() {
        }

        @objid ("19ce93e0-81d1-4aae-b518-7a56310aa842")
        public PlayerName(int entityId, String name) {
            this.entityId = entityId;
            this.name = name;
        }

    }

    @objid ("ee3a28ab-0e4f-47d1-a03a-788e569f76f4")
    public static class ControllerPlayer {
        @objid ("1e08dab4-e231-4cc6-b50f-76fc7e0f81a1")
        public int controllerId;

        @objid ("e72e891d-09f1-4228-80da-998184ebc556")
        public int entityId;

        @objid ("96ac45fd-aa58-4f95-a943-15617048df3d")
        public ControllerPlayer() {
        }

        @objid ("f1a29548-642f-465c-8f91-f34e8d690c90")
        public ControllerPlayer(int controllerId, int entityId) {
            this.controllerId = controllerId;
            this.entityId = entityId;
        }

    }

    @objid ("14b90368-d1a8-466e-a211-78822ba454dd")
    public static class TimeRemaining {
        @objid ("f9c66a4c-24e1-4e7a-9aad-904fa35c344b")
        public int timeRemaining;

        @objid ("84cb2a26-8520-4e0d-84ef-17ce0ca7b79a")
        public TimeRemaining() {
        }

        @objid ("89c35d87-9571-41d4-b8e7-0be1fbb8d18b")
        public TimeRemaining(int timeRemaining) {
            this.timeRemaining = timeRemaining;
        }

    }

    @objid ("fc16095a-226a-424c-904f-38205833f3d0")
    public static class WarmupTimeRemaining {
        @objid ("2b669cbb-8f4d-43a6-a967-cb77fbabc97d")
        public int warmupTimeRemaining;

        @objid ("890a2a4b-46b8-4b1a-a6f7-f50752e69ead")
        public WarmupTimeRemaining() {
        }

        @objid ("95da5c40-440a-4935-8cf5-5da4ab96a757")
        public WarmupTimeRemaining(int warmupTimeRemaining) {
            this.warmupTimeRemaining = warmupTimeRemaining;
        }

    }

    @objid ("95e2a1da-5201-47ab-bb6f-4364dadbfd58")
    public static class ToRemove {
        @objid ("0998e812-e5c1-495e-ab72-30f15650895f")
        public ArrayList<Integer> toRemove;

        @objid ("9c1d39e6-0541-4ea8-803c-00718d72ba98")
        public ToRemove() {
            this.toRemove = new ArrayList<Integer>();
        }

        @objid ("f82d193d-d11d-4007-afab-9077136ee9b6")
        public ToRemove(int capacity) {
            this.toRemove = new ArrayList<Integer>(capacity);
        }

        @objid ("021c1e4b-a6a4-4bfc-97da-dc51489d2328")
        public ToRemove(ArrayList<Integer> toRemove) {
            this.toRemove = toRemove;
        }

    }

    @objid ("45b35dad-4e06-4eaf-9e28-1a60eaf74c41")
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

    @objid ("9e52f3cd-68c3-49d2-bdab-4955712ea8c3")
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
