package game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import com.modeliosoft.modelio.javadesigner.annotations.objid;


@objid ("8d1e22ca-441c-437e-83a3-fee76166baff")
public class Local extends World {
    @objid ("f708fe23-9f16-4721-b206-d54749adf7fb")
     String mapFileName = new String();

    @objid ("bea83389-bb33-4211-97c6-1b6fc49e23eb")
     HashMap<Bomb, Direction> queueKickBomb = new HashMap<>();

    @objid ("f4f3efa0-82ed-4a6e-a844-a5b784ab690e")
     int nextPlayerID = 0;

    @objid ("a6755987-9eb6-4703-a202-cad2bb6345a4")
     Queue<Player> queuePlayer = new LinkedList<Player>();

    @objid ("8f67b398-0aba-4beb-872c-590118673a03")
     Queue<Bomb> queueBomb = new LinkedList<Bomb>();

    @objid ("d09dce2a-6ccc-4d37-838d-8a1f201d7b56")
     Queue<GridCoordinates> queueBonus = new LinkedList<GridCoordinates>();
    
    @objid ("560005cd-1e82-4dc8-8a17-39d3577463ae")
    public Local(String mapFilename, int tileSize, int fps, int duration) throws Exception {
        createMap(tileSize);
        loadMap(mapFilename);
        setFps(fps);
        setDuration(duration);
        setTimeRemaining(duration);
    }
    
    void createMap(int tileSize) {
        map = new Map(tileSize);
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
            this.duration = duration;
            if (timeRemaining > duration)
                timeRemaining = duration;
        }
    }

    @objid ("77769968-3eb5-4133-880a-e74cedee78ae")
    public void setTimeRemaining(int time) {
        if (timeRemaining < 0)
            throw new RuntimeException("time remaining not positive");
        else
            this.timeRemaining = time;
    }

    @objid ("739ccd1c-6053-48a2-a809-596cf4134d36")
    public void setTileSize(int tileSize) {
        map.setTileSize(tileSize);
    }

    @objid ("4164c416-9e5c-461f-a7dc-1758c0f94d36")
    public void loadMap(String filename) throws Exception {
        if (filename == null || filename.isEmpty())
            throw new Exception("Nom de fichier vide");
        
        try {
            map.loadMap(Paths.get(filename));
        } catch (IOException e) {
            InputStream input = getClass().getResourceAsStream("/maps/"+filename);
            if (input != null)
                map.loadMap(input);
            else
                throw new Exception("Impossible de charger " + filename);
        }
        
        mapFileName = filename;
    }

    @objid ("57eecd7c-87d7-4fb4-933f-3928adf88bf1")
    public void newController(Controller controller) {
        newPlayer(controller);
        controllers.add(controller);
    }

    @objid ("3201955a-ab70-48b8-b676-a53ca4da06a7")
    void newPlayer(Controller controller) {
        if (map.spawningLocations.size() == 0)
            return;
        
        int spawnIndex = nextPlayerID % map.spawningLocations.size();
        Player player = new Player(this, map.toCenterX(map.spawningLocations.get(spawnIndex)),
                                         map.toCenterY(map.spawningLocations.get(spawnIndex)),
                                         controller,
                                         nextPlayerID,
                                         START_LIVES,
                                         START_BOMB_MAX,
                                         START_RANGE,
                                         (int)(START_INVULNERABITY_DURATION*fps));
        addEntity(player);
        nextPlayerID++;
        controller.setPlayer(player);
        controller.setWorldView(this);
    }

    @objid ("15f9ba61-54f9-4783-8bd0-923098e480d7")
    public void update() {
        //update of Entities
        Iterator<Entity> iterator = entities.iterator();
        while(iterator.hasNext()){
            Entity entity = iterator.next();
            entity.update();
            if(entity.isToRemove()){
                iterator.remove();
            }
        }
        
        //update of the map
        map.update();
        
        if(timeRemaining == 0){
            fireEvent(Event.SuddenDeath);
        }
        //sudden death case
        if(timeRemaining<0 && timeRemaining%(0.750*fps) == 0){
            boolean bombPlanted = false;
            while(!bombPlanted){
                GridCoordinates gcRnd = new GridCoordinates((int)(Math.random()*map.getColumnCount()), (int)(Math.random()*map.getRowCount()));
                if(map.getTileType(gcRnd) == TileType.Empty){
                    addEntity(new Bomb(this,gcRnd,4,(int)(TIME_BEFORE_EXPLOSION*fps)));
                    bombPlanted = true;
                }
            }
        
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    ((Player)entity).setLives(1);
                    ((Player)entity).removeShield();
                }
            }
            
        }
        
        //update of the new bombs
        for(Player player : queuePlayer){
            addEntity(new Bomb(this, player, (int)(TIME_BEFORE_EXPLOSION*fps)));
        }
        
        //update of the bonus
        for(GridCoordinates bonus : queueBonus){
            map.setTileType(TileType.Empty,bonus);
        }
           
        //update of the bombs explosions
        for(Bomb bomb : queueBomb){
            //locate center of the bomb impact
            GridCoordinates bombGC = map.toGridCoordinates(bomb.getX(), bomb.getY());
            GridCoordinates explosionGC = new GridCoordinates(bombGC);
            
            if (map.isExplodable(explosionGC))
                map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Center, null, explosionGC);
            
            for (Direction direction : Direction.values()) {
                explosionGC = bombGC;
                GridCoordinates nextGC = bombGC.neighbor(direction);
                boolean hasCollided = false;
                while (GridCoordinates.distance(bombGC, nextGC) <= bomb.getRange() &&
                       map.isInsideMap(nextGC) && !hasCollided && map.isExplodable(nextGC)) {
                    explosionGC = nextGC;
                    map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Branch, direction, explosionGC);
                    hasCollided = map.isCollidable(explosionGC) || map.hasBomb(explosionGC);
                    nextGC = explosionGC.neighbor(direction);
                }
                map.setExplosionEnd(explosionGC);
            }
        }
        
        //Update kicks
        for (Entry<Bomb, Direction> entry : queueKickBomb.entrySet()) {
            if (entry.getValue() != null) {
                entry.getKey().setDirection(entry.getValue());
                entry.getKey().setSpeed(Bomb.BOMB_DEFAULT_SPEED*map.getTileSize()/getFps());
            }
        }
        
        if(!queueBomb.isEmpty()){  
            fireEvent(Event.Explosion);
        }
        if(!queueBonus.isEmpty()){
            fireEvent(Event.PickUp);
        }
        
        //clearing all the queue lists
        queuePlayer.clear();
        queueBomb.clear();
        queueBonus.clear();
        queueKickBomb.clear();
        
        //update of timeRemaining
        timeRemaining -= 1;
    }

    @objid ("a193a9c9-e032-4940-953b-5923c9da849e")
    public void restart() throws Exception {
        //reinitialize entities 
        entities.clear();
        //renew players
        for(Controller controller : controllers){
            newPlayer(controller);
        }
        //time remaining back to beginning
        timeRemaining = duration;
        //reload map
        loadMap(mapFileName);
    }

/*@objid ("b9fb0ddd-3cff-4226-9167-0e4f94ea4d9e")
    @Override
    void plantBomb(double x, double y, int range) {
        not really necessary for the moment
    }*/
    @objid ("b13fef2c-1897-428c-871d-8a201627e755")
    @Override
    void plantBomb(Player player) {
        queuePlayer.add(player);
    }

    @objid ("d7b25576-cf20-47f5-9a75-9bc74eee10c2")
    @Override
    void createExplosion(Bomb bomb) {
        queueBomb.add(bomb);
    }

    @objid ("1883d42c-f691-41ab-acc6-c37fc049f80c")
    @Override
    void pickUpBonus(double x, double y) {
        queueBonus.add(map.toGridCoordinates(x,y));
    }

    @objid ("cd4f1181-89bd-4537-a370-4d025098310e")
    @Override
    void kickBomb(Bomb bomb, Direction direction) {
        if (queueKickBomb.containsKey(bomb)) {
            //Si plusieurs joueurs veulent pousser la bombe mais pas dans le même sens
            if (queueKickBomb.get(bomb) != direction)
                queueKickBomb.put(bomb, null);
        } else
            queueKickBomb.put(bomb, direction);
    }

    @objid ("27111301-80b0-479b-af73-bb78da106041")
    private void addEntity(Entity entity) {
        entities.add(entity);
        map.addEntity(entity);
    }

}
