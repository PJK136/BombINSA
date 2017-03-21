package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("7787b9db-abdc-433d-863e-9ffee325df12")
public enum BonusType {
    Random((3./4.)*(1./10.)),
    MoreBomb((3./4.)*(4./10.)),
    LessBomb((1./4.)*(1./3.)),
    MoreRange((3./4.)*(1./10.)),
    LessRange((1./4.)*(1./3.)),
    MoreSpeed((3./4.)*(1./10.)),
    LessSpeed((1./4.)*(1./3.)),
    Shield((3./5.)*(1./5.)),
    Kick((3./5.)*(1./10.));
    private double lootRate;
    
    private BonusType(double lr) {
        lootRate=lr;
    }
    public double getLootRate() {
        return lootRate;
    }
}
