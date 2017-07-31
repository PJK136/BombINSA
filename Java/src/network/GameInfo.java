package network;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Cette classe contient des informations sur une partie
 */
@objid ("f822c6e6-6046-4f08-bfb9-482592b1a59a")
public class GameInfo {
    @objid ("e0c7f27e-5fb7-4377-af32-f63f8f1cf264")
    public int fps;

    @objid ("39d837ac-8c39-44c4-9b36-195b187da1c2")
    public int duration;

    @objid ("2827b6a0-cb11-4e81-b60f-61b6900a4313")
    public int timeRemaining;

    @objid ("138690bd-20a0-4b22-9cff-d693f7b48b6d")
    public int warmupDuration;

    @objid ("412031ee-2f0e-43e1-8daf-973efcce1518")
    public int warmupTimeRemaining;
    
    public int restTimeDuration;
    
    public int restTimeRemaining;

    @objid ("e414a2c2-5cb9-4646-8921-1244a983a9a8")
    public int round;
    
    public int roundMax;

    @objid ("700f5789-d7ac-454a-a1a4-e4cd76bf3743")
    public int tileSize;

    @objid ("3c68eb3a-02de-46a2-8e87-d97f6e64a4fd")
    public String map;

    /**
     * Constructeur par défaut
     */
    @objid ("37fb7691-3a29-4969-bf72-d3fd5c591278")
    public GameInfo() {
    }

    /**
     * Construit l'instance avec les informations données en paramètre
     * @param fps Nombre d'images par seconde
     * @param duration Durée d'une partie
     * @param timeRemaining Temps restant
     * @param warmupDuration Temps d'échauffement d'une partie
     * @param warmupTimeRemaining Temps restant à l'échauffement
     * @param round Numéro du round
     * @param tileSize Taille des tuiles
     * @param map Carte sérialisée
     */
    @objid ("7e6be8f6-2889-427b-b4ee-944683be2de8")
    public GameInfo(int fps, int duration, int timeRemaining, int warmupDuration, int warmupTimeRemaining,
            int restTimeDuration, int restTimeRemaining, int round, int roundMax, int tileSize, String map) {
        this.fps = fps;
        this.duration = duration;
        this.timeRemaining = timeRemaining;
        this.warmupDuration = warmupDuration;
        this.warmupTimeRemaining = warmupTimeRemaining;
        this.restTimeDuration = restTimeDuration;
        this.restTimeRemaining = restTimeRemaining;
        this.round = round;
        this.roundMax = roundMax;
        this.tileSize = tileSize;
        this.map = map;
    }

}
