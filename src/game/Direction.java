package game;


/** Représente les directions disponibles pour les joueurs */
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
    public static boolean doHaveSameAxis(Direction d1, Direction d2) {
        if (d1 == null || d2 == null)
            return false;
        return d1.equals(d2) || areOpposite(d1, d2);
    }
    
    public static boolean areOpposite(Direction d1, Direction d2) {
        return (d1 == Direction.Up && d2 == Direction.Down) ||
               (d2 == Direction.Up && d1 == Direction.Down) ||
               (d1 == Direction.Left && d2 == Direction.Right) ||
               (d2 == Direction.Left && d1 == Direction.Right);
    }
    
    /**
     * Sélectionne une direction aléatoire
     * @return la direction aléatoire
     */
    public static Direction getRandomDirection() {
        return values()[(int)(Math.random()*values().length)];
    }
    
    public static Direction getOpposite(Direction direction) {
        switch (direction) {
        case Left:
            return Direction.Right;
        case Right:
            return Direction.Left;
        case Up:
            return Direction.Down;
        case Down:
            return Direction.Up;
        default:
            return null;
        }
    }

}
