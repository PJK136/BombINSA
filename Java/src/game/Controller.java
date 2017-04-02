package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("435a3f56-662a-44c1-a8c7-b82ab53bd0e1")
public abstract class Controller {
    @objid ("d1b9902a-8b0f-4eaa-95ee-934fea8da2a9")
     String name;

    @objid ("6c06ee22-bc28-4f35-aa7a-56b57771f669")
     Player player;

    @objid ("697c8788-0531-4864-9423-7c5e05988fad")
     WorldView world;

    @objid ("ca776232-1a17-4e2e-a7a6-eeffe4a104e4")
    public String getName() {
        return name;
    }

    @objid ("f3735353-af64-476c-8a6d-040da98f2ae6")
    public void setName(String name) {
        this.name = name;
    }

    @objid ("f1ad4fb1-c0c9-41d7-83b7-3b7b702b734d")
    public Player getPlayer() {
        return player;
    }

    @objid ("2de0046a-fb4b-4667-ae22-fccfb9d3d853")
    public void setPlayer(Player player) {
        this.player = player;
    }

    @objid ("b4919637-6675-4e6b-bd27-a18c64057a48")
    public void setWorldView(WorldView world) {
        this.world = world;
    }

    @objid ("0ebad723-c10a-4def-be2c-c30ca87e26c9")
    public abstract Direction getDirection();

    @objid ("fbd83e0a-a70c-4e3b-a076-312bb8d237d8")
    public abstract boolean isPlantingBomb();

    @objid ("99886342-ec39-44a9-aae1-581d9aa251df")
    public abstract void update();

}
