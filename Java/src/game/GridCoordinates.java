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

}
