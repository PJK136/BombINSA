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

/**
 * Menu princpal
 */
public class MainMenu extends JPanel implements ActionListener {
    private MainWindow mainWindow;

    private GameSettings settings;

    private JButton btnPlay;

    private JButton btnCreator;

    private JButton btnSettings;

    private JButton btnQuit;

    private JLabel lblWallpaper;

    private Sprite wallPaperSprite;

    /**
     * Construit le menu principal
     * @param mainWindow Fenêtre principale
     */
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

    public void paintComponent(Graphics g) {
        int wallPaperSize = Math.max(lblWallpaper.getHeight(), lblWallpaper.getWidth());
        if (wallPaperSize != wallPaperSprite.getSize()) {
            ImageIcon wallPaper = new ImageIcon(wallPaperSprite.getImage(wallPaperSize));
            lblWallpaper.setIcon(wallPaper);
        }
        super.paintComponent(g);
    }

}
