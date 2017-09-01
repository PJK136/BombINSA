package game;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe mère abstraite de l'ensemble des tuiles (cases du terrain de jeu)
 */
public abstract class Tile {
    /**
     * Liste des entites présentes sur la tuile
     */
    protected List<Entity> entities = new LinkedList<Entity> ();

    /**
     * Carte à laquelle la tuile appartient
     */
     Map map;

    public abstract TileType getType();

    /**
     * @return si la tuile est percutable
     */
    public abstract boolean isCollidable();

    List<Entity> getEntities() {
        return this.entities;
    }

    void setEntities(List<Entity> value) {
        this.entities = value;
    }

    Tile update() {
        return this;
    }

    /**
     * Ajoute une entité à la liste de celles présentens sur la tuile
     * @param entity : entité à rajouter
     */
    void addEntity(Entity entity) {
        entities.add(entity);
    }

}
