package network;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import game.DummyController;
import network.Network.AddController;
import network.Network.ControllerUpdate;

public class GameConnection extends Connection {
    List<DummyController> controllers;
    
    public GameConnection() {
        super();
        controllers = new ArrayList<DummyController>();
    }
    
    public DummyController addController(AddController message) {
        DummyController controller = new DummyController(message.name);
        controllers.add(controller);
        return controller;
    }
    
    public void updateController(ControllerUpdate update) {
        if (update.id < controllers.size())
            controllers.get(update.id).update(update);
    }

    public List<DummyController> getControllers() {
        return controllers;
    }
}
