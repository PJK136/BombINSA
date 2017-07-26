package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Entité joueur
 */
@objid ("7d9743df-c7cd-4679-9771-fa22b1be441d")
public class Character extends Entity {
    @objid ("c10c97b4-7ea2-4021-aff7-4c0b87aac71b")
     int lives;

    @objid ("42154f7e-37fd-4c85-b8bf-440560e7ccdf")
     int bombCount;

    @objid ("b8822ff6-0716-4acf-844b-19ea1d23faa0")
     int bombMax;

    @objid ("0ec9b326-319f-4b00-8ebb-c02b47dc166f")
     int range;

    @objid ("d3e49717-5f5c-49c0-b87a-e6ed29629386")
     List<Boolean> characterAbilities;

    @objid ("efe71f6e-6ac1-4f9a-bb96-98975b970b23")
     int invulnerability;

    @objid ("5b4c8db6-08b3-4215-8a96-4348f89623df")
    public static final double CHARACTER_DEFAULT_SPEED = 4; // tile/sec
    
     transient Player player;

    /**
     * Constructeur Character par défaut
     */
    @objid ("c346899f-0664-4813-bcc6-157babe0b50f")
    private Character() {
        super(null, 0, 0);
    }

    /**
     * Construit un joueur avec les paramètres indiqués
     * @param world Monde dans lequel est le joueur
     * @param x Position horizontale en pixel
     * @param y Position verticale en pixel
     * @param lives Nombre de vies
     * @param bombMax Nombre de bombes
     * @param range Portée des bombes
     * @param invulnerability Temps d'invulnérabilité (en nombre de ticks/frames)
     * @param player Joueur contrôlant le personnage
     */
    @objid ("1c494051-0d17-471a-a273-fd48c48928d7")
    public Character(World world, double x, double y, int lives, int bombMax, int range, int invulnerability, Player player) {
        super(world, x, y);
        setLives(lives);
        setBombMax(bombMax);
        setRange(range);
        setInvulnerability(invulnerability);
        setPlayer(player);
        
        this.bombCount = 0;
        
        CharacterAbility[] pa = CharacterAbility.values();
        this.characterAbilities = new ArrayList<Boolean>(pa.length);
        for (int i = 0; i < pa.length; i++)
            this.characterAbilities.add(false);
    }
    
    /**
     * Vérifie si le Joueur est vivant
     * @return boolean
     */
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
    public List<Boolean> getCharacterAbilities() {
        return Collections.unmodifiableList(this.characterAbilities);
    }

    @objid ("2559d9b8-e592-4923-928b-ebc444992c5c")
    void setCharacterAbilities(List<Boolean> value) {
        this.characterAbilities = value;
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

    public Player getPlayer() {
        return this.player;
    }
    
    public Controller getController() {
        if (this.player != null)
            return this.player.getController();
        else
            return null;
    }
    
    void setPlayer(Player player) {
        this.player = player;
    }
    
    @objid ("e2e7b5c2-c647-40b9-b8e3-d980b80dde0d")
    public double getMaxSpeed() {
        double maxSpeed = CHARACTER_DEFAULT_SPEED*world.map.getTileSize()/world.getFps();
        if (characterAbilities.get(CharacterAbility.MoreSpeed.ordinal()))
            maxSpeed *= 1.5;
        else if (characterAbilities.get(CharacterAbility.LessSpeed.ordinal())) 
            maxSpeed /= 1.5;
        return maxSpeed;
    }

    @objid ("8cb4ed00-b6b9-4918-86a9-6a90e6368f8f")
    void decreaseLives() {
        this.lives = Math.max(0, this.lives-1);
        world.fireEvent(GameEvent.Hit);
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

    @objid ("4713a141-cd5b-4b52-aed7-d3a781156555")
    void removeShield() {
        this.characterAbilities.set(CharacterAbility.Shield.ordinal(), false);
    }
    
    /**
     * Appelle la méthode canCollide de Entity et vérifie en plus la collision avec les bombes
     */
    @Override
    @objid ("19e970e7-cbf2-4148-99bc-6828a561f8de")
    boolean canCollide(double x, double y) {
        if (!super.canCollide(x, y)) {
            return !world.getMap().toGridCoordinates(this.x, this.y).equals(world.getMap().toGridCoordinates(x, y))
                    && world.getMap().hasBomb(x, y);
        }
        return true;
    }
    
    /**
     * Met à jour le Joueur en faisant les actions suivantes :
     * - Update controller pour fixer la direction et la vitesse du Joueur
     * - Appel de l'Update de Entity
     * - Vérifie si le Kick est disponible puis Kick la bombe si possible
     * - Update acquisition de Bonus/Malus
     * - Vérifie si le Joueur possède un Shield ou s'il perd une vie
     * - Vérifie si le Joueur est encore vivant
     */
    @Override
    @objid ("83716caf-4650-4a93-b6e4-a9f241a25c9c")
    void update() {
        Direction nextDirection = null;
        
        if (getController() != null) {
            getController().update();
            nextDirection = getController().getDirection();
            if (nextDirection != null) {
                direction = nextDirection;
                speed = getMaxSpeed();
            } else
                speed = 0.;
        }
        
        super.update();
        
        //Kick
        if (characterAbilities.get(CharacterAbility.Kick.ordinal()) && nextDirection != null) {
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
        
        if(this.world.getMap().isExploding(this.x, this.y)) { // On vérifie si la case où se trouve le CENTRE du joueur explose
            if (getInvulnerability() == 0) { //S'il n'est pas invulnérable
                if(characterAbilities.get(CharacterAbility.Shield.ordinal()) == true){
                    characterAbilities.set(CharacterAbility.Shield.ordinal(), false); // On enlève le Shield
                } else {
                    decreaseLives(); //Perte d'une vie si les conditions sont vérifiées
                }
            }
        
            setInvulnerability(Math.max(invulnerability, world.getFps()+1));
        }
        else
            decreaseInvulnerability(); // On diminue progressivement l'invulnérabilité pour ramener à 0
        
        if (getController() != null && getController().isPlantingBomb() && bombCount < bombMax && !world.getMap().isExploding(x, y) && !world.getMap().hasBomb(x,y)) {
            bombCount++;
            world.plantBomb(this);
        }
        
        // Vérifier si le joueur est encore vivant
        if(isAlive() == false){
            remove(); // On indique qu'il faut enlever le character qui a perdu toutes ses vies
        }
    }
    
    /**
     * Gère les acquisitions de Bonus et Malus puis enlève les Bonus/Malus de la map
     */
    @objid ("e8cbd4f6-a55d-4030-8e99-752530c9845d")
    private void updateBonusMalus() {
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
                if(characterAbilities.get(CharacterAbility.LessSpeed.ordinal()) == true){
                    characterAbilities.set(CharacterAbility.LessSpeed.ordinal(), false);
                } else {
                    characterAbilities.set(CharacterAbility.MoreSpeed.ordinal(), true);
                }
                break;
                
            case LessSpeed:
                if(characterAbilities.get(CharacterAbility.MoreSpeed.ordinal()) == true){
                    characterAbilities.set(CharacterAbility.MoreSpeed.ordinal(), false);
                } else {
                    characterAbilities.set(CharacterAbility.LessSpeed.ordinal(), true);
                }
                break;
                
            case Shield:
                characterAbilities.set(CharacterAbility.Shield.ordinal(), true);
                break;
                
            case Kick:
                characterAbilities.set(CharacterAbility.Kick.ordinal(), true);
                break;
            }
            
            this.world.pickUpBonus(this.x, this.y);
        }
    }

    @objid ("0574ac7a-1eb8-4298-ad3f-788efa878621")
    @Override
    void updateFrom(Entity entity) {
        super.updateFrom(entity);
        if (entity instanceof Character) {
            Character character = (Character)entity;
            this.lives = character.lives;
            this.bombCount = character.bombCount;
            this.bombMax = character.bombMax;
            this.range = character.range;
            this.characterAbilities = character.characterAbilities;
            this.invulnerability = character.invulnerability;
        }
    }

    public WorldView getWorldView() {
        return this.world;
    }
}
