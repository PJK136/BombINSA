package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Liste des types de tuiles présentes dans le jeu
 */
public enum TileType {
    Empty(EmptyTile.class),
    Breakable(BreakableTile.class),
    Unbreakable(UnbreakableTile.class),
    Bonus(BonusTile.class),
    Arrow(ArrowTile.class),
    Frozen(FrozenTile.class);
    
    private Constructor<? extends Tile> tileConstructor;
    
    private TileType(Class<? extends Tile> tileClass) {
        try {
            this.tileConstructor = tileClass.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    public Tile newTile() {
        try {
            return this.tileConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            assert false;
        }
        
        return null;
    }
}
