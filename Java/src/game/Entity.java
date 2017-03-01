package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("c2a5ec32-8fb5-4eea-beff-33ee327f4352")
public abstract class Entity {
    @objid ("5ce64a3c-e0c0-4de4-b322-183a2e6a4c25")
     boolean toRemove;

    @objid ("f1ebcc19-f0b0-48a1-b6d3-cc22527239dd")
     double x;

    @objid ("adab154c-5601-468f-8356-4b51060cd90f")
     double y;

    @objid ("da6922c1-5f11-4728-90b0-fccc76eb2bd2")
     Direction direction;

    @objid ("46bbc37c-9300-456a-87b3-a7774a36e395")
     double speed = 0;

    @objid ("83945ddf-99e1-4a55-8f7c-5f7a2c89a534")
     World world;

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
    	if(value >= 0){
    		this.x = value;
    	} else {
    		throw new RuntimeException("X can't be negative");
    	}
    }

    @objid ("9c402d71-f604-49ca-ac91-079aa3515974")
    public double getY() {
        return this.y;
    }

    @objid ("08e29169-aac4-43a6-9bc5-02f2282fe480")
    void setY(double value) {
    	if(value >= 0){
    		this.y = value;
    	} else {
    		throw new RuntimeException("Y can't be negative");
    	}
    }

    @objid ("002e3653-fdf9-4157-9a2b-ffcf9d5fc685")
    public double getSpeed() {
        return this
                .speed;
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
        int i = 1;
        double vitesse = this.speed; //Variable intermédiaire pour fixer la vitesse à l'approche d'un mur et arrêter l'entité si elle est collé au mur
        switch(direction){
            case Up :
                while(this.world.getMap().isCollidable(this.x, this.y - i) == false && i<this.speed){ //x et y coordonnées coins supérieur gauche puis on se place sur la bordure qui nous intéresse (ici en haut) et on rajoute i pour voir si l'entité rencontre un mur
                        vitesse = i-1; // Si un mur est rencontré alors la vitesse devient i-1 de telle manière à ce que si l'entitée est collée au mur, elle ne bouge plus car i = 0
                        i = Math.min((int)this.speed, i++); //Gérer le fait que la vitesse ne doit pas dépasser celle fixé de base.
                }
                this.y -= vitesse;
                break;
            
            case Down :
                while(this.world.getMap().isCollidable(this.x, this.y + this.world.getMap().getTileSize() + i) == false && i<this.speed){  
                    vitesse = i-1; 
                    i = Math.min((int)this.speed, i++);
                }    
                
                this.y += vitesse; // on modifie y pour qu'elle fasse effectivement le déplacement vérifié ci-dessus
                break;
            
            case Right :
                while(this.world.getMap().isCollidable(this.x + this.world.getMap().getTileSize() + i , this.y) == false && i<this.speed){ 
                        vitesse = i-1;
                        i = Math.min((int)this.speed, i++);
                }
                this.x += vitesse;
                break;
            
            case Left :
                while(this.world.getMap().isCollidable(this.x - i , this.y) == false && i<this.speed){ 
                        vitesse = i-1;
                        i = Math.min((int)this.speed, i++);   
                }
                this.x -= vitesse;
                break;
                
            default:
                break;
        
        }
    }

}
