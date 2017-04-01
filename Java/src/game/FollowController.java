package game;

public class FollowController extends Controller {
    
    public FollowController() { }

    @Override
    public Direction getDirection() {
        if (player != null && player.getSpeed() != 0.)
            return player.getDirection();
        else
            return null;
    }

    @Override
    public boolean isPlantingBomb() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }
}
