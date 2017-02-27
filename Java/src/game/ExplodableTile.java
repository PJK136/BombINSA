package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("760b79e9-bc69-4190-a3ce-f08660795ed5")
public class ExplodableTile extends Tile {
    @objid ("7ffae68f-5714-4e34-91d3-598f263115eb")
     int explosionTimeRemaining;

    @objid ("afce3c9f-2a7e-43fa-b8fe-9183c2e26f63")
    public boolean isExploding() {
    }

    @objid ("a0f2cff1-fab0-4f53-b3e0-2666e4eb5b0b")
    public int getExplosionTimeRemaining() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.explosionTimeRemaining;
    }

    @objid ("986e8a59-7984-46ab-ba71-b725066f7aaa")
    void setExplosionDuration(int duration) {
    }

    @objid ("d601fae6-e003-4465-a524-487f251e6f88")
    void update() {
    }

    @objid ("847c1e18-ba21-46a3-9c9a-af2ebb645805")
    void postExplosion() {
    }

}
