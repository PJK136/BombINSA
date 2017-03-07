package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

import game.GridCoordinates;
import game.MapView;
import game.TileType;
import game.WorldView;

@objid ("932712f7-e00f-431a-abd5-e4322b7407bd")
public class GameViewer extends JPanel {
    @objid ("12584c94-a9fc-48b1-bdf6-42c3345b8404")
    private BufferedImage world;

    private BufferedImage tiles[];
    
    @objid ("e8b05c80-1463-4060-8ffd-82157c92adb5")
    public GameViewer() {
        tiles = new BufferedImage[TileType.values().length];
        for (TileType type : TileType.values()) {
            try {
                tiles[type.ordinal()] = ImageIO.read(new File("img/" + type.name().toLowerCase() + ".png"));
            } catch (IOException e) {
                System.err.println("Can't read : " + "img/" + type.name().toLowerCase() + ".png");
            }
        }
    }

    private Color toColor(TileType type) {
        switch (type) {
        case Arrow:
            return Color.GREEN;
        case Bonus:
            return Color.YELLOW;
        case Breakable:
            return Color.GRAY;
        case Empty:
            return Color.WHITE;
        case Unbreakable:
            return Color.BLACK;
        default:
            return Color.RED;
        }
    }
    @objid ("18e3e04f-dec2-45e2-a3f5-dbabb34447b4")
    public void drawWorld(WorldView worldView) {
        drawMap(worldView.getMap());
        //TODO : À enlever ou garder ?
    }
    
    public void drawMap(MapView map) {
        BufferedImage newWorld = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB); //ARGB ?
        Graphics2D g = newWorld.createGraphics();
        g.fillRect(0, 0, map.getWidth(), map.getHeight());
        g.setColor(Color.black);
        //g.setFont(new Font("Dialog", Font.BOLD, map.getTileSize()/2));
        GridCoordinates gc = new GridCoordinates();
        for (gc.x = 0; gc.x < map.getColumnCount(); gc.x++) {
            for (gc.y = 0; gc.y < map.getRowCount(); gc.y++) {
                //g.drawImage(tiles[map.getTileType(gc).ordinal()], gc.x*map.getTileSize(), gc.y*map.getTileSize(), this);
                //g.drawString(String.valueOf(map.getTileType(gc).ordinal()), gc.x*map.getTileSize(), gc.y*map.getTileSize());
                g.setColor(toColor(map.getTileType(gc)));
                g.fillRect(gc.x*map.getTileSize(), gc.y*map.getTileSize(), map.getTileSize(), map.getTileSize());
            }
        }
        world = newWorld;
        repaint();
    }

    @objid ("8a85e92f-ba76-4ae7-8d93-ab5ea648949a")
    @Override
    protected void paintComponent(Graphics g) {
        //TODO : Remove filling the background with RED color
        g.setColor(Color.RED);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(world, 0, 0, this);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(world.getWidth(), world.getHeight());
    }

}
