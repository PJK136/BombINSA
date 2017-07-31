package gui;

import java.awt.Color;
import java.net.InetAddress;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.AIController;
import game.Client;
import game.GameEvent;
import game.GameListener;
import game.Local;
import game.Server;
import game.World;
import game.GameState;

/**
 * Classe qui gère l'exécution du jeu 
 */
@objid ("0352607c-7ee6-4aa1-839f-fc6a174af9fd")
public class GameWorker implements Runnable, GameListener {
    @objid ("261a2da6-2f7b-4394-83a1-d38b8510f2cb")
    private boolean stop;

    @objid ("e6c9ac41-77db-49aa-b718-1530b3eebbcd")
    private GameSettings settings;

    @objid ("60d0171f-8216-483d-bb98-ccb1fb946a53")
    private MainWindow mainWindow;

    @objid ("f996874c-1764-42a2-867d-851bf61a9f40")
    private World world;

    @objid ("26747bae-5549-4449-8fc3-6aaccd6d8bf3")
    private GamePanel panel;

    @objid ("0c0b3418-de2f-49f0-91d2-008a02cea763")
    private GameViewer viewer;

    /**
     * Construit l'objet gérant l'exécution du jeu
     * @param mainWindow Fenêtre principale
     * @param panel Panneau d'affichage du jeu
     * @throws Exception s'il y a une erreur lors du chargement du jeu
     */
    @objid ("5510e2b1-78a5-4452-a177-88e5ac8f1590")
    public GameWorker(MainWindow mainWindow, GamePanel panel) throws Exception {
        this.settings = GameSettings.getInstance();
        this.mainWindow = mainWindow;
        this.panel = panel;
        this.viewer = panel.getGameViewer();
        createWorld();
    }

    @objid ("57911ebe-31d3-4df2-b871-111cf25912bf")
    public void run() {
        try {
            this.stop = false;
            
            while (!stop && !world.isReady()) {
                Thread.sleep(1000/settings.fps, 0);
            }
            
            viewer.drawMap(world.getMap());
            SwingUtilities.invokeAndWait(() -> panel.showGameStatus(world));
            
            final long timeStep = 1000000000/world.getFps();
                                        
            long offset = 0;
            long start = System.nanoTime();
            
            //int frame = 0;
            //long lastDisplay = System.nanoTime();
            
            GameState state;
            
            while (!stop && (state = world.update()) != GameState.End)
            {          
                SwingUtilities.invokeLater(() -> panel.showGameStatus(world));
                viewer.drawWorld(world);
                
                if (world.getWarmupTimeRemaining() > 0) {
                    final String[] messages = new String[]{"Round " + world.getRound(), //Ralongement du temps
                                                           "Round " + world.getRound(), //d'affichage
                                                           "Round " + world.getRound(), //du round
                                                           "À vos marques.",
                                                           "Prêts.",
                                                           "Jouez !"};
                    int i = messages.length*(world.getWarmupDuration()-world.getWarmupTimeRemaining())/world.getWarmupDuration();
                    if (!messages[i].equals(mainWindow.getMessageShown()))
                        SwingUtilities.invokeLater(() -> mainWindow.showMessage(messages[i], Color.black, 1000*world.getWarmupTimeRemaining()/world.getFps()));
                    
                    Audio.getInstance().stop();
                } else if (world.isRoundEnded()) {
                    if (settings.gameType != GameType.Sandbox) {
                        String message = null;
                        Color color = null;
                        
                        String winnerName = world.getWinnerName();
                        
                        if (winnerName != null) {
                            PlayerColor[] colors = PlayerColor.values();
                            message = winnerName + " gagne !";
                            color = colors[world.getWinnerID() % colors.length].toColor();
                        } else if (world.getPlayerAliveCount() > 1) {
                            message = "Les IAs gagnent !";
                            color = Color.black;
                        } else {
                            message = "Égalité !";
                            color = Color.black;
                        }
                        
                        if (!message.equals(mainWindow.getMessageShown())) {
                            final String x = message; 
                            final Color y = color;
                        
                            SwingUtilities.invokeLater(() -> mainWindow.showMessage(x, y, 1000*world.getRestTimeRemaning()/world.getFps()));
                        }
                    }
                }
                
                long duration = System.nanoTime() - start;
                
                if (timeStep-duration-offset > 0) {
                    Thread.sleep((timeStep-duration-offset)/1000000, (int)(timeStep-duration-offset)%1000000);
                }
                
                offset += (System.nanoTime() - start) - timeStep;
                
                start = System.nanoTime();
                
                /*frame++;

                if (System.nanoTime() - lastDisplay >= 1000000000) {
                    System.out.println(frame*(System.nanoTime() - lastDisplay)/1000000000 + " FPS");
                    frame = 0;
                    lastDisplay = System.nanoTime();
                }*/
            }
            
            if (!stop && settings.gameType == GameType.Client && !((Client)world).isConnected())
                SwingUtilities.invokeAndWait(() -> mainWindow.showMessage("Déconnecté.", Color.darkGray, 5000));
            
            Audio.getInstance().stop();
        } catch (InterruptedException e) {
            //Interrompu
        }  catch (Exception e) {
            e.printStackTrace(); //Sinon l'erreur n'est jamais propagée
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(panel,
                                                  e.getMessage(),
                                                  "Une erreur est survenue...",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            });
        } finally {
            world.stop();
            Audio.getInstance().stop();
        }
    }

    
    @Override
    public void gameChanged(GameEvent e) {
        switch (e) {
        case SuddenDeath:
            SwingUtilities.invokeLater(() -> mainWindow.showMessage("Mort subite !", Color.red, 750));
            break;
        default:
            break;
        
        }
    }
    
    /**
     * Met fin au jeu
     */
    @objid ("9f94bd39-ac7e-4e2f-8f19-afc50b48f9ad")
    public void stop() {
        this.stop = true;
    }

    /**
     * Crée une partie
     * @throws Exception s'il y a une erreur lors de la création
     */
    @objid ("b5e7e42e-c73b-4130-bed6-7aa32aa55eb3")
    void createWorld() throws Exception {
        if (settings.gameType.equals(GameType.Local) || settings.gameType.equals(GameType.Sandbox) || settings.gameType.equals(GameType.Server)) {
            
            if (settings.gameType.equals(GameType.Local) || settings.gameType.equals(GameType.Sandbox)) {
                world = new Local(settings.mapName+".map",
                                  settings.tileSize,
                                  settings.fps,
                                  settings.roundCount,
                                  settings.duration*settings.fps,
                                  (int)(settings.warmupDuration*settings.fps),
                                  (int)(settings.restTimeDuration*settings.fps));
            } else {
                world = new Server(settings.mapName+".map",
                                   settings.tileSize,
                                   settings.fps,
                                   settings.roundCount,
                                   settings.duration*settings.fps,
                                   (int)(settings.warmupDuration*settings.fps),
                                   (int)(settings.restTimeDuration*settings.fps));
            }
            
            addKeyboardControllers();
            
            for (int i = 0; i < settings.aiCount; i++) {
                AIController iaController = new AIController();
                world.newController(iaController);
            }
            
            if (settings.gameType.equals(GameType.Local) && world.getPlayerCount() <= 1)
                throw new Exception("Il faut au moins deux joueurs !");
        } else if (settings.gameType.equals(GameType.Client)) {
            if (settings.ipAddress.isEmpty())
                world = new Client();
            else
                world = new Client(InetAddress.getByName(settings.ipAddress));
            addKeyboardControllers();
        } else {
            throw new Exception("Non implémenté !");
        }
        
        world.addGameListener(this);
        world.addGameListener(Audio.getInstance());
    }

    /**
     * Ajoute des controleurs clavier au jeu
     */
    @objid ("9ce6b5e0-2241-473b-aafa-77d986bd3027")
    private void addKeyboardControllers() {
        for (int i = 0; i < Math.min(settings.playerCount, settings.controls.size()); i++) {
            KeyboardController kbController = new KeyboardController(settings.controls.get(i));
            viewer.addKeyListener(kbController);
            world.newController(kbController);
        }
    }
}
