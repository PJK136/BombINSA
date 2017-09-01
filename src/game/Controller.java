package game;


/** Cette classe indique les différentes actions à effectuer pour un joueur */
public abstract class Controller {
    protected String name;

    protected Character character;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character value) {
        this.character = value;
    }
    
    public abstract Direction getDirection();
    
    /** 
     * Indique si le joueur pose une bombe
     * @return true si oui, false sinon
     */
    public abstract boolean isPlantingBomb();
    
    /**
     * Met à jour le contrôleur
     */
    public abstract void update();

}
