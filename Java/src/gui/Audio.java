package gui;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import game.Event;
import game.GameListener;

public class Audio implements GameListener
{
    private Sound bombe, bonus, hit;
    private Music suddenDeath;
  
    public Audio()
    {
        TinySound.init();
        bombe = TinySound.loadSound("/audio/Explosion.ogg");
        bonus = TinySound.loadSound("/audio/Bonus.ogg");
        hit = TinySound.loadSound("/audio/Hit.ogg");
        suddenDeath = TinySound.loadMusic("/audio/SuddenDeath.ogg");
    }
    
    void stop() {
        hit.stop();
        bombe.stop();
        bonus.stop();
        suddenDeath.stop();
    }

    @Override
    public void processEvent(Event e) {
        switch(e){
        case Hit:
            hit.play();
            break;
        case Explosion:
            bombe.play();
            break;
        case PickUp:
            bonus.play();
            break;
        case SuddenDeath:
            suddenDeath.play(true);
            break;
        }
        
    }

}