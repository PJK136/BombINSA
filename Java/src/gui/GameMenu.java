package gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import javax.swing.JTextField;

@objid ("1792b20d-e79e-4f35-8f63-20089eca61f0")
public class GameMenu extends JPanel implements ActionListener {
    @objid ("a39767d1-eca3-41c4-85d7-7d0bde3a14cc")
    private MainWindow mainWindow;

    @objid ("0b5dea46-9866-4b4e-9ccb-f30bb706f360")
    private GameSettings settings;

    @objid ("56ed9616-f3e2-4c23-abc7-d31623572af8")
    private JButton btnPlay;

    @objid ("91fe7324-e7a9-4479-9307-8183fa44e192")
    private JButton btnBack;

    @objid ("276bf142-216c-48e8-b35b-b3b22b2bca83")
    private JComboBox<GameType> gameType;

    private JLabel lblIpAddress;
    
    private JTextField ipAddress;
    
    private JLabel lblMaps;
    
    @objid ("ef8d0d33-3c2b-4e50-92ff-ea79a7776078")
    private JComboBox<String> maps;

    @objid ("6fac6104-5782-4732-9bde-7820184bba18")
    private JSpinner playerCount;

    private JLabel lblAiCount;
    
    @objid ("0a56b5ff-f7dd-4db3-b7ce-44218221a273")
    private JSpinner aiCount;
    
    private JLabel lblRoundCount;
    
    @objid ("e49ecd14-7c49-417a-95cd-2a58b03e06a7")
    private JSpinner roundCount;
    
    private JLabel lblRoundDuration;
    
    @objid ("0963e96d-06f3-4813-bcde-a4a9cc51afc3")
    private JSpinner roundDuration;


    @objid ("9b4377fe-7088-4d49-ac76-20f2e508013d")
    public GameMenu(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.settings = GameSettings.getInstance();
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 25, 0, 64, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 32, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JLabel lblTypeDuJeu = new JLabel("Type du jeu :");
        settings.scaleFont(lblTypeDuJeu);
        GridBagConstraints gbc_lblTypeDuJeu = new GridBagConstraints();
        gbc_lblTypeDuJeu.anchor = GridBagConstraints.WEST;
        gbc_lblTypeDuJeu.insets = new Insets(0, 0, 5, 5);
        gbc_lblTypeDuJeu.gridx = 1;
        gbc_lblTypeDuJeu.gridy = 1;
        add(lblTypeDuJeu, gbc_lblTypeDuJeu);
        
        gameType = new JComboBox<GameType>();
        settings.scaleFont(gameType);
        GridBagConstraints gbc_gameType = new GridBagConstraints();
        gbc_gameType.insets = new Insets(0, 0, 5, 5);
        gbc_gameType.fill = GridBagConstraints.HORIZONTAL;
        gbc_gameType.gridx = 2;
        gbc_gameType.gridy = 1;
        add(gameType, gbc_gameType);
        
        for (GameType type : GameType.values())
            gameType.addItem(type);
        
        gameType.setSelectedItem(settings.gameType);
        gameType.addActionListener(this);
        
        JLabel lblHumain = new JLabel("Nombre de joueurs :");
        settings.scaleFont(lblHumain);
        GridBagConstraints gbc_lblHumain = new GridBagConstraints();
        gbc_lblHumain.anchor = GridBagConstraints.WEST;
        gbc_lblHumain.insets = new Insets(0, 0, 5, 5);
        gbc_lblHumain.gridx = 4;
        gbc_lblHumain.gridy = 1;
        add(lblHumain, gbc_lblHumain);
        
        playerCount = new JSpinner();
        playerCount.setModel(new SpinnerNumberModel(new Integer(settings.playerCount), new Integer(0), new Integer(settings.controls.size()), new Integer(1)));
        settings.scaleFont(playerCount);
        GridBagConstraints gbc_playerCount = new GridBagConstraints();
        gbc_playerCount.fill = GridBagConstraints.HORIZONTAL;
        gbc_playerCount.insets = new Insets(0, 0, 5, 5);
        gbc_playerCount.gridx = 5;
        gbc_playerCount.gridy = 1;
        add(playerCount, gbc_playerCount);
        
        lblIpAddress = new JLabel("Adresse IP :");
        settings.scaleFont(lblIpAddress);
        GridBagConstraints gbc_lblAdresseIp = new GridBagConstraints();
        gbc_lblAdresseIp.anchor = GridBagConstraints.WEST;
        gbc_lblAdresseIp.insets = new Insets(0, 0, 5, 5);
        gbc_lblAdresseIp.gridx = 1;
        gbc_lblAdresseIp.gridy = 2;
        add(lblIpAddress, gbc_lblAdresseIp);
        
        ipAddress = new JTextField(settings.ipAddress);
        settings.scaleFont(ipAddress);
        GridBagConstraints gbc_ipAddress = new GridBagConstraints();
        gbc_ipAddress.insets = new Insets(0, 0, 5, 5);
        gbc_ipAddress.fill = GridBagConstraints.HORIZONTAL;
        gbc_ipAddress.gridx = 2;
        gbc_ipAddress.gridy = 2;
        add(ipAddress, gbc_ipAddress);
        ipAddress.setColumns(10);
        
        lblAiCount = new JLabel("Nombre d'IA :");
        settings.scaleFont(lblAiCount);
        GridBagConstraints gbc_lblOrdinateur = new GridBagConstraints();
        gbc_lblOrdinateur.anchor = GridBagConstraints.WEST;
        gbc_lblOrdinateur.insets = new Insets(0, 0, 5, 5);
        gbc_lblOrdinateur.gridx = 4;
        gbc_lblOrdinateur.gridy = 2;
        add(lblAiCount, gbc_lblOrdinateur);
        
        aiCount = new JSpinner();
        aiCount.setModel(new SpinnerNumberModel(new Integer(settings.aiCount), new Integer(0), null, new Integer(1)));
        settings.scaleFont(aiCount);
        GridBagConstraints gbc_aiCount = new GridBagConstraints();
        gbc_aiCount.fill = GridBagConstraints.HORIZONTAL;
        gbc_aiCount.insets = new Insets(0, 0, 5, 5);
        gbc_aiCount.gridx = 5;
        gbc_aiCount.gridy = 2;
        add(aiCount, gbc_aiCount);
        
        lblMaps = new JLabel("Carte :");
        settings.scaleFont(lblMaps);
        GridBagConstraints gbc_lblCarte = new GridBagConstraints();
        gbc_lblCarte.anchor = GridBagConstraints.WEST;
        gbc_lblCarte.insets = new Insets(0, 0, 5, 5);
        gbc_lblCarte.gridx = 1;
        gbc_lblCarte.gridy = 3;
        add(lblMaps, gbc_lblCarte);
        
        maps = new JComboBox<String>();
        settings.scaleFont(maps);
        GridBagConstraints gbc_map = new GridBagConstraints();
        gbc_map.insets = new Insets(0, 0, 5, 5);
        gbc_map.fill = GridBagConstraints.HORIZONTAL;
        gbc_map.gridx = 2;
        gbc_map.gridy = 3;
        add(maps, gbc_map);
        updateMapList();
        maps.setSelectedItem(settings.mapName);
        
        lblRoundCount = new JLabel("Nombre de round :");
        settings.scaleFont(lblRoundCount);
        GridBagConstraints gbc_lblNombreDeRound = new GridBagConstraints();
        gbc_lblNombreDeRound.anchor = GridBagConstraints.WEST;
        gbc_lblNombreDeRound.insets = new Insets(0, 0, 5, 5);
        gbc_lblNombreDeRound.gridx = 4;
        gbc_lblNombreDeRound.gridy = 3;
        add(lblRoundCount, gbc_lblNombreDeRound);
        
        roundCount = new JSpinner();
        roundCount.setModel(new SpinnerNumberModel(new Integer(settings.roundCount), new Integer(1), null, new Integer(1)));
        settings.scaleFont(roundCount);
        GridBagConstraints gbc_roundCount = new GridBagConstraints();
        gbc_roundCount.fill = GridBagConstraints.HORIZONTAL;
        gbc_roundCount.insets = new Insets(0, 0, 5, 5);
        gbc_roundCount.gridx = 5;
        gbc_roundCount.gridy = 3;
        add(roundCount, gbc_roundCount);
        
        lblRoundDuration = new JLabel("Durée d'un round (s) :");
        settings.scaleFont(lblRoundDuration);
        GridBagConstraints gbc_lblDureeRound = new GridBagConstraints();
        gbc_lblDureeRound.anchor = GridBagConstraints.WEST;
        gbc_lblDureeRound.insets = new Insets(0, 0, 5, 5);
        gbc_lblDureeRound.gridx = 4;
        gbc_lblDureeRound.gridy = 4;
        add(lblRoundDuration, gbc_lblDureeRound);
        
        roundDuration = new JSpinner();
        roundDuration.setModel(new SpinnerNumberModel(new Integer(settings.duration), new Integer(0), null, new Integer(1)));
        settings.scaleFont(roundDuration);
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
        
        btnBack = new JButton("Retour");
        settings.scaleFont(btnBack);
        btnBack.addActionListener(this);
        panel.add(btnBack);
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        panel.add(horizontalStrut);
        
        btnPlay = new JButton("Jouer");
        settings.scaleFont(btnPlay);
        btnPlay.addActionListener(this);
        panel.add(btnPlay);
        
        updateFields();
    }

    @objid ("774feb6e-66a6-4fc0-869a-297ed681852b")
    private void updateMapList() {
        maps.removeAllItems();
        
        File folder = new File(".");
        File[] fileList = folder.listFiles();
        
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    String filename = file.getName();
                    if (filename.endsWith("." + MapCreatorPanel.MAP_EXTENSION))
                        maps.addItem(filename.substring(0, filename.length() - 4));
                }
            }
        }
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/maps/list.txt")))) {
            for(String line; (line = br.readLine()) != null; ) {
                if (!line.isEmpty())
                    maps.addItem(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFields() {
        switch ((GameType)gameType.getSelectedItem()) {
        case Client:
            lblIpAddress.setVisible(true);
            ipAddress.setVisible(true);
            lblMaps.setVisible(false);
            maps.setVisible(false);
            lblAiCount.setVisible(false);
            aiCount.setVisible(false);
            lblRoundCount.setVisible(false);
            roundCount.setVisible(false);
            lblRoundDuration.setVisible(false);
            roundDuration.setVisible(false);
            break;
        case Local:
        case Sandbox:
        case Server:
            lblIpAddress.setVisible(false);
            ipAddress.setVisible(false);
            lblMaps.setVisible(true);
            maps.setVisible(true);
            lblAiCount.setVisible(true);
            aiCount.setVisible(true);
            lblRoundCount.setVisible(true);
            roundCount.setVisible(true);
            lblRoundDuration.setVisible(true);
            roundDuration.setVisible(true);
            break;
        default:
            break;
        }
    }
    
    @objid ("d1e9ba38-aebd-4bc4-b6f9-a97681e13fe0")
    private void updateGameSettings() {
        settings.gameType = (GameType) gameType.getSelectedItem();
        settings.ipAddress = ipAddress.getText();
        settings.mapName = (String) maps.getSelectedItem();
        settings.playerCount = (int) playerCount.getValue();
        settings.aiCount = (int) aiCount.getValue();
        settings.roundCount = (int) roundCount.getValue();
        settings.duration = (int) roundDuration.getValue();
    }

    @objid ("3c87a9f3-8dd6-480e-a2ae-5e1534c8c5a2")
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == gameType) {
            updateFields();
        } else if (event.getSource() == btnBack || event.getSource() == btnPlay) {
            updateGameSettings();
            try {
                settings.save();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de sauvegarder la configuration actuelle dans " + GameSettings.SETTINGS_FILENAME + " !",
                        "Erreur lors de la sauvegarde",
                        JOptionPane.ERROR_MESSAGE);
            }
            
            if (event.getSource() == btnBack)
                mainWindow.showMenu();
            else
                mainWindow.startGame();
        }
    }
}
