package main.gui;

import java.awt.Dialog;


import javax.swing.JDialog;



import main.logic.Sheet;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class SheetDetailSelector extends JDialog {
	public String classID=null;
	private ArrayList<String> newSubjects = null; 

	/**
	 * Create the dialog.
	 */
	public SheetDetailSelector(Base base) {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		newSubjects = new ArrayList<String>();
		for (String subject: base.sheets.getSubjects()){
			newSubjects.add(subject);
		}
		
		setTitle("Sheet Details");
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(base);
		getContentPane().setLayout(null);
		
		JLabel lblClassIdentityLetter = new JLabel("Class identity");
		lblClassIdentityLetter.setToolTipText("eg. A, B, M1. M2, etc.");
		lblClassIdentityLetter.setBounds(38, 38, 112, 14);
		getContentPane().add(lblClassIdentityLetter);
		
		JTextField txtID = new JTextField();
		lblClassIdentityLetter.setLabelFor(txtID);
		txtID.setBounds(168, 35, 180, 20);
		getContentPane().add(txtID);
		txtID.setColumns(10);
		
		JComboBox<String> cmbSubject1 = new JComboBox<String>();
		cmbSubject1.setBounds(168, 75, 180, 20);
		getContentPane().add(cmbSubject1);
		
		JComboBox<String> cmbSubject2 = new JComboBox<String>();
		cmbSubject2.setBounds(168, 107, 180, 20);
		getContentPane().add(cmbSubject2);
		
		JComboBox<String> cmbSubject3 = new JComboBox<String>();
		cmbSubject3.setBounds(168, 139, 180, 20);
		getContentPane().add(cmbSubject3);
		
		//handle subject names
		for(String subject: newSubjects){
			cmbSubject1.addItem(subject);
			cmbSubject2.addItem(subject);
			cmbSubject3.addItem(subject);
		}
		
		JLabel lblSubject1 = new JLabel("Subject 1 Name");
		lblSubject1.setLabelFor(cmbSubject1);
		lblSubject1.setBounds(38, 78, 112, 14);
		getContentPane().add(lblSubject1);
		
		JLabel lblSubject2 = new JLabel("Subject 2 Name");
		lblSubject2.setLabelFor(cmbSubject2);
		lblSubject2.setBounds(38, 110, 112, 14);
		getContentPane().add(lblSubject2);
		
		JLabel lblSubject3 = new JLabel("Subject 3 Name");
		lblSubject3.setLabelFor(cmbSubject3);
		lblSubject3.setBounds(38, 142, 112, 14);
		getContentPane().add(lblSubject3);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				classID = txtID.getText();
				
				for (String classid: base.sheets.getClassIds()){
					if(classID.equals(classid)){
						JOptionPane.showMessageDialog(null, "Cannot proceed. Class id is already there. Use new id.", "Error", JOptionPane.WARNING_MESSAGE);
						classID = null;
						return;
					}
				}
				
				if (cmbSubject1.getItemCount()==0 || cmbSubject2.getItemCount()==0 || cmbSubject3.getItemCount()==0){
					JOptionPane.showMessageDialog(null, "You can add new subjects using 'not here' option. Then select three subjects", "Error", JOptionPane.WARNING_MESSAGE);
					classID = null;
					return;
				}
				
				if (cmbSubject1.getSelectedItem()==cmbSubject2.getSelectedItem() || cmbSubject1.getSelectedItem()==cmbSubject3.getSelectedItem() || cmbSubject3.getSelectedItem()==cmbSubject2.getSelectedItem()){
					JOptionPane.showMessageDialog(null, "Please choose different subjects.", "Error", JOptionPane.WARNING_MESSAGE);
					classID = null;
					return;
				}

				if (!base.sheets.getSubjects().contains((String)cmbSubject1.getSelectedItem())){
					base.sheets.getSubjects().add((String)cmbSubject1.getSelectedItem());
				}
				
				if (!base.sheets.getSubjects().contains((String)cmbSubject2.getSelectedItem())){
					base.sheets.getSubjects().add((String)cmbSubject2.getSelectedItem());
				}
				
				if (!base.sheets.getSubjects().contains((String)cmbSubject3.getSelectedItem())){
					base.sheets.getSubjects().add((String)cmbSubject3.getSelectedItem());
				}
				
				base.sheets.getClassIds().add(classID);
				
				//create a sheet and add to sheets
				Vector<Object> v = new Vector<Object>();
				v.addElement(null);v.addElement(null);v.addElement(null);v.addElement(null);
				v.addElement(null);v.addElement(null);v.addElement(null);
				Vector<Vector<Object>> V = new Vector<Vector<Object>>();
				V.addElement(v);
				base.sheets.getSheets().add(new Sheet(V, classID, (String)cmbSubject1.getSelectedItem(), (String)cmbSubject2.getSelectedItem(), (String)cmbSubject3.getSelectedItem()));
				
				SheetDetailSelector.this.dispose();
				
				
			}
		});
		btnApply.setBounds(272, 189, 89, 23);
		getContentPane().add(btnApply);
		
		JLabel lblNotHere = new JLabel("not here");
		lblNotHere.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNotHere.setBounds(361, 140, 56, 14);
		getContentPane().add(lblNotHere);
		
		JLabel label = new JLabel("not here");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String subjectName = JOptionPane.showInputDialog("Please enter the new subject name: ");
				
				if (subjectName==null || subjectName.equals("")){
					return;
				}
				
				for(String subject: newSubjects){
					if (subject.equals(subjectName)){
						JOptionPane.showMessageDialog(null, "Subject name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				newSubjects.add(subjectName);
				
				//clear
				cmbSubject1.removeAllItems();
				cmbSubject2.removeAllItems();
				cmbSubject3.removeAllItems();
				
				//fill		
				for(String subject: newSubjects){
					cmbSubject1.addItem(subject);
					cmbSubject2.addItem(subject);
					cmbSubject3.addItem(subject);
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(Color.DARK_GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(Color.WHITE);
			}
		});
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.setBounds(361, 109, 56, 14);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("not here");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String subjectName = JOptionPane.showInputDialog("Please enter the new subject name: ");
				
				if (subjectName==null || subjectName.equals("")){
					return;
				}
				
				for(String subject: newSubjects){
					if (subject.equals(subjectName)){
						JOptionPane.showMessageDialog(null, "Subject name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				newSubjects.add(subjectName);
				
				//clear
				cmbSubject1.removeAllItems();
				cmbSubject2.removeAllItems();
				cmbSubject3.removeAllItems();
				
				//fill		
				for(String subject: newSubjects){
					cmbSubject1.addItem(subject);
					cmbSubject2.addItem(subject);
					cmbSubject3.addItem(subject);
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				label_1.setForeground(Color.DARK_GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				label_1.setForeground(Color.WHITE);
			}
		});
		label_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label_1.setBounds(361, 78, 56, 14);
		getContentPane().add(label_1);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SheetDetailSelector.this.dispose();
			}
		});
		btnCancel.setBounds(124, 188, 80, 24);
		getContentPane().add(btnCancel);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setIcon(new ImageIcon(SheetDetailSelector.class.getResource("/main/resources/background/ProjectDetailSelector.png")));
		lblBackground.setBounds(0, 0, 440, 270);
		getContentPane().add(lblBackground);
		
		lblNotHere.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String subjectName = JOptionPane.showInputDialog("Please enter the new subject name: ");
				
				if (subjectName==null || subjectName.equals("")){
					return;
				}
				
				for(String subject: newSubjects){
					if (subject.equals(subjectName)){
						JOptionPane.showMessageDialog(null, "Subject name already exists.", "Error", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				newSubjects.add(subjectName);
				
				//clear
				cmbSubject1.removeAllItems();
				cmbSubject2.removeAllItems();
				cmbSubject3.removeAllItems();
				
				//fill		
				for(String subject: newSubjects){
					cmbSubject1.addItem(subject);
					cmbSubject2.addItem(subject);
					cmbSubject3.addItem(subject);
				}
				
			
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNotHere.setForeground(Color.DARK_GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNotHere.setForeground(Color.WHITE);
			}
		});

	}

	public String getClassID() {
		if (classID==null || classID.equals("")){
			return null;
		}else{
			return classID;
		}
	}
}
