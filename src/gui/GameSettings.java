package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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

/**
 * Paramètres du jeu
 */
public class GameSettings {
    public GameType gameType;

    public String ipAddress;

    public List<String> maps;

    public int tileSize;

    public int fps;

    public int duration;

    public double warmupDuration;

    public double restTimeDuration;

    public int playerCount;

    public int roundCount;

    public int aiCount;

    public static final String SETTINGS_FILENAME = "settings.json";

    public double scale;

    public boolean tags;

    public List<ControlSettings> controls;

    /**
     * Construit des paramètres par défaut
     */
    private GameSettings() {
        //Configuration par défaut
        gameType = GameType.Local;
        ipAddress = "";
        maps = new ArrayList<>();
        playerCount = 1;
        aiCount = 1;
        roundCount = 1;
        duration = 180;
        warmupDuration = 2;
        restTimeDuration = 5;
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

    /**
     * Charge les paramètres depuis le fichier de configuration
     * @return Les paramètres
     */
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

    /**
     * Enregistre les paramètres dans le fichier de configuration
     * @throws FileNotFoundException s'il y a une erreur lors de la sauvegarde
     */
    public void save() throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        PrintWriter printWriter = new PrintWriter(SETTINGS_FILENAME);
        printWriter.write(gson.toJson(this));
        printWriter.close();
    }

    /**
     * Met à l'échelle la taille d'après les paramètres
     * @param size Taille à mettre à l'échelle
     * @return Taille mise à l'échelle
     */
    public int scale(int size) {
        return (int)(size*scale);
    }

    /**
     * Met à l'échelle la taille d'après les paramètres
     * @param size Taille à mettre à l'échelle
     * @return Taille mise à l'échelle
     */
    public double scale(double size) {
        return size*scale;
    }

    /**
     * Met à l'échelle des dimensions d'après les paramètres
     * @param dim Dimensions à mettre à l'échelle
     * @return Dimensions mises à l'échelle
     */
    public Dimension scale(Dimension dim) {
        return new Dimension(scale(dim.width), scale(dim.height));
    }

    /**
     * Met à l'échelle la police d'après les paramètres
     * @param font Police à mettre à l'échelle
     * @return Police mise à l'échelle
     */
    public Font scale(Font font) {
        return font.deriveFont((float)scale(font.getSize()));
    }

    /**
     * Met à l'échelle la police du composant
     * @param component Composant à mettre à l'échelle
     */
    public void scaleFont(Component component) {
        MainWindow.setFontSize(component, scale(component.getFont().getSize()));
    }

    public void scaleFontComponents(Component[] components)
    {
      for (Component component : components) {
          if (component instanceof Container)
              scaleFontComponents(((Container) component).getComponents());

          try {
              scaleFont(component);
          } catch (Exception e) {  }
      }
    }

    /**
     * @return Une instance des paramètres
     */
    public static GameSettings getInstance() {
        return SingletonHolder.instance;
    }

    // http://thecodersbreakfast.net/index.php?post/2008/02/25/26-de-la-bonne-implementation-du-singleton-en-java
    private static class SingletonHolder {
        private static final GameSettings instance = load();

    }

}
