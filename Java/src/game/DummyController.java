package game;


/** Ce contrôleur se conforte aux paramètres définis manuellement */
public class DummyController extends Controller {
    protected Direction direction;

    protected boolean bombing;
    
    public DummyController() {
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
    }

}
