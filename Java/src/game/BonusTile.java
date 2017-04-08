package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Tuile bonus qui devient une tuile vide une fois que le bonus est pris
 */
@objid ("e0899fb2-43c0-4905-a390-6c536094eade")
public class BonusTile extends ExplodableTile {
    @objid ("3e29d10d-6372-4dd9-bd6f-32bbedb5490d")
     BonusType bonusType;

    /**
     * Construit un bonus aléatoire
     */
    @objid ("2c6b3524-435a-414e-86f8-3a8b772005f9")
    public BonusTile() {
        bonusType = BonusType.Random;
    }

    /**
     * Construit le bonus indiquée
     * @param type Type du bonus désiré
     */
    @objid ("ef5a626b-54a8-41e3-833e-b15b09f2c6ba")
    public BonusTile(BonusType type) {
        this.bonusType = type;
    }

    @objid ("d6775c48-2c05-42c4-ad12-706ebc1b8ee8")
    @Override
    public TileType getType() {
        return TileType.Bonus;
    }

    @objid ("b391e578-677f-47b3-bf9c-8bd76328658c")
    public boolean isCollidable() {
        return false;
    }

    @objid ("6823a6b7-ebf3-44cf-8890-65bc440392e2")
    public BonusType getBonusType() {
        return this.bonusType;
    }

    @objid ("1277cdc7-164d-41c6-a297-9ad372607823")
    void setBonusType(BonusType value) {
        this.bonusType = value;
    }

    /**
     * Renvoie une tuile vide
     */
    @objid ("78fbf9a6-83ae-474b-9040-5c9e25f3769d")
    @Override
    Tile postExplosion() {
        EmptyTile tile = new EmptyTile();
        tile.setEntities(entities);
        return tile;
    }

    /**
     * @return Un type de bonus choisi aléatoirement parmi ceux existants 
     * en tenant compte de leur taux d'apparition
     */
    @objid ("06b379a9-5529-4a67-a73c-9ab4058c8da7")
    public static BonusType randomBonus() {
        double random = Math.random();
        double summ = 0;
        BonusType[] bonusList = BonusType.values();
        for (int i=0; i<bonusList.length; i++){
            if(random<summ+bonusList[i].getLootRate()){
                return bonusList[i];
            } else {
                summ = summ + bonusList[i].getLootRate();
            }
        }
        System.err.println("Problème bonus " + summ);
        return BonusType.Random;
    }

}
