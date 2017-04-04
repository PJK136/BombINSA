package game;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/** Ce contrôleur est l'intelligence artificielle du jeu */
@objid ("cbe503f7-eb4d-4747-9547-3001ad190b16")
public class AIController extends Controller {
    @objid ("b3611ff7-8085-4126-9180-545efad68da8")
    private Direction currentDirection = null;

    @objid ("b6acfd1f-3b8b-4e90-80e7-cbe3d3afb0a9")
    private boolean turned = false;

    @objid ("946744c7-3ebf-45d8-bab5-f2533cd24562")
    private boolean bombing = false;

    @objid ("6acb90de-974c-4546-a93e-0b8cc35a6bd2")
    private GridCoordinates aiLocation;

    /**
     * Crée un nouveau contrôleur de type intelligence artificielle
     */
    @objid ("3d3cd868-87c6-4b1c-b1b2-335b1d2eb3e3")
    public AIController() {
        setName("IA");
    }

    @objid ("a6dfdad3-f290-4e15-ba75-694888e5d4c2")
    @Override
    public Direction getDirection() {
        if (!bombing) //Avance que si elle ne pose pas de bombe
            return currentDirection;
        else
            return null;
    }

    @objid ("9603acc0-4525-461e-a093-393a85378044")
    @Override
    public boolean isPlantingBomb() {
        if (bombing) {
            bombing = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Met à jour le contrôleur de type intelligence artificielle en faisant les actions suivantes :
     * - si l'IA peut poser und bombe, elle le fait
     * - arrête de bouger si elle est cernée
     * - tourne ou pas en fonction de sa situation
     */
    @objid ("2960c0e0-6a89-4208-87eb-888ae75547e6")
    @Override
    public void update() {
        aiLocation = world.getMap().toGridCoordinates((int)player.getX(),(int)player.getY());
        if(readyToBomb()){
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
        } else if(currentDirection == null || player.isColliding(currentDirection, player.getMaxSpeed())){ //Si l'IA rencontre un obstacle
            turn();
        } else if (!(isSafe(aiLocation.neighbor(currentDirection))) && isSafe(aiLocation)) { //Si l'IA va atteindre une case dangereuse
            turn();
        } else if(!isSafe(aiLocation)){ //Si l'IA est en danger
            for(Direction dir : Direction.values()){
                if(isEmptyAndSafe(aiLocation.neighbor(dir)) && !player.isColliding(dir, player.getMaxSpeed())){
                    currentDirection = dir;
                }
            }
        }
    }

    /**
     * Fait tourner l'IA dans une direction différente de l'actuelle
     */
    @objid ("e87291f0-a8c8-4d07-99ba-b34fa92d336b")
    public void turn() {
        Direction randomDirection;
        for (int i = 0; i < 10 && !turned; i++) {
            randomDirection = Direction.getRandomDirection();
            if(randomDirection != currentDirection && !player.isColliding(randomDirection, player.getMaxSpeed())){
                currentDirection = randomDirection;
                turned = true;
            }
        }
        turned = false;
    }

    /**
     * Controle la dangerosité d'une tuile
     * @param target La tuile qu'il faut contrôler
     * @return true Si la tuile est sécurisée, false sinon
     */
    @objid ("cc20da1a-09e2-4259-b21e-47277450e318")
    public boolean isSafe(GridCoordinates target) {
        if (world.getMap().isCollidable(target))
            return true;
            
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

    /**
     * Teste si une tuile est vide
     * @param gc les coordonnées de grille de la tuile
     * @return true si oui, false sinon
     */
    @objid ("83a5286c-60c1-409c-9714-d0a4f224cdf5")
    private boolean isEmpty(GridCoordinates gc) {
        return !world.getMap().isCollidable(gc) && !world.getMap().hasBomb(gc);
    }

    /**
     * Teste si une tuile est vide et sécurisée
     * @param gc les coordonnées de grille de la tuile
     * @return true si oui, false sinon
     */
    @objid ("64edb969-c16b-4556-a23e-edffa2de598d")
    private boolean isEmptyAndSafe(GridCoordinates gc) {
        return isEmpty(gc) && isSafe(gc);
    }

    /**
     * Détermine si l'IA peut poser une bombe dans la situation actuelle
     * @return true si oui, false sinon
     */
    @objid ("abcfe8f8-3507-4b7c-a175-d63eebfae72c")
    public boolean readyToBomb() {
        if(player.getBombCount() < player.getBombMax() && world.getTimeRemaining()>0 && isSafe(aiLocation) && Math.random() < 1./world.getFps()) {
            //check up
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Up))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Up).neighbor(Direction.Left)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Up).neighbor(Direction.Right))) {
                    currentDirection = Direction.Up;
                    return true;
                }
            }
            
            //check down
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Down))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Down).neighbor(Direction.Left)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Down).neighbor(Direction.Right))) {
                    currentDirection = Direction.Down;
                    return true;
                }
            }
            
            //check left
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Left))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Left).neighbor(Direction.Up)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Left).neighbor(Direction.Down))) {
                    currentDirection = Direction.Left;
                    return true;
                }
            }
        
            //check right
            if(isEmptyAndSafe(aiLocation.neighbor(Direction.Right))) {
                if(isEmptyAndSafe(aiLocation.neighbor(Direction.Right).neighbor(Direction.Up)) ||
                   isEmptyAndSafe(aiLocation.neighbor(Direction.Right).neighbor(Direction.Down))) {
                    currentDirection = Direction.Right;
                    return true;
                }
            }
        }
        return false;
    }

}
