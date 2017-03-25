package gui;

import java.awt.event.KeyEvent;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("a9e520e6-c602-4e73-9fe6-390ff4f5fabb")
public class ControlSettings {
    public String name;
    
    @objid ("ef47dcfe-ed1a-44b1-ab68-df9640e77ce1")
    public int up;

    @objid ("bf2750b4-b25d-457e-8a9c-6e7a52503371")
    public int down;

    @objid ("7d06b6c3-b596-4bac-bc4d-49b9172177c1")
    public int left;

    @objid ("83e8e03e-0b20-43f7-bfb4-08d75c5ac81a")
    public int right;

    @objid ("9f725747-d404-44fa-b88e-dffb549e8f13")
    public int plantBomb;

    @objid ("948d2cb4-d3f5-4e61-a924-4b263d52c766")
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
