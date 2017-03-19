package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import game.Player;
import game.WorldView;

@objid ("edc89ef6-b498-483e-875c-befa52d629f4")
public class GamePanel extends JPanel implements ActionListener, PropertyChangeListener {
    @objid ("1630e521-8ea4-48df-9278-b85be1fba591")
    private GameViewer gameViewer;

    @objid ("74e92561-a1a0-467e-a74c-be5e17aa47d1")
    private MainWindow mainWindow;

    private JPanel topBar;
    
    private JPanel playerStateGroup;
    
    HashMap<Player, PlayerStatePanel> playerStates;
    
    @objid ("dbd6f5cb-8848-4a03-9b74-402de0479e51")
    private JLabel timeRemaining;

    @objid ("1c3818b3-5d8a-4afc-a3fe-a21f1c2480f9")
    private JButton btnExit;

    @objid ("c0d6533a-0897-40bb-94e0-4be89488c38b")
    public GamePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(0, 0));
        
        topBar = new JPanel();
        add(topBar, BorderLayout.NORTH);
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        
        playerStates = new HashMap<Player, PlayerStatePanel>();
        playerStateGroup = new JPanel() ;
        playerStateGroup.setLayout(new WrapLayout(WrapLayout.LEFT));
        topBar.add(playerStateGroup);
        
        Component horizontalGlue = Box.createHorizontalGlue();
        topBar.add(horizontalGlue);
        
        timeRemaining = new JLabel("9:99 ");
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
    
    public void showGameStatus(WorldView view) {
        final int size = (int) (view.getMap().getTileSize()*0.75);
        updateTimeRemaining(view, size);
        if (btnExit.getIcon() == null || btnExit.getIcon().getIconHeight() != size)
            btnExit.setIcon(SpriteFactory.getInstance().getImageIcon("Stop24", size));
        
        List<Player> playerList = view.getPlayers();
        
        for (Player player : playerList) {
            PlayerStatePanel pState = playerStates.get(player);
            if (pState == null) {
                pState = new PlayerStatePanel(player.getPlayerID(), size);
                playerStates.put(player, pState);
                playerStateGroup.add(pState);
            }
            pState.updatePlayerState(player);
        }
        
        Iterator<Entry<Player, PlayerStatePanel>> iterator = playerStates.entrySet().iterator();
        Entry<Player, PlayerStatePanel> player;
        while (iterator.hasNext()) {
            player = iterator.next();
            if (!playerList.contains(player.getKey())) {
                playerStateGroup.remove(player.getValue());
                iterator.remove();
            }
        }
    }
    
    @objid ("28945b2a-0dba-494d-8e43-6992d3e5b089")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state"))
            return;
        
        GameProperty property = GameProperty.valueOf(evt.getPropertyName());
        
        switch (property) {
            case GameState:
                if ((GameState)evt.getNewValue() == GameState.Init) {
                    mainWindow.pack();
                    
                    int previousHeight;
                    do { //Réajuste la fenêtre tant que la hauteur est modifiée
                        previousHeight = playerStateGroup.getHeight();
                        playerStateGroup.revalidate();
                        mainWindow.pack();
                    } while (previousHeight != playerStateGroup.getHeight());
                    setVisible(true);
                    gameViewer.requestFocusInWindow();
                }
                break;
        }
    }

    @objid ("4eda1677-dd51-4090-9ffa-956332a2fc44")
    private void updateTimeRemaining(WorldView view, int size) {
        if (timeRemaining.getFont().getSize() != size)
            MainWindow.setFontSize(timeRemaining, size);
        
        if (view.getTimeRemaining() % view.getFps() != 0)
            return;
        
        int remaining = view.getTimeRemaining()/view.getFps();
        if (remaining <= 10 && remaining % 2 == 0) //Fait clignoter en rouge
            timeRemaining.setForeground(Color.red);
        else
            timeRemaining.setForeground(Color.black);
        StringBuilder text = new StringBuilder("⌛ ");
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
