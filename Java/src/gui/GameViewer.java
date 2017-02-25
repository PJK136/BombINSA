package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("932712f7-e00f-431a-abd5-e4322b7407bd")
public class GameViewer extends JPanel {
    @objid ("12584c94-a9fc-48b1-bdf6-42c3345b8404")
     BufferedImage world;

    @objid ("e8b05c80-1463-4060-8ffd-82157c92adb5")
    public GameViewer() {
    }

    @objid ("18e3e04f-dec2-45e2-a3f5-dbabb34447b4")
    void setWorldImage(BufferedImage world) {
        this.world = world;
    }

    @objid ("8a85e92f-ba76-4ae7-8d93-ab5ea648949a")
    @Override
    protected void paintComponent(Graphics g) {
        //TODO : Remove filling the background with white color
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(world, 0, 0, this);
    }

}
