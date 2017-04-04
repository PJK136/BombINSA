package game;

import java.util.Iterator;
import java.util.LinkedList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("01912cfd-22a7-41a2-b251-b8a95ee24494")
public abstract class ExplodableTile extends Tile {
    @objid ("92f1f412-d0f4-4ce9-9d08-b7ae35deda00")
     ExplosionType explosionType;

    @objid ("17c62bcf-3895-45e5-a8d0-04861e6e18b6")
     Direction explosionDirection;

    @objid ("8a18bd7f-8636-4b55-b5af-394d896dc043")
     LinkedList<ExplosionState> explosionStates;

    @objid ("67bfec5b-1eca-46f5-a78e-86c18ad95d1e")
    public ExplodableTile() {
        explosionStates = new LinkedList<ExplosionState>();
    }

    @objid ("afce3c9f-2a7e-43fa-b8fe-9183c2e26f63")
    /**
     * @return si la tuile est en train d'exploser
     */
    public boolean isExploding() {
        return !explosionStates.isEmpty();
    }

    @objid ("fe5d73c7-8cd9-4d33-9fed-48117a165c06")
    public ExplosionType getExplosionType() {
        return this.explosionType;
    }

    @objid ("f580fd24-cfbc-4765-8cf0-de74ab991da4")
    void setLastExplosionEnd() {
        explosionStates.getLast().type = ExplosionType.End;
        updateExternalState();
    }

    @objid ("147b60ea-8e42-4f89-9e0d-845226b4bc38")
    public Direction getExplosionDirection() {
        return this.explosionDirection;
    }

    @objid ("9ae3c961-25a8-40e9-b5f7-beaeae153e0b")
    /**
     * met à jour la tuile en :
     *   - diminuant le temps restant des explosions qu'elle contient
     *   - ajustant la liste des explosions qu'elle contient
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
        
        if (needUpdate) {
            updateExternalState();
            if (explosionStates.isEmpty()) {
                return postExplosion();
            }
        }
        return super.update();
    }

    @objid ("9f2c3dd7-e9e3-46b8-82e0-23ea933b9eda")
    /**
     * Rajoute une explosion définie par les paramêtres suivants sur la tuile
     * @param duration
     * @param type
     * @param direction
     */
    void explode(int duration, ExplosionType type, Direction direction) {
        ExplosionState state = new ExplosionState(duration, type, direction);
        explosionStates.add(state);
        updateExternalState();
    }

    @objid ("dca542ae-8f72-492c-87d0-443745d00491")
    /**
     * met à jour la liste des explosions présentes sur la tuile 
     * puis en déduit le type d'explosion que la tuile affiche et sa direction
     */
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

    @objid ("b4b6ffe2-bdfe-439f-8c49-0fcffc6bb171")
    /**
     * @return renvoie la tuile qu'il restera après explosion
     */
    Tile postExplosion() {
        return this;
    }

    @objid ("173dc16c-a787-4c94-a1e1-eea4b4ae5394")
    public class ExplosionState {
        @objid ("e42d73f9-4a60-4504-8287-1129299f4ab1")
         int timeRemaining;

        @objid ("83193733-f3cf-4afc-93cf-00307cb4acf6")
         ExplosionType type;

        @objid ("b87fc944-cd44-408a-9d8d-d49f86eceaf6")
         Direction direction;

        @objid ("fdfd3a3b-6d6d-45fc-aaef-75f185c4ae59")
        ExplosionState(int duration, ExplosionType type, Direction direction) {
            this.timeRemaining = duration;
            this.type = type;
            this.direction = direction;
        }

    }

}
