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
    public static final String SETTINGS_FILENAME = "settings.json";
    
    @objid ("59545b35-c143-4d23-81e7-cfa605a46f32")
    public GameType gameType;

    @objid ("97b01c6d-719e-40a4-a35e-4cf8ca0a1f5f")
    public String mapName;

    @objid ("3d89f7ec-1b8b-4c83-ac61-bb0fcabae609")
    public int duration;

    @objid ("23a2dc78-69f4-4cfb-9196-282f0059da01")
    public int playerCount;

    @objid ("2483f0fe-ee4e-4fde-a985-e59f6dd881a1")
    public int roundCount;

    @objid ("cff69b76-eea0-4a50-a107-1c15c7d9e87b")
    public int aiCount;

    @objid ("3358695a-0d2b-4e4c-ac1b-71c6800c0fe6")
    public int tileSize;

    @objid ("518dcb54-de5e-4a36-8b59-228bfd65feb9")
    public int fps;
    
    @objid ("35c7ed6d-70f2-46bc-9c5a-047fcaed1713")
    public List<ControlSettings> controls;
    
    public double scale;

    public boolean tags;
    
    @objid ("c6740cfb-642c-4bbc-82c0-d26fc0f5b072")
    private GameSettings() {
        //Configuration par défaut
        gameType = GameType.Local;
        mapName = "default";
        playerCount = 1;
        aiCount = 1;
        roundCount = 1;
        duration = 180;
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
    
    public int scale(int size) {
        return (int)(size*scale);
    }
    
    public double scale(double size) {
        return size*scale;
    }
    
    public Dimension scale(Dimension dim) {
        return new Dimension(scale(dim.width), scale(dim.height));
    }
    
    public Font scale(Font font) {
        return font.deriveFont((float)scale(font.getSize()));
    }
    
    public void scaleFont(JComponent component) {
        MainWindow.setFontSize(component, scale(component.getFont().getSize()));
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
   
    // http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    private static class SingletonHolder
    {       
        /** Instance unique non préinitialisée */
        private final static GameSettings instance = load();
    }
 
    /** Point d'accès pour l'instance unique du singleton */
    public static GameSettings getInstance() {
        return SingletonHolder.instance;
    }
}
