package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("794db354-1a0b-4b63-9087-5353187a5919")
public class Controller {
    @objid ("6decc51f-7b91-435b-a23e-f5e854f1454d")
    protected Direction direction;

    @objid ("cd7b303a-4323-46dd-8f0d-81eb15b7689c")
    protected boolean planting;

    @objid ("4f01695c-8624-467e-8504-59563b0552e0")
    public Player player;

    @objid ("49a9b3a3-aace-4724-9916-7d0763f52797")
    Controller(Player player) {
    }

    @objid ("54fdc83d-eeb6-431b-bfd2-c43ed4e95fe1")
    public void move(Direction direction) {
    }

    @objid ("55ab3986-4268-475f-b335-c2ac9a9ff690")
    public void plantBomb(boolean plant) {
    }

    @objid ("3507e829-eed4-4009-935c-79f085780be3")
    void update() {
    }

    @objid ("c3a2ff01-466b-42fa-8b41-351612bdfe2b")
    Direction getDirection() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.direction;
    }

    @objid ("41fc6ec8-f9c0-4314-ab40-fbdc056b87e1")
    void setDirection(Direction value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.direction = value;
    }

    @objid ("9d25d602-42db-4e12-9866-ed66cff5670e")
    boolean isPlanting() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.planting;
    }

    @objid ("e4b62381-e337-4e48-b863-af6500540599")
    void setPlanting(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.planting = value;
    }

}
