package network;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import game.Player;

/**
 * Connexion d'un client au jeu
 */
@objid ("22d624e0-455b-46c1-8b53-a45b4ef03e76")
public class GameConnection extends Connection {
    @objid ("0db9a074-6abe-4310-8ad4-11c781f605a1")
     List<Player> players;

    /**
     * Construit une connexion
     */
    @objid ("7cd1c4f6-22bd-43ae-9ec9-8686d1faa75c")
    public GameConnection() {
        super();
        players = new ArrayList<Player>();
    }

    /***
     * Ajoute un joueur associé à la connexion
     * @param player Joueur associé
     */
    @objid ("d9c073d4-e263-4a9e-8a04-068c7180b708")
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * @return la liste des joueurs associés à cette connexion
     */
    @objid ("a0231b85-5578-49c9-8999-689d4abe98ed")
    public List<Player> getPlayers() {
        return players;
    }

}
