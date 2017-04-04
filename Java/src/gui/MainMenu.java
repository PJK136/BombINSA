package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Menu princpal
 */
@objid ("e22186a6-1bcc-48ec-a2ac-a88a3eb90491")
public class MainMenu extends JPanel implements ActionListener {
    @objid ("cb8bf489-916e-4b78-a5d9-44be8855f17f")
    private MainWindow mainWindow;

    @objid ("c17a27fb-24d3-4873-b8e5-86da947b8163")
    private GameSettings settings;

    @objid ("b2c4e4e8-bfe6-429e-a138-b575a0165129")
    private JButton btnPlay;

    @objid ("a8f404a7-a099-4fdc-b955-5012e1502ee4")
    private JButton btnCreator;

    @objid ("dbdc7038-7251-48fc-9b2a-13aa604faf8c")
    private JButton btnSettings;

    @objid ("954b1ba0-6aed-4f56-a7c5-e04330ffdfdf")
    private JButton btnQuit;

    @objid ("9de52b5f-cf36-4616-b764-492e8c9688ce")
    private JLabel lblWallpaper;

    @objid ("2696f807-a667-4fc1-a456-5229bf28e27a")
    private Sprite wallPaperSprite;

    /**
     * Construit le menu principal
     * @param mainWindow Fenêtre principale
     */
    @objid ("a31a2175-f166-434f-b6fd-96a25b344584")
    public MainMenu(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.settings = GameSettings.getInstance();
        
        setLayout(new BorderLayout(0,0));
        
        lblWallpaper = new JLabel();
        wallPaperSprite = SpriteFactory.getInstance().getSprite("wallpaper");
        ImageIcon wallPaper = new ImageIcon(wallPaperSprite.getImage());
        lblWallpaper.setIcon(wallPaper);
        add(lblWallpaper, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        
        buttonPanel.add(Box.createVerticalGlue());
        
        JLabel lblBombinsa = new JLabel("BombINSA");
        lblBombinsa.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBombinsa.setFont(new Font("Dialog", Font.BOLD, settings.scale(36)));
        buttonPanel.add(lblBombinsa);
        
        buttonPanel.add(Box.createVerticalStrut(settings.scale(40)));
        
        btnPlay = addButton("Jouer",buttonPanel);
        btnCreator = addButton("Créateur de carte",buttonPanel);
        btnSettings = addButton("Paramètres",buttonPanel);
        btnQuit = addButton("Quitter",buttonPanel);
        
        buttonPanel.add(Box.createVerticalGlue());
        
        JLabel credits = new JLabel("<html><div style=\"text-align: center;\">© 2017 Paul Du, Neil Borthwick,<br/>"
                                    + "Maxime Lutz et Bastien Jeannin</div></html>");
        settings.scaleFont(credits);
        credits.setHorizontalAlignment(SwingConstants.CENTER); //Texte par rapport au JLabel
        credits.setAlignmentX(Component.CENTER_ALIGNMENT); //JLabel par rapport au layout
        buttonPanel.add(credits);
        
        buttonPanel.setPreferredSize(new Dimension(settings.scale(250), buttonPanel.getPreferredSize().height));
        
        add(buttonPanel, BorderLayout.EAST);
    }

    /**
     * Crée un bouton dans un panneau vertical
     * @param text Texte du bouton
     * @param panel Panneau vertical
     * @return Bouton créé et ajouté
     */
    @objid ("07a869f1-82e5-45c3-9ad9-a445c4aa7dde")
    private JButton addButton(String text, JPanel panel) {
        JButton button = new JButton(text);
        settings.scaleFont(button);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(settings.scale(160), button.getMaximumSize().height));
        button.addActionListener(this);
        panel.add(button);
        panel.add(Box.createVerticalStrut(settings.scale(20)));
        return button;
    }

    @objid ("8346e6ce-7734-4a0c-a6b2-83edb22b96c5")
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnPlay) {
            mainWindow.setPage(new GameMenu(mainWindow));
        }
        else if (event.getSource() == btnCreator) {
            mainWindow.startCreator();
        }
        else if (event.getSource() == btnSettings) {
            mainWindow.setPage(new SettingsMenu(mainWindow));
        }
        else if (event.getSource() == btnQuit) {
            System.exit(0);
        }
    }

    @objid ("333b8411-a0d3-48f9-823c-0cbc65efcc74")
    public void paintComponent(Graphics g) {
        int wallPaperSize = Math.max(lblWallpaper.getHeight(), lblWallpaper.getWidth());
        if (wallPaperSize != wallPaperSprite.getSize()) {
            ImageIcon wallPaper = new ImageIcon(wallPaperSprite.getImage(wallPaperSize));
            lblWallpaper.setIcon(wallPaper);
        }
        super.paintComponent(g);
    }

}
