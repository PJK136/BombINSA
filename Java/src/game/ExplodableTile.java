package game;

import java.util.Iterator;
import java.util.LinkedList;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

public abstract class ExplodableTile extends Tile {
    @objid ("7ffae68f-5714-4e34-91d3-598f263115eb")
    
    public class ExplosionState {
        int timeRemaining;
        ExplosionType type;
        Direction direction;
        
        ExplosionState(int duration, ExplosionType type, Direction direction) {
            this.timeRemaining = duration;
            this.type = type;
            this.direction = direction;
        }
    }
    
    LinkedList<ExplosionState> explosionStates;
    
    ExplosionType explosionType;
    Direction explosionDirection;
    
    public ExplodableTile() {
        explosionStates = new LinkedList<ExplosionState>();
    }

    @objid ("afce3c9f-2a7e-43fa-b8fe-9183c2e26f63")
    public boolean isExploding() {
        return !explosionStates.isEmpty();
    }
    
    public ExplosionType getExplosionType() {
        return this.explosionType;
    }
    
    void setLastExplosionEnd() {
        explosionStates.getLast().type = ExplosionType.End;
        updateExternalState();
    }
    
    public Direction getExplosionDirection() {
        return this.explosionDirection;
    }
    
    @Override
    Tile update() {
        Iterator<ExplosionState> iterator = explosionStates.iterator();
        
        boolean needUpdate = false;
        while (iterator.hasNext()) {
            ExplosionState state = iterator.next();
            state.timeRemaining--;
            if (state.timeRemaining == 0) {
                iterator.remove();
                needUpdate = true;
            }
        }
        
        if (needUpdate) {
            updateExternalState();
            if (explosionStates.isEmpty()) {
                return postExplosion();
            }
        }
        
        return super.update();
    }

    @objid ("9f2c3dd7-e9e3-46b8-82e0-23ea933b9eda")
    void explode(int duration, ExplosionType type, Direction direction) {
        ExplosionState state = new ExplosionState(duration, type, direction);
        explosionStates.add(state);
        updateExternalState();
    }
    
    private void updateExternalState() {
        if (explosionStates.isEmpty()) {
            explosionType = null;
            explosionDirection = null;
            return;
        }
        
        explosionType = ExplosionType.End;
        explosionDirection = explosionStates.get(0).direction;
        
        for (ExplosionState state : explosionStates) {
            if (state.type == ExplosionType.Center || !Direction.isSameAxis(explosionDirection, state.direction)) {
                explosionType = ExplosionType.Center;
                explosionDirection = null;
                return;
            } else if (state.type == ExplosionType.Branch){
                explosionType = ExplosionType.Branch;
            } //Si state.type == End, alors soit explosionType l'est déjà, soit il n'est pas pris en compte
        }
    }
    
    Tile postExplosion() {
        return this;
    }
}
