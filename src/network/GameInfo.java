package network;

import game.SuddenDeathType;

/**
 * Cette classe contient des informations sur une partie
 */
public class GameInfo {
    public int fps;

    public int duration;

    public int timeRemaining;

    public int warmupDuration;

    public int warmupTimeRemaining;
    
    public int restTimeDuration;
    
    public int restTimeRemaining;

    public int round;
    
    public int roundMax;
    
    public SuddenDeathType suddenDeathType;

    public int tileSize;

    public String map;

    /**
     * Constructeur par défaut
     */
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
    public GameInfo(int fps, int duration, int timeRemaining, int warmupDuration, int warmupTimeRemaining,
            int restTimeDuration, int restTimeRemaining, int round, int roundMax, SuddenDeathType suddenDeathType, int tileSize, String map) {
        this.fps = fps;
        this.duration = duration;
        this.timeRemaining = timeRemaining;
        this.warmupDuration = warmupDuration;
        this.warmupTimeRemaining = warmupTimeRemaining;
        this.restTimeDuration = restTimeDuration;
        this.restTimeRemaining = restTimeRemaining;
        this.round = round;
        this.roundMax = roundMax;
        this.suddenDeathType = suddenDeathType;
        this.tileSize = tileSize;
        this.map = map;
    }

}
