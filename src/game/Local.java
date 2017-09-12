package game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;


/** Cette classe gère une partie de type Local */
public class Local extends World {
     List<String> maps;

     int actualMap = 0;

     Random random = new Random();

     public static final int SUDDEN_DEATH_DURATION = 60; //s

     HashMap<Bomb, Direction> queueKickBomb = new HashMap<>();

     Queue<Character> queueCharacter = new LinkedList<Character>();

     Queue<Bomb> queueBomb = new LinkedList<Bomb>();

     Queue<GridCoordinates> queueBonus = new LinkedList<GridCoordinates>();

    /**
     * Construit une partie de jeu en local
     * @param mapFilename Nom du ficher de la carte
     * @param tileSize Taille des tuiles
     * @param fps Nombre d'images par secondes
     * @param duration Durée d'un round en images
     * @param warmup Durée du temps d'échauffement en images
     * @param restTime Durée entre deux rounds en images
     * @throws java.lang.Exception Erreur liée au chargement de la carte
     */
    public Local(List<String> maps, int tileSize, int fps, int roundMax, int duration, int warmup, int restTime) throws Exception {
        this.maps = new ArrayList<>(maps);
        createMap(tileSize);
        loadMap(0);
        setFps(fps);
        setRoundMax(roundMax);
        setDuration(duration);
        setWarmupDuration(warmup);
        setRestTimeDuration(restTime);
        newRound();
    }

    /**
     * Crée une carte
     * @param tileSize taille des tuiles de la carte
     */
    void createMap(int tileSize) {
        map = new Map(tileSize);
    }

    /**
     * Charge une carte parmi la liste donnée
     * @param index Position de la carte dans la liste
     * @throws java.lang.Exception Erreur liée au chargement de la carte
     */
    public void loadMap(int index) throws Exception {
        String filename = maps.get(index) + ".map";

        try {
            map.loadMap(Paths.get(filename));
        } catch (IOException e) {
            InputStream input = getClass().getResourceAsStream("/maps/"+filename);
            if (input != null)
                map.loadMap(input);
            else
                throw new Exception("Impossible de charger " + filename);
        }

        map.setName(maps.get(index));
    }

    @Override
    public Player newPlayer(Controller controller) {
        return newPlayer(controller, players.size()); // Aucun joueur ne peut être enlevé
    }

    Player newPlayer(Controller controller, int playerID) {
        Player player = new Player(playerID, controller);
        players.put(playerID, player);
        newCharacter(player);
        return player;
    }

    /**
     * Crée un personnage à partir du joueur
     * @param player le joueur
     */
    void newCharacter(Player player) {
        if (warmupTimeRemaining <= 0 && (isRoundEnded() || timeRemaining < 0)) //N'ajoute pas de joueur si le jeu est fini ou en mort subite
            return;

        if (map.spawningLocations.size() == 0)
            return;

        int spawnIndex = getCharacterCount() % map.spawningLocations.size();
        Character character = new Character(this,
                                   map.toCenterX(map.spawningLocations.get(spawnIndex)),
                                   map.toCenterY(map.spawningLocations.get(spawnIndex)),
                                   START_LIVES,
                                   START_BOMB_MAX,
                                   START_RANGE,
                                   (int)(START_INVULNERABITY_DURATION*fps),
                                   player);

        player.setCharacter(character);
        addEntity(character);
    }

    /**
     * Met à jour la partie locale en faisant les actions supplémentaires suivantes :
     * - fait apparaitre des bombes aléatoirement et rend les joueurs "fragiles" en cas de mort subite
     * - crée toutes les bombes que les joueurs plantent
     * - supprime tous les bonus qui ont été ramassés
     * - fait exploser toutes les bombes qui doivent exploser
     * - déplace toutes les bombes qui ont étés poussées
     */
    @Override
    public GameState update() {
        if (warmupTimeRemaining > 0) {
            return super.update();
        }

        GameState state = super.update();

        if (state == GameState.Playing || state == GameState.SuddenDeath) {
            //sudden death case
            if (timeRemaining == 0) {
                for (Entity entity : entities.values()) {
                    if (entity.toRemove)
                        continue;

                    if (entity instanceof Character) {
                        ((Character)entity).setLives(1);
                        ((Character)entity).removeShield();
                    }
                }
            }

            if(state == GameState.SuddenDeath) {
                if (suddenDeathType == null) {
                    SuddenDeathType[] types = SuddenDeathType.values();
                    suddenDeathType = types[random.nextInt(types.length)];
                }

                if (suddenDeathType == SuddenDeathType.BOMBS) {
                    int interval = (SUDDEN_DEATH_DURATION*fps+timeRemaining)/120;
                    if (interval <= 0 || timeRemaining % interval == 0) {
                        boolean bombPlanted = false;
                        while(!bombPlanted){
                            double x = Math.random()*map.getWidth();
                            double y = Math.random()*map.getHeight();
                            if(!map.isCollidable(x, y)) {
                                addEntity(new Bomb(this, map.toCenterX(x), map.toCenterY(y), 4,(int)(TIME_BEFORE_EXPLOSION*fps)));
                                bombPlanted = true;
                            }
                        }
                    }
                } else if (suddenDeathType == SuddenDeathType.WALLS) {
                    updateSuddenDeathWalls();
                }
            }

            //update of the new bombs
            for(Character character : queueCharacter){
                addEntity(new Bomb(this, character, (int)(TIME_BEFORE_EXPLOSION*fps)));
            }

            //update of the bonus
            for(GridCoordinates bonus : queueBonus){
                map.setTileType(TileType.Empty,bonus);
            }

            //update of the bombs explosions
            for(Bomb bomb : queueBomb){
                //locate center of the bomb impact
                GridCoordinates bombGC = map.toGridCoordinates(bomb.getX(), bomb.getY());
                GridCoordinates explosionGC = new GridCoordinates(bombGC);

                if (map.isExplodable(explosionGC)) {
                    map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Center, null, explosionGC);

                    for (Direction direction : Direction.values()) {
                        explosionGC = bombGC;
                        GridCoordinates nextGC = bombGC.neighbor(direction);
                        boolean hasCollided = false;
                        while (GridCoordinates.distance(bombGC, nextGC) <= bomb.getRange() &&
                                map.isInsideMap(nextGC) && !hasCollided && map.isExplodable(nextGC)) {
                            explosionGC = nextGC;
                            map.setExplosion((int)(EXPLOSION_DURATION*fps), ExplosionType.Branch, direction, explosionGC);
                            hasCollided = map.isCollidable(explosionGC) || map.hasBomb(explosionGC);
                            nextGC = explosionGC.neighbor(direction);
                        }
                        map.setExplosionEnd(explosionGC);
                    }
                }
            }

            //Update kicks
            for (Entry<Bomb, Direction> entry : queueKickBomb.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getKey().setDirection(entry.getValue());
                    entry.getKey().setSpeed(Bomb.BOMB_DEFAULT_SPEED*map.getTileSize()/getFps());
                }
            }

            if(!queueBomb.isEmpty()){
                fireEvent(GameEvent.Explosion);
            }
            if(!queueBonus.isEmpty()){
                fireEvent(GameEvent.PickUp);
            }

            //clearing all the queue lists
            queueCharacter.clear();
            queueBomb.clear();
            queueBonus.clear();
            queueKickBomb.clear();
        }

        return state;
    }

    @Override
    void newRound() {
        super.newRound();

        queueCharacter.clear();
        queueBomb.clear();
        queueBonus.clear();
        queueKickBomb.clear();
    }

    @Override
    public void nextRound() {
        super.nextRound();

        //reload map
        try {
            actualMap = (actualMap+1) % maps.size();
            loadMap(actualMap);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        //renew players
        List<Player> playerList = getPlayers();
        Collections.shuffle(playerList);
        for (Player player : playerList) {
            newCharacter(player);
        }
    }

    @Override
    void plantBomb(Character character) {
        queueCharacter.add(character);
    }

    @Override
    void createExplosion(Bomb bomb) {
        queueBomb.add(bomb);
    }

    @Override
    void pickUpBonus(double x, double y) {
        queueBonus.add(map.toGridCoordinates(x,y));
    }

    @Override
    void kickBomb(Bomb bomb, Direction direction) {
        if (queueKickBomb.containsKey(bomb)) {
            //Si plusieurs joueurs veulent pousser la bombe mais pas dans le même sens
            if (queueKickBomb.get(bomb) != direction)
                queueKickBomb.put(bomb, null);
        } else
            queueKickBomb.put(bomb, direction);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public boolean isRoundEnded() {
        if (getPlayerCount() == 1) {
            return getCharacterCount() <= 0;
        } else if (getCharacterCount() <= 1) {
            return true;
        } else if (getHumanCount() >= 1) {
            return getHumanAliveCount() <= 0;
        }
        return false;
    }

    @Override
    public String getWinnerName() {
        if (getCharacterCount() == 0 || getCharacterCount() > 1)
            return null;
        else {
            return getCharacters().get(0).getController().getName();
        }
    }

    @Override
    public int getWinnerID() {
        if (getCharacterCount() == 0 || getCharacterCount() > 1)
            return -1;
        else
            return getCharacters().get(0).getPlayer().getID();
    }


    public void updateSuddenDeathWalls() {
        final int interval = (SUDDEN_DEATH_DURATION*fps)/(map.getColumnCount()*map.getRowCount());
        if (timeRemaining % interval != 0)
            return;

        for (GridCoordinates start = new GridCoordinates();
                start.x != map.getColumnCount()-start.x && start.y != map.getRowCount()-start.y;
                start.x++, start.y++) {
            GridCoordinates gc = new GridCoordinates(start);

            for (; gc.x < map.getColumnCount()-start.x; gc.x++) {
                if (makeWall(gc))
                    return;
            }
            gc.x--;

            for (; gc.y < map.getRowCount()-start.y; gc.y++) {
                if (makeWall(gc))
                    return;
            }
            gc.y--;

            for (; gc.x >= start.x; gc.x--) {
                if (makeWall(gc))
                    return;
            }
            gc.x++;

            for (; gc.y > start.y; gc.y--) {
                if (makeWall(gc))
                    return;
            }
        }
    }


    private boolean makeWall(GridCoordinates gc) {
        if (map.getTileType(gc) == TileType.Unbreakable)
            return false;

        for (Entity entity : map.getEntities(gc)) {
            if (entity instanceof Character)
                ((Character) entity).decreaseLives(((Character) entity).getLives());

            entity.remove();
        }

        map.setTileType(TileType.Unbreakable, gc);
        return true;
    }

}
