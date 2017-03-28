package game;

import network.Network.ControllerUpdate;

public class DummyController extends Controller {

    Direction direction;
    boolean bombing;

    public DummyController() { }
    
    public DummyController(String name) {
        setName(name);
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean isPlantingBomb() {
        if (bombing) {
            bombing = false;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }
    
    public void update(ControllerUpdate update) {
        direction = update.direction;
        bombing = update.bombing;
    }
}
