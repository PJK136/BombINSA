package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/** Cette classe coordonne l'ensemble des actions pendant une partie */
@objid ("4e38e511-34e5-49ff-aa51-e135ece3c5eb")
public abstract class World implements WorldView {
    @objid ("85c96d62-8a34-4a32-b45b-ae1b9c9d4112")
     int fps = 60;

    @objid ("0a994301-baff-4943-8fc9-5e40b755921d")
     int timeRemaining = -1;

    @objid ("ed497149-29e3-423f-aa53-4d59e7f2d0a9")
     int duration;

    @objid ("179b51a3-de36-42d1-b4e7-5910602e3eb1")
     int warmupDuration;

     int restTimeDuration;

    @objid ("7cc86e7d-355f-47e2-9db6-26e82b9e1684")
     int warmupTimeRemaining = -1;

     int restTimeRemaining = -1;

    @objid ("dd221d3c-d9db-4f3a-9a49-3a81e5cc95df")
     int round = 0;

     int roundMax = 3;

    @objid ("6a5f65d2-9b0d-4b5a-a388-17ff14b96ca8")
    private int nextID = 1;

    @objid ("d4ef51fc-99a1-4469-a144-395b415f38c6")
    public static final int START_LIVES = 1;

    @objid ("acdeeb9e-e8f4-4338-953e-9a58f3e8e5d6")
    public static final int START_BOMB_MAX = 1;

    @objid ("c4fcf211-c9b3-4bd3-8331-cb2c246c1b66")
    public static final int START_RANGE = 1;

    @objid ("d92377a2-aad4-4982-9040-3f2145155a66")
    public static final double START_INVULNERABITY_DURATION = 1;

    @objid ("e291da2d-3413-4c04-9354-a2bdfd92e336")
    public static final double TIME_BEFORE_EXPLOSION = 3;

    @objid ("751024b2-9b44-4803-a99b-325fd609fbd3")
    public static final double EXPLOSION_DURATION = 0.25;

    @objid ("cdff29e5-a4b9-4b5a-865a-838fddcdb57d")
     Map map;

    @objid ("ea256a0b-ce0d-4498-ac24-bd5ea8fb8825")
     java.util.Map<Integer, Entity> entities = Collections.synchronizedMap(new HashMap<Integer, Entity> ());

    @objid ("20f367fb-bcb4-4389-912c-a406baff8d4e")
    public List<Controller> controllers = new ArrayList<Controller> ();

    @objid ("d409f763-e743-4545-b3b6-8c3aa8ce966a")
     List<GameListener> listeners = new LinkedList<GameListener>();

    @Override
    @objid ("9aa33376-6f71-4b5f-a0af-9fcba7c1a5cd")
    public int getFps() {
        return fps;
    }

    @Override
    @objid ("74f79401-b120-4eaf-81b7-d8ddf36eae60")
    public int getDuration() {
        return duration;
    }

    @Override
    @objid ("181a1f7d-91b0-4a1e-8a77-235ba0c5dd0c")
    public int getTimeRemaining() {
        return timeRemaining;
    }

    @objid ("1f2828a0-0e38-487d-87ca-21186c74455a")
    @Override
    public int getTimeElapsed() {
        return duration-timeRemaining;
    }

    @objid ("bb6812a0-7efd-4d61-91b2-aba3d64358c6")
    @Override
    public int getWarmupDuration() {
        return warmupDuration;
    }

    @objid ("6f6fa271-940b-4fe1-8dbb-f1f88d8555ab")
    @Override
    public int getWarmupTimeRemaining() {
        return warmupTimeRemaining;
    }

    public int getRestTimeRemaining() {
        return restTimeRemaining;
    }

    @objid ("a8a0c673-0be2-4b59-9a70-0b336383479c")
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
    @objid ("83167709-dc8e-456e-b787-b9a4ed0d8113")
    public List<Entity> getEntities() {
        //Thread-safety
        synchronized (entities) {
            return new LinkedList<Entity>(entities.values());            
        }
    }

    @Override
    @objid ("8696ef92-01cf-4263-80c5-6bcf172924d8")
    public MapView getMap() {
        return map;
    }

    @Override
    @objid ("cb863fac-e1bd-4a91-aa76-95e63ad3fc08")
    public int getPlayerCount() {
        return controllers.size();
    }

    @Override
    @objid ("fe9b5eb2-5def-4f69-8e0e-49e6eaaa3315")
    public int getCharacterAliveCount() {
        int sum = 0;
        for (Entity entity : getEntities()) { //Thread-safety
            if(entity instanceof Character){
                sum ++;
            }       
        }
        return sum;
    }

    @objid ("750bdf7a-3247-4d68-b7fc-a113033db70a")
    @Override
    public int getHumanCount() {
        int sum = 0;
        for (Controller controller : controllers) {
            if (!(controller instanceof AIController))
                sum++;
        }
        return sum;
    }

    @objid ("98c22605-3cc4-4413-9967-fcd03f4b00f1")
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

    @objid ("2aa100c7-ebde-4cd8-840f-24b2f13f54cd")
    public void setFps(int fps) {
        if (fps <= 0) {
            throw new RuntimeException("fps not positive");
        } else {
            this.fps = fps;
        }
    }

    @objid ("3b2131f9-67d7-42dd-b764-55a156072456")
    public void setDuration(int duration) {
        if (duration < 0) {
            throw new RuntimeException("duration not positive");
        } else {
            this.duration = Math.max(1, duration);
            if (timeRemaining > duration)
                setTimeRemaining(duration);
        }
    }

    @objid ("77769968-3eb5-4133-880a-e74cedee78ae")
    public void setTimeRemaining(int time) {
        if (timeRemaining < 0)
            throw new RuntimeException("time remaining not positive");
        else
            this.timeRemaining = time;
    }

    @objid ("33e095d8-68dd-4046-b765-17ecc0fe4365")
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

    @objid ("739ccd1c-6053-48a2-a809-596cf4134d36")
    public void setTileSize(int tileSize) {
        map.setTileSize(tileSize);
    }

    /**
     * Pose une bombe sur la map par un joueur
     * @param character le joueur qui veut poser une bombe
     */
    @objid ("9c563a21-9aa5-4a46-9bc7-944623f9796c")
    abstract void plantBomb(Character character);

    /**
     * Crée une explosion sur la map par une bombe
     * @param bomb la bombe qui explose
     */
    @objid ("df2bc239-ca9f-42a9-b92a-6255de4d5c86")
    abstract void createExplosion(Bomb bomb);

    /**
     * Signale qu'un bonus se fait ramasser par un joueur
     * @param x la coordonnée en x du joueur en pixel
     * @param y la coordonnée en y du joueur en pixel
     */
    @objid ("15006b09-55e2-4635-b9b7-f4cee5978c5d")
    abstract void pickUpBonus(double x, double y);

    /**
     * Pousse une bombe dans une direction
     * @param bomb Bombe à pousser
     * @param direction Direction vers laquelle pousser
     */
    @objid ("d2244540-1a9f-4ec7-bee2-e5b2a2c83862")
    abstract void kickBomb(Bomb bomb, Direction direction);

    /**
     * construit un nouveau controlleur à partir d'un controlleur préexistant
     * @param controller le controlleur préexistant
     */
    @objid ("d65622a5-0611-42cd-87a7-975a15931e59")
    public abstract void newController(Controller controller);

    /**
     * indique si la partie est prête à être commencée
     * @return true si oui, false sinon
     */
    @objid ("c1a7c8a0-32d1-41a0-a08a-d8fd2bbb79b0")
    public abstract boolean isReady();

    /**
     * indique si le round est terminé
     * @return true si oui, false sinon
     */
    @objid ("8573d9f6-2b91-46e8-815c-d1e296750608")
    public abstract boolean isRoundEnded();

    public abstract String getWinnerName();

    public abstract int getWinnerID();

    /**
     * Met à jour le monde en controllant :
     * - s'il faut enclencher la mort subite
     * - s'il faut supprimer des entités
     * puis en mettant la carte à jour
     */
    @objid ("30c7a359-b727-428f-8ef4-493db313017c")
    public GameState update() {       
        if (!isRoundEnded()) {
            if (warmupTimeRemaining > 0) {
                warmupTimeRemaining--;
                return GameState.WarmUp;
            } else {
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
            }
        }
        else if (round < roundMax || restTimeRemaining > 0) { //S'il reste des rounds ou si le
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
    @objid ("ff6dfd59-975b-4d3b-af34-f38b4e3ac3b5")
    public void stop() {
    }

    void newRound() {
        //time remaining back to beginning
        timeRemaining = duration;
        warmupTimeRemaining = warmupDuration;
        restTimeRemaining = restTimeDuration;
        round++;
      
        //reinitialize entities 
        entities.clear();
    }

    /**
     * Relance la partie
     */
    @objid ("cb1c5304-fd98-4582-be17-1c7dc3353443")
    public void nextRound() {
        newRound();
    }

    @Override
    @objid ("0b9057d7-eb96-4376-92be-bfb39cef93ff")
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
    @objid ("a94d9c92-e103-47fc-85c2-8ec0e6be9521")
    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Envoie l'événement aux listeners
     * @param e Événement
     */
    @objid ("4b33edc4-09ea-4366-aaed-a774e91e6c5a")
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
    @objid ("6159a84b-d00e-47f9-a813-9b123a35ee27")
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
    @objid ("27111301-80b0-479b-af73-bb78da106041")
    void addEntity(Entity entity) {
        addEntity(entity, nextID);
        nextID++;
    }
}
