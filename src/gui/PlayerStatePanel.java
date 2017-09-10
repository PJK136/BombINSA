package gui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;


import game.Character;
import game.CharacterAbility;
import game.Player;

/**
 * JPanel qui gère l'affichage de l'état d'un joueur 
 */
public class PlayerStatePanel extends JPanel {
    Player player;
    
    int size;
    
     JLabel lives;

     JLabel bombMax;

     JLabel range;

     JLabel[] abilities;

     JSeparator separator;

    /**
     * Construit un panneau d'affichage de l'état d'un joueur
     * @param playerID ID du joueur
     * @param size Taille des icônes
     */
    PlayerStatePanel(Player player, int size) {       
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        this.player = player;
        
        lives = new JLabel();
        add(lives);
        
        
        bombMax = new JLabel();
        add(bombMax);
        
        range = new JLabel();
        add(range);
        
        abilities = new JLabel[CharacterAbility.values().length];
        
        for (int i = 0; i < abilities.length; i++) {
            abilities[i] = new JLabel(" ");
            abilities[i].setVisible(false);
            add(abilities[i]);
        }
        
        separator = new JSeparator(JSeparator.VERTICAL);
        separator.setMaximumSize(new Dimension(1, Integer.MAX_VALUE));
        add(separator);
        
        setSize(size);
        updatePlayerState();
    }

    /**
     * Met à jour l'état du joueur
     * @param player Joueur
     */
    void updatePlayerState() {       
        Character character = player.getCharacter();
        
        if (character != null) {
            lives.setText("×" + character.getLives() + "  ");
            bombMax.setText("×" + character.getBombMax() + "  ");
            range.setText("×" + character.getRange() + "  ");
            List<Boolean> characterAbilities = character.getCharacterAbilities();
            for (int i = 0; i < characterAbilities.size(); i++) {
                abilities[i].setVisible(characterAbilities.get(i));
            }
        } else {
            lives.setText("×0  ");
            bombMax.setText("×0  ");
            range.setText("×0  ");
            for (int i = 0; i < abilities.length; i++) {
                abilities[i].setVisible(false);
            }
        }
    }

    void setSize(int size) {
        if (this.size == size)
            return;
        
        this.size = size;
        
        SpriteFactory factory = SpriteFactory.getInstance();
        
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
        
        abilities[CharacterAbility.MoreSpeed.ordinal()].setIcon(
                factory.getImageIcon("moreSpeed", size));
        abilities[CharacterAbility.LessSpeed.ordinal()].setIcon(
                factory.getImageIcon("lessSpeed", size));
        abilities[CharacterAbility.Shield.ordinal()].setIcon(
                factory.getImageIcon("shield", size));
        abilities[CharacterAbility.Kick.ordinal()].setIcon(
                factory.getImageIcon("kick", size));
    }
}
