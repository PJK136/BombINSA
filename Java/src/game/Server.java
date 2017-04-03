package game;

import java.util.Collections;
import java.util.List;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import network.GameConnection;
import network.GameInfo;
import network.Network.AddController;
import network.Network.ControllerPlayer;
import network.Network.ControllerUpdate;
import network.Network.PlayerName;
import network.Network.Restart;
import network.Network.RoundEnded;
import network.Network.TimeRemaining;
import network.Network.ToRemove;
import network.Network.WarmupTimeRemaining;
import network.Network;
import network.NetworkController;

@objid ("47364a1e-3fd0-4e56-8047-f80aca5b2b36")
public class Server extends Local implements Listener {
    @objid ("0af280ea-a7b2-4232-a15b-220a4b0d977f")
     com.esotericsoftware.kryonet.Server network;

    @objid ("c0e17e22-6d82-404c-81c8-d2c427dec052")
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

    @objid ("3d81f44b-6533-445f-86aa-7373e47ea97c")
    @Override
    void createMap(int tileSize) {
        map = new DeltaMap(tileSize);
    }

    @objid ("969d20cd-0105-4d21-98c6-db3d6d93c845")
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
        
        if (isRoundEnded())
            network.sendToAllUDP(new RoundEnded());
    }

    @objid ("8c95d6c7-e515-4188-918d-205812f19cc8")
    public void stop() {
        network.stop();
    }

    @objid ("a17698f1-df19-4c29-b3be-3b78424f3c88")
    @Override
    public void restart() throws Exception {
        network.sendToAllTCP(new Restart());
        
        synchronized (controllers) {
            super.restart();
        }
    }

    @objid ("941de7bd-0997-4c7d-a82a-2b4237d3716c")
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

    @objid ("eddf6bd8-2e40-4c63-8435-f6ef11a69e01")
    public GameInfo getGameInfo() {
        return new GameInfo(fps, duration, timeRemaining, warmupDuration, warmupTimeRemaining,
                                            round, map.getTileSize(), map.saveMap());
    }

    @objid ("15d969ea-6190-4c81-8279-3ae9894b6ed4")
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

    @objid ("51b6e150-fee5-4d9f-969d-b15be8d71c94")
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

    @objid ("0115b9e2-9532-4404-8cae-3d3707b03bfe")
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
