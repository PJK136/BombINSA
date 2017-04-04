package network;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.DummyController;
import network.Network.ControllerUpdate;

/**
 * Ce contrôleur permet la synchronisation avec un client pour contrôler le joueur côté serveur
 */
@objid ("e62ac67a-9aae-44f9-a5bd-9ce981bfe0af")
public class NetworkController extends DummyController {
    @objid ("1b7715b8-928d-4f50-8bf0-6f565671b107")
     int id;

    @objid ("9d397391-71fd-4ad7-8991-c293ae0679f2")
     GameConnection connection;

    /**
     * Constructeur par défaut
     */
    @objid ("127db686-679a-48c1-9005-7f28b80ffa74")
    public NetworkController() {
    }

    /**
     * Construit un contrôleur réseau associé à une connexion
     * @param connection Connection
     * @param id ID du contrôleur par rapport à la connexion
     * @param name Nom associé contrôleur
     */
    @objid ("8eea4976-78fc-40c1-8e7e-445e22193cd5")
    public NetworkController(GameConnection connection, int id, String name) {
        this.connection = connection;
        this.id = id;
        setName(name);
    }

    @objid ("2bce17dd-5016-418c-bf7a-792835d6acbf")
    public GameConnection getConnection() {
        return connection;
    }

    @objid ("b813de28-0945-41ad-b037-c6e7a248d94f")
    public int getID() {
        return id;
    }

    /**
     * Met à jour le contrôleur avec de nouvelles informations
     * @param update Nouvelles informations
     */
    @objid ("e2bf0d14-ccab-4898-a04a-1d9369d9f249")
    public void update(ControllerUpdate update) {
        direction = update.direction;
        bombing = update.bombing;
    }

}
