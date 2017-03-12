package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("4399d714-75ab-40bb-b694-6207782f55ce")
public class Bomb extends Entity {
    @objid ("7cf845bf-dacd-4f1f-9c63-46ad9d0e478b")
     int range;

    @objid ("57ad54f8-e371-4980-8fcc-383afd09c461")
     int timeRemaining;

    @objid ("d663f27d-b4de-411a-b687-8ae5d439ab48")
    protected Player owner;

    public static final double BOMB_DEFAULT_SPEED = 4; // tile/sec
    
    @objid ("f9841259-647d-4cd7-9bb7-f10aea5a4794")
    public Bomb(World world, GridCoordinates gc, int range, int duration) {
        super(world, (gc.x+0.5)*world.getMap().getTileSize(), (gc.y+0.5)*world.getMap().getTileSize());
        this.range = range;
        this.timeRemaining = duration;
    }

    @objid ("37d6734c-0f78-448a-a2c5-6fa8930b3233")
    public Bomb(World world, Player owner, int duration) {
        this(world, world.getMap().toGridCoordinates(owner.getX(), owner.getY()), owner.getRange(), duration);
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

    @objid ("46c24a88-c0e7-42af-aa08-f2217f450044")
    public int getTimeRemaining() {
        return this.timeRemaining;
    }

    @objid ("8568627a-17f2-49d3-a8e6-c690ecf21ab1")
    void setTimeRemaining(int value) {
        if(value >= 0){
            this.timeRemaining = value;
        } else {
            throw new RuntimeException("Remaining Time cannot be negative");
        }
    }

    @objid ("ce00607d-3c72-45ea-b3e1-5a3ac0315b22")
    
    void update() {
        // Decrease TimeRemaining
        timeRemaining -= 1;

        // Update Position
        super.update();

        // Update Marche sur une flèche (Déplacement dans la direction de la
        // flèche d'un nombre de case déterminé)
        if (this.world.getMap().getTileType(this.x, this.y) == TileType.Arrow) {
            Direction d = this.world.getMap().getArrowDirection(this.x, this.y);
            switch (d) {
            case Left:
                this.direction = Direction.Left;
                break;
            case Right:
                this.direction = Direction.Right;
                break;
            case Up:
                this.direction = Direction.Up;
                break;
            case Down:
                this.direction = Direction.Down;
                break;
            }
            this.speed = BOMB_DEFAULT_SPEED*world.map.getTileSize()/world.getFps();
        }
        // On vérifie le TimeRemaining, si nulle ou si la case en train
        // d'exploser, on modifie l'état des case dans la portée en train
        // d'exploser
        if (this.timeRemaining == 0 || this.world.getMap().isExploding(this.x, this.y) == true) {
            this.world.createExplosion(this);
            if (owner != null)
                owner.decreaseBombCount();
            remove();
        }
    }
    
    @Override
    boolean canCollide(double x, double y) {
        for (Entity entity : world.getMap().getEntities(x, y)) {
            if (entity != this) {
                return true;
            }
        }
        return super.canCollide(x, y);
    }
}
