package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("e22186a6-1bcc-48ec-a2ac-a88a3eb90491")
public class Menu extends JPanel implements ActionListener {
    @objid ("b2c4e4e8-bfe6-429e-a138-b575a0165129")
    private JButton btnLocal;

    @objid ("76d13c2f-6d3a-424a-8142-c2979822d6d4")
    private JButton btnNetwork;

    @objid ("c002cb95-7e3b-4f7d-802c-adc8b88d5146")
    private JButton btnSandbox;

    @objid ("a8f404a7-a099-4fdc-b955-5012e1502ee4")
    private JButton btnCreator;

    @objid ("954b1ba0-6aed-4f56-a7c5-e04330ffdfdf")
    private JButton btnQuit;

    @objid ("a31a2175-f166-434f-b6fd-96a25b344584")
    public Menu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        add(Box.createVerticalGlue());
        
        JLabel lblBombinsa = new JLabel("BombINSA");
        lblBombinsa.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBombinsa.setFont(new Font("Dialog", Font.BOLD, 72));
        add(lblBombinsa);
        
        add(Box.createVerticalStrut(40));
        
        btnLocal = addButton("Local");
        btnNetwork = addButton("Réseau");
        btnSandbox = addButton("Sandbox");
        btnCreator = addButton("Créateur de carte");
        btnQuit = addButton("Quitter");
        
        add(Box.createVerticalGlue());
    }

    @objid ("07a869f1-82e5-45c3-9ad9-a445c4aa7dde")
    private JButton addButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, button.getMaximumSize().height));
        button.addActionListener(this);
        add(button);
        add(Box.createVerticalStrut(20));
        return button;
    }

    @objid ("8346e6ce-7734-4a0c-a6b2-83edb22b96c5")
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btnQuit) {
            System.exit(0);
        }
    }

}
