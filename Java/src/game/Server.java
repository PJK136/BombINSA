package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid("8d1e22ca-441c-437e-83a3-fee76166baff")
public class Server extends World {
    
    final int START_LIVES = 3;
    final int START_BOMB_MAX = 1;
    final int START_RANGE = 1;
    final int START_INVULNERABITY_SEC = 1;
    int playerSpawnNumber;
        
    @objid("560005cd-1e82-4dc8-8a17-39d3577463ae")
    public Server(String mapFilename, int tileSize, int fps, int duration) {
        map = new Map(tileSize);
        map.loadMap(mapFilename);
        setFps(fps);
        setDuration(duration);
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
        }
    }

    @objid("77769968-3eb5-4133-880a-e74cedee78ae")
    public void setTimeRemaining(int time) {
        if (time < 0) {
            throw new RuntimeException("time remaining not positive");
        } else {
            this.timeRemaining = time;
        }
    }

    @objid("739ccd1c-6053-48a2-a809-596cf4134d36")
    public void setTileSize(int tileSize) {

    }

    @objid("4164c416-9e5c-461f-a7dc-1758c0f94d36")
    public void loadMap(String filename) {
        String mapContent = new String();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                mapContent = mapContent + line;
            }
        } catch (IOException x) {
            //TODO : exception à gérer
            System.err.format("IOException: %s%n", x);
        }
        map.loadMap(mapContent);
    }
    

    @objid("3201955a-ab70-48b8-b676-a53ca4da06a7")
    @Override
    public void newPlayer(Controller controller) {
        int count = playerSpawnNumber;
        while(count > map.spawningLocations.size()){
            count -= map.spawningLocations.size();
        }
        new Player(this, (map.spawningLocations.get(count).x+0.5)*map.tileSize, (map.spawningLocations.get(count).y+0.5)*map.tileSize, controller, START_LIVES, START_BOMB_MAX, START_RANGE, START_INVULNERABITY_SEC*fps);
    }

    @objid("15f9ba61-54f9-4783-8bd0-923098e480d7")
    public void update() {
    }

    @objid("a193a9c9-e032-4940-953b-5923c9da849e")
    public void restart() {
    }

    @objid("b9fb0ddd-3cff-4226-9167-0e4f94ea4d9e")
    @Override
    void plantBomb(double x, double y, int range) {
        // TODO Auto-generated method stub
    }

    @objid("b13fef2c-1897-428c-871d-8a201627e755")
    @Override
    void plantBomb(Player player) {
        // TODO Auto-generated method stub
    }

    @objid("d7b25576-cf20-47f5-9a75-9bc74eee10c2")
    @Override
    void createExplosion(double x, double y, int range) {
        // TODO Auto-generated method stub
    }

    @objid("1883d42c-f691-41ab-acc6-c37fc049f80c")
    @Override
    void pickUpBonus(double x, double y) {
        // TODO Auto-generated method stub
    }

}
