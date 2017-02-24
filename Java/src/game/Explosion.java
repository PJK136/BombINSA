package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("6f5b53c8-2117-4a0a-b672-90af08afc55f")
public class Explosion extends Tile {
    @objid ("7ffae68f-5714-4e34-91d3-598f263115eb")
     int timeRemaining;

    @objid ("e9f0c6af-38ae-4e84-8693-08703a6a4ba8")
     Tile nextTile;

    @objid ("c37f246c-1b88-4875-918c-5891b910ac10")
    public Explosion(int duration, Tile nextTile) {
    }

    @objid ("a0f2cff1-fab0-4f53-b3e0-2666e4eb5b0b")
    public int getTimeRemaining() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.timeRemaining;
    }

    @objid ("063162a4-f7f1-4461-b355-894f79597731")
    void setTimeRemaining(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.timeRemaining = value;
    }

    @objid ("d601fae6-e003-4465-a524-487f251e6f88")
    void update() {
    }

}
