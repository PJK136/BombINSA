package network;

import game.DummyController;
import network.Network.ControllerUpdate;

/**
 * Ce contrôleur permet la synchronisation avec un client pour contrôler le joueur côté serveur
 */
public class NetworkController extends DummyController {
     int id;

     GameConnection connection;

    /**
     * Constructeur par défaut
     */
    public NetworkController() {
    }

    /**
     * Construit un contrôleur réseau associé à une connexion
     * @param connection Connection
     * @param id ID du contrôleur par rapport à la connexion
     * @param name Nom associé contrôleur
     */
    public NetworkController(GameConnection connection, int id, String name) {
        this.connection = connection;
        this.id = id;
        setName(name);
    }

    public GameConnection getConnection() {
        return connection;
    }

    public int getID() {
        return id;
    }

    /**
     * Met à jour le contrôleur avec de nouvelles informations
     * @param update Nouvelles informations
     */
    public void update(ControllerUpdate update) {
        direction = update.direction;
        bombing = update.bombing;
    }

}
