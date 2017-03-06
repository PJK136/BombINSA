package gui;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingWorker;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import game.Controller;
import game.Direction;
import game.Player;
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

    @objid ("0c0b3418-de2f-49f0-91d2-008a02cea763")
    private GameViewer viewer;
    
    private Timer timer;

    class WakeUpTask extends TimerTask {
        private GameWorker worker;
        public WakeUpTask(GameWorker worker) {
            this.worker = worker;
        }
        
        @Override
        public void run() {
            synchronized (worker) {
                worker.notifyAll();                
            }
        }
    }
    
    @objid ("5510e2b1-78a5-4452-a177-88e5ac8f1590")
    public GameWorker(GameSettings settings, GameViewer viewer) throws Exception {
        this.state = GameState.Init;
        this.settings = settings;
        this.viewer = viewer;
        this.timer = new Timer();
        createWorld();
    }
    
    @objid ("57911ebe-31d3-4df2-b871-111cf25912bf")
    @Override
    protected Integer doInBackground() {
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
                viewer.drawWorld(world);
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
        return null;
    }

    @objid ("b5e7e42e-c73b-4130-bed6-7aa32aa55eb3")
    void createWorld() throws Exception {
        setGameState(GameState.Init);
        if (settings.gameType.equals(GameType.Local)) {
            world = new Server(settings.mapName+".map", settings.tileSize, settings.fps, settings.duration);
            //TODO : Ajout des joueurs etc.
        }
    }

    @objid ("df3a5c13-59cb-491e-811a-ea1af7e23cda")
    void setGameState(GameState state) {
        if (!this.state.equals(state) || this.state.equals(GameState.Init)) {
            GameState oldState = this.state;
            this.state = state;
            firePropertyChange(GameState.class.getName(), oldState, state);
            //TODO : Enlever debug message
            System.err.println(state);
        }
    }
}
