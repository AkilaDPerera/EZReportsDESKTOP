package main.gui.support;


import javax.swing.JDialog;

import javax.swing.JLabel;

import main.gui.Base;

import javax.swing.ImageIcon;
import java.awt.Color;

@SuppressWarnings("serial")
public class Processing extends JDialog {
	Base base;

	/**
	 * Create the dialog.
	 */
	public Processing(Base base) {
		getContentPane().setBackground(Color.BLACK);
		this.base = base;
		setType(Type.POPUP);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Processing");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 339, 127);
		setLocationRelativeTo(base);
		getContentPane().setLayout(null);
		
		JLabel lblWaitUntilThe = new JLabel("Wait until the processing is completed");
		lblWaitUntilThe.setBounds(106, 40, 226, 16);
		getContentPane().add(lblWaitUntilThe);
		
		JLabel lblProcessing = new JLabel("");
		lblProcessing.setIcon(new ImageIcon(Processing.class.getResource("/main/resources/gif/logo.gif")));
		lblProcessing.setBounds(0, 0, 105, 99);
		getContentPane().add(lblProcessing);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setBackground(Color.BLACK);
		lblBackground.setBounds(278, 0, -277, 99);
		getContentPane().add(lblBackground);
	}
}
