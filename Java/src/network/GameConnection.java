package network;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import network.Network.AddController;
import network.Network.ControllerUpdate;

public class GameConnection extends Connection {
    List<NetworkController> controllers;
    
    public GameConnection() {
        super();
        controllers = new ArrayList<NetworkController>();
    }
    
    public NetworkController addController(AddController message) {
        NetworkController controller = new NetworkController(this, controllers.size(), message.name);
        controllers.add(controller);
        return controller;
    }
    
    public void updateController(ControllerUpdate update) {
        if (update.id < controllers.size())
            controllers.get(update.id).update(update);
    }

    public List<NetworkController> getControllers() {
        return controllers;
    }
}
