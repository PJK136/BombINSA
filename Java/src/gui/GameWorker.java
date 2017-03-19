package gui;

import java.util.Timer;
import java.util.TimerTask;

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

    @objid ("564ac678-305a-4fb5-a045-5d19f13a522f")
    private Timer timer;

    @objid ("5510e2b1-78a5-4452-a177-88e5ac8f1590")
    public GameWorker(GameSettings settings, GamePanel panel) throws Exception {
        this.state = null;
        this.settings = settings;
        this.panel = panel;
        this.viewer = panel.getGameViewer();
        this.timer = new Timer();
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
            timer.scheduleAtFixedRate(new WakeUpTask(this), 0, 1000/settings.fps);
            
            for (int round = 0; round < settings.roundCount && !isCancelled(); round++)
            {
                setGameState(GameState.Playing);
                while (world.getPlayerAliveCount() > 0 && !isCancelled())
                {
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            // TODO Is it okay to do nothing ?
                        }
                    }
                    
                    world.update();
                    
                    if (world.getTimeRemaining() == 0)
                        setGameState(GameState.SuddenDeath);
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        
                        @Override
                        public void run() {
                            panel.showGameStatus(world);
                            viewer.drawWorld(world);
                        }
                    });
                }
                setGameState(GameState.EndRound);
                
                if (!isCancelled() && round < settings.roundCount-1) {
                    try {
                        world.restart();
                    } catch (Exception e) {
                        //TODO : Gérer l'exception
                        e.printStackTrace();
                    }
                }
            }
            setGameState(GameState.End);
            
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace(); //Sinon l'erreur n'est jamais propagée
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

    @objid ("3f14b285-5a29-4704-9368-f0af1d1c9d5c")
    class WakeUpTask extends TimerTask {
        @objid ("8b170840-8de8-4867-b02f-5071f87d2d83")
        private GameWorker worker;

        @objid ("d09dedc5-799f-4bd7-8703-4703009100a1")
        public WakeUpTask(GameWorker worker) {
            this.worker = worker;
        }

        @objid ("f0783eca-04ef-4feb-927f-1987c8ce7474")
        @Override
        public void run() {
            synchronized (worker) {
                worker.notifyAll();                
            }
        }

    }

}
