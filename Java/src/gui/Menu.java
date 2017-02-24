package gui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Font;
import javax.swing.JButton;

@objid ("e22186a6-1bcc-48ec-a2ac-a88a3eb90491")
public class Menu extends JPanel {
	public Menu() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);
		
		JLabel lblBombinsa = new JLabel("BombINSA");
		add(lblBombinsa);
		lblBombinsa.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblBombinsa.setFont(new Font("Dialog", Font.BOLD, 48));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		JButton btnLocal = new JButton("Local");
		add(btnLocal);
		btnLocal.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1);
		
		JButton btnMultijoueur = new JButton("Multijoueur");
		btnMultijoueur.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnMultijoueur);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		add(verticalStrut_2);
		
		JButton btnSandbox = new JButton("Sandbox");
		btnSandbox.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnSandbox);
		
		Component verticalStrut_3 = Box.createVerticalStrut(20);
		add(verticalStrut_3);
		
		JButton btnOptions = new JButton("Options");
		btnOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnOptions);
		
		Component verticalStrut_4 = Box.createVerticalStrut(20);
		add(verticalStrut_4);
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnQuitter);
		
		Component verticalGlue_1 = Box.createVerticalGlue();
		add(verticalGlue_1);
	}
}
