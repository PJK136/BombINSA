package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("8b53145e-40f1-4120-b590-417a35a7feb5")
public class GameSettings {
    @objid ("59545b35-c143-4d23-81e7-cfa605a46f32")
    public GameType gameType;

    @objid ("fa41dd97-8866-4dbf-b87c-372978447d09")
    public String ipAddress;

    @objid ("97b01c6d-719e-40a4-a35e-4cf8ca0a1f5f")
    public String mapName;

    @objid ("3358695a-0d2b-4e4c-ac1b-71c6800c0fe6")
    public int tileSize;

    @objid ("518dcb54-de5e-4a36-8b59-228bfd65feb9")
    public int fps;

    @objid ("3d89f7ec-1b8b-4c83-ac61-bb0fcabae609")
    public int duration;

    @objid ("3b09f6d9-a7d8-4a9b-8d54-6343cf3bb343")
    public double warmupDuration;

    @objid ("23a2dc78-69f4-4cfb-9196-282f0059da01")
    public int playerCount;

    @objid ("2483f0fe-ee4e-4fde-a985-e59f6dd881a1")
    public int roundCount;

    @objid ("cff69b76-eea0-4a50-a107-1c15c7d9e87b")
    public int aiCount;

    @objid ("f319afc7-5c57-4504-a4ac-adade6f5a28d")
    public static final String SETTINGS_FILENAME = "settings.json";

    @objid ("6c9a5fee-337a-4dfd-bc72-0c2f021a5ce7")
    public double scale;

    @objid ("fbf49312-5381-4704-bf5b-297edc1b9879")
    public boolean tags;

    @objid ("35c7ed6d-70f2-46bc-9c5a-047fcaed1713")
    public List<ControlSettings> controls;

    @objid ("c6740cfb-642c-4bbc-82c0-d26fc0f5b072")
    private GameSettings() {
        //Configuration par défaut
        gameType = GameType.Local;
        ipAddress = "";
        mapName = "default";
        playerCount = 1;
        aiCount = 1;
        roundCount = 1;
        duration = 180;
        warmupDuration = 2;
        tileSize = 32;
        fps = 60;
        controls = new ArrayList<ControlSettings> ();
        controls.add(ControlSettings.defaultControls(0));
        controls.add(ControlSettings.defaultControls(1));
        controls.add(ControlSettings.defaultControls(2));
        controls.add(ControlSettings.defaultControls(3));
        scale = 1;
        tags = true;
    }

    @objid ("f454de1c-cf21-4345-acd1-5debae4eb46b")
    private static GameSettings load() {
        try {
            Gson gson = new Gson();
            return gson.fromJson(new FileReader(new File(SETTINGS_FILENAME)), GameSettings.class);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            System.err.println("Impossible de lire " + SETTINGS_FILENAME);
            e.printStackTrace();
            return new GameSettings();
        }
    }

    @objid ("a03739e2-2ae7-4c62-a7b3-0d2edbd4412a")
    public void save() throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PrintWriter printWriter = new PrintWriter(SETTINGS_FILENAME);
        printWriter.write(gson.toJson(this));
        printWriter.close();
    }

    @objid ("61e52266-93e7-4c1f-beb6-01283d8ac592")
    public int scale(int size) {
        return (int)(size*scale);
    }

    @objid ("465f7317-a123-4e6a-8f44-80c8ef2466fc")
    public double scale(double size) {
        return size*scale;
    }

    @objid ("4a58d5a4-01fa-433c-b027-eec38d51b4f0")
    public Dimension scale(Dimension dim) {
        return new Dimension(scale(dim.width), scale(dim.height));
    }

    @objid ("2fdc9a81-c218-40b3-a8e8-4f632a8d7fa7")
    public Font scale(Font font) {
        return font.deriveFont((float)scale(font.getSize()));
    }

    @objid ("e0876dec-e217-40cd-b8f3-584fd78d5e6e")
    public void scaleFont(JComponent component) {
        MainWindow.setFontSize(component, scale(component.getFont().getSize()));
    }

    @objid ("42057e54-8e3a-42ba-ae75-06870b71ee27")
    public static GameSettings getInstance() {
        return SingletonHolder.instance;
    }

// http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    @objid ("7b61ee5d-38fe-44b9-aab2-698bca537f0d")
    private static class SingletonHolder {
        @objid ("15ea41c0-58a7-4cb8-981d-2b3565ad3981")
        private static final GameSettings instance = load();

    }

}
