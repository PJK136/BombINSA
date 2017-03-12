package game;

import java.time.chrono.IsoChronology;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("c2a5ec32-8fb5-4eea-beff-33ee327f4352")
public abstract class Entity {
    @objid ("5ce64a3c-e0c0-4de4-b322-183a2e6a4c25")
     boolean toRemove;

    @objid ("f1ebcc19-f0b0-48a1-b6d3-cc22527239dd")
     double x = 0;

    @objid ("adab154c-5601-468f-8356-4b51060cd90f")
     double y = 0;

    @objid ("da6922c1-5f11-4728-90b0-fccc76eb2bd2")
     Direction direction;

    @objid ("46bbc37c-9300-456a-87b3-a7774a36e395")
     double speed = 0;
    
    @objid ("83945ddf-99e1-4a55-8f7c-5f7a2c89a534")
     World world;
    
    static final double offset_PERCENTAGE = 1./3.;

    @objid ("2482ca74-f651-4b89-8f4e-b25e74535a33")
    Entity(World world, double x, double y) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.speed = 0.;
        this.direction = Direction.Down;
    }

    @objid ("012ef32d-c8e8-44f7-9f8f-11c1d0092657")
    boolean isToRemove() {
        return this.toRemove;
    }

    @objid ("cfa08ee4-954e-4656-a19b-46273c6d37e7")
    void remove() {
        this.toRemove = true;
    }

    @objid ("5620cd2d-8826-4d40-a754-9ac568123a25")
    public double getX() {
        return this.x;
    }

    @objid ("78c63e54-9035-40d2-ae0a-99cdbbcc8788")
    void setX(double value) {
    	if(value >= world.getMap().getTileSize()/2. &&
    	   value < world.getMap().getWidth()-(world.getMap().getTileSize()/2.)) {
    		this.x = value;
    	} else {
    		throw new RuntimeException("x = " + value + " can't be outside of the map");
    	}
    }

    @objid ("9c402d71-f604-49ca-ac91-079aa3515974")
    public double getY() {
        return this.y;
    }
    
    public double getBorderLeft() {
        return this.x-(world.getMap().getTileSize()/2.);
    }

    public double getBorderRight() {
        return this.x+(world.getMap().getTileSize()/2.);
    }
    
    public double getBorderTop() {
        return this.y-(world.getMap().getTileSize()/2.);
    }
    
    public double getBorderDown() {
        return this.y+(world.getMap().getTileSize()/2.);
    }
    

    @objid ("08e29169-aac4-43a6-9bc5-02f2282fe480")
    void setY(double value) {
        if(value >= world.getMap().getTileSize()/2. &&
           value < world.getMap().getHeight()-(world.getMap().getTileSize()/2.)) {
             this.y = value;
    	} else {
    		throw new RuntimeException("y = " + value + " can't be outside of the map");
    	}
    }

    @objid ("002e3653-fdf9-4157-9a2b-ffcf9d5fc685")
    public double getSpeed() {
        return this.speed;
    }

    @objid ("00edac08-fa82-4b44-aa6f-c993f59649f3")
    void setSpeed(double value) {
    	if(value >= 0){
    		this.speed = value;
    	} else {
    		throw new RuntimeException("Speed can't be negative");
    	}
    }

    @objid ("b37c2cf9-7059-4677-bd3a-15fe9a906e34")
    public Direction getDirection() {
        return this.direction;
    }

    @objid ("2ef03dc6-2d6e-422a-ba30-d39cdbf70d9d")
    void setDirection(Direction value) {
        this.direction = value;
    }


    @objid ("a2924691-b5ff-4b3e-9a94-659f2e120988")
    void update() {
        double step = this.speed;
        double offset = world.getMap().getTileSize()*offset_PERCENTAGE;
        
        switch (direction) {
        case Left:
            while (step >= 0 && (canCollide(getBorderLeft()-step, getBorderTop()+offset) || 
                                 canCollide(getBorderLeft()-step, getBorderDown()-offset)))
                step -= 1;
            break;
        case Right:
            while (step >= 0 && (canCollide(getBorderRight()+step, getBorderTop()+offset) || 
                                 canCollide(getBorderRight()+step, getBorderDown()-offset)))
                step -= 1;
            break;
        case Up:
            while (step >= 0 && (canCollide(getBorderLeft()+offset, getBorderTop()-step) || 
                                 canCollide(getBorderRight()-offset, getBorderTop()-step)))
                step -= 1;
            break;
        case Down:
            while (step >= 0 && (canCollide(getBorderLeft()+offset, getBorderDown()+step) || 
                                 canCollide(getBorderRight()-offset, getBorderDown()+step)))
                step -= 1;
            break;
        }
        
        //TODO : approche dichotomique ?
        
        updatePosition(Math.max(0, step));
    }
    
    private void updatePosition(double step) {
        if (step == 0)
            return;

        switch (direction) {
        case Left:
            this.x -= step;
            if (canCollide(getBorderLeft(), getBorderTop()) || canCollide(getBorderLeft(), getBorderDown())) {
                this.y = (world.getMap().toGridCoordinates(x, y).y+0.5)*world.getMap().getTileSize();
            }
            break;
        case Right:
            this.x += step;
            if (canCollide(getBorderRight(), getBorderTop()) || canCollide(getBorderRight(), getBorderDown())) {
                this.y = (world.getMap().toGridCoordinates(x, y).y+0.5)*world.getMap().getTileSize();
            }
            break;
        case Up:
            this.y -= step;
            if (canCollide(getBorderLeft(), getBorderTop()) || canCollide(getBorderRight(), getBorderTop())) {
                this.x = (world.getMap().toGridCoordinates(x, y).x+0.5)*world.getMap().getTileSize();
            }
            break;
        case Down:
            if (canCollide(getBorderLeft(), getBorderDown()) || canCollide(getBorderRight(), getBorderDown())) {
                this.x = (world.getMap().toGridCoordinates(x, y).x+0.5)*world.getMap().getTileSize();
            }
            this.y += step;
            break;
        }
    }
    
    
    boolean canCollide(double x, double y){
        //Il faut aussi vérifier la collision dans les sous classes
        return this.world.getMap().isCollidable(x, y);
    }
}
