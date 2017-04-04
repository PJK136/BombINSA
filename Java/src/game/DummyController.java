package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("74c3a922-a208-46bf-aca4-54cc99330dda")
public class DummyController extends Controller {
    @objid ("e0be10f7-40dd-4611-bdcf-fbffb74e0c80")
    protected Direction direction;

    @objid ("aecfaad0-e24d-47f8-b86a-c14fa6070470")
    protected boolean bombing;
    
    @objid ("81b2855a-13bd-435a-b9ac-1d14266f3317")
    public DummyController() {
    }

    @objid ("712f9d33-cde0-4325-9c9e-efb0ecd88a66")
    @Override
    public Direction getDirection() {
        return direction;
    }

    @objid ("2364fb29-030c-4b5f-abf7-f23754311395")
    @Override
    public boolean isPlantingBomb() {
        if (bombing) {
            bombing = false;
            return true;
        }
        return false;
    }
    
    @objid ("29579ec4-a744-4d1b-ae6d-50569074af38")
    @Override
    public void update() {
        // TODO Auto-generated method stub
    }

}
