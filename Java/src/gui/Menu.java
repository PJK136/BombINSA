package gui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import java.awt.Dimension;

@objid ("e22186a6-1bcc-48ec-a2ac-a88a3eb90491")
public class Menu extends JPanel implements ActionListener {
	private JButton btnLocal;
	private JButton btnMultijoueur;
	private JButton btnSandbox;
	private JButton btnCreateur;
	private JButton btnQuitter;
	
	private JButton addButton(String text) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setMaximumSize(new Dimension(160, button.getMaximumSize().height));
		button.addActionListener(this);
		add(button);
		Component verticalStrut = Box.createVerticalStrut(20);
		add(Box.createVerticalStrut(20));
		return button;
	}
	
	public Menu() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(Box.createVerticalGlue());
		
		JLabel lblBombinsa = new JLabel("BombINSA");
		lblBombinsa.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblBombinsa.setFont(new Font("Dialog", Font.BOLD, 72));
		add(lblBombinsa);

		add(Box.createVerticalStrut(40));
		
		btnLocal = addButton("Local");
		btnMultijoueur = addButton("Multijoueur");
		btnSandbox = addButton("Sandbox");
		btnCreateur = addButton("Créateur de carte");
		btnQuitter = addButton("Quitter");

		add(Box.createVerticalGlue());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == btnQuitter) {
			System.exit(0);
		}
	}
}
