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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

/**
 * Menu des options d'une partie de jeu
 */
public class GameMenu extends JPanel implements ActionListener {
    private MainWindow mainWindow;

    private GameSettings settings;

    private JButton btnPlay;

    private JButton btnBack;

    private JComboBox<GameType> gameType;

    private JLabel lblIpAddress;

    private JTextField ipAddress;

    private JLabel lblMaps;

    private JScrollPane mapPane;

    private JList<String> maps;

    private JSpinner playerCount;

    private JLabel lblAiCount;

    private JSpinner aiCount;

    private JLabel lblRoundCount;

    private JSpinner roundCount;

    private JLabel lblRoundDuration;

    private JSpinner roundDuration;
    private JPanel buttonPanel;

    /**
     * Construit le menu des options avant une partie
     * @param mainWindow Fenêtre principale
     */
    public GameMenu(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.settings = GameSettings.getInstance();

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 25, 0, 64, 25, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 32, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
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

        lblMaps = new JLabel("Cartes :");
        settings.scaleFont(lblMaps);
        GridBagConstraints gbc_lblCarte = new GridBagConstraints();
        gbc_lblCarte.anchor = GridBagConstraints.WEST;
        gbc_lblCarte.insets = new Insets(0, 0, 5, 5);
        gbc_lblCarte.gridx = 7;
        gbc_lblCarte.gridy = 1;
        add(lblMaps, gbc_lblCarte);

        maps = new JList<String>(getMapList());
        maps.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        settings.scaleFont(maps);

        mapPane = new JScrollPane(maps);
        GridBagConstraints gbc_map = new GridBagConstraints();
        gbc_map.gridheight = 4;
        gbc_map.insets = new Insets(0, 0, 5, 5);
        gbc_map.fill = GridBagConstraints.BOTH;
        gbc_map.gridx = 8;
        gbc_map.gridy = 1;
        add(mapPane, gbc_map);

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
        loadSelectedMaps();

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
        gbc_lblDureeRound.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblDureeRound.insets = new Insets(0, 0, 5, 5);
        gbc_lblDureeRound.gridx = 4;
        gbc_lblDureeRound.gridy = 4;
        add(lblRoundDuration, gbc_lblDureeRound);

        roundDuration = new JSpinner();
        roundDuration.setModel(new SpinnerNumberModel(new Integer(settings.duration), new Integer(0), null, new Integer(1)));
        settings.scaleFont(roundDuration);
        GridBagConstraints gbc_roundDuration = new GridBagConstraints();
        gbc_roundDuration.anchor = GridBagConstraints.NORTH;
        gbc_roundDuration.fill = GridBagConstraints.HORIZONTAL;
        gbc_roundDuration.insets = new Insets(0, 0, 5, 5);
        gbc_roundDuration.gridx = 5;
        gbc_roundDuration.gridy = 4;
        add(roundDuration, gbc_roundDuration);

        buttonPanel = new JPanel();
        GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
        gbc_buttonPanel.gridwidth = 8;
        gbc_buttonPanel.insets = new Insets(0, 0, 5, 5);
        gbc_buttonPanel.gridx = 1;
        gbc_buttonPanel.gridy = 6;
        add(buttonPanel, gbc_buttonPanel);
        buttonPanel.setLayout(new GridLayout(0, 5, 0, 0));

        Component horizontalStrut_1 = Box.createHorizontalStrut(20);
        buttonPanel.add(horizontalStrut_1);

        btnBack = new JButton("Retour");
        settings.scaleFont(btnBack);
        btnBack.addActionListener(this);
        buttonPanel.add(btnBack);

        Component horizontalStrut = Box.createHorizontalStrut(20);
        buttonPanel.add(horizontalStrut);

        btnPlay = new JButton("Jouer");
        settings.scaleFont(btnPlay);
        btnPlay.addActionListener(this);
        buttonPanel.add(btnPlay);

        updateFields();
    }

    /**
     * Met à jour la liste des cartes
     */
    private DefaultListModel<String> getMapList() {
        DefaultListModel<String> mapList = new DefaultListModel<String>();

        File folder = new File(".");
        File[] fileList = folder.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    String filename = file.getName();
                    if (filename.endsWith("." + MapCreatorPanel.MAP_EXTENSION))
                        mapList.addElement(filename.substring(0, filename.length() - 4));
                }
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/maps/list.txt")))) {
            for(String line; (line = br.readLine()) != null; ) {
                if (!line.isEmpty())
                    mapList.addElement(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapList;
    }

    private void loadSelectedMaps() {
        ArrayList<Integer> indices = new ArrayList<>();
        ListModel<String> list = maps.getModel();
        for (int i = 0; i < list.getSize(); i++) {
            if (settings.maps.contains(list.getElementAt(i))) {
                indices.add(i);
            }
        }

        int[] indiceArray = new int[indices.size()];
        for (int i = 0; i < indiceArray.length; i++)
            indiceArray[i] = indices.get(i);

        maps.setSelectedIndices(indiceArray);
    }

    /**
     * Met à jour l'affichage des champs
     */
    private void updateFields() {
        switch ((GameType)gameType.getSelectedItem()) {
        case Client:
            lblIpAddress.setVisible(true);
            ipAddress.setVisible(true);
            lblMaps.setVisible(false);
            mapPane.setVisible(false);
            lblAiCount.setVisible(false);
            aiCount.setVisible(false);
            lblRoundCount.setVisible(false);
            roundCount.setVisible(false);
            lblRoundDuration.setVisible(false);
            roundDuration.setVisible(false);

            {
                GridBagConstraints gbc_buttonPanel = ((GridBagLayout)getLayout()).getConstraints(buttonPanel);
                gbc_buttonPanel.gridwidth = 5;
                ((GridBagLayout)getLayout()).setConstraints(buttonPanel, gbc_buttonPanel);
                ((GridBagLayout)getLayout()).columnWidths = new int[]{0, 0, 0, 25, 0, 64, 25, 0};
                ((GridBagLayout)getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
                revalidate();
                repaint();
            }
            break;
        case Local:
        case Sandbox:
        case Server:
            lblIpAddress.setVisible(false);
            ipAddress.setVisible(false);
            lblMaps.setVisible(true);
            mapPane.setVisible(true);
            lblAiCount.setVisible(true);
            aiCount.setVisible(true);
            lblRoundCount.setVisible(true);
            roundCount.setVisible(true);
            lblRoundDuration.setVisible(true);
            roundDuration.setVisible(true);

            {
                GridBagConstraints gbc_buttonPanel = ((GridBagLayout)getLayout()).getConstraints(buttonPanel);
                gbc_buttonPanel.gridwidth = 8;
                ((GridBagLayout)getLayout()).setConstraints(buttonPanel, gbc_buttonPanel);
                ((GridBagLayout)getLayout()).columnWidths = new int[]{0, 0, 0, 25, 0, 64, 25, 0, 0, 0, 0};
                ((GridBagLayout)getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
                revalidate();
                repaint();
            }
            break;
        default:
            break;
        }
    }

    /**
     * Met à jour les paramètres
     */
    private void updateGameSettings() {
        settings.gameType = (GameType) gameType.getSelectedItem();
        settings.ipAddress = ipAddress.getText();
        settings.maps = maps.getSelectedValuesList();
        settings.playerCount = (int) playerCount.getValue();
        settings.aiCount = (int) aiCount.getValue();
        settings.roundCount = (int) roundCount.getValue();
        settings.duration = (int) roundDuration.getValue();
    }

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
