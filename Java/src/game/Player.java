package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("7d9743df-c7cd-4679-9771-fa22b1be441d")
public class Player extends Entity {
    int playerID;
    
    @objid ("c10c97b4-7ea2-4021-aff7-4c0b87aac71b")
     int lives;

    @objid ("42154f7e-37fd-4c85-b8bf-440560e7ccdf")
     int bombCount;

    @objid ("b8822ff6-0716-4acf-844b-19ea1d23faa0")
     int bombMax;

    @objid ("0ec9b326-319f-4b00-8ebb-c02b47dc166f")
     int range;

    @objid ("d3e49717-5f5c-49c0-b87a-e6ed29629386")
     List<Boolean> playerAbilities;

    @objid ("efe71f6e-6ac1-4f9a-bb96-98975b970b23")
     int invulnerability; 

    @objid ("2f129ce7-ac16-42b5-85cb-d57015645a67")
    protected Controller controller;
    
    public static final double PLAYER_DEFAULT_SPEED = 4; // tile/sec

    @objid ("1c494051-0d17-471a-a273-fd48c48928d7")
    public Player(World world, double x, double y, Controller controller, int playerID, int lives, int bombMax, int range, int invulnerability) {
        super(world, x, y);
        this.controller = controller;
        this.playerID = playerID;
        this.lives = lives;
        this.bombMax = bombMax;
        this.range = range;
        this.invulnerability = invulnerability;
        this.bombCount = 0;
        
        PlayerAbility[] pa = PlayerAbility.values();
        this.playerAbilities = new ArrayList<Boolean>(pa.length);
        for (int i = 0; i < pa.length; i++)
            this.playerAbilities.add(false);
    }
    
    public int getPlayerID() {
        return playerID;
    }

    @objid ("d8c3c2ca-78cd-4d35-8d4a-df8c6f1cbe55")
    public boolean isAlive() {
        return lives > 0;
    }

    @objid ("007b7078-5eba-4ff5-a413-43e779f00b19")
    public int getLives() {
        return this.lives;
    }

    @objid ("c5f1c15a-e638-4096-9f19-f77708761f51")
    void setLives(int value) {
        if (value >= 0) {
            this.lives = value;
        } else {
            throw new RuntimeException("Lives can't be negative");
        }
    }

    @objid ("31123492-c96a-4af6-a1df-d35617285e33")
    public int getBombCount() {
        return this.bombCount;
    }

    @objid ("778d6e7d-a16e-44d4-824d-f4ad18670c31")
    void decreaseBombCount() {
        this.bombCount = Math.max(0,this.bombCount-1);
    }

    @objid ("38aca366-45f0-4745-934b-576af97cd356")
    public int getBombMax() {
        return this.bombMax;
    }

    @objid ("c3b45755-91c1-4670-b1a9-c988acb88d59")
    void setBombMax(int value) {
        if (value >= 0) {
            this.bombMax = value;
        } else {
            throw new RuntimeException("BombMax can't be negative");
        }
    }

    @objid ("869e644a-c1c8-4013-937b-a11e9ac05ada")
    public int getRange() {
        return this.range;
    }

    @objid ("14a39b3e-b3ae-42e4-8757-4b67b7d510f6")
    void setRange(int value) {
        if (value >= 0) {
            this.range = value;
        } else {
            throw new RuntimeException("Range can't be negative");
        }
    }

    @objid ("671682b3-f854-4f65-8616-a0dee38409f6")
    public List<Boolean> getPlayerAbilities() {
        return Collections.unmodifiableList(this.playerAbilities);
    }

    @objid ("2559d9b8-e592-4923-928b-ebc444992c5c")
    void setPlayerAbilities(List<Boolean> value) {
        this.playerAbilities = value;
    }

    @objid ("1c7621e2-784f-4508-933f-55ea6bea5b83")
    public int getInvulnerability() {
        return this.invulnerability;
    }

    @objid ("3322d7de-cc32-48aa-8dde-e19bf6d5ba0c")
    void setInvulnerability(int value) {
        if (value >= 0) {
            this.invulnerability = value;
        } else {
            throw new RuntimeException("Invulnerability can't be negative");
        }
    }

    @objid ("d24ca8af-7294-4611-bab0-5541345d4258")
    public Controller getController() {
        return this.controller;
    }

    @objid ("79de1afe-e6c2-4c96-ae54-f3d57da135dc")
    void setController(Controller value) {
        this.controller = value;
    }

    @objid ("8cb4ed00-b6b9-4918-86a9-6a90e6368f8f")
    void decreaseLives() {
        this.lives = Math.max(0, this.lives-1);
    }

    @objid ("e0e09e0d-eb13-467a-b35f-235dff6e7fef")
    void increaseBombCount() {
        this.bombCount += 1;
    }

    @objid ("cae42f54-c6d5-484a-a987-2f4d38798b99")
    void decreaseBombMax() {
        this.bombMax = Math.max(1, this.bombMax-1);
    }

    @objid ("875e493f-36d6-4037-b1f4-235ed8b543b0")
    void increaseBombMax() {
        this.bombMax += 1;
    }

    @objid ("0ccac578-1a45-4e1a-828d-56a96c7e7b2a")
    void decreaseRange() {
        this.range = Math.max(1, this.range-1);
    }

    @objid ("6b5d4100-6a50-4fd6-b170-e9f343894c25")
    void increaseRange() {
        this.range += 1;
    }

    @objid ("e1e22784-8fdf-4da6-9c9c-3b900eef4dd6")
    void decreaseInvulnerability() {
        this.invulnerability = Math.max(0, this.invulnerability-1);
    }
    
    void removeShield() {
        this.playerAbilities.set(PlayerAbility.Shield.ordinal(), false);
    }
    
    boolean canCollide(double x, double y){
        if (!super.canCollide(x, y)) {
            return !world.getMap().toGridCoordinates(this.x, this.y).equals(world.getMap().toGridCoordinates(x, y))
                    && world.getMap().hasBomb(x, y);
        }
        return true;
    }

    @objid ("83716caf-4650-4a93-b6e4-a9f241a25c9c")
    void update() {       
        controller.update();
        Direction nextDirection = controller.getDirection();
        if (nextDirection != null) {
            direction = nextDirection;
            speed = PLAYER_DEFAULT_SPEED*world.map.getTileSize()/world.getFps();
            if (playerAbilities.get(PlayerAbility.MoreSpeed.ordinal()))
                speed *= 1.5;
            else if (playerAbilities.get(PlayerAbility.LessSpeed.ordinal())) 
                speed /= 2;
        } else
            speed = 0.;
        
        super.update();
        //Kick
        if (playerAbilities.get(PlayerAbility.Kick.ordinal()) && nextDirection != null) {
            double footX = -1;
            double footY = -1;
            
            switch (direction) {
            case Left:
                footX = getBorderLeft()-1;
                footY = this.y;
                break;
            case Right:
                footX = getBorderRight()+1;
                footY = this.y;
                break;
            case Up:
                footX = this.x;
                footY = getBorderTop()-1;
                break;
            case Down:
                footX = this.x;
                footY = getBorderDown()+1;
                break;
            }         
            
            if (world.getMap().isInsideMap(footX, footY) &&
                !world.getMap().toGridCoordinates(footX, footY).equals(world.getMap().toGridCoordinates(x, y))) {
                Bomb target = world.getMap().getFirstBomb(footX, footY);

                if(target != null){
                    this.world.kickBomb(target, nextDirection);
                }
            }
        }
        
        //Update acquisition Bonus/Malus (Random, More/Less Bomb, More/Less Range, More/Less Speed, Shield, Kick)
        updateBonusMalus();
        
        //Update Marche sur une case en Explosion (diminuer le nb de vie du joueur touché + vérification du Shield)
        
        if(this.world.getMap().isExploding(this.x, this.y) && getInvulnerability() == 0){ // On vérifie si la case où se trouve le CENTRE du joueur explose et qu'il n'est pas invulnérable
            if(playerAbilities.get(PlayerAbility.Shield.ordinal()) == true){
                playerAbilities.set(PlayerAbility.Shield.ordinal(), false); // On enlève le Shield
            } else {
                decreaseLives(); //Perte d'une vie si les conditions sont vérifiées
            }
            
            setInvulnerability((int) (World.EXPLOSION_DURATION*world.getFps())+1);
        }
        else
            decreaseInvulnerability(); // On diminue progressivement l'invulnérabilité pour ramener à 0
        
        if (controller.isPlantingBomb() && bombCount < bombMax && !world.getMap().isExploding(x, y) && !world.getMap().hasBomb(x,y)) {
            bombCount++;
            world.plantBomb(this);
        }
        
        // Vérifier si le joueur est encore vivant
        if(isAlive() == false){
            remove(); // On indique qu'il faut enlever le player qui a perdu toutes ses vies
        }
    }
    
    private void updateBonusMalus(){
        if(this.world.getMap().getTileType(this.x , this.y) == TileType.Bonus){
            BonusType b = this.world.getMap().getBonusType(this.x, this.y);
            while (b == BonusType.Random) {
                b = BonusTile.randomBonus();
            }
            
            switch(b){ 
            case Random:
                break;
                
            case MoreBomb:
                increaseBombMax();
                break;
                
            case LessBomb:
                decreaseBombMax();
                break;
                
            case MoreRange:
                increaseRange();
                break;
                
            case LessRange:
                decreaseRange();
                break;
                
            case MoreSpeed:
                if(playerAbilities.get(PlayerAbility.LessSpeed.ordinal()) == true){
                    playerAbilities.set(PlayerAbility.LessSpeed.ordinal(), false);
                } else {
                    playerAbilities.set(PlayerAbility.MoreSpeed.ordinal(), true);
                }
                break;
                
            case LessSpeed:
                if(playerAbilities.get(PlayerAbility.MoreSpeed.ordinal()) == true){
                    playerAbilities.set(PlayerAbility.MoreSpeed.ordinal(), false);
                } else {
                    playerAbilities.set(PlayerAbility.LessSpeed.ordinal(), true);
                }
                break;
                
            case Shield:
                playerAbilities.set(PlayerAbility.Shield.ordinal(), true);
                break;
                
            case Kick:
                playerAbilities.set(PlayerAbility.Kick.ordinal(), true);
                break;
            }
            
            this.world.pickUpBonus(this.x, this.y);
        }     
    }
}
