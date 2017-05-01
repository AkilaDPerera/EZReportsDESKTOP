package main.gui;


import javax.swing.JDialog;
import javax.swing.JPanel;

import main.gui.support.LineGraph;
import main.gui.support.Processing;
import main.gui.support.StatusBarMessage;
import main.logic.ClassReport;
import main.logic.Sheet;
import main.logic.Sheets;
import main.logic.SubjectReport;
import main.logic.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;


import java.awt.Color;

@SuppressWarnings("serial")
public class ReportSettings extends JDialog {
	/**
	 * Create the dialog.
	 */
	public ReportSettings(Base base) {
		getContentPane().setBackground(Color.BLACK);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Generate Report");
		
		setResizable(false);
		setBounds(100, 100, 507, 302);
		setLocationRelativeTo(base);
		getContentPane().setLayout(null);
		
		JPanel panelClass = new JPanel();
		panelClass.setOpaque(false);
		panelClass.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelClass.setBounds(12, 12, 150, 248);
		getContentPane().add(panelClass);
		panelClass.setLayout(null);
		
		JLabel lblClassReport = new JLabel("Class Report");
		lblClassReport.setBounds(12, 12, 126, 16);
		panelClass.add(lblClassReport);
		
		JComboBox<String> cmbClass = new JComboBox<String>();
		cmbClass.setBounds(12, 40, 126, 20);
		panelClass.add(cmbClass);
		
		for (String class_id: base.sheets.getClassIds()){
			cmbClass.addItem(class_id);
		}
		
		JButton btnGen = new JButton("Generate");

		btnGen.setBounds(12, 71, 127, 24);
		panelClass.add(btnGen);
		
		JButton btnSavePDF = new JButton("Save as PDF");
		btnSavePDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String className = (String) cmbClass.getSelectedItem();
				
				Sheets sheets = base.sheets;
				Sheet sheet = sheets.getSheets().get(sheets.getClassIds().indexOf((className)));
				
				String fileLocation = base.srcRoot.substring(0, base.srcRoot.length()-4)+"_"+sheets.getYear()+"_"+sheets.getTerm()+"_"+sheets.getGrade()+"_"+sheet.getClassID()+ ".pdf";
	
				ClassReport.createPDF(base, fileLocation, sheet, ClassReport.genReport(sheets, className), className, sheets);
				JOptionPane.showMessageDialog(ReportSettings.this, "File is saved in the project location", "Successful", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnSavePDF.setBounds(12, 106, 126, 24);
		panelClass.add(btnSavePDF);
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String className = (String) cmbClass.getSelectedItem();
				Sheets sheets = base.sheets;

				int year = base.sheets.getYear();
				int term = base.sheets.getTerm();
				int grade = base.sheets.getGrade();
				
				User user = base.user;
				
				Processing pDialog = new Processing(base);
				new Thread() {
					@Override
					public void run() {
						ClassReport.upload(user, year, term, grade, className, ClassReport.genReport(sheets, className), base.lblStatus);
						(new StatusBarMessage(base.lblStatus, "Uploading completed...")).run();
						JOptionPane.showMessageDialog(ReportSettings.this, "Uploading successful...", "Uploading", JOptionPane.INFORMATION_MESSAGE);
						pDialog.dispose();
					}
				}.start();
				pDialog.setVisible(true);
			}
		});
		btnUpload.setBounds(12, 141, 126, 24);
		panelClass.add(btnUpload);
		
		JButton btnNewButton = new JButton("Upload all");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sheets sheets = base.sheets;

				int year = base.sheets.getYear();
				int term = base.sheets.getTerm();
				int grade = base.sheets.getGrade();
				
				User user = base.user;
				
				Processing pDialog = new Processing(base);
				new Thread() {
					@Override
					public void run() {
						ClassReport.uploadAll(user, year, term, grade, sheets, base.lblStatus);
						(new StatusBarMessage(base.lblStatus, "Uploading completed...")).run();
						JOptionPane.showMessageDialog(ReportSettings.this, "Uploading successful...", "Uploading", JOptionPane.INFORMATION_MESSAGE);
						pDialog.dispose();
					}
				}.start();
				pDialog.setVisible(true);
			}
		});
		btnNewButton.setBounds(12, 211, 126, 24);
		panelClass.add(btnNewButton);
		
		JPanel panelSubject = new JPanel();
		panelSubject.setOpaque(false);
		panelSubject.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelSubject.setBounds(174, 12, 150, 248);
		getContentPane().add(panelSubject);
		panelSubject.setLayout(null);
		
		JLabel lblSubject = new JLabel("Subject Report");
		lblSubject.setBounds(12, 12, 126, 16);
		panelSubject.add(lblSubject);
		
		JComboBox<String> cmbSubjects = new JComboBox<String>();
		cmbSubjects.setBounds(12, 41, 126, 20);
		panelSubject.add(cmbSubjects);
		
		JButton btnGenSub = new JButton("Generate");
		btnGenSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String subject = (String)cmbSubjects.getSelectedItem();
				SubjectReport report = new SubjectReport(subject);
				new LineGraph(report.getData(), subject);
				ShowOutput output = new ShowOutput(base, null, "Subject Report", subject, base.sheets, report);
				output.setVisible(true);
			}
		});
		btnGenSub.setBounds(12, 72, 126, 24);
		panelSubject.add(btnGenSub);
		
		JButton btnSavePDFSub = new JButton("Save as PDF");
		btnSavePDFSub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sheets sheets = base.sheets;
				
				String fileLocation = base.srcRoot.substring(0, base.srcRoot.length()-4)+"_"+sheets.getYear()+"_"+sheets.getTerm()+"_"+sheets.getGrade()+"_sub_report.pdf";
				String subject = (String)cmbSubjects.getSelectedItem();
				SubjectReport report = new SubjectReport(subject);
				new LineGraph(report.getData(), subject);
				report.createPDFForSubject(base, fileLocation, sheets, subject, System.getProperty("java.io.tmpdir")+subject+"s.jpg");
				JOptionPane.showMessageDialog(ReportSettings.this, "File is saved in the project location", "Successful", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnSavePDFSub.setBounds(12, 107, 126, 24);
		panelSubject.add(btnSavePDFSub);
		
		for (String subject: base.sheets.getSubjects()){
			cmbSubjects.addItem(subject);
		}
		
		JPanel panelIndividual = new JPanel();
		panelIndividual.setOpaque(false);
		panelIndividual.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelIndividual.setBounds(336, 12, 150, 250);
		getContentPane().add(panelIndividual);
		panelIndividual.setLayout(null);
		
		JLabel lblIndividual = new JLabel("Individual Report");
		lblIndividual.setBounds(12, 12, 126, 16);
		panelIndividual.add(lblIndividual);
		
		JComboBox<String> cmbClasses = new JComboBox<String>();
		cmbClasses.setBounds(12, 76, 126, 20);
		panelIndividual.add(cmbClasses);
		
		for (String class_id: base.sheets.getClassIds()){
			cmbClasses.addItem(class_id);
		}
		
		JButton btnSavePDFIndi = new JButton("Save as PDF");
		btnSavePDFIndi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String className = (String) cmbClasses.getSelectedItem();
				
				Sheets sheets = base.sheets;
				Sheet sheet = sheets.getSheets().get(sheets.getClassIds().indexOf((className)));
				
				String fileLocation = base.srcRoot.substring(0, base.srcRoot.length()-4)+"_"+sheets.getYear()+"_"+sheets.getTerm()+"_"+sheets.getGrade()+"_"+sheet.getClassID()+"_Individuals.pdf";
	
				ClassReport.createPDFForIndividual(base, fileLocation, sheet, ClassReport.genReport(sheets, className), className, sheets);
				JOptionPane.showMessageDialog(ReportSettings.this, "File is saved in the project location", "Successful", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnSavePDFIndi.setBounds(12, 107, 126, 24);
		panelIndividual.add(btnSavePDFIndi);
		
		JLabel lblThisIsFor = new JLabel("<html>This is for the <br>printing purposes</html>");
		lblThisIsFor.setBounds(12, 40, 126, 32);
		panelIndividual.add(lblThisIsFor);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ProjectDetailSelector.class.getResource("/main/resources/background/reportset.png")));
		lblNewLabel.setBounds(0, 0, 501, 274);
		getContentPane().add(lblNewLabel);
		
		btnGen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ShowOutput output = new ShowOutput(base, ClassReport.genReport(base.sheets,(String) cmbClass.getSelectedItem()),"Class Report", (String) cmbClass.getSelectedItem(), base.sheets, null);
				output.setVisible(true);
			}
		});
	}
}
