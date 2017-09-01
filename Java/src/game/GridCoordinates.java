package game;

import java.util.Objects;


/** Représente les coordonnées de grille de la carte */
public class GridCoordinates {
    public int x;

    public int y;

    public GridCoordinates() {
        this(0, 0);
    }

    /**
     * Crée des coordonnées de grille à partir de coordonnées en pixel
     * @param x la coordonée en x en pixel
     * @param y la coordonée en y en pixel
     */
    public GridCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Crée une copie de coordonnées de grilles préexistantes
     * @param gc les cooronnées préexistantes
     */
    public GridCoordinates(GridCoordinates gc) {
        this(gc.x, gc.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        else if (obj == this)
            return true;
        else if (!(obj instanceof GridCoordinates))
            return false;
        
        GridCoordinates gc = (GridCoordinates)obj;
        return x == gc.x && y == gc.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "("+x+";"+y+")";
    }

    /**
     * Calcule les coordonnées d'une case voisine
     * @param direction La direction dans laquelle on cherche la voisine
     * @return Les coordonnées de la case voisine
     */
    public GridCoordinates neighbor(Direction direction) {
        switch (direction) {
        case Left:
            return new GridCoordinates(x-1, y);
        case Right:
            return new GridCoordinates(x+1, y);
        case Up:
            return new GridCoordinates(x, y-1);
        case Down:
            return new GridCoordinates(x, y+1);
        }
        return null;
    }

    /**
     * Mesure la distance entre 2 coordonnées de grille à partir d'une norme de type 1
     * @param gc1 Les premières coordonnées
     * @param gc2 Les secondes coordonnées
     * @return La distance entre les 2 coordonnées
     */
    public static int distance(GridCoordinates gc1, GridCoordinates gc2) {
        return Math.abs(gc2.x-gc1.x) + Math.abs(gc2.y-gc1.y); //Norme 1 #Maths 2A
    }

}
