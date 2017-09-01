package gui;


/** Liste des modes de jeu */
/**
 *   - En local (sur une seule machine, doit forcément jouer contre quelqu'un, IA ou autre joueur)
 *   - En L.A.N. : Server héberge la partie ; Client la rejoint
 *   - SandBox : permet de jouer tout seul sur une carte (sans adversaires ni d'IA) 
 */
public enum GameType {
    Local,
    Server,
    Client,
    Sandbox;
}
