package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import game.Character;
import game.CharacterAbility;
import game.Player;

/**
 * JPanel qui gère l'affichage de l'état d'un joueur
 */
public class PlayerStatePanel extends JPanel {
    JPanel topLine;

    JPanel bottomLine;

    Player player;

    int size;

     JLabel name;

     JLabel lives;

     JLabel bombMax;

     JLabel range;

     ImageIcon[] abilityIcons;

     ImageIcon placeholder;

     JLabel[] abilities;

     JSeparator separator;

    /**
     * Construit un panneau d'affichage de l'état d'un joueur
     * @param playerID ID du joueur
     * @param size Taille des icônes
     */
    PlayerStatePanel(Player player, int size) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(separator);

        name = new JLabel();
        name.setAlignmentX(CENTER_ALIGNMENT);
        add(name);

        topLine = new JPanel();
        topLine.setLayout(new BoxLayout(topLine, BoxLayout.X_AXIS));
        add(topLine);

        lives = new JLabel();
        topLine.add(lives);

        bombMax = new JLabel();
        topLine.add(bombMax);

        range = new JLabel();
        topLine.add(range);

        bottomLine = new JPanel();
        bottomLine.setLayout(new BoxLayout(bottomLine, BoxLayout.X_AXIS));
        add(bottomLine);

        bottomLine.add(Box.createHorizontalGlue());

        abilityIcons = new ImageIcon[CharacterAbility.values().length];
        abilities = new JLabel[CharacterAbility.values().length];

        for (int i = 0; i < abilities.length; i++) {
            abilities[i] = new JLabel(" ");
            bottomLine.add(abilities[i]);
        }

        this.player = player;

        setSize(size);
        updatePlayerState();
    }

    /**
     * Met à jour l'état du joueur
     * @param player Joueur
     */
    void updatePlayerState() {
        if (player.getController() != null)
            name.setText(player.getController().getName());

        Character character = player.getCharacter();

        if (character != null) {
            if (character.getLives() <= 0)
                name.setForeground(Color.red);
            else
                name.setForeground(Color.black);

            lives.setText("×" + character.getLives() + "  ");
            bombMax.setText("×" + character.getBombMax() + "  ");
            range.setText("×" + character.getRange() + "  ");
            List<Boolean> characterAbilities = character.getCharacterAbilities();
            for (int i = 0; i < characterAbilities.size(); i++) {
                if (characterAbilities.get(i))
                    abilities[i].setIcon(abilityIcons[i]);
                else
                    abilities[i].setIcon(placeholder);
            }
        } else {
            lives.setText("×0  ");
            bombMax.setText("×0  ");
            range.setText("×0  ");
            for (int i = 0; i < abilities.length; i++) {
                abilities[i].setIcon(placeholder);
            }
        }
    }

    void setSize(int size) {
        if (this.size == size)
            return;

        this.size = size;

        SpriteFactory factory = SpriteFactory.getInstance();

        MainWindow.setFontSize(name, size/2);

        PlayerColor[] colors = PlayerColor.values();
        int color = player.getID() % colors.length;
        String bombermanIcon = "bomberMan_"+colors[color].name().toLowerCase();
        lives.setIcon(factory.getImageIcon(bombermanIcon, size));
        MainWindow.setFontSize(lives, size);

        bombMax.setIcon(factory.getImageIcon("bombMax", size));
        MainWindow.setFontSize(bombMax, size);

        range.setIcon(factory.getImageIcon("range", size));
        MainWindow.setFontSize(range, size);

        for (int i = 0; i < abilities.length; i++) {
            MainWindow.setFontSize(abilities[i], size);
        }

        placeholder = factory.getImageIcon("noBonus", size);

        abilityIcons[CharacterAbility.MoreSpeed.ordinal()] =
                factory.getImageIcon("moreSpeed", size);
        abilityIcons[CharacterAbility.LessSpeed.ordinal()] =
                factory.getImageIcon("lessSpeed", size);
        abilityIcons[CharacterAbility.Shield.ordinal()] =
                factory.getImageIcon("shield", size);
        abilityIcons[CharacterAbility.Kick.ordinal()] =
                factory.getImageIcon("kick", size);
    }
}
