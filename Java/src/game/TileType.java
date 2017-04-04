package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("2ce1720a-2b07-4544-8d70-37232840e2d5")
/**
 * Liste des types de tuiles présentes dans le jeu
 */
public enum TileType {
    Empty,
    Breakable,
    Unbreakable,
    Bonus,
    Arrow;
}
