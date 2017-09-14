package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import game.Character;
import game.Controller;
import game.Direction;

/**
 * Gère les contrôles clavier : renvoie la direction associée à la touche appuyée
 */
public class KeyboardController extends Controller implements KeyListener {
    /**
     * Liste des directions qui sont enfoncées triées dans l'ordre d'apparition
     * décroissant (^plus récente en début et plus ancienne à la fin)
     */
    private LinkedList<Direction> keysPressed;

    /**
     * Indique si on doit psoer une bombe ou pas
     */
    private boolean bombing;

    /**
     * Paramêtres qui permettent d'associer les touches enfoncées sur le clavier à
     * une commande en jeu
     */
    private ControlSettings settings;


    public KeyboardController(ControlSettings settings) {
        setName(settings.name);
        this.settings = settings;
        keysPressed = new LinkedList<Direction>();
        bombing = false;
    }

    @Override
    public void setCharacter(Character value) {
    	super.setCharacter(value);
    	bombing = false;
    }

    @Override
    public Direction getDirection() {
        // renvoie la direction qui correspond a la touche qui est enfoncée
        return keysPressed.peek();
    }

    @Override
    /**
     * renvoie si la commande "poser une bombe" a été activée
     */
    public boolean isPlantingBomb() {
        if (bombing){
            bombing = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {
    }

    @Override
    /**
     * Rajoute à la liste en attribut la direction qui correspond a une
     * touche quand elle est appuyée.
     * Pour éviter les effets du rebond on efface de la liste l'instance
     * de la direction avant de l'ajouter
     */
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == settings.right){
            keysPressed.remove(Direction.Right); //fais en sorte qu'il y ai au maximum 1 exemplaire dans la liste sinon ca bug..
            keysPressed.push(Direction.Right);
        } else if(e.getKeyCode() == settings.up){
            keysPressed.remove(Direction.Up);
            keysPressed.push(Direction.Up);
        } else if(e.getKeyCode() == settings.left){
            keysPressed.remove(Direction.Left);
            keysPressed.push(Direction.Left);
        } else if(e.getKeyCode() == settings.down){
            keysPressed.remove(Direction.Down);
            keysPressed.push(Direction.Down);
        } else if(e.getKeyCode() == settings.plantBomb){
            bombing = true;
        }
    }

    @Override
    /**
     * Enlève la direction de la liste en attribut lorque la touche
     * qui y correspond est relachée
     */
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == settings.right){
            keysPressed.remove(Direction.Right);
        } else if(e.getKeyCode() == settings.up){
            keysPressed.remove(Direction.Up);
        } else if(e.getKeyCode() == settings.left){
            keysPressed.remove(Direction.Left);
        } else if(e.getKeyCode() == settings.down){
            keysPressed.remove(Direction.Down);
        } else if(e.getKeyCode() == settings.plantBomb){
            bombing = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
