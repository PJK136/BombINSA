package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Ce contrôleur suit le déplacement du joueur
 */
@objid ("756f6969-df41-4003-83d3-def42ac8a49a")
public class FollowController extends Controller {
    @objid ("d6d5397c-9b93-41a8-a7cc-53821fcf8371")
    public FollowController() {
    }

    @objid ("64e637c6-fb7b-48be-aac7-dd9ab2cc9285")
    @Override
    public Direction getDirection() {
        if (player != null && player.getSpeed() != 0.)
            return player.getDirection();
        else
            return null;
    }

    @objid ("f26084d0-54f2-40f0-94b7-51c9b4e2d76b")
    @Override
    public boolean isPlantingBomb() {
        // TODO Auto-generated method stub
        return false;
    }

    @objid ("cc395a3f-f171-424a-b844-6f96aa91403d")
    @Override
    public void update() {
        // TODO Auto-generated method stub
    }

}
