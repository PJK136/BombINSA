package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("580889f0-8e1d-4a59-9bfb-00c967a10ffd")
public class MainWindow {
    @objid ("b7100b61-40ce-4a3b-bc84-213a61d275d0")
    private String message;

    @objid ("3e3265a5-358a-47b3-a2b4-e7aefe04df03")
    private static final int START_WIDTH = 640;

    @objid ("4bf8129e-77ea-44b3-b213-ea9b537b9eee")
    private static final int START_HEIGHT = 480;

    @objid ("d7d0818d-1589-4942-982c-1a9cbfa0f600")
    private JFrame frame;

    @objid ("3adeebc1-fc0c-491c-ab18-955b04cdfba4")
    private JPanel glassPanel;

    @objid ("7604bbda-b462-466b-b8a4-680fae86a454")
    private GameSettings settings;

    @objid ("294a7f1e-7ead-4bc6-bf80-c970a68ae918")
    private GameWorker gameWorker;

    @objid ("6a4c9e53-d41c-4237-99b1-a540a0004898")
    private Color messageColor;

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
        this.settings = GameSettings.getInstance();
        initialize();
    }

    @objid ("14da88d1-2c75-4382-849c-9c7a54c281ac")
    private void initialize() {
        frame = new JFrame();
        frame.setSize(settings.scale(START_WIDTH), settings.scale(START_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("BombINSA");
        
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

    @objid ("d0b8245a-6b07-481e-b47e-45ce8cfbffec")
    public void showMenu() {
        if (gameWorker != null) {
            gameWorker.stop();
            gameWorker = null;
        }
        
        setPage(new MainMenu(this));
    }

    @objid ("6b34965b-9d8d-4d4f-ac64-d111b1e847c0")
    public void startGame() {
        try {
            GamePanel gamePanel = new GamePanel(this);
            gameWorker = new GameWorker(this, gamePanel);
            setPage(gamePanel);
            new Thread(gameWorker).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.frame,
                                         e.getMessage(),
                                         "Une erreur est survenue...",
                                         JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @objid ("8898c61c-fe11-44b3-8431-2a93405194ae")
    public void startCreator() {
        setPage(new MapCreatorPanel(this));
        pack();
    }

    @objid ("030bbdba-dcc6-4a25-9366-dab889f9d934")
    void setPage(JPanel page) {
        frame.getGlassPane().setVisible(false);
        frame.setContentPane(page);
        frame.revalidate();
    }

    @objid ("c6c9afec-5bba-4a55-ab6c-14848f545c6c")
    void pack() {
        frame.pack();
    }

    @objid ("4219eb02-4dcd-45fd-88cf-b6c685cc8d32")
    void showMessage(String message, Color color) {
        this.message = message;
        this.messageColor = color;
        frame.getGlassPane().setVisible(true);
        frame.repaint();
    }

    @objid ("2b490ea0-a3a0-4f8a-979a-8c20574e3a60")
    void clearMessage() {
        this.message = null;
        frame.getGlassPane().setVisible(false);
    }

    @objid ("d3f6e6d9-3541-4526-8433-d7a080952a55")
    public static void setFontSize(JComponent component, float size) {
        component.setFont(component.getFont().deriveFont((float)size));
    }

}
