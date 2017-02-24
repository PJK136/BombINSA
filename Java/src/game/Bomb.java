package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("4399d714-75ab-40bb-b694-6207782f55ce")
public class Bomb extends Entity {
    @objid ("7cf845bf-dacd-4f1f-9c63-46ad9d0e478b")
     int fire;

    @objid ("57ad54f8-e371-4980-8fcc-383afd09c461")
     int timeRemaining;

    @objid ("d663f27d-b4de-411a-b687-8ae5d439ab48")
    protected Player owner;

    @objid ("f9841259-647d-4cd7-9bb7-f10aea5a4794")
    public Bomb(double x, double y, int fire, int duration, Player owner) {
    }

    @objid ("af854c36-bd42-4e95-bfb6-98d2b4864671")
    public int getFire() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.fire;
    }

    @objid ("4f8c6754-27ec-4d70-8fa4-df7fd74b49fe")
    void setFire(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.fire = value;
    }

    @objid ("46c24a88-c0e7-42af-aa08-f2217f450044")
    public int getTimeRemaining() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.timeRemaining;
    }

    @objid ("8568627a-17f2-49d3-a8e6-c690ecf21ab1")
    void setTimeRemaining(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.timeRemaining = value;
    }

    @objid ("ce00607d-3c72-45ea-b3e1-5a3ac0315b22")
    void update() {
    }

}
