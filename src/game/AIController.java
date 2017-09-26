package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gui.PlayerColor;


/** Ce contrôleur est l'intelligence artificielle du jeu */
public class AIController extends Controller {

    public enum Level {
        DUMMY("Idiot", 0, 0, 0., 0),
        PASSIVE("Passif", 5, 0, 0., 0),
        EASY("Facile", 2, 2, 1., 0),
        MEDIUM("Moyen", 4, 4, 2., 1),
        HARD("Difficile", 6, 6, 3., 2),
        INSANE("Démentiel", 10, 10, 5., 2);

        private final String name;
        private final int movePrediction;
        private final int bombPrediction;
        private final double bombAggressivity;
        private final int smartPriorities;

        private Level(String name, int movePrediction, int bombPrediction,
                double bombAggressivity, int smartPriorities) {
            this.name = name;
            this.movePrediction = movePrediction;
            this.bombPrediction = bombPrediction;
            this.bombAggressivity = bombAggressivity;
            this.smartPriorities = smartPriorities;
        }

        public int getMovePrediction() {
            return movePrediction;
        }

        public int getBombPrediction() {
            return bombPrediction;
        }

        public double getBombAggressivity() {
            return bombAggressivity;
        }

        private int getSmartPriorities() {
            return smartPriorities;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private WorldView world;

    private Direction currentDirection = null;

    private int timeElapsedSinceLastShuffle = 0;

    private boolean bombingSimulation = false;

    private boolean bombing = false;

    private GridCoordinates aiPosition;

    private ArrayList<Direction> directions;

    private Level level;

    /**
     * Construit une intelligence artificielle
     */
    public AIController(Level level) {
        this.level = level;
        setName("IA");
        directions = new ArrayList<Direction>(Arrays.asList(Direction.values()));
        Collections.shuffle(directions);
    }

    @Override
    public void setCharacter(Character value) {
    	super.setCharacter(value);
		currentDirection = null;
		bombing = false;
		bombingSimulation = false;
		if (value.getPlayer() != null) {
		    PlayerColor[] colors = PlayerColor.values();
		    setName("IA " + colors[value.getPlayer().getID() % colors.length]);
		}
    }

    @Override
    public Direction getDirection() {
        if (!bombing) //Avance que si elle ne pose pas de bombe
            return currentDirection;
        else
            return null;
    }

    @Override
    public boolean isPlantingBomb() {
        if (bombing) {
            bombing = false;
            return true;
        } else {
            return false;
        }
    }

    private GridCoordinates nextPosition(Direction direction) {
        double x = character.getX();
        double y = character.getY();

        switch (direction) {
        case Left:
            x -= character.getMaxSpeed()+(world.getMap().getTileSize()/2.);
            break;
        case Right:
            x += character.getMaxSpeed()+(world.getMap().getTileSize()/2.);
            break;
        case Up:
            y -= character.getMaxSpeed()+(world.getMap().getTileSize()/2.);
            break;
        case Down:
            y += character.getMaxSpeed()+(world.getMap().getTileSize()/2.);
            break;
        default:
            break;
        }

        return world.getMap().toGridCoordinates(x, y);
    }

    /**
     * Met à jour l'intelligence artificielle
     */
    @Override
    public void update() {
        world = character.getWorldView();
        aiPosition = world.getMap().toGridCoordinates(character.getX(), character.getY());

        if (isReadyToBomb()) {
            bombing = true;
            return;
        }

        if (currentDirection == null)
            setCurrentDirection(character.getDirection());

        GridCoordinates nextPosition = nextPosition(currentDirection);

        timeElapsedSinceLastShuffle++;
        if (timeElapsedSinceLastShuffle >= 0.5*world.getFps()) {
            if (Math.random() < 0.5/world.getFps())
                shuffleDirections();
        }

        if (character.isColliding(currentDirection, character.getMaxSpeed())) {
            shuffleDirections();
            if (!turnSafely(level.getMovePrediction()))
                turnRandomly();
        } else if (!aiPosition.equals(nextPosition)) {
            turnSafely(level.getMovePrediction());
        }
    }

    private void shuffleDirections() {
        if (isSafe(aiPosition)) {
            Collections.shuffle(directions);
            timeElapsedSinceLastShuffle = 0;
            updateDirectionList();
        }
    }

    private void setCurrentDirection(Direction direction) {
        currentDirection = direction;
        updateDirectionList();
    }

    private void updateDirectionList() {
        if (currentDirection != null && !bombingSimulation) {
            Direction opposite = Direction.getOpposite(currentDirection);
            if (directions.get(directions.size()-1) != opposite) {
                directions.remove(opposite);
                directions.add(opposite);
            }
        }
    }

    private boolean isSafeToStand() {
        if (isSafe(aiPosition)) {
            if (world.getMap().getTileType(aiPosition) != TileType.Frozen ||
                    character.getSpeed() == 0.)
                return true;
        }

        return false;
    }

    private boolean turnSafely(int maxStep) {
        return turnSafely(maxStep, -1, null);
    }

    private boolean turnSafely(int maxStep, Direction exclude) {
        return turnSafely(maxStep, -1, exclude);
    }


    private boolean turnSafely(int maxStep, int minSafe) {
        return turnSafely(maxStep, minSafe, null);
    }

    private boolean turnSafely(int maxStep, int minSafe, Direction exclude) {
        if (maxStep <= 0)
            return false;

        DirectionInfo safest = null;
        Direction direction = null;

        for (Direction nextDirection : directions) {
            if (character.isColliding(nextDirection, character.getMaxSpeed()))
                continue;

            if (nextDirection == exclude)
                continue;

            DirectionInfo next = isDirectionSafe(aiPosition, nextDirection, maxStep);
            if (next.compareTo(safest, getPriorities()) > 0) {
                safest = next;
                direction = nextDirection;
            }
        }

        if ((safest == null || safest.safe <= 0 || safest.step > 1 || safest.badBonus > 0) && isSafeToStand()) {
            setCurrentDirection(null);
            return true;
        } else if (safest != null && safest.safe >= minSafe) {
            setCurrentDirection(direction);
            return true;
        }
        else
            return false;
    }

    private void turnRandomly() {
        for (Direction direction : directions) {
            if (currentDirection != direction && !character.isColliding(direction, character.getMaxSpeed())) {
                setCurrentDirection(direction);
                return;
            }
        }
    }

    private int getThreateningBombTimeRemaining(GridCoordinates target) {
        int timeRemaining = -1;
        GridCoordinates temp = new GridCoordinates(target);

        for(Direction dir : Direction.values()){
            temp = target; // Pas besoin de copier car neighbor donne une autre instance
            while(!world.getMap().isCollidable(temp)){
                Bomb bomb = world.getMap().getFirstBomb(temp);

                if(bomb != null && GridCoordinates.distance(target,temp) <= bomb.getRange()) {
                    if (timeRemaining < 0 || bomb.getTimeRemaining() < timeRemaining)
                        timeRemaining = bomb.getTimeRemaining();
                }

                if (bombingSimulation && temp.equals(aiPosition)) {
                    if (GridCoordinates.distance(target, aiPosition) <= character.getRange()) {
                        if (timeRemaining == -1)
                            timeRemaining = (int)World.TIME_BEFORE_EXPLOSION*world.getFps();
                        else
                            timeRemaining = Math.min((int)World.TIME_BEFORE_EXPLOSION*world.getFps(), timeRemaining);
                    }
                }

                temp = temp.neighbor(dir);
            }
        }

        return timeRemaining;
    }

    /**
     * Vérifie la dangerosité d'une tuile
     * @param target La tuile à vérifier
     * @return true Si la tuile est sécurisée, false sinon
     */
    private boolean isSafe(GridCoordinates target) {
        if (world.getMap().isCollidable(target))
            return true;

        if(world.getMap().isExploding(target)){
            return false;
        }

        Bomb bomb = null;
        GridCoordinates temp = new GridCoordinates(target);

        for(Direction dir : Direction.values()){
            temp = target; // Pas besoin de copier car neighbor donne une autre instance
            while(!world.getMap().isCollidable(temp)){
                bomb = world.getMap().getFirstBomb(temp);

                if(bomb != null && GridCoordinates.distance(target,temp) <= bomb.getRange())
                    return false;

                if (bombingSimulation && temp.equals(aiPosition)) {
                    if (GridCoordinates.distance(target, aiPosition) <= character.getRange())
                        return false;
                }

                temp = temp.neighbor(dir);
            }
        }

        return true;
    }

    /**
     * Vérifie si une tuile est vide
     * @param gc les coordonnées de grille de la tuile
     * @return true si oui, false sinon
     */
    private boolean isEmpty(GridCoordinates gc) {
        return !world.getMap().isCollidable(gc) && !world.getMap().hasBomb(gc);
    }

    /**
     * Vérifie si une tuile est vide et sécurisée
     * @param gc les coordonnées de grille de la tuile
     * @return true si oui, false sinon
     */
    private boolean isEmptyAndSafe(GridCoordinates gc) {
        return isEmpty(gc) && isSafe(gc);
    }

    private boolean isBadBonus(GridCoordinates gc) {
        if (!world.getMap().isInsideMap(gc) || world.getMap().getTileType(gc) != TileType.Bonus)
            return false;

        return !world.getMap().getBonusType(gc).isGood();
    }

    private boolean isGoodBonus(GridCoordinates gc) {
        if (!world.getMap().isInsideMap(gc) || world.getMap().getTileType(gc) != TileType.Bonus)
            return false;

        return world.getMap().getBonusType(gc).isGood();
    }

    private boolean hasTarget(int range) {
        boolean destroyGoodBonus = false;
        boolean destroyBreakable = false;
        boolean hitCharacter = false;

        for (Direction direction : Direction.values()) {
        	GridCoordinates position = aiPosition;
            for (int i = 0; i < range; i++) {
            	position = position.neighbor(direction);
                if (!world.getMap().isInsideMap(position))
                	break;
                else if (world.getMap().getTileType(position) == TileType.Breakable) {
                    destroyBreakable = true;
                    break;
                }
                else if (!isEmpty(position))
                    break;
                else if (isGoodBonus(position))
                	destroyGoodBonus = true;
                else {
                	List<Entity> entities = world.getMap().getEntities(position);
                	for (Entity entity : entities) {
                		if (entity instanceof Character && entity != character)
                			return true;
                	}
                }
            }
        }

        if (hitCharacter)
            return true;
        else if (!destroyGoodBonus && destroyBreakable)
            return true;
        else
            return false;
    }

    /**
     * Détermine si l'IA peut poser une bombe dans la situation actuelle
     * @return true si oui, false sinon
     */
    private boolean isReadyToBomb() {
        if (world.getSuddenDeathType() == SuddenDeathType.BOMBS)
            return false;

        if(character.getBombCount() < character.getBombMax() && isSafe(aiPosition) &&
                Math.random() < level.getBombAggressivity()/world.getFps() &&
                hasTarget(Math.min(character.getRange(), level.getBombPrediction()))) {
            Direction beforeSimulation = currentDirection;
            bombingSimulation = true;
            if (turnSafely(level.getBombPrediction(), 1)) {
                bombingSimulation = false;
                return true;
            } else {
                bombingSimulation = false;
                setCurrentDirection(beforeSimulation);
            }
        }

        return false;
    }

    private static class DirectionInfo {
        public int safe = -3;
        public int step = 0;
        public int badBonus = 0;
        public int goodBonus = 0;

        public static enum Attribute {
            Safe,
            Step,
            BadBonus,
            GoodBonus
        }

        public DirectionInfo(int safe, int step, int badBonus, int goodBonus) {
            this.safe = safe;
            this.step = step;
            this.badBonus = badBonus;
            this.goodBonus = goodBonus;
        }

        public int compareTo(DirectionInfo o, Attribute[] attributePriority) {
            if (o == null)
                return 1;

            int ret = 0;
            for (Attribute a : attributePriority) {
                ret += compareTo(o, a);
                if (ret != 0)
                    return ret;
            }

            return 0;
        }

        private int compareTo(DirectionInfo o, Attribute a) {
            switch (a) {
            case Safe:
                if (safe < o.safe)
                    return -1;
                else if (safe > o.safe)
                    return 1;
                break;
            case Step:
                if (step > o.step)
                    return -1;
                else if (step < o.step)
                    return 1;
                break;
            case BadBonus:
                if (badBonus > o.badBonus)
                    return -1;
                else if (badBonus < o.badBonus)
                    return 1;
                break;
            case GoodBonus:
                if (goodBonus < o.goodBonus)
                    return -1;
                else if (goodBonus > o.goodBonus)
                    return 1;
                break;
            default:
                break;
            }

            return 0;
        }
    }

    private DirectionInfo.Attribute[] getPriorities() {
        switch (level.getSmartPriorities()) {
        case 0:
            return new DirectionInfo.Attribute[] {
                    DirectionInfo.Attribute.Safe,
                    DirectionInfo.Attribute.Step
                    };
        case 1:
            return new DirectionInfo.Attribute[] {
                    DirectionInfo.Attribute.Safe,
                    DirectionInfo.Attribute.Step,
                    DirectionInfo.Attribute.BadBonus,
                    DirectionInfo.Attribute.GoodBonus
                    };
        case 2:
            if (world.getSuddenDeathType() == SuddenDeathType.BOMBS) {
                return new DirectionInfo.Attribute[] {
                        DirectionInfo.Attribute.Safe,
                        DirectionInfo.Attribute.Step,
                        DirectionInfo.Attribute.BadBonus,
                        DirectionInfo.Attribute.GoodBonus,
                        };
            } else {
                return new DirectionInfo.Attribute[] {
                        DirectionInfo.Attribute.Safe,
                        DirectionInfo.Attribute.BadBonus,
                        DirectionInfo.Attribute.GoodBonus,
                        DirectionInfo.Attribute.Step
                        };
            }
        default:
            return null;
        }
    }

    private DirectionInfo isDirectionSafe(GridCoordinates position, Direction direction, int maxStep) {
        return isDirectionSafe(position, direction, maxStep, 0);
    }

    private DirectionInfo isDirectionSafe(GridCoordinates position, Direction direction, int maxStep, int step) {
        GridCoordinates nextPosition = position.neighbor(direction);
        DirectionInfo ret = new DirectionInfo(0, 1, isBadBonus(nextPosition) ? 1 : 0, isGoodBonus(nextPosition) ? 1 : 0);

        if (!isEmpty(nextPosition))
            ret.safe = -1;
        else if (world.getMap().isExploding(nextPosition)) {
            if (world.getMap().getExplosionTimeRemaining(nextPosition) <= (step-1)*world.getMap().getTileSize()/character.getMaxSpeed())
                ret.safe = 1;
            else
                ret.safe = -2;
        }
        else {
            int timeRemaining = getThreateningBombTimeRemaining(nextPosition);

            if (timeRemaining < 0) { //Safe
                ret.safe = 1;

                for (int i = step; i < maxStep; i++) { //Compte les bons bonus si l'on suit cette voie
                	nextPosition = nextPosition.neighbor(direction);

                	if (!isEmpty(nextPosition) || world.getMap().isExploding(nextPosition) ||
                	        isBadBonus(nextPosition) || !isSafe(nextPosition))
                    	break;
                    else if (isGoodBonus(nextPosition))
                    	ret.goodBonus++;
                }
            } else if (step+1 >= maxStep) { //Inconnu mais nb de pas max atteint
                ret.safe = 0;
            } else { //Vide mais unsafe
                if ((step+1)*world.getMap().getTileSize()/character.getMaxSpeed() > timeRemaining) {
                    ret.safe = -2;
                }
                else {
                    DirectionInfo safest = null;

                    for (Direction nextDirection : directions) {
                        if (Direction.areOpposite(direction, nextDirection))
                            continue;

                        DirectionInfo next = isDirectionSafe(nextPosition, nextDirection, maxStep, step+1);
                        if (next.compareTo(safest, getPriorities()) > 0)
                            safest = next;
                    }

                    ret.safe = safest.safe;
                    ret.step += safest.step;
                    ret.badBonus += safest.badBonus;
                    ret.goodBonus += safest.goodBonus;
                }
            }
        }

        return ret;
    }
}
