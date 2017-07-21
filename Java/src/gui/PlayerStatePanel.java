package gui;

import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import game.Player;
import game.PlayerAbility;

/**
 * JPanel qui gère l'affichage de l'état d'un joueur 
 */
@objid ("46db285b-3b47-467c-9f0f-477cd2eb3613")
public class PlayerStatePanel extends JPanel {
    int size;
    
    String bombermanIcon;
    
    @objid ("a93c2a2a-7d08-409f-8eff-a00ecdf4e7ae")
     JLabel lives;

    @objid ("3719b0d6-ed48-4e64-a269-e35145d82253")
     JLabel bombMax;

    @objid ("3a94a784-a738-4e02-ba1d-acc59d9a4fb1")
     JLabel range;

    @objid ("07d17f65-c576-4265-87c8-7e4c20174627")
     JLabel[] abilities;

    @objid ("cc0439d1-6524-42e4-ae85-4fba137c27d9")
     JSeparator separator;

    /**
     * Construit un panneau d'affichage de l'état d'un joueur
     * @param playerID ID du joueur
     * @param size Taille des icônes
     */
    @objid ("e8dc4fa0-32c2-4b8c-90c6-df91088eba72")
    PlayerStatePanel(int playerID, int size) {       
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        PlayerColor[] colors = PlayerColor.values();
        bombermanIcon = "bomberMan_"+colors[playerID % colors.length].name().toLowerCase();
        
        lives = new JLabel();
        add(lives);
        
        
        bombMax = new JLabel();
        add(bombMax);
        
        range = new JLabel();
        add(range);
        
        abilities = new JLabel[PlayerAbility.values().length];
        
        for (int i = 0; i < abilities.length; i++) {
            abilities[i] = new JLabel(" ");
            abilities[i].setVisible(false);
            add(abilities[i]);
        }
        
        setSize(size);
        
        separator = new JSeparator(JSeparator.VERTICAL);
        separator.setMaximumSize(new Dimension(1, Integer.MAX_VALUE));
        add(separator);
    }

    /**
     * Met à jour l'état du joueur
     * @param player Joueur
     */
    @objid ("8536a41a-23a7-444d-8f96-1adeb430eee3")
    void updatePlayerState(Player player) {
        lives.setText("×" + player.getLives() + "  ");
        bombMax.setText("×" + player.getBombMax() + "  ");
        range.setText("×" + player.getRange() + "  ");
        List<Boolean> playerAbilities = player.getPlayerAbilities();
        for (int i = 0; i < playerAbilities.size(); i++) {
            abilities[i].setVisible(playerAbilities.get(i));
        }
    }

    void setSize(int size) {
        if (this.size == size)
            return;
        
        this.size = size;
        
        SpriteFactory factory = SpriteFactory.getInstance();
        
        lives.setIcon(factory.getImageIcon(bombermanIcon, size));
        MainWindow.setFontSize(lives, size);
        
        
        bombMax.setIcon(factory.getImageIcon("bombMax", size));
        MainWindow.setFontSize(bombMax, size);
        
        range.setIcon(factory.getImageIcon("range", size));
        MainWindow.setFontSize(range, size);
        
        for (int i = 0; i < abilities.length; i++) {
            MainWindow.setFontSize(abilities[i], size);
        }
        
        abilities[PlayerAbility.MoreSpeed.ordinal()].setIcon(
                factory.getImageIcon("moreSpeed", size));
        abilities[PlayerAbility.LessSpeed.ordinal()].setIcon(
                factory.getImageIcon("lessSpeed", size));
        abilities[PlayerAbility.Shield.ordinal()].setIcon(
                factory.getImageIcon("shield", size));
        abilities[PlayerAbility.Kick.ordinal()].setIcon(
                factory.getImageIcon("kick", size));
    }
}
