package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Liste des bonus/malus avec leurs taux d'apparition
 */
@objid ("7787b9db-abdc-433d-863e-9ffee325df12")
public enum BonusType {
    Random ((3./4.)*(1./10.)),
    MoreBomb ((3./4.)*(3./10.)),
    LessBomb ((1./4.)*(1./3.)),
    MoreRange ((3./4.)*(2./10.)),
    LessRange ((1./4.)*(1./3.)),
    MoreSpeed ((3./4.)*(1./10.)),
    LessSpeed ((1./4.)*(1./3.)),
    Shield ((3./4.)*(1./5.)),
    Kick ((3./4.)*(1./10.));

    @objid ("9ce96982-803a-4f05-86cb-7472cd493052")
    private double lootRate;

    @objid ("d7eb4770-c2ef-42b0-94da-ea083fee605b")
    private BonusType(double lr) {
        lootRate = lr;
    }

    @objid ("14a21d6f-2c04-4d55-b218-7a7f8af4e9f6")
    public double getLootRate() {
        return lootRate;
    }

}
