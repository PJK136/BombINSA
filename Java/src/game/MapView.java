package game;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("27201ab7-2fd3-4c35-abb3-37155f6d5822")
public interface MapView {
    @objid ("f9e27f3e-5590-406d-8e1c-c2283d78b1c0")
    int getWidth();

    @objid ("5d4ce44b-0b1b-467b-a4d4-a9c59b922f08")
    int getHeight();

    @objid ("bc0fe536-ee94-44d9-8252-030f1ca4ddc3")
    int getTileSize();

    @objid ("f966b298-131f-4a66-b7f3-23dee860247f")
    GridCoordinates toGridCoordinates(double x, double y);

    @objid ("530a81b2-8f93-4453-bff3-ab4a31d9de02")
    boolean isCollidable(Entity entity, double x, double y);

    @objid ("81182a1c-ea0f-4d7b-8f92-2f8b49896d52")
    boolean isExploding(double x, double y);

    @objid ("81e81ab9-21a7-423b-ac6c-36f7186cd24a")
    TileType getTileType(double x, double y);

    @objid ("8d579098-48e5-4fc5-9e18-32c991448739")
    BonusType getBonusType(double x, double y);

    @objid ("0048caaf-5a58-438a-8b3b-1a2a4c41cdec")
    Direction getArrowDirection(double x, double y);

    @objid ("9ae4fa5d-6b6e-4175-965a-97e27b2ffd71")
    List<GridCoordinates> getSpawningLocations();

}
