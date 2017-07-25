package game;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import network.GameConnection;
import network.GameInfo;
import network.Network;
import network.Network.AddController;
import network.Network.ControllerPlayer;
import network.Network.ControllerUpdate;
import network.Network.EntityUpdateList;
import network.Network.NextRound;
import network.Network.PlayerName;
import network.Network.RoundEnded;
import network.Network.TimeRemaining;
import network.Network.ToRemove;
import network.Network.WarmupTimeRemaining;
import network.NetworkController;

/**
 * Cette classe gère une partie de type Serveur
 */
@objid ("47364a1e-3fd0-4e56-8047-f80aca5b2b36")
public class Server extends Local implements Listener {
    @objid ("0af280ea-a7b2-4232-a15b-220a4b0d977f")
     com.esotericsoftware.kryonet.Server network;

    ExecutorService threadPool;
    
    private int timestamp;
    
    private static final int UPDATE_RATE = 20; //Hz
    
    /**
     * Construit un serveur de jeu
     * @param mapFilename Nom du ficher de la carte
     * @param tileSize Taille des tuiles
     * @param fps Nombre d'images par secondes
     * @param duration Durée d'un round en images
     * @param warmup Durée du temps d'échauffement en images
     * @throws java.lang.Exception Erreur liée au chargement de la carte
     */
    @objid ("c0e17e22-6d82-404c-81c8-d2c427dec052")
    public Server(String mapFilename, int tileSize, int fps, int roundMax, int duration, int warmup, int restTime) throws Exception {
        super(mapFilename, tileSize, fps, roundMax, duration, warmup, restTime);
        
        timestamp = 0;
        
        controllers = Collections.synchronizedList(controllers);
        
        network = new com.esotericsoftware.kryonet.Server() {
            @Override
            protected Connection newConnection() {
                return new GameConnection();
            }
        };
        
        Network.register(network);
        
        threadPool = Executors.newFixedThreadPool(1);
        
        network.addListener(new Listener.ThreadedListener(this, threadPool));
        
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
    public GameState update() {
        timestamp++;
        
        boolean hasEnded = isRoundEnded();
        
        GameState state = super.update();
              
        if (state == GameState.WarmUp && warmupTimeRemaining % (fps/2) == 0) {
            network.sendToAllUDP(new WarmupTimeRemaining(warmupTimeRemaining));
        } else if (!isRoundEnded() && timeRemaining % fps == 0)
            network.sendToAllUDP(new TimeRemaining(timeRemaining));
        
        if (!hasEnded) {
            if (timeRemaining % (fps/UPDATE_RATE) == 0 || isRoundEnded()) {              
                List<Entity> updates = getEntities();
                for (int start = 0; start < updates.size(); start += 5)
                    network.sendToAllUDP(new EntityUpdateList(timestamp, updates.subList(start, Math.min(start+5, updates.size()))));
                
                network.sendToAllTCP(((DeltaMap)map).deltas);
                ((DeltaMap)map).deltas.clear();
            }
        }
       
        if (!hasEnded && isRoundEnded())
            network.sendToAllTCP(new RoundEnded(getWinnerName(), getWinnerID()));
        
        return state;
    }
    
    @Override
    void removeEntities(List<Integer> entityIDs) {
        super.removeEntities(entityIDs);
        
        ToRemove message = new ToRemove();
        
        for (Integer id : entityIDs) {
            if (entities.get(id) instanceof Character)
                message.toRemove.add(id);
        }
        
        network.sendToAllTCP(message);
    }

    @Override
    @objid ("8c95d6c7-e515-4188-918d-205812f19cc8")
    public void stop() {
        network.stop();
        threadPool.shutdown();
    }

    @objid ("a17698f1-df19-4c29-b3be-3b78424f3c88")
    @Override
    public void nextRound() {
        network.sendToAllTCP(new NextRound());
        
        synchronized (controllers) {
            super.nextRound();
        }
    }

    @objid ("941de7bd-0997-4c7d-a82a-2b4237d3716c")
    @Override
    void addEntity(Entity entity) {
        super.addEntity(entity);
        network.sendToAllTCP(entity);
        if (entity instanceof Character) {
            Controller controller = ((Character) entity).getController();
            network.sendToAllTCP(new PlayerName(entity.getID(), controller.getName()));
            if (controller instanceof NetworkController) {
                NetworkController networkController = (NetworkController)controller;
                networkController.getConnection().sendTCP(new ControllerPlayer(networkController.getID(), entity.getID()));
            }
        }
    }

    /**
     * @return Les informations de la partie en cours
     */
    @objid ("eddf6bd8-2e40-4c63-8435-f6ef11a69e01")
    public GameInfo getGameInfo() {
        return new GameInfo(fps, duration, timeRemaining, warmupDuration, warmupTimeRemaining,
                            restTimeDuration, restTimeRemaining, round, roundMax, map.getTileSize(), map.saveMap());
    }

    @objid ("15d969ea-6190-4c81-8279-3ae9894b6ed4")
    @Override
    public void connected(Connection connection) {
        connection.sendTCP(getGameInfo());
        List<Entity> entities = getEntities();
        connection.sendTCP(entities);
        for (Entity entity : entities) {
            if (entity instanceof Character) {
                connection.sendTCP(new PlayerName(entity.getID(), ((Character)entity).getController().getName()));
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
        
        for (NetworkController controller : controllers) {
            if (controller.getCharacter() != null) {
                controller.getCharacter().remove();
            }
        }
    }

}
