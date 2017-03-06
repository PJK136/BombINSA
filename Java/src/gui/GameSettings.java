package gui;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("8b53145e-40f1-4120-b590-417a35a7feb5")
public class GameSettings {
    @objid ("3358695a-0d2b-4e4c-ac1b-71c6800c0fe6")
    public int tileSize;

    @objid ("518dcb54-de5e-4a36-8b59-228bfd65feb9")
    public int fps;
    
    @objid ("35c7ed6d-70f2-46bc-9c5a-047fcaed1713")
    public List<ControlSettings> controls;
    
    @objid ("59545b35-c143-4d23-81e7-cfa605a46f32")
    public GameType gameType;

    @objid ("97b01c6d-719e-40a4-a35e-4cf8ca0a1f5f")
    public String mapName;

    @objid ("23a2dc78-69f4-4cfb-9196-282f0059da01")
    public int playerCount;
    
    public int aiCount;

    @objid ("3d89f7ec-1b8b-4c83-ac61-bb0fcabae609")
    public int duration;
    
    @objid ("2483f0fe-ee4e-4fde-a985-e59f6dd881a1")
    public int roundCount;

    public GameSettings() {
        //Configuration par défaut
        tileSize = 32;
        fps = 60;
        controls = new ArrayList<ControlSettings> ();
        controls.add(ControlSettings.defaultControls());
        gameType = GameType.Local;
        mapName = "default";
        playerCount = 1;
        aiCount = 0;
        roundCount = 1;
        duration = 180;
    }
    
    @objid ("f454de1c-cf21-4345-acd1-5debae4eb46b")
    public void load(String filename) {
    }

    @objid ("a03739e2-2ae7-4c62-a7b3-0d2edbd4412a")
    public void save(String filename) {
    }
}
