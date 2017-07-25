package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Entité bombe
 */
@objid ("4399d714-75ab-40bb-b694-6207782f55ce")
public class Bomb extends Entity {
    @objid ("7cf845bf-dacd-4f1f-9c63-46ad9d0e478b")
     int range;

    @objid ("feedcc52-db18-461f-912f-aafb9559d01a")
     int duration;

    @objid ("57ad54f8-e371-4980-8fcc-383afd09c461")
     int timeRemaining;

    @objid ("2b3b069c-a6aa-42ef-a7d3-54482e395b63")
    public static final double BOMB_DEFAULT_SPEED = 4; // tile/sec

    @objid ("d663f27d-b4de-411a-b687-8ae5d439ab48")
     transient Character owner;

    /**
     * Constructeur par défaut de Bomb
     */
    @objid ("282df99d-8010-4cca-a7b1-e03cba0302ea")
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
    @objid ("f9841259-647d-4cd7-9bb7-f10aea5a4794")
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
    @objid ("37d6734c-0f78-448a-a2c5-6fa8930b3233")
    public Bomb(World world, Character owner, int duration) {
        this(world, world.getMap().toCenterX(owner.getX()), world.getMap().toCenterX(owner.getY()), owner.getRange(), duration);
        this.owner = owner;
    }

    @objid ("af854c36-bd42-4e95-bfb6-98d2b4864671")
    public int getRange() {
        return this.range;
    }

    @objid ("4f8c6754-27ec-4d70-8fa4-df7fd74b49fe")
    void setRange(int value) {
        if (value >= 0) {
            this.range = value;
        } else {
            throw new RuntimeException("Range can't be negative");
        }
    }

    @objid ("16747d9c-2fc0-4547-b27a-f292f80c9a19")
    public int getDuration() {
        return this.duration;
    }

    @objid ("8568627a-17f2-49d3-a8e6-c690ecf21ab1")
    void setDuration(int value) {
        if(value >= 0){
            this.duration = value;
            this.timeRemaining = value;
        } else {
            throw new RuntimeException("Duration cannot be negative");
        }
    }

    @objid ("46c24a88-c0e7-42af-aa08-f2217f450044")
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
    @objid ("ce00607d-3c72-45ea-b3e1-5a3ac0315b22")
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
            if (owner != null)
                owner.decreaseBombCount();
            remove();
        }
    }
    
    @objid ("6ed55b50-6182-4e9f-9274-183b0145eacf")
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

    @objid ("0be4b6b6-bba8-473e-9fa7-d6e4dbe08226")
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
