package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.InetAddress;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import game.AIController;
import game.Client;
import game.GameEvent;
import game.GameListener;
import game.GameState;
import game.Local;
import game.Server;
import game.World;

/**
 * Classe qui gère l'exécution du jeu
 */
public class GameWorker implements Runnable, GameListener {
    private boolean stop;

    private GameSettings settings;

    private MainWindow mainWindow;

    private World world;

    private GamePanel panel;

    private GameViewer viewer;

    /**
     * Construit l'objet gérant l'exécution du jeu
     * @param mainWindow Fenêtre principale
     * @param panel Panneau d'affichage du jeu
     * @throws Exception s'il y a une erreur lors du chargement du jeu
     */
    public GameWorker(MainWindow mainWindow, GamePanel panel) throws Exception {
        this.settings = GameSettings.getInstance();
        this.mainWindow = mainWindow;
        this.panel = panel;
        this.viewer = panel.getGameViewer();
        createWorld();
    }

    @Override
    public void run() {
        try {
            this.stop = false;

            while (!stop && !world.isReady()) {
                Thread.sleep(1000/settings.fps, 0);
            }

            final long timeStep = 1000000000/world.getFps();

            long offset = 0;
            long start = System.nanoTime();

            //int frame = 0;
            //long lastDisplay = System.nanoTime();

            boolean adjustWindowSize = true;

            if (world instanceof Server)
                SwingUtilities.invokeLater(() -> panel.setType("Serveur"));
            else if (world instanceof Local)
                SwingUtilities.invokeLater(() -> panel.setType("Local"));
            else if (world instanceof Client)
                SwingUtilities.invokeLater(() -> panel.setType("Client"));

            if (!(world instanceof Client))
                SwingUtilities.invokeLater(() -> panel.setMap(settings.mapName));

            while (!stop && world.update() != GameState.End) {
                SwingUtilities.invokeLater(() -> panel.showGameStatus(world));
                viewer.drawWorld(world);

                if (adjustWindowSize) {
                    SwingUtilities.invokeAndWait(() -> {
                        Insets insets = viewer.getInsets();
                        Dimension preferredSize = new Dimension();
                        preferredSize.width = world.getMap().getColumnCount()*settings.scale(settings.tileSize)+insets.left+insets.right;
                        preferredSize.height = world.getMap().getRowCount()*settings.scale(settings.tileSize)+insets.top+insets.bottom;
                        viewer.setPreferredSize(preferredSize);
                    });

                    if (viewer.getScaleFactor() < 1) {
                        SwingUtilities.invokeAndWait(() -> mainWindow.pack());
                        // Laise le temps aux autres composants de s'actualiser
                        SwingUtilities.invokeAndWait(() -> mainWindow.pack());
                    }

                    adjustWindowSize = false;
                }

                if (world.getWarmupTimeRemaining() > 0) {
                    final String[] messages = new String[]{"Round " + world.getRound(), //Ralongement du temps
                                                           "Round " + world.getRound(), //d'affichage
                                                           "Round " + world.getRound(), //du round
                                                           "À vos marques.",
                                                           "Prêts.",
                                                           "Jouez !"};
                    int i = messages.length*(world.getWarmupDuration()-world.getWarmupTimeRemaining())/world.getWarmupDuration();
                    if (!messages[i].equals(mainWindow.getMessageShown()))
                        showMessage(messages[i], Color.black, 1000*world.getWarmupTimeRemaining()/world.getFps());

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
                        } else if (world.getCharacterCount() > 1) {
                            message = "Les IAs gagnent !";
                            color = Color.black;
                        } else {
                            message = "Égalité !";
                            color = Color.black;
                        }

                        if (!message.equals(mainWindow.getMessageShown())) {
                            final String x = message;
                            final Color y = color;

                            if (world.getRestTimeRemaining() >= 10)
                                showMessage(x, y, 1000*world.getRestTimeRemaining()/world.getFps());
                            else
                                showMessage(x, y, 10);
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
                showMessage("Déconnecté.", Color.darkGray, 5000);

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

    private void showMessage(String message, Color color, int duration) {
        SwingUtilities.invokeLater(() -> mainWindow.showMessage(message, color, duration, viewer));
    }

    @Override
    public void gameChanged(GameEvent e) {
        switch (e) {
        case SuddenDeath:
            showMessage("Mort subite !", Color.red, 750);
            break;
        default:
            break;

        }
    }

    /**
     * Met fin au jeu
     */
    public void stop() {
        this.stop = true;
    }

    /**
     * Crée une partie
     * @throws Exception s'il y a une erreur lors de la création
     */
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
                AIController aiController = new AIController();
                world.newPlayer(aiController);
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
    private void addKeyboardControllers() {
        for (int i = 0; i < Math.min(settings.playerCount, settings.controls.size()); i++) {
            KeyboardController kbController = new KeyboardController(settings.controls.get(i));
            viewer.addKeyListener(kbController);
            world.newPlayer(kbController);
        }
    }
}
