package game;

import java.util.Collections;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import network.GameConnection;
import network.GameInfo;
import network.Network;
import network.Network.AddController;
import network.Network.ControllerUpdate;
import network.Network.PlayerName;
import network.Network.ControllerPlayer;
import network.Network.TimeRemaining;
import network.Network.WarmupTimeRemaining;
import network.Network.ToRemove;
import network.NetworkController;
import network.Network.Restart;

public class Server extends Local implements Listener {

    com.esotericsoftware.kryonet.Server network;
    
    public Server(String mapFilename, int tileSize, int fps, int duration, int warmup) throws Exception {
        super(mapFilename, tileSize, fps, duration, warmup);
        
        controllers = Collections.synchronizedList(controllers);
        
        network = new com.esotericsoftware.kryonet.Server() {
            @Override
            protected Connection newConnection() {
                return new GameConnection();
            }
        };
        
        Network.register(network);
        
        network.addListener(new Listener.ThreadedListener(this));
        
        network.bind(Network.tcpPort, Network.udpPort);
        
        network.start();
    }

    @Override
    void createMap(int tileSize) {
        map = new DeltaMap(tileSize);
    }
    
    @Override
    public void update() {
        super.update();
        
        for (Entity entity : getEntities()) {
            network.sendToAllUDP(entity);
        }
        
        if (warmupTimeRemaining > 0 && timeRemaining % (fps/2) == 0) {
            network.sendToAllUDP(new WarmupTimeRemaining(warmupTimeRemaining));
        } else if (timeRemaining % fps == 0)
            network.sendToAllUDP(new TimeRemaining(timeRemaining));
        
        network.sendToAllTCP(((DeltaMap)map).deltas);
        ((DeltaMap)map).deltas.clear();
    }
    
    public void stop() {
        network.stop();
    }
    
    @Override
    public void restart() throws Exception {
        network.sendToAllTCP(new Restart());
        
        synchronized (controllers) {
            super.restart();
        }
    }
    
    @Override
    void addEntity(Entity entity) {
        super.addEntity(entity);
        network.sendToAllTCP(entity);
        if (entity instanceof Player) {
            Controller controller = ((Player) entity).getController();
            network.sendToAllTCP(new PlayerName(entity.getID(), controller.getName()));
            if (controller instanceof NetworkController) {
                NetworkController networkController = (NetworkController)controller;
                networkController.getConnection().sendTCP(new ControllerPlayer(networkController.getID(), entity.getID()));
            }
        }
    }
    
    public GameInfo getGameInfo() {
        return new GameInfo(fps, duration, timeRemaining, warmupDuration, warmupTimeRemaining,
                            round, map.getTileSize(), map.saveMap());
    }
    
    @Override
    public void connected(Connection connection) {
        connection.sendTCP(getGameInfo());
        List<Entity> entities = getEntities();
        connection.sendTCP(entities);
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                connection.sendTCP(new PlayerName(entity.getID(), ((Player)entity).getController().getName()));
            }
        }
    }
    
    @Override
    public void received(Connection connection, Object object) {
        GameConnection gConnection = (GameConnection)connection;
        if (object instanceof AddController) {
            Controller controller = gConnection.addController((AddController)object);
            newController(controller);
        } else if (object instanceof ControllerUpdate) {
            gConnection.updateController((ControllerUpdate)object);
        }
    }
    
    @Override
    public void disconnected(Connection connection) {
        GameConnection gConnection = (GameConnection)connection;
        List<NetworkController> controllers = gConnection.getControllers();
        if (controllers.isEmpty())
             return;
        
        ToRemove message = new ToRemove(controllers.size());
        for (NetworkController controller : controllers) {
            if (controller.getPlayer() != null) {
                message.toRemove.add(controller.getPlayer().getID());
                controller.getPlayer().remove();
            }
        }
        
        network.sendToAllTCP(message);
    }
}
