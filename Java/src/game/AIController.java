package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("cbe503f7-eb4d-4747-9547-3001ad190b16")
public class AIController implements Controller {
    @objid ("b3611ff7-8085-4126-9180-545efad68da8")
    private Direction currentDirection;

    @objid ("b6acfd1f-3b8b-4e90-80e7-cbe3d3afb0a9")
    private boolean turned = false;

    @objid ("946744c7-3ebf-45d8-bab5-f2533cd24562")
    private boolean bombing = false;

    @objid ("a517c478-aa58-42fc-bf09-0687c8d3e721")
    private int droppedBomb = 0;

    @objid ("2a62ecd6-114d-4fb9-8b1d-14b826963658")
    private WorldView world;

    @objid ("81d4dca1-b16d-4247-96f1-d35f172be4c3")
    private Player aiPlayer;

    @objid ("6acb90de-974c-4546-a93e-0b8cc35a6bd2")
    private GridCoordinates aiLocation;

    @objid ("3d3cd868-87c6-4b1c-b1b2-335b1d2eb3e3")
    public AIController() {
        currentDirection = null;
    }

    @objid ("b58b228f-36e7-44df-b4ab-525989724476")
    @Override
    public void setPlayer(Player player) {
        aiPlayer = player;
    }

    @objid ("b9d8617a-93c3-42c9-9cd8-fdd04230d457")
    @Override
    public void setWorldView(World world) {
        this.world = world;
    }

    @objid ("a6dfdad3-f290-4e15-ba75-694888e5d4c2")
    @Override
    public Direction getDirection() {
        return currentDirection;
    }

    @objid ("9603acc0-4525-461e-a093-393a85378044")
    @Override
    public boolean isPlantingBomb() {
        if (bombing){
            bombing = false;
            return true;
        } else {
            return false;
        }
    }

    @objid ("2960c0e0-6a89-4208-87eb-888ae75547e6")
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
                if(isEmptyAndSafe(aiLocation.neighbor(dir)) && !aiPlayer.isColliding(dir, aiPlayer.getSpeed())){
                    currentDirection = dir;
                    System.out.println("third turn");
                }
            }
        }
    }

    @objid ("e87291f0-a8c8-4d07-99ba-b34fa92d336b")
    public void turn(Direction dir, GridCoordinates position) {
        Direction randomDirection;
        for (int i = 0; i < 10 && !turned; i++) {
            randomDirection = Direction.getRandomDirection();
            //if(randomDirection != dir && isEmpty(position.neighbor(randomDirection))){
            if(randomDirection != dir && !aiPlayer.isColliding(randomDirection, aiPlayer.getSpeed())){
                currentDirection = randomDirection;
                turned = true;
            }
        }
        turned = false;
    }

    @objid ("cc20da1a-09e2-4259-b21e-47277450e318")
    public boolean isSafe(GridCoordinates target) {
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

    @objid ("83a5286c-60c1-409c-9714-d0a4f224cdf5")
    private boolean isEmpty(GridCoordinates gc) {
        return !world.getMap().isCollidable(gc) && !world.getMap().hasBomb(gc);
    }

    @objid ("64edb969-c16b-4556-a23e-edffa2de598d")
    private boolean isEmptyAndSafe(GridCoordinates gc) {
        return isEmpty(gc) && isSafe(gc);
    }

    @objid ("abcfe8f8-3507-4b7c-a175-d63eebfae72c")
    public boolean readyToBomb() {
        if((world.getTimeElapsed() - droppedBomb)>3*world.getFps() && world.getTimeRemaining()>0 && isSafe(aiLocation)){
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

    @objid ("33761719-0d5d-423b-b723-36b0a60391e8")
    @Override
    public String getName() {
        return "IA";
    }

}
