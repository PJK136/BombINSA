package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


/**
 * Fenêtre princpale
 */
public class MainWindow implements WindowListener {
    private static final int START_WIDTH = 800;

    private static final int START_HEIGHT = 600;

    private JFrame frame;

    private GlassPanel glassPanel;

    private GameSettings settings;

    private GameWorker gameWorker;

    private Thread gameWorkerThread;

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

    private class GlassPanel extends JPanel {
        private String message;

        private Timer messageTimer;

        private Color messageColor;

        private Component target;

        @Override
        protected void paintComponent(Graphics g) {
            if (target == null)
                return;

            setOpaque(false);

            Rectangle dest = SwingUtilities.convertRectangle(target, new Rectangle(0, 0, target.getWidth(), target.getHeight()), this);

            g.setColor(new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 127));
            g.fillRect(dest.x, dest.y, dest.width, dest.height);
            g.setColor(messageColor);
            g.setFont(g.getFont().deriveFont((float)settings.scale(60.)));

            GameViewer.drawCenteredString(g, message, (int) dest.getCenterX(), (int) dest.getCenterY());
        }

        private void setMessage(String message, Color color, Component target) {
            this.message = message;
            this.messageColor = color;
            this.target = target;
            frame.getGlassPane().setVisible(true);
            frame.repaint();
        }

        void showMessage(String message, Color color, Component target) {
            if (glassPanel.messageTimer != null)
                glassPanel.messageTimer.stop();

            setMessage(message, color, target);
        }

        public void showMessage(String message, Color color, int duration, Component target) {
            if (messageTimer == null) {
                messageTimer = new Timer(duration, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        clear();
                    }
                });
                messageTimer.setRepeats(false);
            }
            else
                messageTimer.stop();

            setMessage(message, color, target);

            messageTimer.setInitialDelay(duration);
            messageTimer.restart();
        }

        public void clear() {
            message = null;
            target = null;
            setVisible(false);
        }
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
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(this);

        Font defaultFont = settings.scale((Font)UIManager.get("OptionPane.font"));
        for (String font : new String[]{"OptionPane.font", "OptionPane.messageFont", "OptionPane.buttonFont"}) {
            UIManager.put(font, defaultFont);
        }

        glassPanel = new GlassPanel();
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
                                         e.toString(),
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

    /**
     * Affiche un message en semi-transparence avec un fond gris par dessus les autres composants
     * @param message Message à afficher
     * @param color Couleur du message
     */
    void showMessage(String message, Color color) {
        glassPanel.showMessage(message, color, frame.getContentPane());
    }

    void showMessage(String message, Color color, Component target) {
        glassPanel.showMessage(message, color, target);
    }

    /**
     * Affiche un message en semi-transparence avec un fond gris par dessus les autres composants pendant une durée limitée
     * @param message Message à afficher
     * @param color Couleur du message
     * @param duration Durée avant effacement en millisecondes
     */
    void showMessage(String message, Color color, int duration) {
        glassPanel.showMessage(message, color, duration, frame.getContentPane());
    }

    void showMessage(String message, Color color, int duration, Component target) {
        glassPanel.showMessage(message, color, duration, target);
    }

    /**
     * Enlève le message en semi-transparence s'il y en a un
     */
    void clearMessage() {
        glassPanel.clear();
    }

    String getMessageShown() {
        return this.glassPanel.message;
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
