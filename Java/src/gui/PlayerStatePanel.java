package gui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import game.BonusType;
import game.Player;
import game.PlayerAbility;

public class PlayerStatePanel extends JPanel {

    JLabel lives;
    JLabel bombMax;
    JLabel range;
    JLabel[] abilities;
    JSeparator separator;
    
    PlayerStatePanel(int playerID, int size) {
        SpriteFactory factory = SpriteFactory.getInstance();
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        PlayerColor[] colors = PlayerColor.values();
        int color = playerID % colors.length;
        
        lives = new JLabel();
        lives.setIcon(factory.getImageIcon("bomberMan_"+colors[color].name().toLowerCase(), size));
        GamePanel.setFontSize(lives, size);
        add(lives);
        
        bombMax = new JLabel();
        bombMax.setIcon(factory.getImageIcon("bonus"+BonusType.MoreBomb.ordinal(), size));
        GamePanel.setFontSize(bombMax, size);
        add(bombMax);
        
        range = new JLabel();
        range.setIcon(factory.getImageIcon("bonus"+BonusType.MoreRange.ordinal(), size));
        GamePanel.setFontSize(range, size);
        add(range);
        
        abilities = new JLabel[PlayerAbility.values().length];
        
        for (int i = 0; i < abilities.length; i++) {
            abilities[i] = new JLabel(" ");
            GamePanel.setFontSize(abilities[i], size);
            abilities[i].setVisible(false);
            add(abilities[i]);
        }
        
        abilities[PlayerAbility.MoreSpeed.ordinal()].setIcon(
                factory.getImageIcon("bonus"+BonusType.MoreSpeed.ordinal(), size));
        abilities[PlayerAbility.LessSpeed.ordinal()].setIcon(
                factory.getImageIcon("bonus"+BonusType.LessSpeed.ordinal(), size));
        abilities[PlayerAbility.Shield.ordinal()].setIcon(
                factory.getImageIcon("bonus"+BonusType.Shield.ordinal(), size));
        abilities[PlayerAbility.Kick.ordinal()].setIcon(
                factory.getImageIcon("bonus"+BonusType.Kick.ordinal(), size));
        
        separator = new JSeparator(JSeparator.VERTICAL);
        separator.setMaximumSize(new Dimension(1, Integer.MAX_VALUE));
        add(separator);
    }
    
    void updatePlayerState(Player player) {
        lives.setText("×" + player.getLives() + "  ");
        bombMax.setText("×" + player.getBombMax() + "  ");
        range.setText("×" + player.getRange() + "  ");
        List<Boolean> playerAbilities = player.getPlayerAbilities();
        for (int i = 0; i < playerAbilities.size(); i++) {
            abilities[i].setVisible(playerAbilities.get(i));
        }
    }
}
