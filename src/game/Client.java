package game;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import network.GameInfo;
import network.Network;
import network.Network.CommandMap;
import network.Network.ControllerPlayer;
import network.Network.ControllerUpdate;
import network.Network.EntityPlayer;
import network.Network.EntityToRemove;
import network.Network.EntityUpdateList;
import network.Network.NextRound;
import network.Network.PlayerInfo;
import network.Network.PlayerToRemove;
import network.Network.RoundEnded;
import network.Network.TimeRemaining;
import network.Network.WarmupTimeRemaining;

/**
 * Cette classe gère une partie de type Client
 */
public class Client extends World implements Listener {
     boolean init;

     boolean roundEnded;

     boolean nextRound;

     RoundEnded winner;

     boolean pickUp;

     boolean explosion;
    
     boolean suddenDeath;

     com.esotericsoftware.kryonet.Client network;

    List<Controller> controllers;
    
    Queue<Object> messages;
    
    int lastTimestamp = -1;
    
    /**
     * Construit un client qui se connecte à l'adresse spécifiée
     * @param address Adresse du serveur
     * @throws Exception si échec de la connexion
     */
    public Client(InetAddress address) throws Exception {
        map = new Map(32) {
            @Override
            Tile newTile(TileType type) {
                if (type != TileType.Breakable)
                    return super.newTile(type);
                else {
                    return new BreakableTile() {
                        @Override
                        Tile postExplosion() {
                            EmptyTile tile = new EmptyTile();
                            tile.setEntities(entities);
                            return tile;
                        }
                    };
                }
            }
        };
        
        controllers = new ArrayList<>();
        
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
    public Client() throws Exception {
        this(null);
    }

    @Override
    void plantBomb(Character character) {
        // TODO Auto-generated method stub
    }

    @Override
    void createExplosion(Bomb bomb) {
        explosion = true;
    }

    @Override
    void pickUpBonus(double x, double y) {
        pickUp = true;
    }

    @Override
    public Player newPlayer(Controller controller) {
        controllers.add(controller);
        return null;
    }

    @Override
    public GameState update() {
        if (!init)
            return GameState.Init;
        
        if (!isConnected())
            return GameState.End;
        
        for (int i = 0; i < controllers.size(); i++) {
            network.sendUDP(new ControllerUpdate(i, controllers.get(i)));
        }

        Object message = null;
        while ((message = messages.poll()) != null) {
            processMessage(message);
        }
        
        GameState state = super.update();
        
        if (suddenDeath) {
            fireEvent(GameEvent.SuddenDeath);
            suddenDeath = false;
        }
            
        if (pickUp) {
            fireEvent(GameEvent.PickUp);
            pickUp = false;
        }
        
        if (explosion) {
            fireEvent(GameEvent.Explosion);
            explosion = false;
        }
        
        return state;
    }

    @Override
    void kickBomb(Bomb bomb, Direction direction) {
        bomb.setDirection(direction);
        bomb.setSpeed(Bomb.BOMB_DEFAULT_SPEED*map.getTileSize()/getFps());
    }

    @Override
    public boolean isReady() {
        return init;
    }

    @Override
    public boolean isRoundEnded() {
        return roundEnded;
    }

    /**
     * @return si le client est connecté à un serveur
     */
    public boolean isConnected() {
        return network.isConnected();
    }

    @Override
    void addEntity(Entity entity, int id) {
        Entity existing = entities.get(id);
        if (existing != null) {
            existing.updateFrom(entity);
        } else {
            super.addEntity(entity, id);
        }
    }

    @Override
    public void stop() {
        network.close();
        network.stop();
    }

    @Override
    void newRound() {
        super.newRound();
        roundEnded = false;
        nextRound = false;
        winner = null;
    }

    @Override
    public void nextRound() {
        if (nextRound)
            super.nextRound();
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
            this.restTimeDuration = info.restTimeDuration;
            this.restTimeRemaining = info.restTimeRemaining;
            this.round = info.round;
            this.roundMax = info.roundMax;
            this.map.setTileSize(info.tileSize);
            this.map.loadMap(info.map);
            
            for (Controller controller : controllers)
                network.sendTCP(new Network.AddController(controller.getName()));
            
            init = true;
            
            if (timeRemaining <= 0)
                suddenDeath = true;
        } else if (init)
            messages.offer(object);
    }
    
    private void processMessage(Object object) { 
        if (object instanceof CommandMap) {
            DeltaMap.executeDelta((CommandMap)object, map);
        } else if (object instanceof Entity) {           
            addEntity((Entity) object, ((Entity)object).getID());
        } else if (object instanceof EntityUpdateList) {
            EntityUpdateList updates = (EntityUpdateList) object;
            if (updates.timestamp >= lastTimestamp) {
                lastTimestamp = updates.timestamp;
                for (Entity entity : updates.entities) {
                    addEntity(entity, entity.getID());
                }
            }
        } else if (object instanceof PlayerInfo) {
            PlayerInfo playerInfo = (PlayerInfo)object;

            if (playerInfo.player.getID() >= 0) {
                Player player = playerInfo.player;
                
                player.setController(new FollowController());
                player.getController().setName(playerInfo.name);
                
                Entity entity = entities.get(playerInfo.entityId);
                if (entity instanceof Character)
                    playerInfo.player.setCharacter((Character) entity);
                
                players.put(player.getID(), player);
            }
        } else if (object instanceof ControllerPlayer) {
            ControllerPlayer cP = (ControllerPlayer) object;
            Player player = players.get(cP.playerId);
            if (player != null && cP.controllerId >= 0 && cP.controllerId < controllers.size()) {
                player.setController(controllers.get(cP.controllerId));
            }
        } else if (object instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) object;
            
            Entity entity = entities.get(entityPlayer.entityId);
            if (entity instanceof Character) {
                Player player = players.get(entityPlayer.playerId);
                if (player != null) {
                    player.setCharacter((Character) entity); 
                }
            }
        } else if (object instanceof TimeRemaining) {
            int newTime = ((TimeRemaining)object).timeRemaining;
            if (timeRemaining > 1 && newTime <= 0)
                suddenDeath = true;
            
            timeRemaining = newTime;
        } else if (object instanceof WarmupTimeRemaining) {
            warmupTimeRemaining = ((WarmupTimeRemaining)object).warmupTimeRemaining;
        } else if (object instanceof RoundEnded) {
            roundEnded = true;
            winner = (RoundEnded) object;
        } else if (object instanceof NextRound) {
            nextRound = true;
            nextRound();
        } else if (object instanceof EntityToRemove) {
            EntityToRemove toRemove = (EntityToRemove)object;
            for (Integer id : toRemove.toRemove)
                entities.remove(id);
        } else if (object instanceof PlayerToRemove) {
            PlayerToRemove toRemove = (PlayerToRemove)object;
            for (Integer id : toRemove.toRemove)
                players.remove(id);
        } else if (object instanceof List<?>) {
            if (((List<?>)object).isEmpty())
                return;
            
            if (((List<?>) object).get(0) instanceof CommandMap)
                DeltaMap.executeDeltas((List<CommandMap>)object, map);
            else if (((List<?>) object).get(0) instanceof Entity) {
                for (Entity entity : (List<Entity>) object) {
                    addEntity(entity, entity.getID());
                }
            } else {
                System.err.println("Unexpected message list : " + object.getClass() + " / " + ((List<?>) object).get(0).getClass());
            }
        } else {
            System.err.println("Unexpected message : " + object.getClass());
        }
    }

    @Override
    public String getWinnerName() {
        if (winner != null)
            return winner.winnerName;
        else
            return null;
    }

    @Override
    public int getWinnerID() {
        if (winner != null)
            return winner.winnerID;
        else
            return -1;
    }

}
