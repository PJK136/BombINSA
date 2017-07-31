package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Character;
import game.WorldView;

/**
 * Affichage du jeu
 */
@objid ("edc89ef6-b498-483e-875c-befa52d629f4")
public class GamePanel extends JPanel implements ActionListener {
    @objid ("74e92561-a1a0-467e-a74c-be5e17aa47d1")
    private MainWindow mainWindow;

    @objid ("a8a9fdd6-1773-4e49-a16d-eb3c08adad30")
    private GameSettings settings;

    @objid ("1630e521-8ea4-48df-9278-b85be1fba591")
    private GameViewer gameViewer;

    @objid ("d499b26b-6b62-4665-bcd7-7b8d480cf409")
    private JPanel topBar;

    @objid ("d71ae60b-862a-46ad-89eb-7faf069d8025")
    private JPanel playerStateGroup;

    @objid ("3ace71d3-e6f1-4997-a200-b55f6b20c619")
     HashMap<Character, PlayerStatePanel> playerStates;

    private int lastTimeRemaining;
    
    @objid ("dbd6f5cb-8848-4a03-9b74-402de0479e51")
    private JLabel timeRemaining;

    @objid ("1c3818b3-5d8a-4afc-a3fe-a21f1c2480f9")
    private JButton btnExit;
    
    private static final int ICON_SIZE = 32;
    
    /**
     * Construit le panneau d'affichage du jeu
     * @param mainWindow Fenêtre principale
     */
    @objid ("c0d6533a-0897-40bb-94e0-4be89488c38b")
    public GamePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.settings = GameSettings.getInstance();
        setLayout(new BorderLayout(0, 0));
        
        topBar = new JPanel();
        add(topBar, BorderLayout.NORTH);
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        
        playerStates = new HashMap<Character, PlayerStatePanel>();
        playerStateGroup = new JPanel() ;
        playerStateGroup.setLayout(new WrapLayout(WrapLayout.LEFT));
        topBar.add(playerStateGroup);
        
        Component horizontalGlue = Box.createHorizontalStrut(10);
        topBar.add(horizontalGlue);
        
        timeRemaining = new JLabel("9:99 ");
        timeRemaining.setIcon(SpriteFactory.getInstance().getImageIcon("hourglass", settings.scale(ICON_SIZE)));
        topBar.add(timeRemaining);
        
        btnExit = new JButton();
        btnExit.addActionListener(this);
        topBar.add(btnExit);
        
        gameViewer = new GameViewer();
        add(gameViewer, BorderLayout.CENTER);
        
        setVisible(false); //Affiche le panel seulement une fois chargé
    }

    @objid ("20db9f0e-9e0f-43ed-88e1-ac5019fb649c")
    GameViewer getGameViewer() {
        return gameViewer;
    }

    @objid ("3da3d7ef-6623-4412-80f1-1c352bd7f909")
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnExit) {
            mainWindow.showMenu();
        }
    }

    /**
     * Met à jour la barre d'état du jeu
     * @param view Vue du de la partie
     */
    @objid ("45932752-17aa-44c7-8689-4c86cb06b683")
    public void showGameStatus(WorldView view) {
        final int size = settings.scale(ICON_SIZE);
        
        updateTimeRemaining(view, size);
        if (btnExit.getIcon() == null || btnExit.getIcon().getIconHeight() != size) {
            btnExit.setIcon(SpriteFactory.getInstance().getImageIcon("Stop24", size));
            timeRemaining.setIcon(SpriteFactory.getInstance().getImageIcon("hourglass", size));
        }
        
        List<Character> characterList = view.getCharacters();
        
        for (Character character : characterList) {
            PlayerStatePanel pState = playerStates.get(character);
            if (pState == null) {
                pState = new PlayerStatePanel(character.getPlayerID(), size);
                playerStates.put(character, pState);
                playerStateGroup.add(pState);
            }
            
            pState.updateCharacterState(character);
        }
        
        Iterator<Entry<Character, PlayerStatePanel>> iterator = playerStates.entrySet().iterator();
        Entry<Character, PlayerStatePanel> character;
        while (iterator.hasNext()) {
            character = iterator.next();
            if (!characterList.contains(character.getKey())) {
                playerStateGroup.remove(character.getValue());
                iterator.remove();
            }
        }
        
        if (!this.isVisible()) {
            setVisible(true);
            gameViewer.requestFocusInWindow();
        }
    }

    /**
     * Met à jour le compteur du temps restant
     * @param view Vue du la partie
     * @param size Taille du texte
     */
    @objid ("4eda1677-dd51-4090-9ffa-956332a2fc44")
    private void updateTimeRemaining(WorldView view, int size) {
        if (timeRemaining.getFont().getSize() != size)
            MainWindow.setFontSize(timeRemaining, size);
        
        if (lastTimeRemaining != 0 && view.getTimeRemaining() % view.getFps() != 0)
            return;
        
        lastTimeRemaining = view.getFps();
        
        int remaining = view.getTimeRemaining()/view.getFps();
        if (remaining <= 10 && remaining % 2 == 0) //Fait clignoter en rouge
            timeRemaining.setForeground(Color.red);
        else
            timeRemaining.setForeground(Color.black);
        StringBuilder text = new StringBuilder("");
        if (view.getTimeRemaining() < 0)
            text.append("-");
        text.append(Math.abs(remaining/60));
        text.append(':');
        int seconds = Math.abs(remaining%60);
        if (seconds < 10)
            text.append("0");
        text.append(seconds);
        text.append("  ");
        timeRemaining.setText(text.toString());
    }

}
