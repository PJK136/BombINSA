package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("580889f0-8e1d-4a59-9bfb-00c967a10ffd")
public class MainWindow {
    @objid ("d7d0818d-1589-4942-982c-1a9cbfa0f600")
    private JFrame frame;
    private GameWorker gameWorker;
    
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

    @objid ("3c9a8780-8ade-4f8d-8f7e-7bfa3ca27a66")
    public MainWindow() {
        initialize();
    }

    @objid ("14da88d1-2c75-4382-849c-9c7a54c281ac")
    private void initialize() {
        frame = new JFrame();
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        showMenu();
    }

    @objid ("d0b8245a-6b07-481e-b47e-45ce8cfbffec")
    public void showMenu() {
        if (gameWorker != null) {
            gameWorker.cancel(true);
            gameWorker = null;
        }
        
        setPage(new MainMenu(this));
    }
    
    void setPage(JPanel page) {
        frame.setContentPane(page);
        frame.revalidate();
    }

    @objid ("6b34965b-9d8d-4d4f-ac64-d111b1e847c0")
    public void startGame(GameSettings settings) {
        GamePanel gamePanel = new GamePanel(this);
        try {
            gameWorker = new GameWorker(settings, gamePanel.getGameViewer());
            gameWorker.addPropertyChangeListener(gamePanel);
            gameWorker.execute();
        } catch (Exception e) {
            // TODO Afficher un message d'erreur
            e.printStackTrace();
            System.exit(1);
        }
        setPage(gamePanel);
    }

    @objid ("8898c61c-fe11-44b3-8431-2a93405194ae")
    public void startEditor() {
    }

}
