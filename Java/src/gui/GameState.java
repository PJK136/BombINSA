package gui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("272af0d0-1009-4ee1-b1a1-c3037d20e7fe")
/**
 * Liste des états du jeu :
 *   - Départ
 *   - En jeu
 *   - Mort Subite
 *   - Fin d'un round
 *   - Fin de la partie
 */
public enum GameState {
    Init,
    Playing,
    SuddenDeath,
    EndRound,
    End;
}
