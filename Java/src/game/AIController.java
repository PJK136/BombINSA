package game;

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
        if(aiPlayer.getSpeed() == 0){
            turn(currentDirection,aiLocation);
        }
    }
    
    public void turn(Direction dir, GridCoordinates position){
        Direction randomDirection;
        while(!turned){
            randomDirection = Direction.getRandomDirection();
            if(randomDirection != dir && world.getMap().getTileType(position.neighbor(randomDirection)) == TileType.Empty){
                currentDirection = randomDirection;
                turned = true;
                System.out.println("turned " + randomDirection);
            }
        }
        turned = false;
    }

}
