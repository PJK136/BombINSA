package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("edc89ef6-b498-483e-875c-befa52d629f4")
public class GamePanel extends JPanel {
    @objid ("1630e521-8ea4-48df-9278-b85be1fba591")
    private GameViewer gameViewer;

    @objid ("c0d6533a-0897-40bb-94e0-4be89488c38b")
    public GamePanel() {
        setLayout(new BorderLayout(0, 0));
        
        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        JLabel lblNewLabel = new JLabel("👤 : 2 | 💣 : 3 | 💥 : 4 | 👊 👞 🛡");
        lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        panel.add(lblNewLabel);
        
        Component horizontalGlue = Box.createHorizontalGlue();
        panel.add(horizontalGlue);
        
        JLabel lblNewLabel_1 = new JLabel("2:31");
        lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 16));
        panel.add(lblNewLabel_1);
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        panel.add(horizontalStrut);
        
        JButton btnNewButton = new JButton("🚪");
        panel.add(btnNewButton);
        
        gameViewer = new GameViewer();
        add(gameViewer, BorderLayout.CENTER);
    }

}
