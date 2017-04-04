package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("435a3f56-662a-44c1-a8c7-b82ab53bd0e1")
public abstract class Controller {
    @objid ("d1b9902a-8b0f-4eaa-95ee-934fea8da2a9")
     String name;

    @objid ("697c8788-0531-4864-9423-7c5e05988fad")
     WorldView world;

    @objid ("bcdf68cd-860d-43c6-9164-a9abc315aa3b")
    protected Player player;

    @objid ("ca776232-1a17-4e2e-a7a6-eeffe4a104e4")
    public String getName() {
        return name;
    }

    @objid ("f3735353-af64-476c-8a6d-040da98f2ae6")
    public void setName(String name) {
        this.name = name;
    }

    @objid ("9eaf5dce-ffc6-4b09-b661-af4e199a78a9")
    public Player getPlayer() {
        return this.player;
    }

    @objid ("9e16b8d9-71bf-40b2-9cbf-69f5e7bc8583")
    public void setPlayer(Player value) {
        this.player = value;
    }

    @objid ("b4919637-6675-4e6b-bd27-a18c64057a48")
    public void setWorldView(WorldView world) {
        this.world = world;
    }
    
    @objid ("0ebad723-c10a-4def-be2c-c30ca87e26c9")
    public abstract Direction getDirection();
    
    /** 
     * Indique si le joueur pose une bombe
     * @return true si oui, false sinon
     */
    @objid ("fbd83e0a-a70c-4e3b-a076-312bb8d237d8")
    public abstract boolean isPlantingBomb();
    
    /**
     * Met à jour le controlleur
     */
    @objid ("99886342-ec39-44a9-aae1-581d9aa251df")
    public abstract void update();

}
