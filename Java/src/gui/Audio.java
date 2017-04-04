package gui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Event;
import game.GameListener;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

@objid ("2c36fc6c-5652-4ca8-b975-04a406e9308a")
public class Audio implements GameListener {
    @objid ("755dca0f-b479-44e9-a6db-3bf577e18c9e")
    private Sound bombe;

    @objid ("e60055a4-e8cc-4b34-8642-e46daba83fb3")
    private Sound bonus;

    @objid ("f769a17e-e192-41c3-97ee-0a45b0d9f6d9")
    private Sound hit;

    @objid ("55f33426-38b8-4d3a-ba4c-2df064019d6f")
    private Music suddenDeath;

    @objid ("df8b6303-6585-4b3a-9b5d-9052eea10b12")
    public static Audio getInstance() {
        return SingletonHolder.instance;
    }

    @objid ("8c067f60-7b69-46aa-8f92-7d00428be5a7")
    private Audio() {
        TinySound.init();
        bombe = TinySound.loadSound("/audio/Explosion.ogg");
        bonus = TinySound.loadSound("/audio/Bonus.ogg");
        hit = TinySound.loadSound("/audio/Hit.ogg");
        suddenDeath = TinySound.loadMusic("/audio/SuddenDeath.ogg");
    }

    @objid ("0d8974f3-6887-476a-ab56-25abdacd8f14")
    @Override
    protected void finalize() throws Throwable {
        TinySound.shutdown();
        super.finalize();
    }

    @objid ("f49b5ee7-754b-4a0f-9405-d64d2607f48e")
    /**
     * Arrête tous les clips audio lancés
     */
    public void stop() {
        hit.stop();
        bombe.stop();
        bonus.stop();
        suddenDeath.stop();
    }

    @objid ("4d6557e8-da88-4a98-922e-52e3b27ce4ca")
    @Override
    /**
     * lance le son qui correspond à l'évènement reçu
     */
    public void processEvent(Event e) {
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
    @objid ("7570d4fd-bd8a-46f5-b27e-a1e9b23cbfac")
    private static class SingletonHolder {
        @objid ("31350b16-85a6-443d-8848-4f2883e5e633")
        private static final Audio instance = new Audio();

    }

}
