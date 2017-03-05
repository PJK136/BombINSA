package gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.SpinnerNumberModel;

public class GameMenu extends JPanel implements ActionListener {
    public final static String SETTINGS_FILENAME = "settings.conf";
    
    private MainWindow mainWindow;
    private GameSettings settings;
    
    private JButton btnPlay;
    private JButton btnReturn;
    private JSpinner roundDuration;
    private JSpinner playerCount;
    private JSpinner roundCount;
    private JSpinner aiCount;
    
    /**
     * Create the panel.
     */
    public GameMenu(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.settings = new GameSettings();
        this.settings.load(SETTINGS_FILENAME);
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 25, 0, 64, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 32, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JLabel lblTypeDuJeu = new JLabel("Type du jeu :");
        GridBagConstraints gbc_lblTypeDuJeu = new GridBagConstraints();
        gbc_lblTypeDuJeu.anchor = GridBagConstraints.WEST;
        gbc_lblTypeDuJeu.insets = new Insets(0, 0, 5, 5);
        gbc_lblTypeDuJeu.gridx = 1;
        gbc_lblTypeDuJeu.gridy = 1;
        add(lblTypeDuJeu, gbc_lblTypeDuJeu);
        
        JComboBox gameType = new JComboBox();
        GridBagConstraints gbc_gameType = new GridBagConstraints();
        gbc_gameType.insets = new Insets(0, 0, 5, 5);
        gbc_gameType.fill = GridBagConstraints.HORIZONTAL;
        gbc_gameType.gridx = 2;
        gbc_gameType.gridy = 1;
        add(gameType, gbc_gameType);
        
        JLabel lblHumain = new JLabel("Nombre de joueurs :");
        GridBagConstraints gbc_lblHumain = new GridBagConstraints();
        gbc_lblHumain.anchor = GridBagConstraints.WEST;
        gbc_lblHumain.insets = new Insets(0, 0, 5, 5);
        gbc_lblHumain.gridx = 4;
        gbc_lblHumain.gridy = 1;
        add(lblHumain, gbc_lblHumain);
        
        playerCount = new JSpinner();
        playerCount.setModel(new SpinnerNumberModel(new Integer(settings.playerCount), null, null, new Integer(1)));
        GridBagConstraints gbc_playerCount = new GridBagConstraints();
        gbc_playerCount.fill = GridBagConstraints.HORIZONTAL;
        gbc_playerCount.insets = new Insets(0, 0, 5, 5);
        gbc_playerCount.gridx = 5;
        gbc_playerCount.gridy = 1;
        add(playerCount, gbc_playerCount);
        
        JLabel lblOrdinateur = new JLabel("Nombre d'IA :");
        GridBagConstraints gbc_lblOrdinateur = new GridBagConstraints();
        gbc_lblOrdinateur.anchor = GridBagConstraints.WEST;
        gbc_lblOrdinateur.insets = new Insets(0, 0, 5, 5);
        gbc_lblOrdinateur.gridx = 4;
        gbc_lblOrdinateur.gridy = 2;
        add(lblOrdinateur, gbc_lblOrdinateur);
        
        aiCount = new JSpinner();
        aiCount.setModel(new SpinnerNumberModel(new Integer(settings.aiCount), new Integer(0), null, new Integer(1)));
        GridBagConstraints gbc_aiCount = new GridBagConstraints();
        gbc_aiCount.fill = GridBagConstraints.HORIZONTAL;
        gbc_aiCount.insets = new Insets(0, 0, 5, 5);
        gbc_aiCount.gridx = 5;
        gbc_aiCount.gridy = 2;
        add(aiCount, gbc_aiCount);
        
        JLabel lblCarte = new JLabel("Carte :");
        GridBagConstraints gbc_lblCarte = new GridBagConstraints();
        gbc_lblCarte.anchor = GridBagConstraints.WEST;
        gbc_lblCarte.insets = new Insets(0, 0, 5, 5);
        gbc_lblCarte.gridx = 1;
        gbc_lblCarte.gridy = 3;
        add(lblCarte, gbc_lblCarte);
        
        JComboBox map = new JComboBox();
        GridBagConstraints gbc_map = new GridBagConstraints();
        gbc_map.insets = new Insets(0, 0, 5, 5);
        gbc_map.fill = GridBagConstraints.HORIZONTAL;
        gbc_map.gridx = 2;
        gbc_map.gridy = 3;
        add(map, gbc_map);
        
        JLabel lblNombreDeRound = new JLabel("Nombre de round :");
        GridBagConstraints gbc_lblNombreDeRound = new GridBagConstraints();
        gbc_lblNombreDeRound.anchor = GridBagConstraints.WEST;
        gbc_lblNombreDeRound.insets = new Insets(0, 0, 5, 5);
        gbc_lblNombreDeRound.gridx = 4;
        gbc_lblNombreDeRound.gridy = 3;
        add(lblNombreDeRound, gbc_lblNombreDeRound);
        
        roundCount = new JSpinner();
        roundCount.setModel(new SpinnerNumberModel(new Integer(settings.roundCount), new Integer(1), null, new Integer(1)));
        GridBagConstraints gbc_roundCount = new GridBagConstraints();
        gbc_roundCount.fill = GridBagConstraints.HORIZONTAL;
        gbc_roundCount.insets = new Insets(0, 0, 5, 5);
        gbc_roundCount.gridx = 5;
        gbc_roundCount.gridy = 3;
        add(roundCount, gbc_roundCount);
        
        JLabel lblDureeRound = new JLabel("Durée d'un round (s) :");
        GridBagConstraints gbc_lblDureeRound = new GridBagConstraints();
        gbc_lblDureeRound.anchor = GridBagConstraints.WEST;
        gbc_lblDureeRound.insets = new Insets(0, 0, 5, 5);
        gbc_lblDureeRound.gridx = 4;
        gbc_lblDureeRound.gridy = 4;
        add(lblDureeRound, gbc_lblDureeRound);
        
        roundDuration = new JSpinner();
        roundDuration.setModel(new SpinnerNumberModel(new Integer(settings.duration), new Integer(0), null, new Integer(1)));
        GridBagConstraints gbc_roundDuration = new GridBagConstraints();
        gbc_roundDuration.fill = GridBagConstraints.HORIZONTAL;
        gbc_roundDuration.insets = new Insets(0, 0, 5, 5);
        gbc_roundDuration.gridx = 5;
        gbc_roundDuration.gridy = 4;
        add(roundDuration, gbc_roundDuration);
        
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 5;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 6;
        add(panel, gbc_panel);
        panel.setLayout(new GridLayout(0, 5, 0, 0));
        
        Component horizontalStrut_1 = Box.createHorizontalStrut(20);
        panel.add(horizontalStrut_1);
        
        btnReturn = new JButton("Retour");
        btnReturn.addActionListener(this);
        panel.add(btnReturn);
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        panel.add(horizontalStrut);
        
        btnPlay = new JButton("Jouer");
        btnPlay.addActionListener(this);
        panel.add(btnPlay);
    }

    private GameSettings updateGameSettings() {
        settings.playerCount = (int) playerCount.getValue();
        settings.aiCount = (int) aiCount.getValue();
        settings.roundCount = (int) roundCount.getValue();
        settings.duration = (int) roundDuration.getValue();
        return settings;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnReturn) {
            updateGameSettings();
            settings.save(SETTINGS_FILENAME);
            mainWindow.showMenu();
        }
        else if (event.getSource() == btnPlay) {
            updateGameSettings();
            settings.save(SETTINGS_FILENAME);
            mainWindow.startGame(settings);
        }
    }
}
