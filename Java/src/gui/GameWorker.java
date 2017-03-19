package gui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import game.AIController;
import game.Server;
import game.World;

@objid ("0352607c-7ee6-4aa1-839f-fc6a174af9fd")
public class GameWorker extends SwingWorker<Integer,Integer> {
    @objid ("48d2d522-c922-4b51-80ca-ea2a20a4e237")
    private GameState state;

    @objid ("f996874c-1764-42a2-867d-851bf61a9f40")
    private World world;

    @objid ("e6c9ac41-77db-49aa-b718-1530b3eebbcd")
    private GameSettings settings;

    private GamePanel panel;
    
    @objid ("0c0b3418-de2f-49f0-91d2-008a02cea763")
    private GameViewer viewer;

    @objid ("5510e2b1-78a5-4452-a177-88e5ac8f1590")
    public GameWorker(GameSettings settings, GamePanel panel) throws Exception {
        this.state = null;
        this.settings = settings;
        this.panel = panel;
        this.viewer = panel.getGameViewer();
        createWorld();
    }

    @objid ("57911ebe-31d3-4df2-b871-111cf25912bf")
    @Override
    protected Integer doInBackground() {
        try {
            viewer.drawWorld(world);
            viewer.requestFocusInWindow();
            panel.showGameStatus(world);
            setGameState(GameState.Init);
            
            int frame = 0;
            long lastDisplay = System.nanoTime();
            final long timeStep = 1000000000/settings.fps; 
            final Runnable updateGamePanel = new Runnable() {
                @Override
                public void run() {
                    panel.showGameStatus(world);
                }
            };
            
            for (int round = 0; round < settings.roundCount && !isCancelled(); round++)
            {
                setGameState(GameState.Playing);
                
                long offset = 0;
                long start = System.nanoTime();
                while (world.getPlayerAliveCount() > 0 && !isCancelled())
                {              
                    world.update();
                    
                    viewer.drawWorld(world);
                    
                    SwingUtilities.invokeLater(updateGamePanel);
                    
                    if (world.getTimeRemaining() == 0)
                        setGameState(GameState.SuddenDeath);
                    
                    long duration = System.nanoTime() - start;
                    
                    if (duration < timeStep) {
                        if (timeStep-duration-offset > 0) {
                            try {
                                Thread.sleep((timeStep-duration-offset)/1000000, (int)(timeStep-duration-offset)%1000000);
                            } catch (InterruptedException e) { }
                        }
                        
                        offset += (System.nanoTime() - start) - timeStep;
                    }
                    
                    start = System.nanoTime();
                    
                    frame++;
                    if (System.nanoTime() - lastDisplay >= 1000000000) {
                        System.out.println(frame + " FPS");
                        frame = 0;
                        lastDisplay = System.nanoTime();
                    }
                }
                setGameState(GameState.EndRound);
                
                if (!isCancelled() && round < settings.roundCount-1) {
                        world.restart();
                }
            }
            setGameState(GameState.End);
        } catch (Exception e) {
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
        }
        
        return null;
    }

    @objid ("b5e7e42e-c73b-4130-bed6-7aa32aa55eb3")
    void createWorld() throws Exception {
        if (settings.gameType.equals(GameType.Local)) {
            world = new Server(settings.mapName+".map", settings.tileSize, settings.fps, settings.duration);
            for (int i = 0; i < Math.min(settings.playerCount, settings.controls.size()); i++) {
                KeyboardController kbController = new KeyboardController(settings.controls.get(i));
                viewer.addKeyListener(kbController);
                world.newController(kbController);
            }
            
            //TODO : Ajout des ias etc.
            for (int i = 0; i < settings.aiCount; i++) {
                AIController iaController = new AIController();
                world.newController(iaController);
            }
        }
        else
            throw new Exception("Non implémenté !");
    }

    @objid ("df3a5c13-59cb-491e-811a-ea1af7e23cda")
    void setGameState(GameState state) {
        GameState oldState = this.state;
        this.state = state;
        firePropertyChange(GameProperty.GameState.name(), oldState, state);
        //TODO : Enlever debug message
        System.err.println(state);
    }
}
