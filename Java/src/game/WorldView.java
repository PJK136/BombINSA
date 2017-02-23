package game;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("ef002ca0-7638-4d64-9860-1c30b5e5c688")
public interface WorldView {
    @objid ("d528a200-b69a-4233-8cd3-96e4aed92199")
    void getFPS();

    @objid ("39ec082e-e21c-4225-b905-acf6c47cbd9b")
    int getTimeRemaining();

    @objid ("32dcdef1-5a7a-4722-92ad-5d4d0aa3f197")
    List<Entity> getEntities();

    @objid ("b3701478-0bf1-402b-98bd-a71cb1065097")
    MapView getMap();

}
