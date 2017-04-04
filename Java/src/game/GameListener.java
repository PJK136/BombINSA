package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Ecouteur des événements du jeu (explosion, se fait toucher, ramasse un bonus...)
 */
@objid ("4cfa9b4f-fbcd-4c9f-b802-38f0e10dca65")
public interface GameListener {
    @objid ("32ecbff2-fbde-47ff-87f6-e99da7de6f8f")
    void processEvent(Event e);

}
