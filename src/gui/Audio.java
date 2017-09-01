package gui;

import game.GameEvent;
import game.GameListener;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 * Classe gérant l'audio du jeu
 */
public class Audio implements GameListener {
    private Sound bombe;

    private Sound bonus;

    private Sound hit;

    private Music suddenDeath;

    public static Audio getInstance() {
        return SingletonHolder.instance;
    }

    private Audio() {
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

    /**
     * Arrête tous les clips audio lancés
     */
    public void stop() {
        hit.stop();
        bombe.stop();
        bonus.stop();
        suddenDeath.stop();
    }

    @Override
    /**
     * lance le son qui correspond à l'évènement reçu
     */
    public void gameChanged(GameEvent e) {
        switch(e){
        case Hit:
            hit.play(0.5);
            break;
        case Explosion:
            bombe.play(0.5);
            break;
        case PickUp:
            bonus.play(0.5);
            break;
        case SuddenDeath:
            suddenDeath.play(true);
            break;
        }
    }

// http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    private static class SingletonHolder {
        private static final Audio instance = new Audio();

    }

}
