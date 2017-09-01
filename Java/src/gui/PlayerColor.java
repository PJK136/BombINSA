package gui;

import java.awt.Color;


/** Représente la couleur du joueur */
public enum PlayerColor {
    Blue ("Bleu", Color.blue),
    Red ("Rouge", Color.red),
    Green ("Vert", Color.green),
    Yellow ("Jaune", Color.yellow),
    Pink ("Rose", Color.pink),
    Turquoise ("Turquoise", Color.cyan),
    Orange ("Orange", Color.orange),
    Purple ("Violet", Color.magenta),
    Gray ("Gris", Color.gray);

    private String name;

    private Color color;

    private PlayerColor(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }

    public Color toColor() {
        return color;
    }

}
