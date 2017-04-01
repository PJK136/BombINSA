package gui;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import game.Event;
import game.GameListener;

public class Audio implements GameListener
{
    private Sound bombe, bonus, hit;
    private Music suddenDeath;
    
    @objid ("42057e54-8e3a-42ba-ae75-06870b71ee27")
    public static Audio getInstance() {
        return SingletonHolder.instance;
    }

    // http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    @objid ("7b61ee5d-38fe-44b9-aab2-698bca537f0d")
    private static class SingletonHolder {
        @objid ("15ea41c0-58a7-4cb8-981d-2b3565ad3981")
        private static final Audio instance = new Audio();

    }
  
    private Audio()
    {
        TinySound.init();
        bombe = TinySound.loadSound("/audio/Explosion.ogg");
        bonus = TinySound.loadSound("/audio/Bonus.ogg");
        hit = TinySound.loadSound("/audio/Hit.ogg");
        suddenDeath = TinySound.loadMusic("/audio/SuddenDeath.ogg");
    }
    
    @Override
    protected void finalize() throws Throwable {
        TinySound.shutdown();
        super.finalize();
    }
    
    public void stop() {
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