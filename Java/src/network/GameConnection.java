package network;

import java.util.ArrayList;
import java.util.List;
import com.esotericsoftware.kryonet.Connection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import network.Network.AddController;
import network.Network.ControllerUpdate;

/**
 * Connexion d'un client au jeu
 */
@objid ("22d624e0-455b-46c1-8b53-a45b4ef03e76")
public class GameConnection extends Connection {
    @objid ("0db9a074-6abe-4310-8ad4-11c781f605a1")
     List<NetworkController> controllers;

    /**
     * Construit une connexion
     */
    @objid ("7cd1c4f6-22bd-43ae-9ec9-8686d1faa75c")
    public GameConnection() {
        super();
        controllers = new ArrayList<NetworkController>();
    }

    /***
     * Crée un contrôleur associé à la connexion
     * @param message
     * @return
     */
    @objid ("d9c073d4-e263-4a9e-8a04-068c7180b708")
    public NetworkController addController(AddController message) {
        NetworkController controller = new NetworkController(this, controllers.size(), message.name);
        controllers.add(controller);
        return controller;
    }

    /**
     * Met à jour un contrôleur de la connexion
     * @param update Nouvelles informations
     */
    @objid ("da21818c-83dd-4865-af2b-75ac5c67f6b1")
    public void updateController(ControllerUpdate update) {
        if (update.id < controllers.size())
            controllers.get(update.id).update(update);
    }

    /**
     * @return la liste des contrôleurs associée à cette connexion
     */
    @objid ("a0231b85-5578-49c9-8999-689d4abe98ed")
    public List<NetworkController> getControllers() {
        return controllers;
    }

}
