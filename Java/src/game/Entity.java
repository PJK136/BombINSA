package game;


/**
 * Entité du jeu
 */
public abstract class Entity {
     int id;

     transient boolean toRemove = false;

     double x = 0;

     double y = 0;

     Direction direction;

     double speed = 0;

     static final double OFFSET_PERCENTAGE = 1./3.;

     transient World world;
    
    /**
     * Construit une entité
     * @param world Monde dans lequel l'entité est crée
     * @param x Position horizontale en pixel
     * @param y Position verticale en pixel
     */
    Entity(World world, double x, double y) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.speed = 0.;
        this.direction = Direction.Down;
    }

    int getID() {
        return id;
    }

    void setID(int id) {
        this.id = id;
    }

    boolean isToRemove() {
        return this.toRemove;
    }

    void remove() {
        this.toRemove = true;
    }

    public double getX() {
        return this.x;
    }

    void setX(double value) {
        if(value >= world.getMap().getTileSize()/2. &&
           value < world.getMap().getWidth()-(world.getMap().getTileSize()/2.)) {
            this.x = value;
        } else {
            throw new RuntimeException("x = " + value + " can't be outside of the map");
        }
    }

    public double getY() {
        return this.y;
    }

    void setY(double value) {
        if(value >= world.getMap().getTileSize()/2. &&
           value < world.getMap().getHeight()-(world.getMap().getTileSize()/2.)) {
             this.y = value;
        } else {
            throw new RuntimeException("y = " + value + " can't be outside of the map");
        }
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

    public double getSpeed() {
        return this.speed;
    }

    void setSpeed(double value) {
        if(value >= 0){
            this.speed = value;
        } else {
            throw new RuntimeException("Speed can't be negative");
        }
    }

    public Direction getDirection() {
        return this.direction;
    }

    void setDirection(Direction value) {
        this.direction = value;
    }

    void setWorld(World world) {
        this.world = world;
    }

    /**
     * Vérifie la collision dans une direction à une certaine distance
     * @param direction Direction
     * @param move Distance
     * @return Vrai s'il y a collision
     */
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
    boolean canCollide(double x, double y) {
        //Il faut aussi vérifier la collision dans les sous classes
        return this.world.getMap().isCollidable(x, y);
    }

    /**
     * Met à jour l'entité
     */
    void update() {
        if (!isColliding(this.direction, this.speed))
            updatePosition(this.speed);
        else {
            double move = this.speed - 1;
            
            //TODO : approche dichotomique ?
            while (move >= 0 && isColliding(this.direction, move)) {
                move--;
            }
            
            updatePosition(Math.max(0, move));
            this.speed = 0;
        }
        
    }
    
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
    
    /**
     * Met à jour la position de l'entité avec vérification des collisions
     */
    private void updatePosition(double move) {
        if (move == 0)
            return;
        
        switch (direction) {
        case Left:
            this.x -= move;
            if (canCollide(getBorderLeft(), getBorderTop()) || canCollide(getBorderLeft(), getBorderDown())) {
                this.y -= clamp(this.y - world.getMap().toCenterY(world.getMap().toGridCoordinates(x, y)), -1, 1);
            }
            break;
        case Right:
            this.x += move;
            if (canCollide(getBorderRight(), getBorderTop()) || canCollide(getBorderRight(), getBorderDown())) {
                this.y -= clamp(this.y - world.getMap().toCenterY(world.getMap().toGridCoordinates(x, y)), -1, 1);
            }
            break;
        case Up:
            this.y -= move;
            if (canCollide(getBorderLeft(), getBorderTop()) || canCollide(getBorderRight(), getBorderTop())) {
                this.x -= clamp(this.x - world.getMap().toCenterX(world.getMap().toGridCoordinates(x, y)), -1, 1);
            }
            break;
        case Down:
            if (canCollide(getBorderLeft(), getBorderDown()) || canCollide(getBorderRight(), getBorderDown())) {
                this.x -= clamp(this.x - world.getMap().toCenterX(world.getMap().toGridCoordinates(x, y)), -1, 1);
            }
            this.y += move;
            break;
        }
    }

    /**
     * Met à jour l'entité depuis une autre entité
     * @param entity Autre entité
     */
    void updateFrom(Entity entity) {
        this.id = entity.id;
        this.x = entity.x;
        this.y = entity.y;
        this.direction = entity.direction;
        this.speed = entity.speed;
    }

}
