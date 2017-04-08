package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/** Représente les coordonnées de grille de la carte */
@objid ("d87556f0-fd2c-4175-a67f-f8625d3f2ed5")
public class GridCoordinates {
    @objid ("a5602982-71ce-4c2a-88b8-3c03a70fc49f")
    public int x;

    @objid ("5c19ee5b-e41a-4e47-ba69-4b6eecbd8209")
    public int y;

    @objid ("c3770a91-6e3f-4ddf-8f5a-46dc15d41bfa")
    public GridCoordinates() {
        this(0, 0);
    }

    /**
     * Crée des coordonnées de grille à partir de coordonnées en pixel
     * @param x la coordonée en x en pixel
     * @param y la coordonée en y en pixel
     */
    @objid ("d62a41d0-01a8-4763-9f1e-d0184f9572e2")
    public GridCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Crée une copie de coordonnées de grilles préexistantes
     * @param gc les cooronnées préexistantes
     */
    @objid ("889a1224-d7a7-4db2-a352-70f6a96311b9")
    public GridCoordinates(GridCoordinates gc) {
        this(gc.x, gc.y);
    }

    @objid ("e332b11d-704a-4500-8511-2ed4f8fa73e5")
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

    @objid ("7142fd4e-32d2-4852-aacc-41cd953691cd")
    @Override
    public String toString() {
        return "("+x+";"+y+")";
    }

    /**
     * Calcule les coordonnées d'une case voisine
     * @param direction La direction dans laquelle on cherche la voisine
     * @return Les coordonnées de la case voisine
     */
    @objid ("596006a8-a08a-41ff-8cb2-d06326c79d55")
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
    @objid ("ce3d5110-3070-4817-acff-fb933af76cca")
    public static int distance(GridCoordinates gc1, GridCoordinates gc2) {
        return Math.abs(gc2.x-gc1.x) + Math.abs(gc2.y-gc1.y); //Norme 1 #Maths 2A
    }

}
