package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("0318a67f-53c8-43de-a366-7ee745ec96ad")
/**
 * Liste des événements se produisant dans le jeu
 * 
 * N'est utilisé ici que pour l'audio
 */
public enum Event {
    Hit,
    Explosion,
    PickUp,
    SuddenDeath;
}
