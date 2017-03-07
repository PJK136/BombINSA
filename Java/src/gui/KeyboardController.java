package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.Controller;
import game.Direction;
import game.Player;
import game.World;

public class KeyboardController implements Controller, KeyListener {
    private ControlSettings settings;
    
    public KeyboardController(ControlSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public void setPlayer(Player player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setWorldView(World world) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Direction getDirection() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPlantingBomb() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

}
