package game;


/**
 * Tuile bonus qui devient une tuile vide une fois que le bonus est pris
 */
public class BonusTile extends ExplodableTile {
     BonusType bonusType;

    /**
     * Construit un bonus aléatoire
     */
    public BonusTile() {
        bonusType = BonusType.Random;
    }

    /**
     * Construit le bonus indiquée
     * @param type Type du bonus désiré
     */
    public BonusTile(BonusType type) {
        this.bonusType = type;
    }

    @Override
    public TileType getType() {
        return TileType.Bonus;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public BonusType getBonusType() {
        return this.bonusType;
    }

    void setBonusType(BonusType value) {
        this.bonusType = value;
    }

    /**
     * Renvoie une tuile vide
     */
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
