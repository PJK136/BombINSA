package game;

import java.util.List;

/** Cette interface est une vue des informations contenues dans la carte */
public interface MapView {
    String getName();

    int getColumnCount();

    int getRowCount();

    int getWidth();

    int getHeight();

    /**
     * @return La taille des tuiles qui composent la carte
     */
    int getTileSize();

    /**
     * Convertit des coordonnées pixels en coordonnées de grille pour la carte
     * @param x coordonnée en x en pixel
     * @param y coordonnée en y en pixel
     * @return des coordonnées de grille
     */
    GridCoordinates toGridCoordinates(double x, double y);

    /**
     * Récupère la coordonnée en x en pixel du centre d'une case de grille
     * @param gc les coordonnées de grille de la case
     * @return la coordonnée en x en pixel
     */
    double toCenterX(GridCoordinates gc);

    /**
     * Récupère la coordonnée en y en pixel du centre d'une case de grille
     * @param gc les coordonnées de grille de la case
     * @return la coordonnée en y en pixel
     */
    double toCenterY(GridCoordinates gc);

    /**
     * Récupère la coordonnée en x en pixel du centre de la case de grille qui contient la coordonnée en paramètre
     * @param x la coordonnée en x en pixel étudiée
     * @return la coordonnée en x en pixel du centre de la case
     */
    double toCenterX(double x);

    /**
     * Récupère la coordonnée en y en pixel du centre de la case de grille qui contient la coordonnée en paramètre
     * @param y la coordonnée en y en pixel étudiée
     * @return la coordonnée en y en pixel du centre de la case
     */
    double toCenterY(double y);

    /**
     * Vérifie si les coordonnées appartiennent à la grille de la carte
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    boolean isInsideMap(GridCoordinates gc);

    /**
     * Vérifie si les coordonnées appartiennent à la grille de la carte
     * @param x la coordonnée en x en pixel
     * @param y la coordonnée en y en pixel
     * @return true si oui, false sinon
     */
    boolean isInsideMap(double x, double y);

    /**
     * Vérifie si la case de grille est percutable
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    boolean isCollidable(GridCoordinates gc);

    /**
     * Vérifie si le contenu de la case de grille est percutable
     * @param x la coordonnée en x en pixel à tester
     * @param y la coordonnée en y en pixel à tester
     * @return true si oui, false sinon
     */
    boolean isCollidable(double x, double y);

    /**
     * Vérifie si le contenu de la case de grille est déstructible
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    boolean isExplodable(GridCoordinates gc);

    /**
     * Vérifie si une case de grille explose
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    boolean isExploding(GridCoordinates gc);

    /**
     * Vérifie si une case de grille explose
     * @param x la coordonnée en x en pixel
     * @param y la coordonnée en y en pixel
     * @return true si oui, false sinon
     */
    boolean isExploding(double x, double y);

    ExplosionType getExplosionType(GridCoordinates gc);

    Direction getExplosionDirection(GridCoordinates gc);

    int getExplosionTimeRemaining(GridCoordinates gc);

    TileType getTileType(GridCoordinates gc);

    TileType getTileType(double x, double y);

    BonusType getBonusType(GridCoordinates gc);

    BonusType getBonusType(double x, double y);

    Direction getArrowDirection(GridCoordinates gc);

    Direction getArrowDirection(double x, double y);

    List<GridCoordinates> getSpawningLocations();

    List<Entity> getEntities(GridCoordinates gc);

    List<Entity> getEntities(double x, double y);

    /**
     * Vérifie si la case contient une bombe
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    boolean hasBomb(GridCoordinates gc);

    /**
     * Vérifie si la case contient une bombe
     * @param x la coordonnée en x en pixel
     * @param y la coordonnée en y en pixel
     * @return true si oui, false sinon
     */
    boolean hasBomb(double x, double y);

    /**
     * Récupère la première entité qui est une bombe dans une case de grille
     * @param gc les coordonnées de grille
     * @return la bombe
     */
    Bomb getFirstBomb(GridCoordinates gc);

    /**
     * Récupère la première entité qui est une bombe dans une case de grille
     * @param x la coordonnée en pixel en x
     * @param y la coordonnée en pixel en y
     * @return la bombe
     */
    Bomb getFirstBomb(double x, double y);

}
