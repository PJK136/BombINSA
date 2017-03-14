package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Controller;
import game.Direction;
import game.Player;
import game.World;

@objid ("0a274714-4157-430f-bba3-fe0b0286cf2f")
public class KeyboardController implements Controller, KeyListener {
    /** TODO implémenter la méthode de lecture clavier
     * Objectif : que l'on sache ou on est en train d'appuyer (doit conserver une variable car java n'a pas de methode qui lit si on est appuyé, seulement si on appui et si on relache)
     * 
     *   
     *   
     */
    
    @objid ("a93e8c2e-c054-4934-923f-acac72ff257c")
    private ControlSettings settings;
    
    private LinkedList<Integer> keysPressed ;
    
    private boolean bombing;
    
  

    @objid ("2c9abb5b-b9fc-4226-bee5-6df775a5d20d")
    public KeyboardController(ControlSettings settings) {
        this.settings = settings;
        keysPressed = new LinkedList<Integer>();
        bombing = false;
    }

    @objid ("5da90aab-5452-4e19-8f6e-f4c0ad78b77d")
    @Override
    public void setPlayer(Player player) {
        // TODO Auto-generated method stub
    }

    @objid ("5dc0c3b2-7578-45d3-9b5e-6192853ef07e")
    @Override
    public void setWorldView(World world) {
        // TODO Auto-generated method stub
    }

    @objid ("c62f96ab-e6cf-42ce-921b-370655bdf461")
    @Override
    public Direction getDirection() {
        // renvoie la direction qui correspond a la touche qui est enfoncée
        if(keysPressed.isEmpty()){
            return null;
        }
        return Direction.values()[keysPressed.getFirst()];
    }

    @objid ("39549206-ede1-4d8e-bdb7-8f5010446eb7")
    @Override
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
        // TODO Auto-generated method stub
    }

    @objid ("3d5514a6-86cc-49d2-8723-289056c0be41")
    @Override
    public void keyPressed(KeyEvent e) {
        // A verifier si la condition est correcte ou pas ^^
        if(e.getKeyCode() == settings.right){
            keysPressed.addFirst(Direction.Right.ordinal());
        } else if(e.getKeyCode() == settings.up){
            keysPressed.addFirst(Direction.Up.ordinal());
        } else if(e.getKeyCode() == settings.left){
            keysPressed.addFirst(Direction.Left.ordinal());
        } else if(e.getKeyCode() == settings.down){
            keysPressed.addFirst(Direction.Down.ordinal());
        } else if(e.getKeyCode() == settings.plantBomb){
            bombing = true;
        }
    }

    @objid ("2aeea9b7-3dee-4dd7-887d-4c312c79010f")
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == settings.right){
            keysPressed.remove((Integer)(Direction.Right.ordinal()));
        } else if(e.getKeyCode() == settings.up){
            keysPressed.remove((Integer)(Direction.Up.ordinal()));
        } else if(e.getKeyCode() == settings.left){
            keysPressed.remove((Integer)(Direction.Left.ordinal()));
        } else if(e.getKeyCode() == settings.down){
            keysPressed.remove((Integer)(Direction.Down.ordinal()));
        }
    }

    @objid ("33d4b5b5-aade-4cf8-94ad-41eba98f283d")
    @Override
    public void keyTyped(KeyEvent e) {
    }

}
