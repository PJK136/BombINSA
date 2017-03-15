package game;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("8d1e22ca-441c-437e-83a3-fee76166baff")
public class Server extends World {
    @objid ("d4ef51fc-99a1-4469-a144-395b415f38c6")
    public static final int START_LIVES = 3;

    @objid ("acdeeb9e-e8f4-4338-953e-9a58f3e8e5d6")
    public static final int START_BOMB_MAX = 1;

    @objid ("c4fcf211-c9b3-4bd3-8331-cb2c246c1b66")
    public static final int START_RANGE = 1;

    @objid ("d92377a2-aad4-4982-9040-3f2145155a66")
    public static final double START_INVULNERABITY_DURATION = 1;

    @objid ("e291da2d-3413-4c04-9354-a2bdfd92e336")
    public static final double TIME_BEFORE_EXPLOSION = 3;

    @objid ("751024b2-9b44-4803-a99b-325fd609fbd3")
    public static final double EXPLOSION_DURATION = 0.3;

    @objid ("f708fe23-9f16-4721-b206-d54749adf7fb")
     String mapFileName = new String();

    @objid ("a6755987-9eb6-4703-a202-cad2bb6345a4")
     Queue<Player> queuePlayer = new LinkedList<Player>();

    @objid ("8f67b398-0aba-4beb-872c-590118673a03")
     Queue<Bomb> queueBomb = new LinkedList<Bomb>();

    HashMap<Bomb, Direction> queueKickBomb = new HashMap<>();
    
    @objid ("d09dce2a-6ccc-4d37-838d-8a1f201d7b56")
     Queue<GridCoordinates> queueBonus = new LinkedList<GridCoordinates>();
    
    int nextPlayerID = 0;

    @objid ("560005cd-1e82-4dc8-8a17-39d3577463ae")
    public Server(String mapFilename, int tileSize, int fps, int duration) throws Exception {
        map = new Map(tileSize);
        loadMap(mapFilename);
        setFps(fps);
        setDuration(duration);
        timeRemaining = duration*fps;
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
            if (timeRemaining > duration*fps)
                timeRemaining = duration*fps;
        }
    }

    @objid ("77769968-3eb5-4133-880a-e74cedee78ae")
    public void setTimeRemaining(int time) {
        this.timeRemaining = time;
    }

    @objid ("739ccd1c-6053-48a2-a809-596cf4134d36")
    public void setTileSize(int tileSize) {
        map.setTileSize(tileSize);
    }

    @objid ("4164c416-9e5c-461f-a7dc-1758c0f94d36")
    public void loadMap(String filename) throws Exception {
        map.loadMap(new String(Files.readAllBytes(Paths.get(filename))));
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
            List<Player> playerList = new LinkedList<Player>();
            playerList = getPlayers();
            for(Player player : playerList){
                player.setLives(1);
                player.removeShield();
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
            boolean collide = false;
            
            map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Center, null, explosionGC);
            
            for (explosionGC.x = bombGC.x-1;
                 explosionGC.x >= Math.max(0, bombGC.x-bomb.getRange()) && !collide;
                 explosionGC.x--) {
                map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Branch, Direction.Left, explosionGC);
                collide = map.isCollidable(explosionGC);
            }
            if (!map.isInsideMap(explosionGC) || !map.isExploding(explosionGC)) {
                explosionGC.x++;
                map.setExplosionEnd(explosionGC);
            }
            
            collide = false;
            for (explosionGC.x = bombGC.x+1;
                 explosionGC.x <= Math.min(map.getColumnCount()-1, bombGC.x+bomb.getRange()) && !collide;
                 explosionGC.x++) {
                map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Branch, Direction.Right, explosionGC);
                collide = map.isCollidable(explosionGC);
            }
            if (!map.isInsideMap(explosionGC) || !map.isExploding(explosionGC)) {
                explosionGC.x--;
                map.setExplosionEnd(explosionGC);
            }
            
            collide = false;
            explosionGC.x = bombGC.x;
            for (explosionGC.y = bombGC.y-1;
                 explosionGC.y >= Math.max(0, bombGC.y-bomb.getRange()) && !collide;
                 explosionGC.y--) {
                map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Branch, Direction.Up, explosionGC);
                collide = map.isCollidable(explosionGC);
            }
            if (!map.isInsideMap(explosionGC) || !map.isExploding(explosionGC)) {
                explosionGC.y++;
                map.setExplosionEnd(explosionGC);
            }
            
            collide = false;
            for (explosionGC.y = bombGC.y+1;
                 explosionGC.y <= Math.min(map.getRowCount()-1, bombGC.y+bomb.getRange()) && !collide;
                 explosionGC.y++) {
                map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Branch, Direction.Down, explosionGC);
                collide = map.isCollidable(explosionGC);
            }
            if (!map.isInsideMap(explosionGC) || !map.isExploding(explosionGC)) {
                explosionGC.y--;
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
        timeRemaining = duration*fps;
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
    
    @Override
    void kickBomb(Bomb bomb, Direction direction) {
        if (queueKickBomb.containsKey(bomb)) {
            //Si plusieurs joueurs veulent pousser la bombe mais pas dans le même sens
            if (queueKickBomb.get(bomb) != direction)
                queueKickBomb.put(bomb, null);
        } else
            queueKickBomb.put(bomb, direction);
    }
    
    private void addEntity(Entity entity) {
        entities.add(entity);
        map.addEntity(entity);
    }

}
