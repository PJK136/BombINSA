package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e3bd57e8-28ae-4908-9d47-82594d4a19d1")
public class Player extends Entity {
    @objid ("7b2bdcd5-8ffe-4abc-9f50-9ad12d6a7516")
    protected int lives;

    @objid ("4d6b8aa5-64b2-482a-a26b-81c5d7f43e04")
    protected int bombCount;

    @objid ("4bc1c5a3-05fe-4f89-a66a-71b8f5749575")
    protected int bombMax;

    @objid ("48101627-07b0-4b99-a298-5f675134063b")
    protected int fire;

    @objid ("b73bb282-e407-4494-9961-76ff9873c811")
    protected boolean[] playerAbilities;

    @objid ("cf5215ad-6268-42d1-9a65-cc326ec0deb9")
    int getLives() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.lives;
    }

    @objid ("7c905bf0-9c37-42c5-9c6e-22157c4759d1")
    void setLives(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.lives = value;
    }

    @objid ("138a7aef-a11c-44d3-bf7b-721594c73f4d")
    int getBombCount() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.bombCount;
    }

    @objid ("8d81f238-f36b-4991-a16a-5713a0a257a6")
    void setBombCount(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.bombCount = value;
    }

    @objid ("b563d913-16b9-4190-9ca9-949cf0653621")
    int getBombMax() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.bombMax;
    }

    @objid ("b0ba51e0-29f8-43f8-9aae-24533bc22708")
    void setBombMax(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.bombMax = value;
    }

    @objid ("a7bbc482-8b4c-4694-95d9-4d8d5fec2430")
    int getFire() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.fire;
    }

    @objid ("4d1bfd0d-2352-434a-a684-c109a64015ae")
    void setFire(int value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.fire = value;
    }

    @objid ("85eaf0ff-81d8-4a07-b3c5-0c2b9d7166ab")
    boolean[] getPlayerAbilities() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.playerAbilities;
    }

    @objid ("0d02f311-709c-4d22-85b1-751eb43c10d8")
    void setPlayerAbilities(boolean[] value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.playerAbilities = value;
    }

    @objid ("b64251c2-4eff-4e14-b39f-f2a84fb565d7")
    void update() {
    }

}
