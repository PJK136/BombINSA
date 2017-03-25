package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JPanel;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JSpinner;
import java.awt.Insets;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class SettingsMenu extends JPanel implements ActionListener {

    @objid ("a39767d1-eca3-41c4-85d7-7d0bde3a14cc")
    private MainWindow mainWindow;

    @objid ("0b5dea46-9866-4b4e-9ccb-f30bb706f360")
    private GameSettings settings;
    
    private JSpinner fps;
    private JSpinner tileSize;
    private JSpinner scale;
    
    private JTextField name0;
    private JTextField name1;
    private JTextField name2;
    private JTextField name3;

    private JButton btnBack;
    private JCheckBox tags;
    
    public SettingsMenu(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.settings = GameSettings.getInstance();
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        
        JLabel lblFps = new JLabel("FPS :");
        settings.scaleFont(lblFps);
        GridBagConstraints gbc_lblFps = new GridBagConstraints();
        gbc_lblFps.anchor = GridBagConstraints.WEST;
        gbc_lblFps.insets = new Insets(0, 0, 5, 5);
        gbc_lblFps.gridx = 1;
        gbc_lblFps.gridy = 2;
        add(lblFps, gbc_lblFps);
        
        fps = new JSpinner();
        settings.scaleFont(fps);
        fps.setModel(new SpinnerNumberModel(new Integer(settings.fps), new Integer(10), null, new Integer(1)));
        GridBagConstraints gbc_fps = new GridBagConstraints();
        gbc_fps.fill = GridBagConstraints.HORIZONTAL;
        gbc_fps.insets = new Insets(0, 0, 5, 5);
        gbc_fps.gridx = 2;
        gbc_fps.gridy = 2;
        add(fps, gbc_fps);
        
        tags = new JCheckBox("Afficher les étiquettes");
        tags.setSelected(settings.tags);
        settings.scaleFont(tags);
        GridBagConstraints gbc_tags = new GridBagConstraints();
        gbc_tags.gridwidth = 2;
        gbc_tags.insets = new Insets(0, 0, 5, 5);
        gbc_tags.gridx = 4;
        gbc_tags.gridy = 2;
        add(tags, gbc_tags);
        
        JLabel lblJoueur = new JLabel("Joueur 1 :");
        settings.scaleFont(lblJoueur);
        GridBagConstraints gbc_lblJoueur = new GridBagConstraints();
        gbc_lblJoueur.anchor = GridBagConstraints.WEST;
        gbc_lblJoueur.insets = new Insets(0, 0, 5, 5);
        gbc_lblJoueur.gridx = 4;
        gbc_lblJoueur.gridy = 3;
        add(lblJoueur, gbc_lblJoueur);
        
        name0 = new JTextField();
        name0.setText(settings.controls.get(0).name);
        settings.scaleFont(name0);
        GridBagConstraints gbc_name0 = new GridBagConstraints();
        gbc_name0.insets = new Insets(0, 0, 5, 5);
        gbc_name0.fill = GridBagConstraints.HORIZONTAL;
        gbc_name0.gridx = 5;
        gbc_name0.gridy = 3;
        add(name0, gbc_name0);
        name0.setColumns(10);
        
        JLabel lblTailleDesTuiles = new JLabel("Taille des tuiles :");
        settings.scaleFont(lblTailleDesTuiles);
        GridBagConstraints gbc_lblTailleDesTuiles = new GridBagConstraints();
        gbc_lblTailleDesTuiles.anchor = GridBagConstraints.WEST;
        gbc_lblTailleDesTuiles.insets = new Insets(0, 0, 5, 5);
        gbc_lblTailleDesTuiles.gridx = 1;
        gbc_lblTailleDesTuiles.gridy = 4;
        add(lblTailleDesTuiles, gbc_lblTailleDesTuiles);
        
        tileSize = new JSpinner();
        settings.scaleFont(tileSize);
        tileSize.setModel(new SpinnerNumberModel(new Integer(settings.tileSize), new Integer(8), null, new Integer(2)));
        GridBagConstraints gbc_tileSize = new GridBagConstraints();
        gbc_tileSize.fill = GridBagConstraints.HORIZONTAL;
        gbc_tileSize.insets = new Insets(0, 0, 5, 5);
        gbc_tileSize.gridx = 2;
        gbc_tileSize.gridy = 4;
        add(tileSize, gbc_tileSize);
        
        JLabel lblJoueur_1 = new JLabel("Joueur 2 :");
        settings.scaleFont(lblJoueur_1);
        GridBagConstraints gbc_lblJoueur_1 = new GridBagConstraints();
        gbc_lblJoueur_1.anchor = GridBagConstraints.WEST;
        gbc_lblJoueur_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblJoueur_1.gridx = 4;
        gbc_lblJoueur_1.gridy = 4;
        add(lblJoueur_1, gbc_lblJoueur_1);
        
        name1 = new JTextField();
        name1.setText(settings.controls.get(1).name);
        settings.scaleFont(name1);
        GridBagConstraints gbc_name1 = new GridBagConstraints();
        gbc_name1.insets = new Insets(0, 0, 5, 5);
        gbc_name1.fill = GridBagConstraints.HORIZONTAL;
        gbc_name1.gridx = 5;
        gbc_name1.gridy = 4;
        add(name1, gbc_name1);
        name1.setColumns(10);
        
        JLabel lblJoueur_2 = new JLabel("Joueur 3 :");
        settings.scaleFont(lblJoueur_2);
        GridBagConstraints gbc_lblJoueur_2 = new GridBagConstraints();
        gbc_lblJoueur_2.anchor = GridBagConstraints.WEST;
        gbc_lblJoueur_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblJoueur_2.gridx = 4;
        gbc_lblJoueur_2.gridy = 5;
        add(lblJoueur_2, gbc_lblJoueur_2);
        
        name2 = new JTextField();
        name2.setText(settings.controls.get(2).name);
        settings.scaleFont(name2);
        GridBagConstraints gbc_name2 = new GridBagConstraints();
        gbc_name2.insets = new Insets(0, 0, 5, 5);
        gbc_name2.fill = GridBagConstraints.HORIZONTAL;
        gbc_name2.gridx = 5;
        gbc_name2.gridy = 5;
        add(name2, gbc_name2);
        name2.setColumns(10);
        
        JLabel lblEchelleInterface = new JLabel("Échelle Interface :");
        settings.scaleFont(lblEchelleInterface);
        GridBagConstraints gbc_lblEchelleInterface = new GridBagConstraints();
        gbc_lblEchelleInterface.insets = new Insets(0, 0, 5, 5);
        gbc_lblEchelleInterface.gridx = 1;
        gbc_lblEchelleInterface.gridy = 6;
        add(lblEchelleInterface, gbc_lblEchelleInterface);
        
        scale = new JSpinner();
        scale.setModel(new SpinnerNumberModel(settings.scale, 0.25, null, 0.5));
        settings.scaleFont(scale);
        GridBagConstraints gbc_scale = new GridBagConstraints();
        gbc_scale.fill = GridBagConstraints.HORIZONTAL;
        gbc_scale.insets = new Insets(0, 0, 5, 5);
        gbc_scale.gridx = 2;
        gbc_scale.gridy = 6;
        add(scale, gbc_scale);
        
        btnBack = new JButton("Retour");
        settings.scaleFont(btnBack);
        btnBack.addActionListener(this);
        
        JLabel lblJoueur_3 = new JLabel("Joueur 4 :");
        settings.scaleFont(lblJoueur_3);
        GridBagConstraints gbc_lblJoueur_3 = new GridBagConstraints();
        gbc_lblJoueur_3.anchor = GridBagConstraints.WEST;
        gbc_lblJoueur_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblJoueur_3.gridx = 4;
        gbc_lblJoueur_3.gridy = 6;
        add(lblJoueur_3, gbc_lblJoueur_3);
        
        name3 = new JTextField();
        name3.setText(settings.controls.get(3).name);
        settings.scaleFont(name3);
        GridBagConstraints gbc_name3 = new GridBagConstraints();
        gbc_name3.insets = new Insets(0, 0, 5, 5);
        gbc_name3.fill = GridBagConstraints.HORIZONTAL;
        gbc_name3.gridx = 5;
        gbc_name3.gridy = 6;
        add(name3, gbc_name3);
        name3.setColumns(10);
        GridBagConstraints gbc_btnBack = new GridBagConstraints();
        gbc_btnBack.gridwidth = 5;
        gbc_btnBack.insets = new Insets(0, 0, 5, 5);
        gbc_btnBack.gridx = 1;
        gbc_btnBack.gridy = 8;
        add(btnBack, gbc_btnBack);
    }

    private void updateGameSettings() {
        settings.fps = (int) fps.getValue();
        settings.tileSize = (int) tileSize.getValue();
        settings.scale = (double) scale.getValue();
        settings.tags = tags.isSelected();
        settings.controls.get(0).name = name0.getText();
        settings.controls.get(1).name = name1.getText();
        settings.controls.get(2).name = name2.getText();
        settings.controls.get(3).name = name3.getText();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            updateGameSettings();
            try {
                settings.save();
            } catch (FileNotFoundException exception) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de sauvegarder la configuration actuelle dans " + GameSettings.SETTINGS_FILENAME + " !",
                        "Erreur lors de la sauvegarde",
                        JOptionPane.ERROR_MESSAGE);
            }
            mainWindow.showMenu();
        }
    }

}
