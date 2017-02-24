package gui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Component;
import javax.swing.Box;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

@objid ("580889f0-8e1d-4a59-9bfb-00c967a10ffd")
public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		frame.getContentPane().add(horizontalGlue, BorderLayout.WEST);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		frame.getContentPane().add(horizontalGlue_1, BorderLayout.EAST);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("New button");
		panel.add(btnNewButton);
		frame.setContentPane(new Menu());
	}

}
