package network;

public class GameInfo {
    public int fps;
    public int duration;
    public int timeRemaining;
    public int warmupDuration;
    public int warmupTimeRemaining;
    public int round;
    public int tileSize;
    public String map;
    
    public GameInfo() {}
    public GameInfo(int fps, int duration, int timeRemaining, int warmupDuration, int warmupTimeRemaining, int round, int tileSize, String map) {
        this.fps = fps;
        this.duration = duration;
        this.timeRemaining = timeRemaining;
        this.warmupDuration = warmupDuration;
        this.warmupTimeRemaining = warmupTimeRemaining;
        this.round = round;
        this.tileSize = tileSize;
        this.map = map;
    }
}
