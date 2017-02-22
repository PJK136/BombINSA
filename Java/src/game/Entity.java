package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("c2a5ec32-8fb5-4eea-beff-33ee327f4352")
public abstract class Entity {
    @objid ("f1ebcc19-f0b0-48a1-b6d3-cc22527239dd")
    protected double x;

    @objid ("adab154c-5601-468f-8356-4b51060cd90f")
    protected double y;

    @objid ("da6922c1-5f11-4728-90b0-fccc76eb2bd2")
    protected Direction direction = Direction.Up;

    @objid ("46bbc37c-9300-456a-87b3-a7774a36e395")
    protected double speed = 0.;

    @objid ("f8e7fb7e-7b51-4d47-ba08-13ad43d772da")
    protected World world;

    @objid ("2482ca74-f651-4b89-8f4e-b25e74535a33")
    Entity(World world, double x, double y) {
    }

    @objid ("9c402d71-f604-49ca-ac91-079aa3515974")
    double getY() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.y;
    }

    @objid ("08e29169-aac4-43a6-9bc5-02f2282fe480")
    void setY(double value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.y = value;
    }

    @objid ("5620cd2d-8826-4d40-a754-9ac568123a25")
    double getX() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.x;
    }

    @objid ("78c63e54-9035-40d2-ae0a-99cdbbcc8788")
    void setX(double value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.x = value;
    }

    @objid ("002e3653-fdf9-4157-9a2b-ffcf9d5fc685")
    double getSpeed() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.speed;
    }

    @objid ("00edac08-fa82-4b44-aa6f-c993f59649f3")
    void setSpeed(double value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.speed = value;
    }

    @objid ("b37c2cf9-7059-4677-bd3a-15fe9a906e34")
    Direction getDirection() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.direction;
    }

    @objid ("2ef03dc6-2d6e-422a-ba30-d39cdbf70d9d")
    void setDirection(Direction value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.direction = value;
    }

    @objid ("a2924691-b5ff-4b3e-9a94-659f2e120988")
    void update() {
        // Implémenter ici le déplacement et la gestion des collisions de l'entité.
    }

}
