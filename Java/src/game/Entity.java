package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Entité du jeu
 */
@objid ("c2a5ec32-8fb5-4eea-beff-33ee327f4352")
public abstract class Entity {
    @objid ("f7548ae8-0e8e-42f3-891e-e0d849b36c13")
     int id;

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

    @objid ("1ab8e8c1-d2ac-4a08-8450-ac9ccdd49749")
     static final double OFFSET_PERCENTAGE = 1./3.;

    @objid ("83945ddf-99e1-4a55-8f7c-5f7a2c89a534")
     transient World world;
    
    /**
     * Constructeur d'Entity
     * @param world Monde dans lequel l'entité est crée
     * @param x Position horizontale en pixel
     * @param y Position verticale en pixel
     */
    @objid ("2482ca74-f651-4b89-8f4e-b25e74535a33")
    Entity(World world, double x, double y) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.speed = 0.;
        this.direction = Direction.Down;
    }

    @objid ("ff542c4f-be42-4c9c-9cf1-02515984de0e")
    int getID() {
        return id;
    }

    @objid ("98f2341a-c9b8-44a2-8a9a-f623109cc56b")
    void setID(int id) {
        this.id = id;
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

    @objid ("08e29169-aac4-43a6-9bc5-02f2282fe480")
    void setY(double value) {
        if(value >= world.getMap().getTileSize()/2. &&
           value < world.getMap().getHeight()-(world.getMap().getTileSize()/2.)) {
             this.y = value;
        } else {
            throw new RuntimeException("y = " + value + " can't be outside of the map");
        }
    }

    @objid ("c44ec029-8944-4621-901f-9f6845993143")
    public double getBorderLeft() {
        return this.x-(world.getMap().getTileSize()/2.);
    }

    @objid ("96c78566-87ca-4712-9fcf-f75c5e760b2a")
    public double getBorderRight() {
        return this.x+(world.getMap().getTileSize()/2.);
    }

    @objid ("b0821d6c-a85b-421a-bbad-44f70980b9e3")
    public double getBorderTop() {
        return this.y-(world.getMap().getTileSize()/2.);
    }

    @objid ("997b8ff6-1fcb-46af-bdcc-78c446121e9e")
    public double getBorderDown() {
        return this.y+(world.getMap().getTileSize()/2.);
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

    @objid ("2435a8a2-3b07-4ced-a559-84d0a5c80a48")
    void setWorld(World world) {
        this.world = world;
    }

    /**
     * Test de collision avec les murs dans une direction à une certaine distance
     * @param direction Direction
     * @param move Distance
     * @return Vrai s'il y a collision
     */
    @objid ("41800255-bf3a-456d-a83a-101f7454243e")
    public boolean isColliding(Direction direction, double move) {
        if (direction == null)
            return false;
        
        final double offset = world.getMap().getTileSize()*OFFSET_PERCENTAGE;
        
        switch (direction) {
        case Left:
            return canCollide(getBorderLeft()-move, getBorderTop()+offset) || canCollide(getBorderLeft()-move, getBorderDown()-offset);
        case Right:
            return canCollide(getBorderRight()+move, getBorderTop()+offset) || canCollide(getBorderRight()+move, getBorderDown()-offset);
        case Up:
            return canCollide(getBorderLeft()+offset, getBorderTop()-move) || canCollide(getBorderRight()-offset, getBorderTop()-move);
        case Down:
            return canCollide(getBorderLeft()+offset, getBorderDown()+move) || canCollide(getBorderRight()-offset, getBorderDown()+move);
        }
        return false;
    }

    /**
     * Vérifie si l'entité entre en collision pour une position donnée
     * @param x Position horizontale en pixel 
     * @param y Position verticale en pixel
     * @return vrai si entre en collision
     */
    @objid ("94f5132c-0a18-4827-bebd-451cd306edff")
    boolean canCollide(double x, double y) {
        //Il faut aussi vérifier la collision dans les sous classes
        return this.world.getMap().isCollidable(x, y);
    }

    /**
     * Met à jour l'entité
     */
    @objid ("a2924691-b5ff-4b3e-9a94-659f2e120988")
    void update() {
        while (this.speed >= 0 && isColliding(this.direction, this.speed)) {
            this.speed--;
        }
        
        this.speed = Math.max(0, this.speed);
        
        //TODO : approche dichotomique ?
        
        updatePosition();
    }
    
    /**
     * Met à jour la position de l'entité avec vérification des collisions
     */
    @objid ("30b55b46-96bb-4d8b-9810-6ccdaa97d7db")
    private void updatePosition() {
        if (this.speed == 0)
            return;
        
        switch (direction) {
        case Left:
            this.x -= this.speed;
            if (canCollide(getBorderLeft(), getBorderTop()) || canCollide(getBorderLeft(), getBorderDown())) {
                this.y = world.getMap().toCenterY(world.getMap().toGridCoordinates(x, y));
            }
            break;
        case Right:
            this.x += this.speed;
            if (canCollide(getBorderRight(), getBorderTop()) || canCollide(getBorderRight(), getBorderDown())) {
                this.y = world.getMap().toCenterY(world.getMap().toGridCoordinates(x, y));
            }
            break;
        case Up:
            this.y -= this.speed;
            if (canCollide(getBorderLeft(), getBorderTop()) || canCollide(getBorderRight(), getBorderTop())) {
                this.x =  world.getMap().toCenterX(world.getMap().toGridCoordinates(x, y));
            }
            break;
        case Down:
            if (canCollide(getBorderLeft(), getBorderDown()) || canCollide(getBorderRight(), getBorderDown())) {
                this.x =  world.getMap().toCenterX(world.getMap().toGridCoordinates(x, y));
            }
            this.y += this.speed;
            break;
        }
    }

    /**
     * Met à jour l'entité depuis une autre entité
     * @param entity Autre entité
     */
    @objid ("ffa0dcae-9640-47ec-b23d-de519e7ed701")
    void updateFrom(Entity entity) {
        this.id = entity.id;
        this.x = entity.x;
        this.y = entity.y;
        this.direction = entity.direction;
        this.speed = entity.speed;
    }

}
