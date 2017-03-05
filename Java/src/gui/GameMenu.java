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

public class GameMenu extends JPanel implements ActionListener {
    MainWindow mainWindow;
    
    JButton btnPlay;
    JButton btnReturn;
    
    /**
     * Create the panel.
     */
    public GameMenu(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        
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
        
        JComboBox comboBox = new JComboBox();
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.insets = new Insets(0, 0, 5, 5);
        gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBox.gridx = 2;
        gbc_comboBox.gridy = 1;
        add(comboBox, gbc_comboBox);
        
        JLabel lblHumain = new JLabel("Nombre de joueurs :");
        GridBagConstraints gbc_lblHumain = new GridBagConstraints();
        gbc_lblHumain.anchor = GridBagConstraints.WEST;
        gbc_lblHumain.insets = new Insets(0, 0, 5, 5);
        gbc_lblHumain.gridx = 4;
        gbc_lblHumain.gridy = 1;
        add(lblHumain, gbc_lblHumain);
        
        JSpinner spinner = new JSpinner();
        GridBagConstraints gbc_spinner = new GridBagConstraints();
        gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner.insets = new Insets(0, 0, 5, 5);
        gbc_spinner.gridx = 5;
        gbc_spinner.gridy = 1;
        add(spinner, gbc_spinner);
        
        JLabel lblOrdinateur = new JLabel("Nombre d'IA :");
        GridBagConstraints gbc_lblOrdinateur = new GridBagConstraints();
        gbc_lblOrdinateur.anchor = GridBagConstraints.WEST;
        gbc_lblOrdinateur.insets = new Insets(0, 0, 5, 5);
        gbc_lblOrdinateur.gridx = 4;
        gbc_lblOrdinateur.gridy = 2;
        add(lblOrdinateur, gbc_lblOrdinateur);
        
        JSpinner spinner_1 = new JSpinner();
        GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
        gbc_spinner_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_1.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_1.gridx = 5;
        gbc_spinner_1.gridy = 2;
        add(spinner_1, gbc_spinner_1);
        
        JLabel lblCarte = new JLabel("Carte :");
        GridBagConstraints gbc_lblCarte = new GridBagConstraints();
        gbc_lblCarte.anchor = GridBagConstraints.WEST;
        gbc_lblCarte.insets = new Insets(0, 0, 5, 5);
        gbc_lblCarte.gridx = 1;
        gbc_lblCarte.gridy = 3;
        add(lblCarte, gbc_lblCarte);
        
        JComboBox comboBox_1 = new JComboBox();
        GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
        gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
        gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBox_1.gridx = 2;
        gbc_comboBox_1.gridy = 3;
        add(comboBox_1, gbc_comboBox_1);
        
        JLabel lblNombreDeRound = new JLabel("Nombre de round :");
        GridBagConstraints gbc_lblNombreDeRound = new GridBagConstraints();
        gbc_lblNombreDeRound.anchor = GridBagConstraints.WEST;
        gbc_lblNombreDeRound.insets = new Insets(0, 0, 5, 5);
        gbc_lblNombreDeRound.gridx = 4;
        gbc_lblNombreDeRound.gridy = 3;
        add(lblNombreDeRound, gbc_lblNombreDeRound);
        
        JSpinner spinner_2 = new JSpinner();
        GridBagConstraints gbc_spinner_2 = new GridBagConstraints();
        gbc_spinner_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_2.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_2.gridx = 5;
        gbc_spinner_2.gridy = 3;
        add(spinner_2, gbc_spinner_2);
        
        JLabel lblDureeRound = new JLabel("Durée d'un round (s) :");
        GridBagConstraints gbc_lblDureeRound = new GridBagConstraints();
        gbc_lblDureeRound.anchor = GridBagConstraints.WEST;
        gbc_lblDureeRound.insets = new Insets(0, 0, 5, 5);
        gbc_lblDureeRound.gridx = 4;
        gbc_lblDureeRound.gridy = 4;
        add(lblDureeRound, gbc_lblDureeRound);
        
        JSpinner spinner_3 = new JSpinner();
        GridBagConstraints gbc_spinner_3 = new GridBagConstraints();
        gbc_spinner_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_spinner_3.insets = new Insets(0, 0, 5, 5);
        gbc_spinner_3.gridx = 5;
        gbc_spinner_3.gridy = 4;
        add(spinner_3, gbc_spinner_3);
        
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

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnReturn) {
            mainWindow.showMenu();
        }
    }

}
