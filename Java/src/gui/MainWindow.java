package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Fenêtre princpale
 */
@objid ("580889f0-8e1d-4a59-9bfb-00c967a10ffd")
public class MainWindow implements WindowListener {
    @objid ("3e3265a5-358a-47b3-a2b4-e7aefe04df03")
    private static final int START_WIDTH = 640;

    @objid ("4bf8129e-77ea-44b3-b213-ea9b537b9eee")
    private static final int START_HEIGHT = 480;

    @objid ("d7d0818d-1589-4942-982c-1a9cbfa0f600")
    private JFrame frame;

    @objid ("3adeebc1-fc0c-491c-ab18-955b04cdfba4")
    private JPanel glassPanel;
    
    @objid ("b7100b61-40ce-4a3b-bc84-213a61d275d0")
    private String message;
    
    private Timer messageTimer;

    @objid ("7604bbda-b462-466b-b8a4-680fae86a454")
    private GameSettings settings;

    @objid ("6f9005ce-2f9d-4803-8554-287fcdcbf2f2")
    private GameWorker gameWorker;

    @objid ("35798f9f-9fd3-4385-9f3f-509b51d00a92")
    private Thread gameWorkerThread;

    @objid ("6a4c9e53-d41c-4237-99b1-a540a0004898")
    private Color messageColor;

    /**
     * Méthode principale main pour lancer le jeu
     * @param args Arguments lors de l'exécution
     */
    @objid ("c0396266-731f-4131-9df4-3e1a8c55ed3a")
    public static void main(String[] args) {
        /* Crée et affiche window dans l'Event Dispatch Thread (EDT)
         * car toute modification de la fenêtre doit se faire seulement depuis l'EDT */
        EventQueue.invokeLater(new Runnable() {
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
    @objid ("3c9a8780-8ade-4f8d-8f7e-7bfa3ca27a66")
    public MainWindow() {
        this.settings = GameSettings.getInstance();
        initialize();
    }

    /**
     * Initialise la fenêtre
     */
    @objid ("14da88d1-2c75-4382-849c-9c7a54c281ac")
    private void initialize() {
        frame = new JFrame();
        frame.setSize(settings.scale(START_WIDTH), settings.scale(START_HEIGHT));
        frame.setTitle("BombINSA");
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
    @objid ("d0b8245a-6b07-481e-b47e-45ce8cfbffec")
    public void showMenu() {
        if (gameWorker != null) {
            gameWorker.stop();
            gameWorker = null;
        }
        
        setPage(new MainMenu(this));
    }

    /**
     * Lance le jeu
     */
    @objid ("6b34965b-9d8d-4d4f-ac64-d111b1e847c0")
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
    @objid ("8898c61c-fe11-44b3-8431-2a93405194ae")
    public void startCreator() {
        setPage(new MapCreatorPanel(this));
        pack();
    }

    /**
     * Change le contenu de la fenêtre
     * @param page Panel à afficher
     */
    @objid ("030bbdba-dcc6-4a25-9366-dab889f9d934")
    void setPage(JPanel page) {
        clearMessage();
        frame.setContentPane(page);
        frame.revalidate();
    }

    /**
     * Redimensionne la fenêtre de manière plus compacte
     */
    @objid ("c6c9afec-5bba-4a55-ab6c-14848f545c6c")
    void pack() {
        frame.pack();
    }

    /**
     * Affiche un message en semi-transparence avec un fond gris par dessus les autres composants
     * @param message Message à afficher
     * @param color Couleur du message
     */
    @objid ("4219eb02-4dcd-45fd-88cf-b6c685cc8d32")
    void showMessage(String message, Color color) {
        this.message = message;
        this.messageColor = color;
        frame.getGlassPane().setVisible(true);
        frame.repaint();
    }
    
    /**
     * Affiche un message en semi-transparence avec un fond gris par dessus les autres composants
     * @param message Message à afficher
     * @param color Couleur du message
     * @param duration Durée avant effacement en millisecondes
     */
    @objid ("4219eb02-4dcd-45fd-88cf-b6c685cc8d32")
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
        
        showMessage(message, color);
        
        messageTimer.setInitialDelay(duration);
        messageTimer.restart();
    }

    /**
     * Enlève le message en semi-transparence s'il y en a un
     */
    @objid ("2b490ea0-a3a0-4f8a-979a-8c20574e3a60")
    void clearMessage() {
        this.message = null;
        frame.getGlassPane().setVisible(false);
    }

    @objid ("5770124d-d120-44f6-bf06-0f01379bd988")
    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("1ddf2152-393e-4cd1-9eb4-f478156cc0f6")
    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("a7b122f4-7115-4297-aeb1-d0f61ea62443")
    @Override
    public void windowClosing(WindowEvent event) {
        if (frame.getContentPane() instanceof MapCreatorPanel) {
            if (!((MapCreatorPanel)frame.getContentPane()).checkSaved())
                return;
        }
        
        frame.dispose();
        
        if (gameWorker != null) {
            gameWorker.stop();
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
        }
        
        try {
            settings.save();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @objid ("4e07e866-0b1f-4473-b006-464d93ac852c")
    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("9fb421e2-3933-4a4f-a5ad-1d03117db053")
    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("57f37abd-2147-47a4-9bf8-afa4a2e9cf40")
    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @objid ("f39abcb1-4310-4e33-a286-4a9c79a80356")
    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    /**
     * Définit la taille de la police du composant
     * @param component Composant à modifier
     * @param size Taille à appliquer
     */
    @objid ("d3f6e6d9-3541-4526-8433-d7a080952a55")
    public static void setFontSize(JComponent component, float size) {
        component.setFont(component.getFont().deriveFont((float)size));
    }

}
