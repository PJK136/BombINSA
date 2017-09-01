package game;

import java.util.List;

/** Cette interface est une vue des informations contenues dans le monde */
public interface WorldView {
    /**
     * @return le nombre d'images par secondes
     */
    int getFps();

    /**
     * @return la durée d'un round en nombre d'images
     */
    int getDuration();

    /**
     * @return la durée restante avant la fin du round en nombre d'images
     */
    int getTimeRemaining();

    /**
     * @return le temps écoulé depuis le début du round en nombre d'images
     */
    int getTimeElapsed();

    /**
     * @return la durée de l'échauffement en nombre de d'images
     */
    int getWarmupDuration();

    /**
     * @return le temps restant avant la fin de l'échauffement en nombre d'images
     */
    int getWarmupTimeRemaining();

    int getRound();

    List<Entity> getEntities();

    MapView getMap();

    List<Player> getPlayers();
    
    int getPlayerCount();

    int getCharacterCount();

    int getHumanCount();

    int getHumanAliveCount();

    List<Character> getCharacters();

}
