package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("7d9743df-c7cd-4679-9771-fa22b1be441d")
public class Player extends Entity {
    @objid ("c10c97b4-7ea2-4021-aff7-4c0b87aac71b")
     int lives;

    @objid ("42154f7e-37fd-4c85-b8bf-440560e7ccdf")
     int bombCount;

    @objid ("b8822ff6-0716-4acf-844b-19ea1d23faa0")
     int bombMax;

    @objid ("0ec9b326-319f-4b00-8ebb-c02b47dc166f")
     int fire;

    @objid ("d3e49717-5f5c-49c0-b87a-e6ed29629386")
     boolean[] playerAbilites;

    @objid ("efe71f6e-6ac1-4f9a-bb96-98975b970b23")
     int invulnerability;

    @objid ("2f129ce7-ac16-42b5-85cb-d57015645a67")
    protected Controller controller;

    @objid ("007b7078-5eba-4ff5-a413-43e779f00b19")
    public int getLives() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.lives;
    }

    @objid ("c5f1c15a-e638-4096-9f19-f77708761f51")
    void setLives(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.lives = value;
    }

    @objid ("31123492-c96a-4af6-a1df-d35617285e33")
    public int getBombCount() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.bombCount;
    }

    @objid ("778d6e7d-a16e-44d4-824d-f4ad18670c31")
    void setBombCount(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.bombCount = value;
    }

    @objid ("38aca366-45f0-4745-934b-576af97cd356")
    public int getBombMax() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.bombMax;
    }

    @objid ("c3b45755-91c1-4670-b1a9-c988acb88d59")
    void setBombMax(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.bombMax = value;
    }

    @objid ("869e644a-c1c8-4013-937b-a11e9ac05ada")
    public int getFire() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.fire;
    }

    @objid ("14a39b3e-b3ae-42e4-8757-4b67b7d510f6")
    void setFire(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.fire = value;
    }

    @objid ("671682b3-f854-4f65-8616-a0dee38409f6")
    public boolean[] getPlayerAbilites() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.playerAbilites;
    }

    @objid ("2559d9b8-e592-4923-928b-ebc444992c5c")
    void setPlayerAbilites(boolean[] value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.playerAbilites = value;
    }

    @objid ("1c7621e2-784f-4508-933f-55ea6bea5b83")
    public int getInvulnerability() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.invulnerability;
    }

    @objid ("3322d7de-cc32-48aa-8dde-e19bf6d5ba0c")
    void setInvulnerability(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.invulnerability = value;
    }

    @objid ("d24ca8af-7294-4611-bab0-5541345d4258")
    public Controller getController() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.controller;
    }

    @objid ("79de1afe-e6c2-4c96-ae54-f3d57da135dc")
    void setController(Controller value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.controller = value;
    }

    @objid ("83716caf-4650-4a93-b6e4-a9f241a25c9c")
    void update() {
    }

}
