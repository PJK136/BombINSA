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
    //private boolean trapped = false;
    
    public AIController(){
        currentDirection = Direction.Right;
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
        if(isSafe(aiLocation) && ((!isSafe(aiLocation.neighbor(Direction.Up)) || world.getMap().getTileType(aiLocation.neighbor(Direction.Up)) == TileType.Breakable || world.getMap().getTileType(aiLocation.neighbor(Direction.Up)) == TileType.Unbreakable) && ((!isSafe(aiLocation.neighbor(Direction.Left)) || world.getMap().getTileType(aiLocation.neighbor(Direction.Left)) == TileType.Breakable || world.getMap().getTileType(aiLocation.neighbor(Direction.Left)) == TileType.Unbreakable) && ((!isSafe(aiLocation.neighbor(Direction.Down)) || world.getMap().getTileType(aiLocation.neighbor(Direction.Down)) == TileType.Breakable || world.getMap().getTileType(aiLocation.neighbor(Direction.Down)) == TileType.Unbreakable) && ((!isSafe(aiLocation.neighbor(Direction.Right)) || world.getMap().getTileType(aiLocation.neighbor(Direction.Right)) == TileType.Breakable || world.getMap().getTileType(aiLocation.neighbor(Direction.Right)) == TileType.Unbreakable)))))){
            aiPlayer.setDirection(null);
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
                if((dir == Direction.Left || dir == Direction.Left) && Math.abs(aiPlayer.getY()% 0.5*world.getMap().getTileSize()) < 1){
                    if(isSafe(aiLocation.neighbor(dir)) && (world.getMap().getTileType(aiLocation.neighbor(dir)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(dir)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(dir)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(dir))){
                        currentDirection = dir;
                        System.out.println("third turn");
                    }
                }
                if((dir == Direction.Up || dir == Direction.Down) && Math.abs(aiPlayer.getY()% 0.5*world.getMap().getTileSize()) < 1){
                    if(isSafe(aiLocation.neighbor(dir)) && (world.getMap().getTileType(aiLocation.neighbor(dir)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(dir)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(dir)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(dir))){
                        currentDirection = dir;
                        System.out.println("third turn");
                    } 
                }
            }
        }
        /*if(readyToBomb()){
            bombing = true;
        }*/
    }
    
    public void turn(Direction dir, GridCoordinates position){
        Direction randomDirection;
        for (int i = 0; i < 10 && !turned; i++) {
            randomDirection = Direction.getRandomDirection();
            if(randomDirection != dir && world.getMap().getTileType(position.neighbor(randomDirection)) == TileType.Empty && world.getMap().isInsideMap(position.neighbor(randomDirection))){
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
        //first direction
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
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
            temp.x ++;
        }
        //second direction
        temp = new GridCoordinates(target);
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
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
            temp.x --;
        }
        //third direction
        temp = new GridCoordinates(target);
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
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
            temp.y ++;
        }
        //last direction
        temp = new GridCoordinates(target);
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
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
            temp.y --;
        }
        return true;
    }
    
    /*public boolean readyToBomb(){
        //check up
        if(isSafe(aiLocation.neighbor(Direction.Up)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Up)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Up)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Up)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Up))){
            if((isSafe(aiLocation.neighbor(Direction.Up).neighbor(Direction.Left)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Up).neighbor(Direction.Left)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Up).neighbor(Direction.Left)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Up).neighbor(Direction.Left)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Up).neighbor(Direction.Left))) || (isSafe(aiLocation.neighbor(Direction.Up).neighbor(Direction.Right)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Up).neighbor(Direction.Right)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Up).neighbor(Direction.Right)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Up).neighbor(Direction.Right)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Up).neighbor(Direction.Right)))){
                currentDirection = Direction.Up;
                return true;
            }
        }
        //check left
        if(isSafe(aiLocation.neighbor(Direction.Left)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Left)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Left)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Left)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Left))){
            if((isSafe(aiLocation.neighbor(Direction.Left).neighbor(Direction.Up)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Left).neighbor(Direction.Up)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Left).neighbor(Direction.Up)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Left).neighbor(Direction.Up)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Left).neighbor(Direction.Up))) || (isSafe(aiLocation.neighbor(Direction.Left).neighbor(Direction.Down)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Left).neighbor(Direction.Down)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Left).neighbor(Direction.Down)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Left).neighbor(Direction.Down)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Left).neighbor(Direction.Down)))){
                currentDirection = Direction.Left;
                return true;
            }
        } 
        //check down
        if(isSafe(aiLocation.neighbor(Direction.Down)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Down)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Down)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Down)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Down))){
            if((isSafe(aiLocation.neighbor(Direction.Down).neighbor(Direction.Left)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Down).neighbor(Direction.Left)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Down).neighbor(Direction.Left)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Down).neighbor(Direction.Left)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Down).neighbor(Direction.Left))) || (isSafe(aiLocation.neighbor(Direction.Down).neighbor(Direction.Right)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Down).neighbor(Direction.Right)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Down).neighbor(Direction.Right)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Down).neighbor(Direction.Right)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Down).neighbor(Direction.Right)))){
                currentDirection = Direction.Down;
                return true;
            }
        } 
        //check right
        if(isSafe(aiLocation.neighbor(Direction.Right)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Right)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Right)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Right)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Right))){
            if((isSafe(aiLocation.neighbor(Direction.Right).neighbor(Direction.Up)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Right).neighbor(Direction.Up)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Right).neighbor(Direction.Up)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Right).neighbor(Direction.Up)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Right).neighbor(Direction.Up))) || (isSafe(aiLocation.neighbor(Direction.Right).neighbor(Direction.Down)) && (world.getMap().getTileType(aiLocation.neighbor(Direction.Right).neighbor(Direction.Down)) == TileType.Empty || world.getMap().getTileType(aiLocation.neighbor(Direction.Right).neighbor(Direction.Down)) == TileType.Bonus || world.getMap().getTileType(aiLocation.neighbor(Direction.Right).neighbor(Direction.Down)) == TileType.Arrow) && world.getMap().isInsideMap(aiLocation.neighbor(Direction.Right).neighbor(Direction.Down)))){
                currentDirection = Direction.Right;
                return true;
            }
        } 
        return false;
        
    }*/
}
