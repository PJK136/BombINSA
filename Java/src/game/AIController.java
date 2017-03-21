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
        if(aiPlayer.getSpeed() == 0 || !(isSafe(aiLocation.neighbor(currentDirection)))){
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
        GridCoordinates temp = target;
        List<Entity> tileEntities = new ArrayList<Entity>();
        Bomb bomb = null;
        //first direction
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
            if(world.getMap().hasBomb(temp)){
                System.out.println("check right");
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
        temp = target;
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
            if(world.getMap().hasBomb(temp)){
                System.out.println("check left");
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
        temp = target;
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
            if(world.getMap().hasBomb(temp)){
                System.out.println("check down");
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
        temp = target;
        while(world.getMap().getTileType(temp) == TileType.Empty || world.getMap().getTileType(temp) == TileType.Bonus || world.getMap().getTileType(temp) == TileType.Arrow){
            if(world.getMap().hasBomb(temp)){
                System.out.println("check up");
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
