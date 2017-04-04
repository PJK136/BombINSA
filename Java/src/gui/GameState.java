package gui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/** Liste des états du jeu */
@objid ("272af0d0-1009-4ee1-b1a1-c3037d20e7fe")

public enum GameState {
    Init,
    Playing,
    SuddenDeath,
    EndRound,
    End;
}
