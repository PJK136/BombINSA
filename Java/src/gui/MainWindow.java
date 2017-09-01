package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;


/**
 * Fenêtre princpale
 */
public class MainWindow implements WindowListener {
    private static final int START_WIDTH = 800;

    private static final int START_HEIGHT = 600;

    private JFrame frame;

    private JPanel glassPanel;
    
    private String message;
    
    private Timer messageTimer;

    private GameSettings settings;

    private GameWorker gameWorker;

    private Thread gameWorkerThread;

    private Color messageColor;

    /**
     * Méthode principale main pour lancer le jeu
     * @param args Arguments lors de l'exécution
     */
    public static void main(String[] args) {
        /* Crée et affiche window dans l'Event Dispatch Thread (EDT)
         * car toute modification de la fenêtre doit se faire seulement depuis l'EDT */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Construit la fenêtre principale
     */
    public MainWindow() {
        this.settings = GameSettings.getInstance();
        initialize();
    }

    /**
     * Initialise la fenêtre
     */
    private void initialize() {
        frame = new JFrame();
        frame.setSize(settings.scale(START_WIDTH), settings.scale(START_HEIGHT));
        frame.setIconImage(SpriteFactory.getInstance().getScaledImage("bomb", 256));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);
        
        Font defaultFont = settings.scale((Font)UIManager.get("OptionPane.font"));
        for (String font : new String[]{"OptionPane.font", "OptionPane.messageFont", "OptionPane.buttonFont"}) {
            UIManager.put(font, defaultFont);
        }
        
        glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 127));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(messageColor);
                g.setFont(g.getFont().deriveFont((float)settings.scale(60.)));
                GameViewer.drawCenteredString(g, message, getWidth()/2, getHeight()/2);
            }
        };
        glassPanel.setOpaque(false);
        frame.setGlassPane(glassPanel);
        
        showMenu();
    }

    /**
     * Affiche le menu principal
     */
    public void showMenu() {
        if (gameWorker != null) {
            gameWorker.stop();
            gameWorker = null;
            gameWorkerThread = null;
        }
        
        setPage(new MainMenu(this));
    }

    /**
     * Lance le jeu
     */
    public void startGame() {
        try {
            GamePanel gamePanel = new GamePanel(this);
            gameWorker = new GameWorker(this, gamePanel);
            setPage(gamePanel);
            gameWorkerThread = new Thread(gameWorker);
            gameWorkerThread.start();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.frame,
                                         e.getMessage(),
                                         "Une erreur est survenue...",
                                         JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Lance le créateur de carte
     */
    public void startCreator() {
        setPage(new MapCreatorPanel(this));
    }

    /**
     * Change le contenu de la fenêtre
     * @param page Panel à afficher
     */
    void setPage(JPanel page) {
        setSuffixTitle(null);
        clearMessage();
        frame.setContentPane(page);
        frame.revalidate();
    }

    void setSuffixTitle(String suffix) {
        if (suffix == null || suffix.isEmpty())
            frame.setTitle("BombINSA");
        else
            frame.setTitle("BombINSA - " + suffix);
    }
    
    private void setMessage(String message, Color color) {
        this.message = message;
        this.messageColor = color;
        frame.getGlassPane().setVisible(true);
        frame.repaint();
    }

    /**
     * Affiche un message en semi-transparence avec un fond gris par dessus les autres composants
     * @param message Message à afficher
     * @param color Couleur du message
     */
    void showMessage(String message, Color color) {
        if (messageTimer != null)
            messageTimer.stop();
        
        setMessage(message, color);
    }

    /**
     * Affiche un message en semi-transparence avec un fond gris par dessus les autres composants pendant une durée limitée
     * @param message Message à afficher
     * @param color Couleur du message
     * @param duration Durée avant effacement en millisecondes
     */
    void showMessage(String message, Color color, int duration) {
        if (messageTimer == null) {
            messageTimer = new Timer(duration, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clearMessage();
                }
            });
            messageTimer.setRepeats(false);
        }
        else
            messageTimer.stop();
        
        setMessage(message, color);
        
        messageTimer.setInitialDelay(duration);
        messageTimer.restart();
    }

    /**
     * Enlève le message en semi-transparence s'il y en a un
     */
    void clearMessage() {
        this.message = null;
        frame.getGlassPane().setVisible(false);
    }

    String getMessageShown() {
        return this.message;
    }
    
    void pack() {
        frame.pack();
        frame.revalidate();
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent event) {
        if (frame.getContentPane() instanceof MapCreatorPanel) {
            if (!((MapCreatorPanel)frame.getContentPane()).checkSaved())
                return;
        }
        
        frame.dispose();
        
        if (gameWorker != null) {
            gameWorker.stop();
            gameWorker = null;
            
            try {
                gameWorkerThread.interrupt();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            try {
                gameWorkerThread.join(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
              
            gameWorkerThread = null;
        }
        
        try {
            settings.save();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        System.err.println("Ended !");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    /**
     * Définit la taille de la police du composant
     * @param component Composant à modifier
     * @param size Taille à appliquer
     */
    public static void setFontSize(Component component, float size) {
        component.setFont(component.getFont().deriveFont(size));
    }

}
