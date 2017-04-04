package game;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("27201ab7-2fd3-4c35-abb3-37155f6d5822")
public interface MapView {
    @objid ("14a5fb0b-a57a-499e-ab6a-00fbd434a72b")
    int getColumnCount();

    @objid ("433694e2-86f3-4165-a7b0-800f219f33f3")
    int getRowCount();

    @objid ("f9e27f3e-5590-406d-8e1c-c2283d78b1c0")
    int getWidth();

    @objid ("5d4ce44b-0b1b-467b-a4d4-a9c59b922f08")
    int getHeight();
    
    /**
     * @return La taille des tuiles qui composent la carte
     */
    @objid ("bc0fe536-ee94-44d9-8252-030f1ca4ddc3")
    int getTileSize();
    
    /**
     * Convertit des coordonnées pixels en coordonnées de grille pour la carte
     * @param x coordonnée en x en pixel
     * @param y coordonnée en y en pixel
     * @return des coordonnées de grille
     */
    @objid ("f966b298-131f-4a66-b7f3-23dee860247f")
    GridCoordinates toGridCoordinates(double x, double y);
    
    /**
     * Récupère la coordonnée en x en pixel du centre d'une case de grille
     * @param gc les coordonnées de grille de la case
     * @return la coordonnée en x en pixel
     */
    @objid ("320eacf7-c2f3-4de5-8da9-1ae7a3693afe")
    double toCenterX(GridCoordinates gc);
    
    /**
     * Récupère la coordonnée en y en pixel du centre d'une case de grille
     * @param gc les coordonnées de grille de la case
     * @return la coordonnée en y en pixel
     */
    @objid ("2ce864ec-0e38-41f7-9821-124bb535dade")
    double toCenterY(GridCoordinates gc);
    
    /**
     * Récupère la coordonnée en x en pixel du centre de la case de grille qui contient la coordonnée en paramètre
     * @param x la coordonnée en x en pixel étudiée
     * @return la coordonnée en x en pixel du centre de la case
     */
    @objid ("bc6c7844-5545-4fe6-816b-50704062ca9c")
    double toCenterX(double x);

    /**
     * Récupère la coordonnée en y en pixel du centre de la case de grille qui contient la coordonnée en paramètre
     * @param y la coordonnée en y en pixel étudiée
     * @return la coordonnée en y en pixel du centre de la case
     */
    @objid ("a0d73d03-9989-428c-8d55-9b2244955614")
    double toCenterY(double y);
    
    /**
     * Controlle si les coordonnées appartiennent à la grille de la carte
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    @objid ("05760269-1145-4d38-ab1c-e72a84c8cb3f")
    boolean isInsideMap(GridCoordinates gc);
    
    /**
     * Controlle si les coordonnées appartiennent à la grille de la carte
     * @param x la coordonnée en x en pixel
     * @param y la coordonnée en y en pixel
     * @return true si oui, false sinon
     */
    @objid ("457e697a-0d30-415e-bcd7-2c05139d8872")
    boolean isInsideMap(double x, double y);
    
    /**
     * Controlle si la case de grille est percutable
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    @objid ("4852ff96-b82e-4459-a23f-1a1a4cbb066f")
    boolean isCollidable(GridCoordinates gc);
    
    /**
     * Controlle si le contenu de la case de grille est percutable
     * @param x la coordonnée en x en pixel à tester
     * @param y la coordonnée en y en pixel à tester
     * @return true si oui, false sinon
     */
    @objid ("530a81b2-8f93-4453-bff3-ab4a31d9de02")
    boolean isCollidable(double x, double y);

    /**
     * Controlle si le contenu de la case de grille est déstructible
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    @objid ("1aa5c5d5-8355-46c6-8210-4e726e00acff")
    boolean isExplodable(GridCoordinates gc);
    
    /**
     * Controlle si une case de grille explose
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    @objid ("3557c50a-eb7b-4615-881a-67b2da2bd9c9")
    boolean isExploding(GridCoordinates gc);
    
    /**
     * Controlle si une case de grille explose
     * @param x la coordonnée en x en pixel
     * @param y la coordonnée en y en pixel
     * @return true si oui, false sinon
     */
    @objid ("81182a1c-ea0f-4d7b-8f92-2f8b49896d52")
    boolean isExploding(double x, double y);

    @objid ("609c79bb-832f-4d9a-ac45-59cf5fc4213d")
    ExplosionType getExplosionType(GridCoordinates gc);

    @objid ("104ed3aa-75aa-4eab-8747-8dd81e7d068b")
    Direction getExplosionDirection(GridCoordinates gc);

    @objid ("fca35abb-78e4-4816-be51-6c014978e4ac")
    TileType getTileType(GridCoordinates gc);

    @objid ("81e81ab9-21a7-423b-ac6c-36f7186cd24a")
    TileType getTileType(double x, double y);

    @objid ("3de227bf-fec6-4032-a818-f1d9ae16440b")
    BonusType getBonusType(GridCoordinates gc);

    @objid ("8d579098-48e5-4fc5-9e18-32c991448739")
    BonusType getBonusType(double x, double y);

    @objid ("c44f236b-1aef-4810-9123-388e5ed593c1")
    Direction getArrowDirection(GridCoordinates gc);

    @objid ("0048caaf-5a58-438a-8b3b-1a2a4c41cdec")
    Direction getArrowDirection(double x, double y);

    @objid ("9ae4fa5d-6b6e-4175-965a-97e27b2ffd71")
    List<GridCoordinates> getSpawningLocations();

    @objid ("dd7db2ac-d4cd-468e-898b-44e1d1a03ec1")
    List<Entity> getEntities(GridCoordinates gc);

    @objid ("2b9b4e06-49aa-4e13-9e04-9cf4ba0215cd")
    List<Entity> getEntities(double x, double y);
    
    /**
     * Controlle si la case contient une bombe
     * @param gc les coordonnées de grille à tester
     * @return true si oui, false sinon
     */
    @objid ("3b2fd7f0-8414-413a-901e-056affb0caaa")
    boolean hasBomb(GridCoordinates gc);
    
    /**
     * Controlle si la case contient une bombe
     * @param x la coordonnée en x en pixel
     * @param y la coordonnée en y en pixel
     * @return true si oui, false sinon
     */
    @objid ("ebf83d6b-67bf-4a64-8ffd-66f4c85e696c")
    boolean hasBomb(double x, double y);
    
    /**
     * Récupère la première entité qui est une bombe dans une case de grille
     * @param gc les coordonnées de grille 
     * @return la bombe
     */
    @objid ("d94e1bf1-9316-4a89-92a9-ec5097fe66fa")
    Bomb getFirstBomb(GridCoordinates gc);
    
    /**
     * Récupère la première entité qui est une bombe dans une case de grille
     * @param x la coordonnée en pixel en x
     * @param y la coordonnée en pixel en y
     * @return la bombe
     */
    @objid ("f4e1a119-f17f-4b10-850f-d1e5cfe70648")
    Bomb getFirstBomb(double x, double y);

}
