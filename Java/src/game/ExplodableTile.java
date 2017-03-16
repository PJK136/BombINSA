package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

public abstract class ExplodableTile extends Tile {
    @objid ("7ffae68f-5714-4e34-91d3-598f263115eb")
    int explosionTimeRemaining;
    ExplosionType explosionType;
    Direction explosionDirection;

    @objid ("afce3c9f-2a7e-43fa-b8fe-9183c2e26f63")
    public boolean isExploding() {
        return explosionTimeRemaining != 0;
    }

    @objid ("a0f2cff1-fab0-4f53-b3e0-2666e4eb5b0b")
    public int getExplosionTimeRemaining() {
        return this.explosionTimeRemaining;
    }
    
    public ExplosionType getExplosionType() {
        return this.explosionType;
    }
    
    void setExplosionType(ExplosionType type) {
        explosionType = type;
    }
    
    public Direction getExplosionDirection() {
        return this.explosionDirection;
    }
    
    @Override
    Tile update() {
        if(explosionTimeRemaining != 0){
            explosionTimeRemaining--;
            if (explosionTimeRemaining == 0)
                return postExplosion();
        }
        
        return super.update();
    }

    @objid ("9f2c3dd7-e9e3-46b8-82e0-23ea933b9eda")
    void explode(int duration, ExplosionType type, Direction direction) {
        if (explosionTimeRemaining == 0) {
            explosionTimeRemaining = duration;
            explosionType = type;
            explosionDirection = direction;
        } else {
            explosionTimeRemaining = duration;
            if (explosionType == ExplosionType.Center)
                return;
            else if (type == ExplosionType.Center || !Direction.isSameAxis(explosionDirection, direction)) {
                explosionType = ExplosionType.Center;
                explosionDirection = null;
            }
            else {
                explosionType = ExplosionType.Branch;
            }
        }
    }
    
    Tile postExplosion() {
        return this;
    }
}
