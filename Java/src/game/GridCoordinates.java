package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

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

    @objid ("d62a41d0-01a8-4763-9f1e-d0184f9572e2")
    public GridCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
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
    public String toString() {
        return "("+x+";"+y+")";
    }
    
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

    public static int distance(GridCoordinates gc1, GridCoordinates gc2) {
        return Math.abs(gc2.x-gc1.x) + Math.abs(gc2.y-gc1.y); //Norme 1 #Maths 2A
    }
}
