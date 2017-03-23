package game;

import java.util.ArrayList;
import java.util.List;

public class AIController implements Controller {
    
    private WorldView world;
    private Player aiPlayer;
    private Direction currentDirection;
    private GridCoordinates aiLocation;
    private boolean turned = false;
    private boolean bombing = false;
    private int droppedBomb = 0;
    
    public AIController(){
        currentDirection = null;
    }

    @Override
    public void setPlayer(Player player) {
        aiPlayer = player;
    }

    @Override
    public void setWorldView(World world) {
        this.world = world;

    }

    @Override
    public Direction getDirection() {
        return currentDirection;
    }

    @Override
    public boolean isPlantingBomb() {
        if (bombing){
            bombing = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {
        aiLocation = world.getMap().toGridCoordinates((int)aiPlayer.getX(),(int)aiPlayer.getY());
        if(readyToBomb()){
            droppedBomb = world.getTimeElapsed();
            currentDirection = null;
            bombing = true;
            return;
        }
        
        //Si sa position est sans danger mais que tous ses voisins sont soit des murs, soit dangereux alors ne bouge pas
        if(isSafe(aiLocation) && 
           !isEmptyAndSafe(aiLocation.neighbor(Direction.Up)) &&
           !isEmptyAndSafe(aiLocation.neighbor(Direction.Down)) &&
           !isEmptyAndSafe(aiLocation.neighbor(Direction.Left)) &&
           !isEmptyAndSafe(aiLocation.neighbor(Direction.Right))){
            currentDirection = null;
        }
        else if(aiPlayer.getSpeed() == 0 || (!(isSafe(aiLocation.neighbor(currentDirection))) && isSafe(aiLocation))){
            turn(currentDirection,aiLocation);
            System.out.println("first turn");
        }
        else if(aiPlayer.getSpeed() == 0){
            turn(currentDirection,aiLocation);
            System.out.println("second turn");
        }
        else if(!isSafe(aiLocation)){
            for(Direction dir : Direction.values()){
                if((dir == Direction.Left || dir == Direction.Left) && Math.abs(aiPlayer.getY()% 0.5*world.getMap().getTileSize()) < 0.5){
                    if(isEmptyAndSafe(aiLocation.neighbor(dir))){
                        currentDirection = dir;
                        System.out.println("third turn");
                    }
                }
                if((dir == Direction.Up || dir == Direction.Down) && Math.abs(aiPlayer.getY()% 0.5*world.getMap().getTileSize()) < 0.5){
                    if(isEmptyAndSafe(aiLocation.neighbor(dir))){
                        currentDirection = dir;
                        System.out.println("third turn");
                    } 
                }
            }
        }
    }
    
    public void turn(Direction dir, GridCoordinates position){
        Direction randomDirection;
        for (int i = 0; i < 10 && !turned; i++) {
            randomDirection = Direction.getRandomDirection();
            if(randomDirection != dir && isEmpty(position.neighbor(randomDirection))){
                currentDirection = randomDirection;
                turned = true;
            }
        }
        turned = false;
    }
    
    public boolean isSafe(GridCoordinates target){
        if(world.getMap().isExploding(target)){
            return false;
        }
        GridCoordinates temp = new GridCoordinates(target);
        List<Entity> tileEntities = new ArrayList<Entity>();
        Bomb bomb = null;
        
        for(Direction dir : Direction.values()){
            temp = target; // Pas besoin de copier car neighbor donne une autre instance
            while(!world.getMap().isCollidable(temp)){
                if(world.getMap().hasBomb(temp)){
                    tileEntities = world.getMap().getEntities(temp);
                    for(Entity entity : tileEntities){
                        if(entity instanceof Bomb){
                            bomb = (Bomb)entity;
                        }
                    }
                    if(GridCoordinates.distance(target,temp) <= bomb.getRange()){
                        return false;
                    }
                }
                temp = temp.neighbor(dir);
            }
        }

        return true;
    }
    
    private boolean isEmpty(GridCoordinates gc) {
        return !world.getMap().isCollidable(gc) && !world.getMap().hasBomb(gc);
    }
    
    private boolean isEmptyAndSafe(GridCoordinates gc) {
        return isEmpty(gc) && isSafe(gc);
    }
    
    public boolean readyToBomb(){
        if((world.getTimeElapsed() - droppedBomb)>3*world.getFps() && world.getTimeRemaining()>0){
            //check up
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Up))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Up).neighbor(Direction.Left)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Up).neighbor(Direction.Right))) {
                    return true;
                }
            }
            
            //check down
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Down))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Down).neighbor(Direction.Left)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Down).neighbor(Direction.Right))) {
                    return true;
                }
            }
            
            //check left
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Left))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Left).neighbor(Direction.Up)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Left).neighbor(Direction.Down))) {
                    return true;
                }
            }

            //check right
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Right))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Right).neighbor(Direction.Up)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Right).neighbor(Direction.Down))) {
                    return true;
                }
            }
        }
        return false;
        
    }
}
