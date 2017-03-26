package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("510df795-41e1-4cc6-8d34-c2e51f231aeb")
public interface Controller {
    @objid ("2de0046a-fb4b-4667-ae22-fccfb9d3d853")
    void setPlayer(Player player);

    @objid ("b4919637-6675-4e6b-bd27-a18c64057a48")
    void setWorldView(World world);

    @objid ("0ebad723-c10a-4def-be2c-c30ca87e26c9")
    Direction getDirection();

    @objid ("fbd83e0a-a70c-4e3b-a076-312bb8d237d8")
    boolean isPlantingBomb();

    @objid ("99886342-ec39-44a9-aae1-581d9aa251df")
    void update();

    @objid ("ca776232-1a17-4e2e-a7a6-eeffe4a104e4")
    String getName();

}
