package game;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("8d1e22ca-441c-437e-83a3-fee76166baff")
public class Server extends World {
    @objid ("560005cd-1e82-4dc8-8a17-39d3577463ae")
    public Server(String mapFilename, int tileSize, int FPS, int duration) {
    }

    @objid ("2aa100c7-ebde-4cd8-840f-24b2f13f54cd")
    public void setFPS(int fps) {
    }

    @objid ("77769968-3eb5-4133-880a-e74cedee78ae")
    public void setTimeRemaining(int time) {
    }

    @objid ("739ccd1c-6053-48a2-a809-596cf4134d36")
    public void setTileSize(int tileSize) {
    }

    @objid ("4164c416-9e5c-461f-a7dc-1758c0f94d36")
    public void loadMap(String filename) {
    }

    @objid ("3201955a-ab70-48b8-b676-a53ca4da06a7")
    @Override
    public void newPlayer(Controller controller) {
        // TODO Auto-generated method stub
    }

    @objid ("15f9ba61-54f9-4783-8bd0-923098e480d7")
    public void update() {
    }

    @objid ("b9fb0ddd-3cff-4226-9167-0e4f94ea4d9e")
    @Override
    void plantBomb(double x, double y, int fire) {
        // TODO Auto-generated method stub
    }

    @objid ("b13fef2c-1897-428c-871d-8a201627e755")
    @Override
    void plantBomb(Player player) {
        // TODO Auto-generated method stub
    }

    @objid ("d7b25576-cf20-47f5-9a75-9bc74eee10c2")
    @Override
    void createExplosion(double x, double y, int fire) {
        // TODO Auto-generated method stub
    }

    @objid ("1883d42c-f691-41ab-acc6-c37fc049f80c")
    @Override
    void pickUpBonus(double x, double y) {
        // TODO Auto-generated method stub
    }

}
