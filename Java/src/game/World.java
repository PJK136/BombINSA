package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("4e38e511-34e5-49ff-aa51-e135ece3c5eb")
public abstract class World implements WorldView {
    @objid ("85c96d62-8a34-4a32-b45b-ae1b9c9d4112")
     int FPS;

    @objid ("0a994301-baff-4943-8fc9-5e40b755921d")
     int timeRemaining;

    @objid ("cdff29e5-a4b9-4b5a-865a-838fddcdb57d")
     Map map;

    @objid ("ea256a0b-ce0d-4498-ac24-bd5ea8fb8825")
     List<Entity> entities = new ArrayList<Entity> ();

    @objid ("9aa33376-6f71-4b5f-a0af-9fcba7c1a5cd")
    public void getFPS() {
    }

    @objid ("181a1f7d-91b0-4a1e-8a77-235ba0c5dd0c")
    public int getTimeRemaining() {
    }

    @objid ("83167709-dc8e-456e-b787-b9a4ed0d8113")
    public List<Entity> getEntities() {
    }

    @objid ("8696ef92-01cf-4263-80c5-6bcf172924d8")
    public MapView getMap() {
    }

    @objid ("7f0207e3-fb26-4a93-8d10-c12f9c0735f1")
    abstract void plantBomb(double x, double y, int fire);

    @objid ("9c563a21-9aa5-4a46-9bc7-944623f9796c")
    abstract void plantBomb(Player player);

    @objid ("df2bc239-ca9f-42a9-b92a-6255de4d5c86")
    abstract void createExplosion(double x, double y, int fire);

    @objid ("15006b09-55e2-4635-b9b7-f4cee5978c5d")
    abstract void pickUpBonus(double x, double y);

    @objid ("d65622a5-0611-42cd-87a7-975a15931e59")
    public abstract void newPlayer(Controller controller);

    @objid ("30c7a359-b727-428f-8ef4-493db313017c")
    public abstract void update();

}
