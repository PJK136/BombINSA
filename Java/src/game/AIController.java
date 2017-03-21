package game;

import java.util.ArrayList;
import java.util.List;

public class AIController implements Controller {
    
    private WorldView world;
    private Player aiPlayer;
    private Direction currentDirection;
    private GridCoordinates aiLocation;
    private boolean turned = false;
    
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void update() {
        aiLocation = world.getMap().toGridCoordinates((int)aiPlayer.getX(),(int)aiPlayer.getY());
        if(aiPlayer.getSpeed() == 0 || (!(isSafe(aiLocation.neighbor(currentDirection))) && world.getTimeRemaining()%0.5*(world.getMap().getTileSize())/aiPlayer.getSpeed() == 0)){
            turn(currentDirection,aiLocation);
        }
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
}
