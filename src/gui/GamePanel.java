package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import game.Player;
import game.WorldView;

/**
 * Affichage du jeu
 */
public class GamePanel extends JPanel implements ActionListener {
    private MainWindow mainWindow;

    private GameSettings settings;

    private GameViewer gameViewer;

    private JPanel topBar;

    private JPanel playerStatePanel;

     HashMap<Player, PlayerStatePanel> playerStates;

    private JLabel lblType;

    private JLabel type;

    private Component spacer1;

    private Component spacer2;

    private JLabel lblMap;

    private JLabel map;

    private int lastTimeRemaining;

    private JLabel timeRemaining;

    private JButton btnExit;

    private static final int ICON_SIZE = 32;

    /**
     * Construit le panneau d'affichage du jeu
     * @param mainWindow Fenêtre principale
     */
    public GamePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.settings = GameSettings.getInstance();
        setLayout(new BorderLayout(0, 0));

        topBar = new JPanel();
        add(topBar, BorderLayout.NORTH);
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));

        lblType = new JLabel("Type : ");
        topBar.add(lblType);

        type = new JLabel("");
        type.setFont(type.getFont().deriveFont(Font.PLAIN));
        topBar.add(type);

        spacer1 = Box.createHorizontalStrut(5);
        topBar.add(spacer1);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setMaximumSize(new Dimension(1, Integer.MAX_VALUE));
        topBar.add(separator);

        spacer2 = Box.createHorizontalStrut(5);
        topBar.add(spacer2);

        lblMap = new JLabel("Carte : ");
        topBar.add(lblMap);

        map = new JLabel("");
        map.setFont(map.getFont().deriveFont(Font.PLAIN));
        topBar.add(map);

        topBar.add(Box.createHorizontalGlue());

        timeRemaining = new JLabel("9:99 ");
        timeRemaining.setIcon(SpriteFactory.getInstance().getImageIcon("hourglass", settings.scale(ICON_SIZE)));
        topBar.add(timeRemaining);

        playerStates = new HashMap<Player, PlayerStatePanel>();
        playerStatePanel = new JPanel() ;
        playerStatePanel.setLayout(new BoxLayout(playerStatePanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(playerStatePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.WEST);

        btnExit = new JButton();
        btnExit.addActionListener(this);
        topBar.add(btnExit);

        gameViewer = new GameViewer();
        add(gameViewer, BorderLayout.CENTER);

        setVisible(false); //Affiche le panel seulement une fois chargé
    }

    GameViewer getGameViewer() {
        return gameViewer;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnExit) {
            mainWindow.showMenu();
        }
    }

    public void setType(String type) {
        this.type.setText(type);
    }

    public void setMap(String map) {
        this.map.setText(map);
    }

    /**
     * Met à jour la barre d'état du jeu
     * @param view Vue du de la partie
     */
    public void showGameStatus(WorldView view) {
        final int size = settings.scale(ICON_SIZE);

        updateTimeRemaining(view, size);
        if (btnExit.getIcon() == null || btnExit.getIcon().getIconHeight() != size) {
            MainWindow.setFontSize(lblType, size*3/4);
            MainWindow.setFontSize(type, size*3/4);
            MainWindow.setFontSize(lblMap, size*3/4);
            MainWindow.setFontSize(map, size*3/4);

            spacer1.setPreferredSize(new Dimension(size/2, size));
            spacer2.setPreferredSize(new Dimension(size/2, size));

            btnExit.setIcon(SpriteFactory.getInstance().getImageIcon("Stop24", size));
            timeRemaining.setIcon(SpriteFactory.getInstance().getImageIcon("hourglass", size));
        }

        List<Player> playerList = view.getPlayers();

        for (Player player : playerList) {
            PlayerStatePanel pState = playerStates.get(player);
            if (pState == null) {
                pState = new PlayerStatePanel(player, size);
                pState.setAlignmentX(Component.LEFT_ALIGNMENT);
                playerStates.put(player, pState);
                playerStatePanel.add(pState);
                playerStatePanel.repaint();
            } else
                pState.updatePlayerState();
        }

        Iterator<Entry<Player, PlayerStatePanel>> iterator = playerStates.entrySet().iterator();
        Entry<Player, PlayerStatePanel> player;
        while (iterator.hasNext()) {
            player = iterator.next();
            if (!playerList.contains(player.getKey())) {
                playerStatePanel.remove(player.getValue());
                iterator.remove();
                playerStatePanel.repaint();
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
