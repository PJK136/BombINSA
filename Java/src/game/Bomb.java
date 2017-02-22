package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("3aaea1b2-78f7-442c-962a-a6539bf9fa5c")
public class Bomb extends Entity {
    @objid ("16b02478-a389-4582-8e37-31931b9fc244")
    protected int timeRemaining;

    @objid ("3237e045-5754-40c1-a209-bb26f9d6d69d")
    protected int fire;

    @objid ("b0fcfe7f-9444-4e96-98dd-bf74962da4ad")
    int getTimeRemaining() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.timeRemaining;
    }

    @objid ("1f122c1e-da68-4f14-a845-baf6dc80d778")
    void setTimeRemaining(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.timeRemaining = value;
    }

    @objid ("e24f4fde-5845-41b9-b9c6-1c3bc72073fd")
    void update() {
    }

    @objid ("eb2d2878-0d9e-40a2-b659-a35ad6afbdc1")
    int getFire() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.fire;
    }

    @objid ("4f9982c9-132e-448d-b7e1-4c465cbdacdf")
    void setFire(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.fire = value;
    }

}
