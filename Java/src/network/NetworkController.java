package network;

import game.DummyController;
import network.Network.ControllerUpdate;

public class NetworkController extends DummyController {
    int id;
    GameConnection connection;
    
    public NetworkController() {
    }

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
    
    public void update(ControllerUpdate update) {
        direction = update.direction;
        bombing = update.bombing;
    }
}
