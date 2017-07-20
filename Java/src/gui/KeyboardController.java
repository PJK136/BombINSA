package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Controller;
import game.Direction;
import game.Player;

/**
 * Gère les contrôles clavier : renvoie la direction associée à la touche appuyée
 */
@objid ("0a274714-4157-430f-bba3-fe0b0286cf2f")
public class KeyboardController extends Controller implements KeyListener {
    @objid ("551f145c-09ad-4538-86bc-5b9129d34274")
    /**
     * Liste des directions qui sont enfoncées triées dans l'ordre d'apparition 
     * décroissant (^plus récente en début et plus ancienne à la fin)
     */
    private LinkedList<Direction> keysPressed;

    @objid ("821786a6-3f54-48d5-8e4d-f8425ef62c20")
    /**
     * Indique si on doit psoer une bombe ou pas
     */
    private boolean bombing;

    @objid ("a93e8c2e-c054-4934-923f-acac72ff257c")
    /**
     * Paramêtres qui permettent d'associer les touches enfoncées sur le clavier à
     * une commande en jeu
     */
    private ControlSettings settings;


    @objid ("2c9abb5b-b9fc-4226-bee5-6df775a5d20d")
    public KeyboardController(ControlSettings settings) {
        setName(settings.name);
        this.settings = settings;
        keysPressed = new LinkedList<Direction>();
        bombing = false;
    }

    @Override
    public void setPlayer(Player value) {
    	super.setPlayer(value);
    	bombing = false;
    }
    
    @objid ("c62f96ab-e6cf-42ce-921b-370655bdf461")
    @Override
    public Direction getDirection() {
        // renvoie la direction qui correspond a la touche qui est enfoncée
        return keysPressed.peek();
    }

    @objid ("39549206-ede1-4d8e-bdb7-8f5010446eb7")
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

    @objid ("257910dc-1922-4e33-8c1b-78cf3a30f0b4")
    @Override
    public void update() {
    }

    @objid ("3d5514a6-86cc-49d2-8723-289056c0be41")
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

    @objid ("2aeea9b7-3dee-4dd7-887d-4c312c79010f")
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
        }
    }

    @objid ("33d4b5b5-aade-4cf8-94ad-41eba98f283d")
    @Override
    public void keyTyped(KeyEvent e) {
    }

}
