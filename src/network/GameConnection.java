package network;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import game.Player;

/**
 * Connexion d'un client au jeu
 */
public class GameConnection extends Connection {
     List<Player> players;

    /**
     * Construit une connexion
     */
    public GameConnection() {
        super();
        players = new ArrayList<Player>();
    }

    /***
     * Ajoute un joueur associé à la connexion
     * @param player Joueur associé
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * @return la liste des joueurs associés à cette connexion
     */
    public List<Player> getPlayers() {
        return players;
    }

}
