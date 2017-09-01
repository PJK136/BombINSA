package game;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Classe mère abstraite de l'ensemble des tuiles explosibles
 */
public abstract class ExplodableTile extends Tile {
     ExplosionState externalState;
     
     LinkedList<ExplosionState> explosionStates;

    public ExplodableTile() {
        externalState = new ExplosionState(0, null, null);
        explosionStates = new LinkedList<ExplosionState>();
    }

    /**
     * @return si la tuile est en train d'exploser
     */
    public boolean isExploding() {
        return !explosionStates.isEmpty();
    }

    public ExplosionType getExplosionType() {
        return externalState.type;
    }

    void setLastExplosionEnd() {
        explosionStates.getLast().type = ExplosionType.End;
        updateExternalState();
    }

    public Direction getExplosionDirection() {
        return externalState.direction;
    }
    
    public int getExplosionTimeRemaining() {
        return externalState.timeRemaining;
    }

    /**
     * Met à jour la tuile en :
     *   - diminuant le temps restant des explosions qu'elle contient
     *   - rafraichissant la liste des explosions qu'elle contient
     */
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
        
        externalState.timeRemaining--;
        
        if (needUpdate) {
            updateExternalState();
            if (explosionStates.isEmpty()) {
                return postExplosion();
            }
        }
        return super.update();
    }

    /**
     * Rajoute une explosion définie par les paramêtres suivants
     * @param duration Durée de l'explosion
     * @param type Type d'explosion
     * @param direction Direction de l'explosion
     */
    void explode(int duration, ExplosionType type, Direction direction) {
        ExplosionState state = new ExplosionState(duration, type, direction);
        explosionStates.add(state);
        updateExternalState();
    }

    /**
     * Met à jour la liste des explosions présentes sur la tuile 
     * puis en déduit le type d'explosion que la tuile affiche et sa direction
     */
    private void updateExternalState() {
        if (explosionStates.isEmpty()) {
            externalState.type = null;
            externalState.direction = null;
            externalState.timeRemaining = 0;
            return;
        }
        
        externalState.type = ExplosionType.End;
        externalState.direction = explosionStates.get(0).direction;
        
        for (ExplosionState state : explosionStates) {
            if (externalState.type != ExplosionType.Center) {
                if (state.type == ExplosionType.Center || !Direction.doHaveSameAxis(externalState.direction, state.direction)) {
                    externalState.type = ExplosionType.Center;
                    externalState.direction = null;
                } else if (state.type == ExplosionType.Branch){
                    externalState.type = ExplosionType.Branch;
                } //Si state.type == End, alors soit explosionType l'est déjà, soit il n'est pas pris en compte
            }
            
            externalState.timeRemaining = Math.max(externalState.timeRemaining, state.timeRemaining);
        }
    }

    /**
     * @return La tuile qui restera après explosion
     */
    Tile postExplosion() {
        return this;
    }

    /**
     * État possible d'une explosion
     */
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

}
