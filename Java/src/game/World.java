package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("4e38e511-34e5-49ff-aa51-e135ece3c5eb")
public abstract class World implements WorldView {
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
    
    @objid ("85c96d62-8a34-4a32-b45b-ae1b9c9d4112")
     int fps;

    @objid ("0a994301-baff-4943-8fc9-5e40b755921d")
     int timeRemaining;

    @objid ("ed497149-29e3-423f-aa53-4d59e7f2d0a9")
     int duration; // En secondes

    @objid ("cdff29e5-a4b9-4b5a-865a-838fddcdb57d")
     Map map;

    @objid ("ea256a0b-ce0d-4498-ac24-bd5ea8fb8825")
     List<Entity> entities = Collections.synchronizedList(new LinkedList<Entity> ());

    @objid ("20f367fb-bcb4-4389-912c-a406baff8d4e")
    public List<Controller> controllers = new ArrayList<Controller> ();

    @objid ("9aa33376-6f71-4b5f-a0af-9fcba7c1a5cd")
    public int getFps() {
        return fps;
    }

    @objid ("74f79401-b120-4eaf-81b7-d8ddf36eae60")
    public int getDuration() {
        return duration;
    }

    @objid ("181a1f7d-91b0-4a1e-8a77-235ba0c5dd0c")
    public int getTimeRemaining() {
        return timeRemaining;
    }
    
    @Override
    public int getTimeElapsed() {
        return duration-timeRemaining;
    }

    @objid ("83167709-dc8e-456e-b787-b9a4ed0d8113")
    public List<Entity> getEntities() { //Thread-safety
        synchronized (entities) {
            return new LinkedList<Entity>(entities);            
        }
    }

    @objid ("8696ef92-01cf-4263-80c5-6bcf172924d8")
    public MapView getMap() {
        return map;
    }

    @objid ("cb863fac-e1bd-4a91-aa76-95e63ad3fc08")
    public int getPlayerCount() {
        return controllers.size();
    }

    @objid ("fe9b5eb2-5def-4f69-8e0e-49e6eaaa3315")
    public int getPlayerAliveCount() {
        int sum = 0;
        for(Entity entity : getEntities()){ //Thread-safety
            if(entity instanceof Player){
                sum ++;
            }       
        }
        return sum;
    }
    
    public List<Player> getPlayers() {
        List<Player> playerList = new LinkedList<Player>();
        for(Entity entity : getEntities()){ //Thread-safety
            if(entity instanceof Player){
                playerList.add((Player)entity);
            }
        }
        return playerList;
    }

    /*@objid ("7f0207e3-fb26-4a93-8d10-c12f9c0735f1")
    abstract void plantBomb(double x, double y, int range);*/

    @objid ("9c563a21-9aa5-4a46-9bc7-944623f9796c")
    abstract void plantBomb(Player player);

    @objid ("df2bc239-ca9f-42a9-b92a-6255de4d5c86")
    abstract void createExplosion(Bomb bomb);

    @objid ("15006b09-55e2-4635-b9b7-f4cee5978c5d")
    abstract void pickUpBonus(double x, double y);
    
    abstract void kickBomb(Bomb bomb, Direction direction);
    
    @objid ("d65622a5-0611-42cd-87a7-975a15931e59")
    public abstract void newController(Controller controller);

    @objid ("30c7a359-b727-428f-8ef4-493db313017c")
    public abstract void update();

    @objid ("cb1c5304-fd98-4582-be17-1c7dc3353443")
    public abstract void restart() throws Exception;
    
    //http://answers.unity3d.com/questions/150347/what-exactly-does-timetime-do-in-mathfpingpong.html
    private static double pingPong(int time, int length) {
        int l = 2 * length;
        int t = time % l;
        
        if (0 <= t && t < length)
            return t;
        else
            return l - t;
    }
    
    public static boolean oscillate(int remaining, int duration, int intervalMin, int intervalMax) {
        //http://answers.unity3d.com/questions/678855/trying-to-get-object-to-blink-speed-acording-to-ti.html
        int interval = intervalMin + remaining*(intervalMax - intervalMin)/duration;        
        return pingPong(duration - remaining, interval) > interval/2; //Ping pong sur la durée écoulée
    }

}
