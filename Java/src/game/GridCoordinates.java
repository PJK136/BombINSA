package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid("d87556f0-fd2c-4175-a67f-f8625d3f2ed5")
public class GridCoordinates {
    @objid("a5602982-71ce-4c2a-88b8-3c03a70fc49f")
    public int x;

    @objid("5c19ee5b-e41a-4e47-ba69-4b6eecbd8209")
    public int y;

    public GridCoordinates(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public GridCoordinates() {
        this(0, 0);
    }
}
