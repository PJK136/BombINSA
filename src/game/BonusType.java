package game;


/**
 * Liste des bonus/malus avec leurs taux d'apparition
 */
public enum BonusType {
    Random      (true,  1./10.),
    MoreBomb    (true,  3./10.),
    LessBomb    (false, 1./3.),
    MoreRange   (true,  3./10.),
    LessRange   (false, 1./3.),
    MoreSpeed   (true,  1./10.),
    LessSpeed   (false, 1./3.),
    Shield      (true,  1./10.),
    Kick        (true,  1./10.);

    private double lootRate;
    
    private boolean good;
    
    private static final double GOOD_BONUS_LOOT_RATE = 3./4.;

    private BonusType(boolean good, double lr) {
        if (good)
            lootRate = lr*GOOD_BONUS_LOOT_RATE;
        else
            lootRate = lr*(1-GOOD_BONUS_LOOT_RATE);
        
        this.good = good;
    }

    public double getLootRate() {
        return lootRate;
    }

    public boolean isGood() {
        return good;
    }
}
