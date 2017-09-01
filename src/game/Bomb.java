package game;


/**
 * Entité bombe
 */
public class Bomb extends Entity {
     int range;

     int duration;

     int timeRemaining;

    public static final double BOMB_DEFAULT_SPEED = 4; // tile/sec

     transient Character owner;

    /**
     * Constructeur par défaut de Bomb
     */
    private Bomb() {
        super(null, 0, 0);
    }

    /**
     * Construit une Bombe
     * @param world Monde dans lequel est la bombe
     * @param x Position horizontale en pixel
     * @param y Position verticale en pixel
     * @param range Portée de l'explosion (en cases)
     * @param duration Durée avant explosion (en ticks)
     */
    public Bomb(World world, double x, double y, int range, int duration) {
        super(world, x, y);
        this.range = range;
        this.duration = duration;
        this.timeRemaining = duration;
    }
    
    /**
     * Construit une Bombe à partir du joueur qui l'a posée
     * @param world Monde dans lequel est la bombe
     * @param owner Joueur à qui appartient la bombe 
     * @param duration Durée avant explosion (en ticks)
     */
    public Bomb(World world, Character owner, int duration) {
        this(world, world.getMap().toCenterX(owner.getX()), world.getMap().toCenterX(owner.getY()), owner.getRange(), duration);
        this.owner = owner;
    }

    public int getRange() {
        return this.range;
    }

    void setRange(int value) {
        if (value >= 0) {
            this.range = value;
        } else {
            throw new RuntimeException("Range can't be negative");
        }
    }

    public int getDuration() {
        return this.duration;
    }

    void setDuration(int value) {
        if(value >= 0){
            this.duration = value;
            this.timeRemaining = value;
        } else {
            throw new RuntimeException("Duration cannot be negative");
        }
    }

    public int getTimeRemaining() {
        return this.timeRemaining;
    }
    
    /**
     * Mise à jour de la bombe :
     * - Diminue le temps jusqu'à l'explosion
     * - Mise à jour de la position
     * - Vérifie si elle est sur une case avec flèche
     * - Vérifie si la bombe doit exploser 
     */
    @Override
    void update() {
        // Decrease TimeRemaining
        timeRemaining -= 1;
        
        // Update Position
        super.update();
        
        // Update Marche sur une flèche (Déplacement dans la direction de la
        // flèche d'un nombre de case déterminé)
        if (this.world.getMap().getTileType(this.x, this.y) == TileType.Arrow) {
            //vérifie si la bombe ne bouge pas ou s'il est bien au milieu de la case dans la direction où il bouge
            boolean changeDir = false;
            if (this.speed == 0.) {
                changeDir = true;
            } else if((this.direction == Direction.Up) && (this.y <= this.world.getMap().toCenterY(this.world.getMap().toGridCoordinates(this.x, this.y)))){
                changeDir = true;
            } else if ((this.direction == Direction.Down) && (this.y >= this.world.getMap().toCenterY(this.world.getMap().toGridCoordinates(this.x, this.y)))){
                changeDir = true;
            } else if ((this.direction == Direction.Right) && (this.x >= this.world.getMap().toCenterX(this.world.getMap().toGridCoordinates(this.x, this.y)))){
                changeDir = true;
            } else if ((this.direction == Direction.Left) && (this.x <= this.world.getMap().toCenterX(this.world.getMap().toGridCoordinates(this.x, this.y)))){
                changeDir = true;
            }
        
            if (changeDir) {
                this.direction = this.world.getMap().getArrowDirection(this.x, this.y);
                this.speed = BOMB_DEFAULT_SPEED*world.map.getTileSize()/world.getFps();
            }
        }
        
        if (this.world.getMap().isExploding(this.x, this.y))
            timeRemaining /= 2;
            
        // On vérifie le TimeRemaining et on fait exploser si nulle
        if (this.timeRemaining == 0) {
            this.world.createExplosion(this);
            remove();
        }
    }
    
    @Override
    void remove() {
        if (owner != null)
            owner.decreaseBombCount();
        super.remove();
    }
    
    @Override
    boolean canCollide(double x, double y) {
        if (!super.canCollide(x, y)) {
            for (Entity entity : world.getMap().getEntities(x, y)) {
                if (entity != this) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    void updateFrom(Entity entity) {
        super.updateFrom(entity);
        if (entity instanceof Bomb) {
            Bomb bomb = (Bomb)entity;
            this.range = bomb.range;
            this.duration = bomb.duration;
            this.timeRemaining = bomb.timeRemaining;
        }
    }

}
