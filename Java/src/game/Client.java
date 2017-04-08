package game;

import java.net.InetAddress;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import network.GameInfo;
import network.Network.CommandMap;
import network.Network.ControllerPlayer;
import network.Network.ControllerUpdate;
import network.Network.PlayerName;
import network.Network.Restart;
import network.Network.RoundEnded;
import network.Network.TimeRemaining;
import network.Network.ToRemove;
import network.Network.WarmupTimeRemaining;
import network.Network;

/**
 * Cette classe gère une partie de type Client
 */
@objid ("b8264f8e-49d1-4a58-ad20-06ceafdee906")
public class Client extends World implements Listener {
    @objid ("07603ddf-f608-4cd6-8206-fe5e7ed74af2")
     boolean init;

    @objid ("dfe866bc-4673-4e51-a1c6-849dc4b42e49")
     boolean roundEnded;

    @objid ("69b14909-1260-4d7a-8292-5d025967581b")
     boolean pickUp;

    @objid ("e75ff67a-fbd6-46ab-b982-d0131ef1a0c4")
     boolean explosion;

    @objid ("a6a97511-69d5-4a0d-8b2f-046814038a98")
     com.esotericsoftware.kryonet.Client network;

    Queue<Object> messages;
    
    /**
     * Construit un client qui se connecte à l'adresse spécifiée
     * @param address Adresse du serveur
     * @throws Exception si échec de la connexion
     */
    @objid ("63d8fe40-31e8-480c-82ff-640b5412de27")
    public Client(InetAddress address) throws Exception {
        map = new Map(32);
        
        messages = new ConcurrentLinkedQueue<Object>();

        init = false;
        
        network = new com.esotericsoftware.kryonet.Client(16384, 8192);
        Network.register(network);
        
        network.addListener(this);
        
        network.start();
        
        if (address == null)
            address = network.discoverHost(Network.udpPort, 2500);
        
        if (address != null)
            network.connect(1500, address, Network.tcpPort, Network.udpPort);
        else
            throw new Exception("Pas de serveur trouvé...");
    }

    /**
     * Construit un client et se connecte au premier serveur trouvé
     * @throws Exception si échec de la connexion
     */
    @objid ("a0b86d60-e56c-4c1c-900d-85fb1d6cd53c")
    public Client() throws Exception {
        this(null);
    }

    @objid ("2116c198-f003-4fbb-8e6b-4ccf16f7f093")
    @Override
    void plantBomb(Player player) {
        // TODO Auto-generated method stub
    }

    @objid ("bd713b54-d5d8-4ff5-a65a-113198425a75")
    @Override
    void createExplosion(Bomb bomb) {
        explosion = true;
    }

    @objid ("269ca09e-9010-4afc-ac4f-aa1b040e55f1")
    @Override
    void pickUpBonus(double x, double y) {
        pickUp = true;
    }

    @objid ("162a4f81-a941-4f21-aaaf-afd832486f5b")
    @Override
    public void newController(Controller controller) {
        controllers.add(controller);
        network.sendTCP(new Network.AddController(controller.getName()));
    }

    @objid ("463bec21-830d-412a-8728-f570ba668f52")
    @Override
    public void update() {
        if (!init)
            return;
        
        for (int i = 0; i < controllers.size(); i++) {
            network.sendUDP(new ControllerUpdate(i, controllers.get(i)));
        }

        Object message = null;
        while ((message = messages.poll()) != null) {
            processMessage(message);
        }
        
        super.update();
        
        if (pickUp) {
            fireEvent(Event.PickUp);
            pickUp = false;
        }
        
        if (explosion) {
            fireEvent(Event.Explosion);
            explosion = false;
        }
    }

    @objid ("9cc03884-80cf-4f76-8510-8f2ca2035eef")
    @Override
    void kickBomb(Bomb bomb, Direction direction) {
        // TODO Auto-generated method stub
    }

    @objid ("21a09aee-03f8-43e6-9cbe-72aeaccddf27")
    @Override
    public boolean isReady() {
        return init;
    }

    @objid ("2ce88ea4-d683-46a3-be3f-2cd53554bdb0")
    @Override
    public boolean isRoundEnded() {
        return roundEnded || !network.isConnected();
    }

    /**
     * @return si le client est connecté à un serveur
     */
    @objid ("78cbc042-3577-4058-a504-77bad8d090c9")
    public boolean isConnected() {
        return network.isConnected();
    }

    @objid ("690dc1ec-de6c-4bf8-953d-857fdd810841")
    @Override
    void addEntity(Entity entity, int id) {
        Entity existing = entities.get(id);
        if (existing != null) {
            existing.updateFrom(entity);
        } else {
            if (entity instanceof Player) {
                Player player = ((Player)entity);
                player.setController(new FollowController());
            }
            
            super.addEntity(entity, id);
        }
    }

    @objid ("9ad97afa-1570-4a4a-8ef4-09bfb11ff449")
    @Override
    public void stop() {
        network.close();
    }

    @objid ("b9510250-2586-427f-88f4-0ef51d2c9232")
    @Override
    public void restart() throws Exception {
        super.restart();
        roundEnded = false;
    }

    @objid ("3114b1af-c101-4ac6-bf27-c61d6175cac7")
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GameInfo) {
            GameInfo info = (GameInfo)object;
            this.fps = info.fps;
            this.duration = info.duration;
            this.timeRemaining = info.timeRemaining;
            this.warmupDuration = info.warmupDuration;
            this.warmupTimeRemaining = info.warmupTimeRemaining;
            this.round = info.round;
            this.map.setTileSize(info.tileSize);
            this.map.loadMap(info.map);
            if (this.timeRemaining < 0)
                fireEvent(Event.SuddenDeath);
            init = true;
        } else if (init)
            messages.offer(object);
    }
    
    private void processMessage(Object object) {  
        if (object instanceof CommandMap) {
            DeltaMap.executeDelta((CommandMap)object, map);
        } else if (object instanceof Entity) {           
            addEntity((Entity) object, ((Entity)object).getID());
        } else if (object instanceof PlayerName) {
            PlayerName playerName = (PlayerName)object;
            Entity entity = entities.get(playerName.entityId);
            if (entity != null && entity instanceof Player) {
                ((Player)entity).getController().setName(playerName.name);
            }
        } else if (object instanceof ControllerPlayer) {
            ControllerPlayer cP = (ControllerPlayer) object;
            Entity entity = entities.get(cP.entityId);
            if (entity != null && entity instanceof Player && cP.controllerId >= 0 && cP.controllerId < controllers.size()) {
                ((Player)entity).setController(controllers.get(cP.controllerId));
            }
        } else if (object instanceof TimeRemaining) {
            int newTime = ((TimeRemaining)object).timeRemaining;
            if (timeRemaining > 0 && newTime < 0)
                fireEvent(Event.SuddenDeath);
            
            timeRemaining = newTime;
        } else if (object instanceof WarmupTimeRemaining) {
            warmupTimeRemaining = ((WarmupTimeRemaining)object).warmupTimeRemaining;
        } else if (object instanceof RoundEnded) {
            roundEnded = true;
        } else if (object instanceof Restart) {
            try {
                restart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (object instanceof ToRemove) {
            ToRemove toRemove = (ToRemove)object;
            for (Integer id : toRemove.toRemove)
                entities.remove(id);
        } else if (object instanceof List<?>) {
            if (((List<?>)object).isEmpty())
                return;
            
            if (((List<?>) object).get(0) instanceof CommandMap)
                DeltaMap.executeDeltas((List<CommandMap>)object, map);
            else if (((List<?>) object).get(0) instanceof Entity) {
                for (Entity entity : (List<Entity>) object) {
                    addEntity(entity, entity.getID());
                }
            }
        }
    }

}
