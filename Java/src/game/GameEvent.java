package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Liste des événements se produisant dans le jeu
 */
@objid ("0318a67f-53c8-43de-a366-7ee745ec96ad")
public enum GameEvent {
    Hit,
    Explosion,
    PickUp,
    SuddenDeath;
}
