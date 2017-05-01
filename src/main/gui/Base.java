package main.gui;



import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Robot;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jtattoo.plaf.noire.NoireLookAndFeel;

import main.gui.support.ExcelAdapter;
import main.gui.support.FileChoose;
import main.gui.support.Processing;
import main.gui.support.StatusBarMessage;
import main.logic.APIHandler;
import main.logic.ProjectFile;
import main.logic.SQLiteController;
import main.logic.Sheet;
import main.logic.Sheets;
import main.logic.User;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import java.awt.AWTException;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import javax.swing.DefaultComboBoxModel;


@SuppressWarnings("serial")
public class Base extends JFrame {
	public static double width;
	public static double height;
	
	//Comman variables for all frames
	public Processing pDialog = null;
	Sheets sheets=null;
	Sheet currentSheet=null;
	
	String srcRoot;
	private JTable table;
	
	public User user=null;
	
	public JLabel lblStatus;
	
	//auth configures
	JButton btnAuth;
	JPanel panel_info;
	JLabel lblUsername;
	JLabel lblEmail;
	JLabel lblFirstName;
	JLabel lblLastName;
	JLabel lblSignUp;
	
	JComboBox<String> cmbAPI;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//Changing look and feel
		try {
			NoireLookAndFeel.setTheme("Default", "", "");
			UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException 
		| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Base frame = new Base();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Base() {
		getContentPane().setBackground(Color.BLACK);
		setBackground(Color.BLACK);
		//Identify the screen size
		Frame frame = this;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		//start full screen
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
				
		setTitle("EZReports");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Base.class.getResource("/main/resources/png/Accounting-48.png")));
		setBounds(100, 100, 919, 540);
		setMinimumSize(new Dimension((int) Base.width/2, (int) Base.height/2));
		setMaximumSize(new Dimension((int) Base.width, (int) Base.height));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBarProject = new JToolBar();
		toolBarProject.setForeground(Color.BLACK);
		toolBarProject.setBackground(Color.BLACK);
		toolBarProject.setOpaque(false);
		toolBarProject.setFloatable(false);
		
		JPanel editorPanel = new JPanel();
		editorPanel.setOpaque(false);
		
		JPanel authPanel = new JPanel();
		authPanel.setOpaque(false);
		
		lblStatus = new JLabel("");
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		lblStatus.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblStatus.setToolTipText("Status");
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(editorPanel, GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
						.addComponent(toolBarProject, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblStatus, GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(authPanel, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
					.addGap(0))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(toolBarProject, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(editorPanel, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblStatus))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(authPanel, GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		
		JLabel lblLOGO = new JLabel("");
		lblLOGO.setIcon(new ImageIcon(Base.class.getResource("/main/resources/background/LOGO.png")));
		
		cmbAPI = new JComboBox<String>();
		cmbAPI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				APIHandler.baseURL = (String) cmbAPI.getSelectedItem();
			}
		});
		cmbAPI.setModel(new DefaultComboBoxModel<String>(new String[] {"http://127.0.0.1:8000/api/", "https://ezreports.herokuapp.com/api/"}));
		GroupLayout gl_authPanel = new GroupLayout(authPanel);
		gl_authPanel.setHorizontalGroup(
			gl_authPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_authPanel.createSequentialGroup()
					.addContainerGap(29, Short.MAX_VALUE)
					.addComponent(lblLOGO)
					.addContainerGap())
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
				.addGroup(Alignment.LEADING, gl_authPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(cmbAPI, 0, 175, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_authPanel.setVerticalGroup(
			gl_authPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_authPanel.createSequentialGroup()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
					.addGap(28)
					.addComponent(cmbAPI, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLOGO))
		);
		panel.setLayout(null);
		
		btnAuth = new JButton("Sign In");
		btnAuth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btnAuth.getText().equals("Sign In")){
					Auth auth = new Auth(Base.this, "signin");
					auth.setVisible(true);
					authPanelRefresh();
				}else{
					Base.this.user = null;
					authPanelRefresh();
				}
			}
		});
		btnAuth.setBounds(10, 34, 178, 24);
		panel.add(btnAuth);
		
		panel_info = new JPanel();
		panel_info.setVisible(false);
		panel_info.setBackground(Color.BLACK);
		panel_info.setBounds(0, 95, 199, 185);
		panel.add(panel_info);
		panel_info.setLayout(null);
		
		JLabel lblUserInfo = new JLabel("USER INFO");
		lblUserInfo.setBounds(0, 0, 97, 16);
		panel_info.add(lblUserInfo);
		
		lblUsername = new JLabel("Username");
		lblUsername.setBounds(0, 29, 199, 16);
		panel_info.add(lblUsername);
		
		lblEmail = new JLabel("Email");
		lblEmail.setBounds(0, 52, 199, 16);
		panel_info.add(lblEmail);
		
		lblFirstName = new JLabel("First Name");
		lblFirstName.setBounds(0, 76, 199, 16);
		panel_info.add(lblFirstName);
		
		lblLastName = new JLabel("Last Name");
		lblLastName.setBounds(0, 103, 199, 16);
		panel_info.add(lblLastName);
		
		lblSignUp = new JLabel("Sign up");
		lblSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSignUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblSignUp.setForeground(Color.YELLOW);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblSignUp.setForeground(null);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				Auth auth = new Auth(Base.this, "signout");
				auth.setVisible(true);
				authPanelRefresh();
			}
		});
		lblSignUp.setBounds(145, 55, 42, 16);
		panel.add(lblSignUp);
		
		JLabel lblAuthPanel = new JLabel("Authentiaction panel");
		lblAuthPanel.setBounds(39, 7, 132, 16);
		panel.add(lblAuthPanel);
		authPanel.setLayout(gl_authPanel);
		
		JPanel projectDetailPanel = new JPanel();
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_editorPanel = new GroupLayout(editorPanel);
		gl_editorPanel.setHorizontalGroup(
			gl_editorPanel.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
				.addComponent(projectDetailPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
		);
		gl_editorPanel.setVerticalGroup(
			gl_editorPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_editorPanel.createSequentialGroup()
					.addComponent(projectDetailPanel, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
		);
		
		JPanel projectPanel = new JPanel();
		projectPanel.setOpaque(false);
		projectPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Project Details", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(255, 255, 255)));
		projectPanel.setLayout(null);
		
		JLabel lblYear = new JLabel("");
		lblYear.setBounds(293, 12, 68, 16);
		projectPanel.add(lblYear);
		
		JLabel lblGrade = new JLabel("");
		lblGrade.setBounds(373, 12, 68, 16);
		projectPanel.add(lblGrade);
		
		JLabel lblTerm = new JLabel("");
		lblTerm.setBounds(453, 12, 68, 16);
		projectPanel.add(lblTerm);
		
		JLabel lblName = new JLabel("");
		lblName.setBounds(103, 12, 178, 16);
		projectPanel.add(lblName);
		
		JPanel sheetPanel = new JPanel();
		sheetPanel.setOpaque(false);
		sheetPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Sheet Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_projectDetailPanel = new GroupLayout(projectDetailPanel);
		gl_projectDetailPanel.setHorizontalGroup(
			gl_projectDetailPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_projectDetailPanel.createSequentialGroup()
					.addGap(52)
					.addGroup(gl_projectDetailPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(projectPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_projectDetailPanel.createSequentialGroup()
							.addComponent(sheetPanel, GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
							.addGap(3)))
					.addGap(57))
		);
		gl_projectDetailPanel.setVerticalGroup(
			gl_projectDetailPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_projectDetailPanel.createSequentialGroup()
					.addComponent(projectPanel, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sheetPanel, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
		);
		sheetPanel.setLayout(null);
		
		JButton btnDelete = new JButton("");
		
		btnDelete.setToolTipText("Delete sheet");
		btnDelete.setBorderPainted(false);
		btnDelete.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Trash-32.png")));
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBounds(108, 12, 21, 21);
		sheetPanel.add(btnDelete);
		
		JComboBox<String> cmbSheets = new JComboBox<String>();
		cmbSheets.setBounds(141, 13, 120, 20);
		sheetPanel.add(cmbSheets);
		projectDetailPanel.setLayout(gl_projectDetailPanel);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
			}
		));
		table.setOpaque(false);
		//adding new record at last
		table.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				int rowCount = table.getRowCount();
				int columnCount = table.getColumnCount();
				Object[] array = new Object[columnCount];
				Arrays.fill(array, null);
				
				for (int column=0; column<columnCount; column++){
					if (table.getValueAt(rowCount-1, column)!=null){
						((DefaultTableModel)table.getModel()).addRow(array);
						break;
					}
				}
			}
		});
		
		table.getTableHeader().setReorderingAllowed(false);
		
		scrollPane.setViewportView(table);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(table, popupMenu);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Copy");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Robot r = null;
				try {
					r = new Robot();
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
				r.keyPress(KeyEvent.VK_CONTROL);
			    r.keyPress(KeyEvent.VK_C);
			    r.keyRelease(KeyEvent.VK_C);
			    r.keyRelease(KeyEvent.VK_CONTROL);
			}
		});
		
		JMenuItem mntmSetAbsent = new JMenuItem("Set absent");
		mntmSetAbsent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				
				if ((column>1 && column<6) && (row>=0)){
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.setValueAt(Double.valueOf("-1"), row, column);
				}else{
					JOptionPane.showMessageDialog(frame, "You have to select valid cell to do this...", "Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		popupMenu.add(mntmSetAbsent);
		popupMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Paste");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Robot r = null;
				try {
					r = new Robot();
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
				r.keyPress(KeyEvent.VK_CONTROL);
			    r.keyPress(KeyEvent.VK_V);
			    r.keyRelease(KeyEvent.VK_V);
			    r.keyRelease(KeyEvent.VK_CONTROL);
			}
		});
		popupMenu.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Remove");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Robot r = null;
				try {
					r = new Robot();
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
				r.keyPress(KeyEvent.VK_DELETE);
			    r.keyRelease(KeyEvent.VK_DELETE);
			}
		});
		popupMenu.add(mntmNewMenuItem_3);
		editorPanel.setLayout(gl_editorPanel);
		
		JButton btnNewProject = new JButton("");
		btnNewProject.setToolTipText("Create a Project");
		btnNewProject.setContentAreaFilled(false);
		
		
		
		
		btnNewProject.setBackground(Color.DARK_GRAY);
		btnNewProject.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Create New-32.png")));
		toolBarProject.add(btnNewProject);
		
		JButton btnSave = new JButton("");
		btnSave.setEnabled(false);
		btnSave.setToolTipText("Save project");
		btnSave.setBackground(Color.DARK_GRAY);
		btnSave.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				//TODO Save
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				currentSheet.setData(tableModel.getDataVector());
				ProjectFile.saveFile(srcRoot, sheets);
				
				new Thread(new StatusBarMessage(lblStatus, "Project is saved...")).start();
				
			}
		});
		btnSave.setContentAreaFilled(false);
		btnSave.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Save-32.png")));
		toolBarProject.add(btnSave);
		
		JButton btnSaveAs = new JButton("");
		btnSaveAs.setEnabled(false);
		btnSaveAs.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				//TODO SaveAS
				String[] file = FileChoose.SaveAsFile(frame);
				
				if (file==null){return;}
				
				String path = ProjectFile.isValidePath(file[0]);
				
				if (path==null){
					JOptionPane.showMessageDialog(null, "Use proper file name.", "Project creation error!", JOptionPane.WARNING_MESSAGE);
					btnSaveAs.doClick();
					return;
				}
				
				srcRoot = path;
				sheets.setProjectName(file[1]);
				
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				currentSheet.setData(tableModel.getDataVector());
				
				ProjectFile.saveFile(srcRoot, sheets);
				
				frame.setTitle("EZReports - "+srcRoot);
				lblName.setText("Project name: "+file[1]);
				
				new Thread(new StatusBarMessage(lblStatus, "Project saved as "+sheets.getProjectName()+" ...")).start();
			}
		});
		btnSaveAs.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Save as-32.png")));
		btnSaveAs.setToolTipText("Save As");
		btnSaveAs.setContentAreaFilled(false);
		btnSaveAs.setBackground(Color.DARK_GRAY);
		toolBarProject.add(btnSaveAs);
		
		JButton btnOpen = new JButton("");
		btnOpen.setVisible(false);
		
		btnOpen.setToolTipText("Open project");
		btnOpen.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Open Folder-32.png")));
		btnOpen.setContentAreaFilled(false);
		btnOpen.setBackground(Color.DARK_GRAY);
		toolBarProject.add(btnOpen);
		
		JButton btnNewsheet = new JButton("");
		toolBarProject.add(btnNewsheet);
		btnNewsheet.setEnabled(false);
		
		btnNewsheet.setToolTipText("Add sheet");
		btnNewsheet.setBackground(Color.DARK_GRAY);
		btnNewsheet.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Add File-32.png")));
		
		JButton btnProcess = new JButton("");
		btnProcess.setEnabled(false);
		btnProcess.setToolTipText("Process data");
		btnProcess.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Process-32.png")));
		btnProcess.setBackground(Color.DARK_GRAY);
		toolBarProject.add(btnProcess);
		
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//authentication required
				if (Base.this.user==null){
					JOptionPane.showMessageDialog(Base.this, "You have to sign in before processing the data...", "Error", JOptionPane.ERROR_MESSAGE);
					btnAuth.doClick();
				}else{
					//getDATAToDB
					new Thread() {
						@Override
						public void run() {
							SQLiteController.initializeTempDB();
							String log = SQLiteController.getDataToDB(sheets, lblStatus);
							SQLiteController.processData(sheets);
							pDialog.dispose();
							if (log.equals("")){
								//JOptionPane.showMessageDialog(frame, "Process successful...", "Info", JOptionPane.INFORMATION_MESSAGE);
								ReportSettings reportForm = new ReportSettings(Base.this);
								reportForm.setVisible(true);
							}else{
								JOptionPane.showMessageDialog(frame, "Error found,\n"+log, "Error", JOptionPane.ERROR_MESSAGE);
							}
							
						}
					}.start();
					
					//any thing
					pDialog = new Processing(Base.this);
					pDialog.setVisible(true);
				}
			}
		});
		
		
		getContentPane().setLayout(groupLayout);
		
		cmbSheets.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				//TODO changing sheets
				
				
				//save the current sheet
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				currentSheet.setData(tableModel.getDataVector());
				ProjectFile.saveFile(srcRoot, sheets);
				
				//change into selected sheet
				//load the sheets
				try{
					table.setModel(sheets.getModels().get(sheets.getClassIds().indexOf((String) cmbSheets.getSelectedItem())));
					new ExcelAdapter(table); 
					
					currentSheet = sheets.getSheets().get(sheets.getClassIds().indexOf((String) cmbSheets.getSelectedItem()));
					
					new Thread(new StatusBarMessage(lblStatus, "Sheet changed")).start();
				}catch(Exception e){
					
				}
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenProject = new JMenuItem("Open project");
		KeyStroke keyStrokeToOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		mntmOpenProject.setAccelerator(keyStrokeToOpen);
		
		mntmOpenProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnOpen.doClick();
			}
		});
		
		KeyStroke keyStrokeToNewProject = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
		JMenuItem mntmNewProject = new JMenuItem("New project");
		mntmNewProject.setAccelerator(keyStrokeToNewProject);
		
		mntmNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewProject.doClick();
			}
		});
		mntmNewProject.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Create New-16.png")));
		mnFile.add(mntmNewProject);
		mntmOpenProject.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Open Folder-16.png")));
		mnFile.add(mntmOpenProject);
		
		
		JMenuItem mntmAddSheet = new JMenuItem("Add sheet");
		mntmAddSheet.setEnabled(false);
		mntmAddSheet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewsheet.doClick();
			}
		});
		mntmAddSheet.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Add File-16.png")));
		mnFile.add(mntmAddSheet);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setEnabled(false);
		KeyStroke keyStrokeToSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		mntmSave.setAccelerator(keyStrokeToSave);
		
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSave.doClick();
			}
		});
		mntmSave.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Save-16.png")));
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save as");
		mntmSaveAs.setEnabled(false);
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSaveAs.doClick();
			}
		});
		mntmSaveAs.setIcon(new ImageIcon(Base.class.getResource("/main/resources/png/Add File-16.png")));
		mnFile.add(mntmSaveAs);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Base.this.dispose();
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		btnNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO newProject
				//ask required to save current
				//then show the create dialog
				String[] file = FileChoose.CreateFile(frame);
				
				if (file==null){return;}
				
				String path = ProjectFile.isValidePath(file[0]);
				
				if (path==null){
					JOptionPane.showMessageDialog(null, "Use proper file name.", "Project creation error!", JOptionPane.WARNING_MESSAGE);
					btnNewProject.doClick();
					return;
				}

				srcRoot = path;
				
				ProjectDetailSelector details = new ProjectDetailSelector(Base.this);
				details.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				details.setVisible(true);

				//save the sheets
				ProjectFile.saveFile(path, new Sheets(details.getYear(), details.getGrade(), details.getTerm(), file[1]));
				details.dispose();
				//load the sheets
				sheets = ProjectFile.loadFile(srcRoot);
				
				table.setModel(new DefaultTableModel());
				
//				if(sheets.getModels().size()!=0){
//					table.setModel(sheets.getModels().get(0));
//					new ExcelAdapter(table); 
//				}
				
				//update cmbsheets
				btnSave.setEnabled(false);
				btnSaveAs.setEnabled(false);
				mntmSave.setEnabled(false);
				mntmSaveAs.setEnabled(false);
				btnProcess.setEnabled(false);
				
				cmbSheets.removeAllItems();
				
				frame.setTitle("EZReports - "+srcRoot);
				lblName.setText("Project name: "+file[1]);
				lblYear.setText("Year : " + String.valueOf(sheets.getYear()));
				lblGrade.setText("Grade : " + String.valueOf(sheets.getGrade()));
				lblTerm.setText("Term : " + String.valueOf(sheets.getTerm()));
				
				if (!frame.getTitle().equals("EZReports")){
					btnNewsheet.setEnabled(true);
					mntmAddSheet.setEnabled(true);
				}
				
				new Thread(new StatusBarMessage(lblStatus, "New project is created...")).start();
			}
		});
		
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO Open
				String[] file = FileChoose.OpenFile(frame);
				
				if (file==null){return;}
				
				String path = ProjectFile.isValidePath(file[0]);
				
				if (path==null){
					JOptionPane.showMessageDialog(null, "Open up a valid file", "Project opening error!", JOptionPane.WARNING_MESSAGE);
					btnOpen.doClick();
					return;
				}

				srcRoot = path;

				//load the sheets
				sheets = ProjectFile.loadFile(srcRoot);
				
				//clear table
				table.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
						}
					));
				
				if(sheets.getModels().size()!=0){
					table.setModel(sheets.getModels().get(0));
					new ExcelAdapter(table); 
					currentSheet = sheets.getSheets().get(0);
				}
				
				//update cmbsheets
				btnSave.setEnabled(false);
				btnSaveAs.setEnabled(false);
				mntmSave.setEnabled(false);
				mntmSaveAs.setEnabled(false);
				btnProcess.setEnabled(false);
				
				
				cmbSheets.removeAllItems();
				for(Sheet sheet: sheets.getSheets()){
					cmbSheets.addItem(sheet.getClassID());
					if (btnSave.isEnabled()==false){
						btnSave.setEnabled(true);
						btnSaveAs.setEnabled(true);
						mntmSave.setEnabled(true);
						mntmSaveAs.setEnabled(true);
						btnProcess.setEnabled(true);
					}
					
				}

				frame.setTitle("EZReports - "+srcRoot);
				lblName.setText("Project name: "+sheets.getProjectName());
				lblYear.setText("Year : " + String.valueOf(sheets.getYear()));
				lblGrade.setText("Grade : " + String.valueOf(sheets.getGrade()));
				lblTerm.setText("Term : " + String.valueOf(sheets.getTerm()));
				
				if (!frame.getTitle().equals("EZReports")){
					btnNewsheet.setEnabled(true);
					mntmAddSheet.setEnabled(true);
				}
				
				new Thread(new StatusBarMessage(lblStatus, sheets.getProjectName()+" opened...")).start();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO Remove sheet
				//Remove sheets button
				if(cmbSheets.getItemCount()==0){
					JOptionPane.showMessageDialog(frame, "Sheet was not selected", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int item_index = cmbSheets.getSelectedIndex();
				int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure that you want to remove sheet "+cmbSheets.getItemAt(item_index)+"?", "Confirmation", JOptionPane.YES_NO_OPTION);
				
				if (confirmation==1){
					return;
				}
				
				//Remove the sheet
				sheets.getClassIds().remove(String.valueOf(sheets.getSheets().get(item_index)));
				sheets.getSheets().remove(item_index);
				
				
				//clear table
				table.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
						}
					));
				
				if(sheets.getModels().size()!=0){
					table.setModel(sheets.getModels().get(0));
					new ExcelAdapter(table); 
					currentSheet = sheets.getSheets().get(0);
				}
				
				//update cmbsheets
				btnSave.setEnabled(false);
				btnSaveAs.setEnabled(false);
				mntmSave.setEnabled(false);
				mntmSaveAs.setEnabled(false);
				btnProcess.setEnabled(false);
				
				cmbSheets.removeAllItems();
				for(Sheet sheet: sheets.getSheets()){
					cmbSheets.addItem(sheet.getClassID());
					if (btnSave.isEnabled()==false){
						btnSave.setEnabled(true);
						btnSaveAs.setEnabled(true);
						mntmSave.setEnabled(true);
						mntmSaveAs.setEnabled(true);
						btnProcess.setEnabled(true);
						
					}
					
				}
				
				new Thread(new StatusBarMessage(lblStatus, "Sheet is removed...")).start();

				
			}
		});
		
		btnNewsheet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO NewSheet
				SheetDetailSelector details = new SheetDetailSelector(Base.this);
				details.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				details.setVisible(true);
				
				//save project
				//save the sheets
				ProjectFile.saveFile(srcRoot, sheets);
				
				//Refresh the window with new data
				//update cmbsheets
				
				if (details.getClassID()==null){
					return;
				}

				//set the table according to the selected sheet
				int index = sheets.getClassIds().indexOf(details.getClassID());
				table.setModel(sheets.getModels().get(index));
				new ExcelAdapter(table); 
				currentSheet = sheets.getSheets().get(index);
				
				//clear previous elements in cmb
				cmbSheets.removeAllItems();
				//fill from beginning
				for(Sheet sheet: sheets.getSheets()){
					cmbSheets.addItem(sheet.getClassID());
					if (btnSave.isEnabled()==false){
						btnSave.setEnabled(true);
						btnSaveAs.setEnabled(true);
						mntmSave.setEnabled(true);
						mntmSaveAs.setEnabled(true);
						btnProcess.setEnabled(true);
					}
				}
				cmbSheets.setSelectedItem(details.classID);
				
				new Thread(new StatusBarMessage(lblStatus, "New sheet is added to "+sheets.getProjectName()+" ...")).start();
			}
		});

	}

	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	public void authPanelRefresh(){
		if (this.user==null){
			this.btnAuth.setText("Sign In");
			this.lblSignUp.setVisible(true);
			this.panel_info.setVisible(false);
			this.cmbAPI.setEnabled(true);
			
		}else{
			this.btnAuth.setText("Sign Out");
			this.lblSignUp.setVisible(false);
			this.panel_info.setVisible(true);
			
			this.lblUsername.setText("U: "+this.user.getName());
			this.lblEmail.setText("@: "+this.user.getEmail());
			this.lblFirstName.setText("FN: "+this.user.getFirstName());
			this.lblLastName.setText("LN: "+this.user.getLastName());
			this.cmbAPI.setEnabled(false);
			
		}
	}
}
