package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Entité joueur
 */
public class Character extends Entity {
     int lives;

     int bombCount;

     int bombMax;

     int range;

     List<Boolean> characterAbilities;

     int invulnerability;

    public static final double CHARACTER_DEFAULT_SPEED = 4; // tile/sec

     transient Player player;

    /**
     * Constructeur Character par défaut
     */
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

    @Override
    void remove() {
        setLives(0);
        super.remove();
    }

    /**
     * Vérifie si le Joueur est vivant
     * @return boolean
     */
    public boolean isAlive() {
        return lives > 0;
    }

    public int getLives() {
        return this.lives;
    }

    void setLives(int value) {
        if (value >= 0) {
            this.lives = value;
        } else {
            throw new RuntimeException("Lives can't be negative");
        }
    }

    public int getBombCount() {
        return this.bombCount;
    }

    void decreaseBombCount() {
        this.bombCount = Math.max(0,this.bombCount-1);
    }

    public int getBombMax() {
        return this.bombMax;
    }

    void setBombMax(int value) {
        if (value >= 0) {
            this.bombMax = value;
        } else {
            throw new RuntimeException("BombMax can't be negative");
        }
    }

    public int getRange() {
        return this.range;
    }

    void setRange(int value) {
        if (value >= 0) {
            this.range = value;
        } else {
            throw new RuntimeException("Range can't be negative");
        }
    }

    public List<Boolean> getCharacterAbilities() {
        return Collections.unmodifiableList(this.characterAbilities);
    }

    void setCharacterAbilities(List<Boolean> value) {
        this.characterAbilities = value;
    }

    public int getInvulnerability() {
        return this.invulnerability;
    }

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

    public double getMaxSpeed() {
        double maxSpeed = CHARACTER_DEFAULT_SPEED*world.map.getTileSize()/world.getFps();
        if (characterAbilities.get(CharacterAbility.MoreSpeed.ordinal()))
            maxSpeed *= 1.5;
        else if (characterAbilities.get(CharacterAbility.LessSpeed.ordinal()))
            maxSpeed /= 1.5;
        return maxSpeed;
    }

    void decreaseLives() {
        decreaseLives(1);
    }

    void decreaseLives(int amount) {
        this.lives = Math.max(0, this.lives-amount);
        world.fireEvent(GameEvent.Hit);
    }

    void increaseBombCount() {
        this.bombCount += 1;
    }

    void decreaseBombMax() {
        this.bombMax = Math.max(1, this.bombMax-1);
    }

    void increaseBombMax() {
        this.bombMax += 1;
    }

    void decreaseRange() {
        this.range = Math.max(1, this.range-1);
    }

    void increaseRange() {
        this.range += 1;
    }

    void decreaseInvulnerability() {
        this.invulnerability = Math.max(0, this.invulnerability-1);
    }

    void removeShield() {
        this.characterAbilities.set(CharacterAbility.Shield.ordinal(), false);
    }

    /**
     * Appelle la méthode canCollide de Entity et vérifie en plus la collision avec les bombes
     */
    @Override
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
    void update() {
        Direction nextDirection = null;

        if (getController() != null) {
            getController().update();
            nextDirection = getController().getDirection();
            if (nextDirection != null) {
                direction = nextDirection;
                speed = getMaxSpeed();
            } else if (world.getMap().getTileType(this.x, this.y) == TileType.Frozen)
                speed = CHARACTER_DEFAULT_SPEED*world.map.getTileSize()/world.getFps()/1.5;
            else
                speed = 0.;
        }

        super.update();

        //Kick
        if (nextDirection != null) {
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

                if (characterAbilities.get(CharacterAbility.Kick.ordinal()) ||
                        world.getMap().getTileType(footX, footY) == TileType.Frozen) {
                    Bomb target = world.getMap().getFirstBomb(footX, footY);

                    if (target != null)
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
                    if (!isAlive()) {
                        Character owner = world.getMap().getExplosionOwner(world.getMap().toGridCoordinates(x, y));
                        if (owner != null && owner.getPlayer() != null) {
                            if (owner == this)
                                owner.getPlayer().decreaseScore();
                            else
                                owner.getPlayer().increaseScore();
                        }
                    }

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
