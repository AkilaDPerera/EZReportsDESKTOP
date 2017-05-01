package main.gui;


import javax.swing.JDialog;
import java.util.Calendar;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ProjectDetailSelector extends JDialog {
	private int year = -1;
	private int grade = -1;
	private int term = -1;

	/**
	 * Create the dialog.
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ProjectDetailSelector(Base base) {
		setResizable(false);
		setTitle("Project Details");
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(base);
		
		getContentPane().setLayout(null);

		JLabel lblOnceYouSelected = new JLabel("Once you selected, ");
		lblOnceYouSelected.setBackground(Color.BLACK);
		lblOnceYouSelected.setBounds(12, 64, 111, 16);
		getContentPane().add(lblOnceYouSelected);
		
		JLabel label_1 = new JLabel("Be careful while selecting.");
		label_1.setBounds(12, 26, 149, 16);
		getContentPane().add(label_1);
		
		JLabel lblFc = new JLabel("you will not be able to do any changes within the project.");
		lblFc.setBounds(12, 84, 329, 16);
		getContentPane().add(lblFc);
		
		JButton button = new JButton("OK");
		button.setBounds(153, 187, 133, 24);
		getContentPane().add(button);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(null);
		panel.setBounds(12, 124, 420, 24);
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(1, 0, 10, 0));
		
		JComboBox cmbYear = new JComboBox();
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		DefaultComboBoxModel model = new DefaultComboBoxModel<>();
		for (int y=year-5; y<=year+5; y++){
			model.addElement(y);
		}
		model.setSelectedItem(year);
		cmbYear.setModel(model);
		
		cmbYear.setPreferredSize(new Dimension(25, 20));
		cmbYear.setMaximumSize(new Dimension(25, 20));
		panel.add(cmbYear);
		
		JComboBox cmbClass = new JComboBox();
		cmbClass.setModel(new DefaultComboBoxModel(new String[] {"Grade 12", "Grade 13"}));
		cmbClass.setPreferredSize(new Dimension(25, 20));
		cmbClass.setMaximumSize(new Dimension(25, 20));
		panel.add(cmbClass);
		
		JComboBox cmbTerm = new JComboBox();
		cmbTerm.setModel(new DefaultComboBoxModel(new String[] {"Term 1", "Term 2", "Term 3"}));
		cmbTerm.setPreferredSize(new Dimension(25, 20));
		cmbTerm.setMaximumSize(new Dimension(25, 20));
		panel.add(cmbTerm);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(ProjectDetailSelector.class.getResource("/main/resources/background/ProjectDetailSelector.png")));
		label_2.setBounds(0, 0, 444, 272);
		getContentPane().add(label_2);
		
		//OK script on actionPerformed
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProjectDetailSelector.this.year = (int) cmbYear.getItemAt(cmbYear.getSelectedIndex());
				ProjectDetailSelector.this.grade = Integer.valueOf(((String) cmbClass.getItemAt((cmbClass.getSelectedIndex()))).substring(6));
				ProjectDetailSelector.this.term = Integer.valueOf(((String) cmbTerm.getItemAt((cmbTerm.getSelectedIndex()))).substring(5));
				ProjectDetailSelector.this.dispose();
			}
		});

	}

	public int getYear() {
		return year;
	}

	public int getGrade() {
		return grade;
	}

	public int getTerm() {
		return term;
	}

}
