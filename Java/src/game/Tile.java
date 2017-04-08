package game;

import java.util.LinkedList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Classe mère abstraite de l'ensemble des tuiles (cases du terrain de jeu)
 */
@objid ("0a4b4bbc-6c10-4d09-9d5f-ebe94f0193bd")
public abstract class Tile {
    /**
     * Liste des entites présentes sur la tuile
     */
    @objid ("2f216b30-27c8-4022-ad19-dbd71146cfd8")
    protected List<Entity> entities = new LinkedList<Entity> ();

    /**
     * Carte à laquelle la tuile appartient
     */
    @objid ("f4911fdc-1c7c-45f9-80e8-3af86c75d6bb")
     Map map;

    @objid ("eba8c794-3248-484e-8fdd-ec19b08a5c19")
    public abstract TileType getType();

    /**
     * @return si la tuile est percutable
     */
    @objid ("20137d66-fdca-4295-90c9-d845540dcb82")
    public abstract boolean isCollidable();

    @objid ("2b2f5efa-8de9-4ab7-932c-ca4af3ebd86f")
    List<Entity> getEntities() {
        return this.entities;
    }

    @objid ("43353fe2-84e4-465e-90e2-a96befea38d6")
    void setEntities(List<Entity> value) {
        this.entities = value;
    }

    @objid ("56948d0a-9630-4a04-8e0a-25aafbb43b4d")
    Tile update() {
        return this;
    }

    /**
     * Ajoute une entité à la liste de celles présentens sur la tuile
     * @param entity : entité à rajouter
     */
    @objid ("547b782f-b41e-4098-bd4a-5de3c5f1b0dd")
    void addEntity(Entity entity) {
        entities.add(entity);
    }

}
