package game;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

//import sun.util.resources.cldr.es.TimeZoneNames_es_AR;

@objid("8d1e22ca-441c-437e-83a3-fee76166baff")
public class Server extends World {
    
    final int START_LIVES = 3;
    final int START_BOMB_MAX = 1;
    final int START_RANGE = 1;
    final int START_INVULNERABITY_SEC = 1;
    final int TIME_BEFORE_EXPLOSION = 10;
    final int EXPLOSION_DURATION = 1;
    String mapFileName = new String();
    int playerSpawnNumber;
    Queue<Player> queuePlayer = new LinkedList<Player>();
    Queue<Bomb> queueBomb = new LinkedList<Bomb>();
    Queue<GridCoordinates> queueBonus = new LinkedList<GridCoordinates>();
    
        
    @objid("560005cd-1e82-4dc8-8a17-39d3577463ae")
    public Server(String mapFilename, int tileSize, int fps, int duration) throws Exception {
        map = new Map(tileSize);
        loadMap(mapFilename);
        setFps(fps);
        setDuration(duration);
        timeRemaining = duration*fps;
        playerSpawnNumber = 0;
    }

    @objid("2aa100c7-ebde-4cd8-840f-24b2f13f54cd")
    public void setFps(int fps) {
        if (fps <= 0) {
            throw new RuntimeException("fps not positive");
        } else {
            this.fps = fps;
        }
    }

    @objid("3b2131f9-67d7-42dd-b764-55a156072456")
    public void setDuration(int duration) {
        if (duration < 0) {
            throw new RuntimeException("duration not positive");
        } else {
            this.duration = duration;
            if (timeRemaining > duration*fps)
                timeRemaining = duration*fps;
        }
    }

    @objid("77769968-3eb5-4133-880a-e74cedee78ae")
    public void setTimeRemaining(int time) {
        this.timeRemaining = time;
    }

    @objid("739ccd1c-6053-48a2-a809-596cf4134d36")
    public void setTileSize(int tileSize) {
        map.setTileSize(tileSize);
    }

    @objid("4164c416-9e5c-461f-a7dc-1758c0f94d36")
    public void loadMap(String filename) throws Exception {
        map.loadMap(new String(Files.readAllBytes(Paths.get(filename))));
        mapFileName = filename;
    }
    
    @objid("3201955a-ab70-48b8-b676-a53ca4da06a7")
    void newPlayer(Controller controller) {
        int count = playerSpawnNumber;
        while(count > map.spawningLocations.size()){
            count -= map.spawningLocations.size();
        }
        Player player = new Player(this, (map.spawningLocations.get(count).x+0.5)*map.tileSize, (map.spawningLocations.get(count).y+0.5)*map.tileSize, controller, START_LIVES, START_BOMB_MAX, START_RANGE, START_INVULNERABITY_SEC*fps);
        entities.add(player);
        controller.setPlayer(player);
    }
    
    public void newController(Controller controller){
        controllers.add(controller);
        newPlayer(controller);
    }

    @objid("15f9ba61-54f9-4783-8bd0-923098e480d7")
    public void update() {
        //update of Entities
        Iterator<Entity> iterator = entities.iterator();
        while(iterator.hasNext()){
            Entity entity = iterator.next();
            if(entity.isToRemove()){
                iterator.remove();
            }
        }
        
        //update of the map
        map.update();
        
        //update of timeRemaining
        timeRemaining -= 1;
        
        //update of the new bombs
        for(Player player : queuePlayer){
            entities.add(new Bomb(this, player.getX(), player.getY(), player.getRange(), TIME_BEFORE_EXPLOSION, player));
        }
        
        //update of the bonus
        for(GridCoordinates bonus : queueBonus){
            map.setTileType(TileType.Empty,bonus);
        }
           
        //update of the bombs explosions
        for(Bomb bomb : queueBomb){
            //locate center of the bomb impact
            GridCoordinates gc = new GridCoordinates();
            gc = map.toGridCoordinates(bomb.getX(), bomb.getY());
            //setExplosion for columns
            for(int i=gc.x-bomb.owner.range; i<=gc.x+bomb.owner.range; i++){
                if(i>=0 && i<map.tiles.length){
                    GridCoordinates gcl = new GridCoordinates(i,gc.y);
                    map.setExplosion(EXPLOSION_DURATION,gcl);
                }
            }
            //setExplosion for lines
            for(int j=gc.y-bomb.owner.range; j<=gc.y+bomb.owner.range; j++){
                if(j>=0 && j<map.tiles.length && j!=gc.y){  //not to explode center twice
                    GridCoordinates gcc = new GridCoordinates(gc.x,j);
                    map.setExplosion(EXPLOSION_DURATION,gcc);
                }
            }
        }
        //clearing all the queue lists
        queuePlayer.clear();
        queueBomb.clear();
        queueBonus.clear();
    }

    @objid("a193a9c9-e032-4940-953b-5923c9da849e")
    public void restart() throws Exception {
        //reinitialize entities 
        entities.clear();
        //renew players
        for(Controller controller : controllers){
            newPlayer(controller);
        }
        //time remaining back to beginning
        timeRemaining = duration*fps;
        //reload map
        loadMap(mapFileName);
    }

    @objid("b9fb0ddd-3cff-4226-9167-0e4f94ea4d9e")
    @Override
    void plantBomb(double x, double y, int range) {
        // TODO Auto-generated method stub
    }

    @objid("b13fef2c-1897-428c-871d-8a201627e755")
    @Override
    void plantBomb(Player player) {
        queuePlayer.add(player);
    }

    @objid("d7b25576-cf20-47f5-9a75-9bc74eee10c2")
    @Override
    void createExplosion(Bomb bomb) {
        queueBomb.add(bomb);
    }

    @objid("1883d42c-f691-41ab-acc6-c37fc049f80c")
    @Override
    void pickUpBonus(double x, double y) {
        GridCoordinates bonus = new GridCoordinates();
        bonus = map.toGridCoordinates(x,y);
        queueBonus.add(bonus);
    }

}
