package gui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/** Liste des modes de jeu */
@objid ("f5a7d15e-110e-4488-a6f6-af5012d84a04")
/**
 *   - En local (sur une seule machine, doit forcément jouer contre quelqu'un, IA ou autre joueur)
 *   - En L.A.N. : Server héberge la partie ; Client la rejoind
 *   - SandBox : permet de jouer tout seul sur une carte (sans adversaires ni d'IA) 
 */
public enum GameType {
    Local,
    Server,
    Client,
    Sandbox;
}
