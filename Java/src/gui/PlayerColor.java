package gui;

import java.awt.Color;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/** Représente la couleur du joueur */
@objid ("91bb794c-142d-4d2f-b0c0-6cc9df1ef66e")
public enum PlayerColor {
    Blue ("Bleu", Color.blue),
    Red ("Rouge", Color.red),
    Green ("Vert", Color.green),
    Yellow ("Jaune", Color.yellow),
    Pink ("Rose", Color.pink),
    Turquoise ("Turquoise", Color.cyan),
    Orange ("Orange", Color.orange),
    Purple ("Violet", Color.magenta);

    @objid ("cdcc9b1e-372f-450c-a0ff-009b4a5bb7ce")
    private String name;

    @objid ("385bb6b1-8b06-417e-bfe3-cc5e5d26e488")
    private Color color;

    @objid ("68cd633e-1ea2-48c1-85d6-c2041a2df082")
    private PlayerColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @objid ("2704c296-4c0a-4ab0-b70c-2339db2c38e4")
    public String toString() {
        return name;
    }

    @objid ("ef2fdc32-9ee1-4106-9635-3dbe8a73936b")
    public Color toColor() {
        return color;
    }

}
