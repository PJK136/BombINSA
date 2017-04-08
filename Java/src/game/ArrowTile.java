package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Tuile flèche qui projette toute bombe qui passe dessus dans une direction donnée 
 */
@objid ("7c0be07f-9851-49c1-9084-d21acfdf1a57")
public class ArrowTile extends ExplodableTile {
    @objid ("e9081624-7d7d-411c-9fae-43e3570502a5")
     Direction direction;

    /**
     * Construit une flèche dirigée vers le haut
     */
    @objid ("894797e2-f762-4426-9ed4-7431cc252a22")
    public ArrowTile() {
        direction = Direction.Up;
    }

    /**
     * Construit une flèche dans la direction indiquée
     * @param direction Direction désirée
     */
    @objid ("3ffd6d9c-5051-4269-9ead-1aa727152d09")
    public ArrowTile(Direction direction) {
        this.direction = direction;
    }

    @objid ("b485a2c1-70f6-4414-94d3-9a06cf720033")
    @Override
    public TileType getType() {
        return TileType.Arrow;
    }

    @objid ("174ded82-9ace-4d9b-aa99-54944b701bdd")
    public boolean isCollidable() {
        return false;
    }

    @objid ("626cbd74-5143-4b0e-9b2e-40d5e7c8c616")
    public Direction getDirection() {
        return this.direction;
    }

    @objid ("d94a5ee2-e6d7-40dd-b30c-3259db5fbaf0")
    void setDirection(Direction value) {
        this.direction = value;
    }

}
