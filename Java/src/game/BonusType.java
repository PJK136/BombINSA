package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("7787b9db-abdc-433d-863e-9ffee325df12")
public enum BonusType {
    Random((3./5.)*(1./10.)),
    MoreBomb((3./5.)*(1./5.)),
    LessBomb((2./5.)*(1./3.)),
    MoreRange((3./5.)*(1./5.)),
    LessRange((2./5.)*(1./3.)),
    MoreSpeed((3./5.)*(1./5.)),
    LessSpeed((2./5.)*(1./3.)),
    Shield((3./5.)*(1./10.)),
    Kick((3./5.)*(1./5.));
    private double lootRate;
    
    private BonusType(double lr) {
        lootRate=lr;
    }
    public double getLootRate() {
        return lootRate;
    }
}
