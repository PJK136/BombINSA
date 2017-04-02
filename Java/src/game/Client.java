package game;

import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import network.GameInfo;
import network.Network;
import network.Network.CommandMap;
import network.Network.ControllerPlayer;
import network.Network.ControllerUpdate;
import network.Network.PlayerName;
import network.Network.Restart;
import network.Network.RoundEnded;
import network.Network.TimeRemaining;
import network.Network.ToRemove;
import network.Network.WarmupTimeRemaining;

@objid ("b8264f8e-49d1-4a58-ad20-06ceafdee906")
public class Client extends World implements Listener {
    com.esotericsoftware.kryonet.Client network;
    
    List<CommandMap> commands;
    
    boolean init;
    boolean roundEnded;
    
    boolean pickUp;
    boolean explosion;
    
    public Client() throws Exception {
        map = new Map(32);
        
        commands = Collections.synchronizedList(new LinkedList<CommandMap>());
        
        init = false;
        
        network = new com.esotericsoftware.kryonet.Client(16384, 8192);
        Network.register(network);
        
        network.addListener(this);
        
        network.start();
        
        InetAddress address = network.discoverHost(Network.udpPort, 2500);
        if (address != null)
            network.connect(1500, address, Network.tcpPort, Network.udpPort);
        else
            throw new Exception("Pas de serveur trouvé...");
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
        
        synchronized (map) {
            synchronized (commands) {
                DeltaMap.executeDeltas(commands, map);
                commands.clear();
            }
        
            super.update();
        }
        
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
    
    @Override
    public boolean isReady() {
        return init;
    }
    
    @Override
    public boolean isRoundEnded() {
        return roundEnded || !network.isConnected();
    }
    
    public boolean isConnected() {
        return network.isConnected();
    }
    
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
            
            synchronized (map) {
                super.addEntity(entity, id);
            }
        }
    }
    
    @Override
    public void stop() {
        network.close();
    }
    
    @Override
    public void restart() throws Exception {
        super.restart();
        roundEnded = false;
    }
    
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
        } else if (!init)
            return;
                
        if (object instanceof CommandMap) {
            commands.add((CommandMap)object);
        } else if (object instanceof Entity) {           
            addEntity((Entity) object, ((Entity)object).getID());
        } else if (object instanceof PlayerName) {
            PlayerName message = (PlayerName)object;
            Entity entity = entities.get(message.entityId);
            if (entity != null && entity instanceof Player) {
                ((Player)entity).getController().setName(message.name);
            }
        } else if (object instanceof ControllerPlayer) {
            ControllerPlayer cP = (ControllerPlayer) object;
            Entity entity = entities.get(cP.entityId);
            if (entity != null && entity instanceof Player && cP.controllerId >= 0 && cP.controllerId < controllers.size()) {
                ((Player)entity).setController(controllers.get(cP.controllerId));
            }
        } else if (object instanceof TimeRemaining) {
            timeRemaining = ((TimeRemaining)object).timeRemaining;
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
            ToRemove message = (ToRemove)object;
            for (Integer id : message.toRemove)
                entities.remove(id);
        }
        
        if (object instanceof List<?>) {
            if (((List)object).isEmpty())
                return;
            
            if (((List)object).get(0) instanceof CommandMap)
                commands.addAll((List<CommandMap>) object);
            else if (((List)object).get(0) instanceof Entity) {
                for (Entity entity : (List<Entity>)object) {
                    addEntity(entity, entity.getID());
                }
            }
        } 
    }
}
