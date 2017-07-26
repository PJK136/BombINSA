package game;

public class Player {
    int id = -1;
    int score = 0;
    transient Controller controller = null;
    transient Character character = null;
    
    public Player() {
    }
    
    public Player(int playerID, Controller controller) {
        this.id = playerID;
        this.score = 0;
        this.controller = controller;
    }
    
    public Player(int playerID, int score, Controller controller, Character character) {
        this.id = playerID;
        this.score = score;
        this.controller = controller;
        this.character = character;
    }
    
    public int getID() {
        return id;
    }
    
    public int getScore() {
        return score;
    }
    
    public Controller getController() {
        return controller;
    }
    
    public Character getCharacter() {
        return character;
    }
    
    void increaseScore() {
        score++;
    }
    
    void increaseScore(int amount) {
        score += amount;
    }
    
    void decreaseScore() {
        score--;
    }
    
    void decreaseScore(int amount) {
        score -= amount;
    }
    
    void setCharacter(Character character) {
        this.character = character;
        
        if (this.character != null) {
            this.character.setPlayer(this);
        }
        
        if (this.controller != null)
            this.controller.setCharacter(this.character);
    }
    
    void setController(Controller controller) {
        this.controller = controller;
        if (this.controller != null)
            this.controller.setCharacter(this.character);
    }
    
    void updateFrom(Player player) {
        this.id = player.id;
        this.score = player.score;
    }
}
