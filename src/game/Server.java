package game;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import network.GameConnection;
import network.GameInfo;
import network.Network;
import network.Network.AddController;
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
import network.NetworkController;

/**
 * Cette classe gère une partie de type Serveur
 */
public class Server extends Local implements Listener {
     com.esotericsoftware.kryonet.Server network;

    ExecutorService threadPool;

    private int nextPlayerId = 0;
    PriorityQueue<Integer> freeIDs = new PriorityQueue<>();

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
    public Server(List<String> maps, int tileSize, int fps, int roundMax, int duration, int warmup, int restTime) throws Exception {
        super(maps, tileSize, fps, roundMax, duration, warmup, restTime);

        timestamp = 0;

        players = Collections.synchronizedMap(players);

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

    @Override
    void createMap(int tileSize) {
        map = new DeltaMap(tileSize);
    }

    @Override
    public GameState update() {
        timestamp++;

        boolean hasEnded = isRoundEnded();

        GameState state = super.update();

        if (state == GameState.WarmUp && warmupTimeRemaining % (fps/2) == 0) {
            network.sendToAllUDP(new WarmupTimeRemaining(warmupTimeRemaining));
        } else if (!isRoundEnded() && timeRemaining % fps == 0)
            network.sendToAllUDP(new TimeRemaining(timeRemaining));

        if (timeRemaining == 0)
            network.sendToAllTCP(suddenDeathType);

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
        EntityToRemove message = new EntityToRemove();

        for (Integer id : entityIDs) {
            if (entities.get(id) instanceof Character)
                message.toRemove.add(id);
        }

        super.removeEntities(entityIDs);
        network.sendToAllTCP(message);
    }

    @Override
    public void stop() {
        network.close();
        network.stop();
        threadPool.shutdown();
    }

    @Override
    public void nextRound() {
        network.sendToAllTCP(new NextRound());

        synchronized (players) {
            super.nextRound();
        }
    }

    @Override
    void addEntity(Entity entity) {
        super.addEntity(entity);
        network.sendToAllTCP(entity);
        if (entity instanceof Character) {
            Player player = ((Character) entity).getPlayer();
            if (player != null)
                network.sendToAllTCP(new EntityPlayer(entity.getID(), player.getID()));
        }
    }

    /**
     * @return Les informations de la partie en cours
     */
    public GameInfo getGameInfo() {
        return new GameInfo(fps, duration, timeRemaining, warmupDuration, warmupTimeRemaining,
                            restTimeDuration, restTimeRemaining, round, roundMax, suddenDeathType,
                            map.getTileSize(), map.getName(), map.saveMap());
    }

    private PlayerInfo getPlayerInfo(Player player) {
        PlayerInfo playerInfo = new PlayerInfo(player, null, -1);
        if (player.getController() != null)
            playerInfo.name = player.getController().getName();
        if (player.getCharacter() != null)
            playerInfo.entityId = player.getCharacter().getID();

        return playerInfo;
    }

    @Override
    public void connected(Connection connection) {
        connection.sendTCP(getGameInfo());
        List<Entity> entities = getEntities();
        connection.sendTCP(entities);
        for (Player player : getPlayers()) {
            connection.sendTCP(getPlayerInfo(player));
        }
    }

    @Override
    public Player newPlayer(Controller controller) {
        Player player = null;
        Integer ID = null;

        synchronized (freeIDs) {
            ID = freeIDs.poll();
            if (ID == null) {
                ID = nextPlayerId;
                nextPlayerId++;
            }
        }

        player = newPlayer(controller, ID.intValue());
        network.sendToAllTCP(getPlayerInfo(player));
        return player;
    }

    @Override
    public void received(Connection connection, Object object) {
        GameConnection gConnection = (GameConnection)connection;
        if (object instanceof AddController) {
            AddController message = (AddController) object;
            NetworkController controller = new NetworkController(gConnection, gConnection.getPlayers().size(), message.name);
            Player player = newPlayer(controller);
            gConnection.addPlayer(player);
            connection.sendTCP(new ControllerPlayer(controller.getID(), player.getID()));
        } else if (object instanceof ControllerUpdate) {
            ControllerUpdate update = (ControllerUpdate) object;
            if (update.id >= 0 && update.id < gConnection.getPlayers().size())
                ((NetworkController) gConnection.getPlayers().get(update.id).getController()).update(update);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        GameConnection gConnection = (GameConnection)connection;
        List<Player> playerList = gConnection.getPlayers();
        if (playerList.isEmpty())
             return;

        PlayerToRemove message = new PlayerToRemove(playerList.size());

        for (Player player : playerList) {
            if (player.getCharacter() != null) {
                player.getCharacter().remove();
            }

            int playerID = player.getID();
            message.toRemove.add(playerID);
            players.remove(playerID);
        }

        network.sendToAllTCP(message);

        synchronized (freeIDs) {
            freeIDs.addAll(message.toRemove);
        }
    }

}
