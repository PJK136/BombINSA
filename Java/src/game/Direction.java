package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/** Représente les directions disponibles pour les joueurs */
@objid ("4b1f3374-5785-401a-bd35-6347dc31d5a9")
public enum Direction {
    Right,
    Up,
    Left,
    Down;
    
    /**
     * Vérifie si deux directions ont la même direction (mais pas forcément dans le même sens)
     * @param d1 la 1ère direction
     * @param d2 la 2è direction
     * @return true si oui, false sinon
     */
    @objid ("0a70e5cc-0ca6-4f98-b031-b505f443e474")
    public static boolean isSameAxis(Direction d1, Direction d2) {
        if (d1 == null || d2 == null)
            return false;
        return d1.equals(d2) ||
                                       (d1 == Direction.Up && d2 == Direction.Down) ||
                                       (d2 == Direction.Up && d1 == Direction.Down) ||
                                       (d1 == Direction.Left && d2 == Direction.Right) ||
                                       (d2 == Direction.Left && d1 == Direction.Right);
    }
    
    /**
     * Sélectionne une direction aléatoire
     * @return la direction aléatoire
     */
    @objid ("f4ed90fa-af83-4ad9-b923-b5d10355cfbc")
    public static Direction getRandomDirection() {
        return values()[(int)(Math.random()*values().length)];
    }

}
