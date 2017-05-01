package main.gui;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.logic.Sheets;
import main.logic.SubjectReport;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ShowOutput extends JDialog {
	private JTable table;
	private JLabel lblGraph;
	/**
	 * Create the dialog.
	 */
	public ShowOutput(Base base, DefaultTableModel model, String reportType, String class_name, Sheets sheets, SubjectReport report) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Output");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 768, 514);
		setMinimumSize(new Dimension((int) (Base.width*0.75), (int) (Base.height*0.75)));
		setMaximumSize(new Dimension((int) Base.width, (int) Base.height));
		setLocationRelativeTo(base);
		
		JPanel panelDetails = new JPanel();
		
		JPanel panelTable = new JPanel();
		
		JPanel panelNonTable = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(panelDetails, GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelNonTable, GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelTable, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panelDetails, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panelTable, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
						.addComponent(panelNonTable, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)))
		);
		
		JPanel panelBasic = new JPanel();
		
		JScrollPane scrollPane_graph = new JScrollPane();
		
		GroupLayout gl_panelNonTable = new GroupLayout(panelNonTable);
		gl_panelNonTable.setHorizontalGroup(
			gl_panelNonTable.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNonTable.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelNonTable.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_graph, GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
						.addComponent(panelBasic, GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)))
		);
		gl_panelNonTable.setVerticalGroup(
			gl_panelNonTable.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNonTable.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelBasic, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_graph, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
		);
		
		lblGraph = new JLabel ();
		lblGraph.setVerticalAlignment(SwingConstants.TOP);
		lblGraph.setHorizontalAlignment(SwingConstants.LEFT);
		scrollPane_graph.setViewportView(lblGraph);
		panelBasic.setLayout(null);
		
		JLabel lblSubjectMax = new JLabel("Subject MAX: ");
		lblSubjectMax.setBounds(12, 12, 139, 16);
		panelBasic.add(lblSubjectMax);
		
		JLabel lblSubjectMin = new JLabel("Subject Min:");
		lblSubjectMin.setBounds(163, 12, 139, 16);
		panelBasic.add(lblSubjectMin);
		
		JLabel lblSubjectAvg = new JLabel("Subject AVG: ");
		lblSubjectAvg.setBounds(314, 12, 139, 16);
		panelBasic.add(lblSubjectAvg);
		
		JLabel lblStdD = new JLabel("Subject Std Deviation:");
		lblStdD.setBounds(465, 12, 188, 16);
		panelBasic.add(lblStdD);
		panelNonTable.setLayout(gl_panelNonTable);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		JLabel lblReportType = new JLabel("");
		JLabel lblClass = new JLabel("");
		JLabel lblProducedBy = new JLabel("Produced by: "+base.user.getFirstName()+" "+base.user.getLastName());
		
		JLabel lblDatetime = new JLabel("Date & Time: " + dateFormat.format(date));
		
		if (reportType.equals("Class Report")){
			lblReportType = new JLabel("Report Type: Class Report");
			
			lblClass = new JLabel("Year: "+sheets.getYear()+"        Term: "+sheets.getTerm()+"        Grade: "+sheets.getGrade()+"        Class: " + class_name);
			

			
			table = new JTable();
			table.setModel(model);
			
			panelNonTable.setVisible(false);
			panelTable.setVisible(true);
		}else if(reportType.equals("Subject Report")){
			lblReportType = new JLabel("Report Type: Subject Report");
			
			lblClass = new JLabel("Year: "+sheets.getYear()+"        Term: "+sheets.getTerm()+"        Grade: "+sheets.getGrade()+"        Subject: " + class_name);
			

			
			lblSubjectMax.setText("Subject MAX: "+report.getSubjectMax());
			lblSubjectMin.setText("Subject Min: "+report.getSubjectMin());
			lblSubjectAvg.setText("Subject AVG: "+report.getSubjectAvg());
			lblStdD.setText("Subject Std Deviation: "+report.getSubjectSD());

			ImageIcon img = new ImageIcon(System.getProperty("java.io.tmpdir")+class_name+".jpg");
			lblGraph.setIcon(img);

			
			panelTable.setVisible(false);
			panelNonTable.setVisible(true);
		}else{
			lblReportType = new JLabel("Report Type");
			
			lblClass = new JLabel("Class");
			
		}

		GroupLayout gl_panelDetails = new GroupLayout(panelDetails);
		gl_panelDetails.setHorizontalGroup(
			gl_panelDetails.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelDetails.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelDetails.createParallelGroup(Alignment.LEADING)
						.addComponent(lblReportType)
						.addComponent(lblClass)
						.addComponent(lblProducedBy)
						.addComponent(lblDatetime))
					.addContainerGap(675, Short.MAX_VALUE))
		);
		gl_panelDetails.setVerticalGroup(
			gl_panelDetails.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelDetails.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblReportType)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblClass)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblProducedBy)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDatetime)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelDetails.setLayout(gl_panelDetails);
		
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setViewportView(table);
		GroupLayout gl_panelTable = new GroupLayout(panelTable);
		gl_panelTable.setHorizontalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
		);
		gl_panelTable.setVerticalGroup(
			gl_panelTable.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
		);
		panelTable.setLayout(gl_panelTable);
		getContentPane().setLayout(groupLayout);

	}
}
