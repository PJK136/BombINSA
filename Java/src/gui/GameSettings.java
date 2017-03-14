package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("8b53145e-40f1-4120-b590-417a35a7feb5")
public class GameSettings {
    @objid ("59545b35-c143-4d23-81e7-cfa605a46f32")
    public GameType gameType;

    @objid ("97b01c6d-719e-40a4-a35e-4cf8ca0a1f5f")
    public String mapName;

    @objid ("3358695a-0d2b-4e4c-ac1b-71c6800c0fe6")
    public int tileSize;

    @objid ("518dcb54-de5e-4a36-8b59-228bfd65feb9")
    public int fps;

    @objid ("3d89f7ec-1b8b-4c83-ac61-bb0fcabae609")
    public int duration;

    @objid ("23a2dc78-69f4-4cfb-9196-282f0059da01")
    public int playerCount;

    @objid ("2483f0fe-ee4e-4fde-a985-e59f6dd881a1")
    public int roundCount;

    @objid ("cff69b76-eea0-4a50-a107-1c15c7d9e87b")
    public int aiCount;

    @objid ("35c7ed6d-70f2-46bc-9c5a-047fcaed1713")
    public List<ControlSettings> controls;

    @objid ("c6740cfb-642c-4bbc-82c0-d26fc0f5b072")
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
    public static GameSettings load(String filename) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(new File(filename)), GameSettings.class);
    }

    @objid ("a03739e2-2ae7-4c62-a7b3-0d2edbd4412a")
    public void save(String filename) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PrintWriter printWriter = new PrintWriter(filename);
        printWriter.write(gson.toJson(this));
        printWriter.close();
    }

}
