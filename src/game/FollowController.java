package game;


/**
 * Ce contrôleur suit le déplacement du joueur
 */
public class FollowController extends Controller {
    public FollowController() {
    }

    @Override
    public Direction getDirection() {
        if (character != null && character.getSpeed() != 0.)
            return character.getDirection();
        else
            return null;
    }

    @Override
    public boolean isPlantingBomb() {
        return false;
    }

    @Override
    public void update() {
    }

}
