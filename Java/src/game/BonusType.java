package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Liste des bonus/malus avec leurs taux d'apparition
 */
@objid ("7787b9db-abdc-433d-863e-9ffee325df12")
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

    @objid ("9ce96982-803a-4f05-86cb-7472cd493052")
    private double lootRate;
    
    private boolean good;
    
    private static final double GOOD_BONUS_LOOT_RATE = 3./4.;

    @objid ("d7eb4770-c2ef-42b0-94da-ea083fee605b")
    private BonusType(boolean good, double lr) {
        if (good)
            lootRate = lr*GOOD_BONUS_LOOT_RATE;
        else
            lootRate = lr*(1-GOOD_BONUS_LOOT_RATE);
        
        this.good = good;
    }

    @objid ("14a21d6f-2c04-4d55-b218-7a7f8af4e9f6")
    public double getLootRate() {
        return lootRate;
    }

    public boolean isGood() {
        return good;
    }
}
