package game;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("ef002ca0-7638-4d64-9860-1c30b5e5c688")
public interface WorldView {
    
    @objid ("d528a200-b69a-4233-8cd3-96e4aed92199")
    /**
     * @return le nombre d'images par secondes
     */
    int getFps();

    /**
     * @return la durée d'un round en nombre d'images
     */
    @objid ("cf7e69c5-28c5-4ca4-a08c-b3e954fe409a")
    int getDuration();

    /**
     * @return la durée restante avant la fin du round en nombre d'images
     */
    @objid ("39ec082e-e21c-4225-b905-acf6c47cbd9b")
    int getTimeRemaining();

    /**
     * @return le temps écoulé depuis le début du round en nombre d'images
     */
    @objid ("bc316acb-cee6-4310-b233-3a4264b07c74")
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

    @objid ("32dcdef1-5a7a-4722-92ad-5d4d0aa3f197")
    List<Entity> getEntities();

    @objid ("b3701478-0bf1-402b-98bd-a71cb1065097")
    MapView getMap();

    @objid ("bc99ac64-6430-4211-82c1-72ea90aac841")
    int getPlayerCount();
    
    @objid ("3c9c020a-4bd5-424b-9d90-1bdda21519a4")
    int getPlayerAliveCount();

    @objid ("28429dd0-b782-48e7-992e-64beab6a4e61")
    List<Player> getPlayers();
}
