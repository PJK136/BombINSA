package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


/** Cette classe coordonne l'ensemble des actions pendant une partie */
public abstract class World implements WorldView {
     int fps = 60;

     int timeRemaining = -1;

     int duration;

     int warmupDuration;

     int restTimeDuration;

     int warmupTimeRemaining = -1;

     int restTimeRemaining = -1;

     int round = 0;

     int roundMax = 3;

    private int nextID = 1;

    public static final int START_LIVES = 1;

    public static final int START_BOMB_MAX = 1;

    public static final int START_RANGE = 1;

    public static final double START_INVULNERABITY_DURATION = 1;

    public static final double TIME_BEFORE_EXPLOSION = 3;

    public static final double EXPLOSION_DURATION = 0.25;

     Map map;

     java.util.Map<Integer, Entity> entities = Collections.synchronizedMap(new HashMap<Integer, Entity> ());

    java.util.Map<Integer, Player> players = new HashMap<> ();

     List<GameListener> listeners = new LinkedList<GameListener>();

    @Override
    public int getFps() {
        return fps;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getTimeRemaining() {
        return timeRemaining;
    }

    @Override
    public int getTimeElapsed() {
        return duration-timeRemaining;
    }

    @Override
    public int getWarmupDuration() {
        return warmupDuration;
    }

    @Override
    public int getWarmupTimeRemaining() {
        return warmupTimeRemaining;
    }

    public int getRestTimeRemaining() {
        return restTimeRemaining;
    }

    @Override
    public int getRound() {
        return round;
    }

    public void setRoundMax(int roundMax) {
        if (roundMax < 0)
            throw new RuntimeException("Round max not positive");
        
        this.roundMax = roundMax;
    }

    @Override
    public List<Entity> getEntities() {
        //Thread-safety
        synchronized (entities) {
            return new LinkedList<Entity>(entities.values());            
        }
    }

    @Override
    public MapView getMap() {
        return map;
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<Player>(players.values());
    }
    
    @Override
    public int getPlayerCount() {
        return players.size();
    }

    @Override
    public int getCharacterCount() {
        int sum = 0;
        for (Entity entity : getEntities()) { //Thread-safety
            if (entity instanceof Character) {
                sum++;
            }       
        }
        return sum;
    }

    @Override
    public int getHumanCount() {
        int sum = 0;
        for (Player player : getPlayers()) {
            if (!(player.getController() instanceof AIController))
                sum++;
        }
        return sum;
    }

    @Override
    public int getHumanAliveCount() {
        int sum = 0;
        for(Entity entity : getEntities()){ //Thread-safety
            if(entity instanceof Character && !(((Character)entity).getController() instanceof AIController)){
                sum ++;
            }       
        }
        return sum;
    }

    public void setFps(int fps) {
        if (fps <= 0) {
            throw new RuntimeException("fps not positive");
        } else {
            this.fps = fps;
        }
    }

    public void setDuration(int duration) {
        if (duration < 0) {
            throw new RuntimeException("duration not positive");
        } else {
            this.duration = Math.max(1, duration);
            if (timeRemaining > duration)
                setTimeRemaining(duration);
        }
    }

    public void setTimeRemaining(int time) {
        if (timeRemaining < 0)
            throw new RuntimeException("time remaining not positive");
        else
            this.timeRemaining = time;
    }

    public void setWarmupDuration(int warmup) {
        if (warmup < 0)
            throw new RuntimeException("warmup not positive");
        else {
            this.warmupDuration = warmup;
            if (this.warmupTimeRemaining < this.warmupDuration)
                this.warmupTimeRemaining = this.warmupDuration;
        }
    }

    public void setRestTimeDuration(int restTime) {
        if (restTime < 0)
            throw new RuntimeException("restTime not positive");
        else {
            this.restTimeDuration = restTime;
            if (this.restTimeRemaining > this.restTimeDuration)
                this.restTimeRemaining = this.restTimeDuration;
        }
    }

    public void setTileSize(int tileSize) {
        map.setTileSize(tileSize);
    }

    /**
     * Pose une bombe sur la map par un joueur
     * @param character le joueur qui veut poser une bombe
     */
    abstract void plantBomb(Character character);

    /**
     * Crée une explosion sur la map par une bombe
     * @param bomb la bombe qui explose
     */
    abstract void createExplosion(Bomb bomb);

    /**
     * Signale qu'un bonus se fait ramasser par un joueur
     * @param x la coordonnée en x du joueur en pixel
     * @param y la coordonnée en y du joueur en pixel
     */
    abstract void pickUpBonus(double x, double y);

    /**
     * Pousse une bombe dans une direction
     * @param bomb Bombe à pousser
     * @param direction Direction vers laquelle pousser
     */
    abstract void kickBomb(Bomb bomb, Direction direction);

    /**
     * Construit un nouveau joueur pour un contrôleur
     * @param controller Contrôleur du joueur
     */
    public abstract Player newPlayer(Controller controller);

    /**
     * indique si la partie est prête à être commencée
     * @return true si oui, false sinon
     */
    public abstract boolean isReady();

    /**
     * indique si le round est terminé
     * @return true si oui, false sinon
     */
    public abstract boolean isRoundEnded();

    public abstract String getWinnerName();

    public abstract int getWinnerID();

    /**
     * Met à jour le monde en controllant :
     * - s'il faut enclencher la mort subite
     * - s'il faut supprimer des entités
     * puis en mettant la carte à jour
     */
    public GameState update() {
        if (warmupTimeRemaining > 0) {
            warmupTimeRemaining--;
            return GameState.WarmUp;
        } else if (!isRoundEnded()) {
            //update of timeRemaining
            timeRemaining -= 1;
        
            if (timeRemaining == 0) {
                fireEvent(GameEvent.SuddenDeath);
            }
            
            List<Integer> toRemove = new ArrayList<Integer>();
        
            synchronized (entities) {
                //update of Entities
                for (Entry<Integer, Entity> entry : entities.entrySet()) {
                    Entity entity = entry.getValue();
                    entity.update();
                    if (entity.isToRemove())
                        toRemove.add(entry.getKey());
                }
            }
        
            removeEntities(toRemove);
        
            //update of the map
            map.update();
            
            if (timeRemaining > 0)
                return GameState.Playing;
            else
                return GameState.SuddenDeath;
        } else if (round < roundMax || restTimeRemaining > 0) { //S'il reste des rounds ou si le
            restTimeRemaining--;                              // dernier temps de repos n'est pas fini
            
            if (round < roundMax && restTimeRemaining <= 0) //On relance s'il reste des rounds
                nextRound();
            
            return GameState.EndRound;
        } else
            return GameState.End;
    }
    
    void removeEntities(List<Integer> entityIDs) {
        for (Integer id : entityIDs)
            entities.remove(id);
    }

    /**
     * Met fin à la partie
     */
    public void stop() {
    }

    void newRound() {
        //time remaining back to beginning
        timeRemaining = duration;
        warmupTimeRemaining = warmupDuration;
        restTimeRemaining = restTimeDuration;
        round++;
      
        //reinitialize entities
        for (Entity entity : entities.values())
            entity.remove();
        removeEntities(new LinkedList<>(entities.keySet()));
        entities.clear();
    }

    /**
     * Relance la partie
     */
    public void nextRound() {
        newRound();
    }

    @Override
    public List<Character> getCharacters() {
        List<Character> characterList = new LinkedList<Character>();
        for(Entity entity : getEntities()){ //Thread-safety
            if(entity instanceof Character){
                characterList.add((Character)entity);
            }
        }
        return characterList;
    }

    /**
     * Ajoute un écouteur à la partie
     * @param listener Écouteur
     */
    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Envoie l'événement aux listeners
     * @param e Événement
     */
    void fireEvent(GameEvent e) {
        for(GameListener listener : listeners){
            listener.gameChanged(e);
        }
    }

    /**
     * Ajoute une entité avec l'ID spécifiée
     * @param entity l'entité qui doit être ajoutée
     * @param id l'identité de l'entité
     */
    void addEntity(Entity entity, int id) {
        entity.setID(id);
        entity.setWorld(this);
        entities.put(id, entity);
        map.addEntity(entity);
    }

    /**
     * Ajoute une entité avec le premier ID disponible
     * @param entity Entité à ajouter
     */
    void addEntity(Entity entity) {
        addEntity(entity, nextID);
        nextID++;
    }
}
