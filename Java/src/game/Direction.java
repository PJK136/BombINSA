package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("4b1f3374-5785-401a-bd35-6347dc31d5a9")
public enum Direction {
    Right,
    Up,
    Left,
    Down;
    
    public static boolean isSameAxis(Direction d1, Direction d2) {
        if (d1 == null || d2 == null)
            return false;
        
        return d1.equals(d2) ||
               (d1 == Direction.Up && d2 == Direction.Down) ||
               (d2 == Direction.Up && d1 == Direction.Down) ||
               (d1 == Direction.Left && d2 == Direction.Right) ||
               (d2 == Direction.Left && d1 == Direction.Right);
    }
}
