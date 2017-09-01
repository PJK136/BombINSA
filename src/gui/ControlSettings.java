package gui;

import java.awt.event.KeyEvent;

/**
 * Définit les contrôles claviers des Joueurs
 */
public class ControlSettings {
    public int up;

    public int down;

    public int left;

    public int right;

    public int plantBomb;

    public String name;
    
    /**
     * Crée des contrôles par défaut au clavier
     * @param i ID des contrôles
     * @return Contrôles au clavier
     */
    public static ControlSettings defaultControls(int i) {
        ControlSettings controls = new ControlSettings();
        switch (i) {
        case 0:
            controls.name = "Joueur 1";
            controls.up = KeyEvent.VK_Z;
            controls.down = KeyEvent.VK_S;
            controls.left = KeyEvent.VK_Q;
            controls.right = KeyEvent.VK_D;
            controls.plantBomb = KeyEvent.VK_SPACE;
            break;
        case 1:
            controls.name = "Joueur 2";
            controls.up = KeyEvent.VK_UP;
            controls.down = KeyEvent.VK_DOWN;
            controls.left = KeyEvent.VK_LEFT;
            controls.right = KeyEvent.VK_RIGHT;
            controls.plantBomb = KeyEvent.VK_ENTER;
            break;
        case 2:
            controls.name = "Joueur 3";
            controls.up = KeyEvent.VK_NUMPAD8;
            controls.down = KeyEvent.VK_NUMPAD5;
            controls.left = KeyEvent.VK_NUMPAD4;
            controls.right = KeyEvent.VK_NUMPAD6;
            controls.plantBomb = KeyEvent.VK_NUMPAD0;
            break;
        case 3:
            controls.name = "Joueur 4";
            controls.up = KeyEvent.VK_I;
            controls.down = KeyEvent.VK_K;
            controls.left = KeyEvent.VK_J;
            controls.right = KeyEvent.VK_L;
            controls.plantBomb = KeyEvent.VK_N;
            break;
        default:
            return null;
        }
        return controls;
    }

}
