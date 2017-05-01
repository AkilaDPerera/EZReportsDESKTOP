package main.gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import main.gui.support.Processing;
import main.logic.APIHandler;
import main.logic.EmailClient;
import main.logic.GeneralValidations;
import main.logic.User;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

@SuppressWarnings("serial")
public class Auth extends JDialog {
	private JTextField txtName;
	private JTextField txtEmail;
	private JTextField txtFirstname;
	private JTextField txtLastName;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	
	private Integer SecretKey;
	private JTextField txtUsername;
	private JPasswordField passwordField_2;
	
	private Base base;
	
	public static boolean isSent = false;

	/**
	 * Create the dialog.
	 */
	public Auth(Base base, String purpose) {
		this.base = base;
		setTitle("Authentication");
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(base);
		getContentPane().setLayout(null);
		
		
		JPanel panelSignUp = new JPanel();
		panelSignUp.setOpaque(false);
		panelSignUp.setBounds(10, 11, 424, 249);
		getContentPane().add(panelSignUp);
		panelSignUp.setLayout(null);
		
		JComboBox<String> cmbMrMrs = new JComboBox<String>();
		
		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if (!GeneralValidations.usernameValidation(txtName.getText())){
					txtName.setForeground(Color.RED);
				}else{
					txtName.setForeground(null);
				};
			}
		});
		txtName.setToolTipText("Required. 30 characters or fewer");
		txtName.setBounds(122, 47, 86, 20);
		panelSignUp.add(txtName);
		txtName.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!GeneralValidations.emailValidation(txtEmail.getText())){
					txtEmail.setForeground(Color.RED);
				}else{
					txtEmail.setForeground(null);
				};
			}
		});
		txtEmail.setToolTipText("The secret code will be sent to this email.");
		txtEmail.setBounds(66, 179, 132, 20);
		panelSignUp.add(txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblSignup = new JLabel("Sign Up");
		lblSignup.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSignup.setBounds(191, 11, 64, 14);
		panelSignUp.add(lblSignup);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setLabelFor(txtName);
		lblUsername.setBounds(10, 53, 92, 14);
		panelSignUp.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 84, 92, 14);
		panelSignUp.add(lblPassword);
		
		JLabel lblReenterPassword = new JLabel("Re-enter password");
		lblReenterPassword.setBounds(10, 115, 113, 14);
		panelSignUp.add(lblReenterPassword);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(10, 182, 46, 14);
		panelSignUp.add(lblEmail);
		
		JButton btnCreateAccount = new JButton("Create account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Checking for valid data
				//username should not be empty
				if(txtName.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(Auth.this, "Username should not be empty...", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					
				//need to have a valid username
				}else if(!GeneralValidations.usernameValidation(txtName.getText().trim())){
					JOptionPane.showMessageDialog(Auth.this, "Username is not valid...", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					
				//non duplicating username
				}else if(APIHandler.getUsers().contains(txtName.getText())){
					JOptionPane.showMessageDialog(Auth.this, "Username is already existed. Choose another...", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				
				//non empty password
				}else if(passwordField.getPassword().length==0){
					JOptionPane.showMessageDialog(Auth.this, "Please provide a password", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					
				//matching password
				}else if(!GeneralValidations.isSamePasswords(passwordField, passwordField_1)){
					JOptionPane.showMessageDialog(Auth.this, "Passwords are not matching...", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					
				//valid email address required
				}else if(!GeneralValidations.emailValidation(txtEmail.getText())){
					JOptionPane.showMessageDialog(Auth.this, "Please enter a valid email address...", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				//Generate secret key for the email validation
				Auth.this.SecretKey = (1000 + Integer.valueOf((int) (Math.random()*10000)));
				System.out.println(Auth.this.SecretKey);
				
				//send the email
				String to = txtEmail.getText().trim();
				String name = txtFirstname.getText().trim();
				String emailmsg = "Dear "+name+",\nWelcome to the EZReports; report generating system. \nHere is your secret key: "+Auth.this.SecretKey+".\nYour sincere,\nEZReports support.\n\nThis is an auto-generating email. Please do not reply to this.";
				Auth.isSent = false;
				
				//handling the emailing time...
				Processing pDialog = new Processing(Auth.this.base);
				Thread email = new Thread() {
					@Override
					public void run() {
						synchronized(Auth.class){
							EmailClient ec = new EmailClient();
							Auth.isSent = ec.sendEmail(to, emailmsg, "EZReports Secret key");
							if (Auth.isSent){
								JOptionPane.showMessageDialog(Auth.this, "Email has sent. check you mail box...", "Info", JOptionPane.INFORMATION_MESSAGE);
							}else{
								JOptionPane.showMessageDialog(Auth.this, "Error occurs. Check your internet connection...", "Error", JOptionPane.ERROR_MESSAGE);
							}
							pDialog.dispose();
							//concurrency handling
							Auth.class.notifyAll();
						}
					}
				};
				email.start();
				pDialog.setVisible(true);
				
				
				//wait this thread... until email finished...
				synchronized(Auth.class){
				    while (email.isAlive()){
				    	try {
							Auth.class.wait();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
				    }
				}
				
				//check emailing successful...
				if (!Auth.isSent){
					return;
				}
				
				//You got 3 chances to try
				int success = 0;
				for(int x=0; x<3; x++){
					String msg = "<html>Please enter your 4 or 5 digits secret number recieved through the email provided...<br>You got "+ String.valueOf(3-x) +" chances more...</html>";
					String output = JOptionPane.showInputDialog(Auth.this, msg, "XXXX");
					
					if(output==null){
						success = -1;
						JOptionPane.showMessageDialog(Auth.this, "You left the validation process...", "Failed", JOptionPane.ERROR_MESSAGE);
						break;
					}
					
					try{
						Integer outputINT = Integer.valueOf(output);

						if ((int)outputINT==(int) Auth.this.SecretKey){
							success = 1;
							JOptionPane.showMessageDialog(Auth.this, "Validation successful...", "Successful", JOptionPane.INFORMATION_MESSAGE);
							
							//Send data to the central database
							Auth.this.base.user = APIHandler.registerUser(txtName.getText().trim(), new String(passwordField.getPassword()), txtEmail.getText().trim(), cmbMrMrs.getSelectedItem()+" "+txtFirstname.getText(), txtLastName.getText());
							Auth.this.dispose();
							break;
						}
					}catch(Exception e1){
						
					}
				}
				if(success==0){
					JOptionPane.showMessageDialog(Auth.this, "You fail to enter the secret key. Try again...", "Failed", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		btnCreateAccount.setBounds(259, 178, 144, 23);
		panelSignUp.add(btnCreateAccount);
		
		JLabel lblNewLabel = new JLabel("*Please enter a valid email address");
		lblNewLabel.setBounds(10, 157, 226, 14);
		panelSignUp.add(lblNewLabel);
		
		JLabel lblFirstName = new JLabel("First name");
		lblFirstName.setBounds(252, 97, 64, 14);
		panelSignUp.add(lblFirstName);
		
		txtFirstname = new JTextField();
		lblFirstName.setLabelFor(txtFirstname);
		txtFirstname.setBounds(326, 94, 86, 20);
		panelSignUp.add(txtFirstname);
		txtFirstname.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last name");
		lblLastName.setBounds(252, 128, 64, 14);
		panelSignUp.add(lblLastName);
		
		txtLastName = new JTextField();
		txtLastName.setBounds(326, 125, 86, 20);
		panelSignUp.add(txtLastName);
		txtLastName.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(122, 79, 86, 20);
		panelSignUp.add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(122, 109, 86, 20);
		panelSignUp.add(passwordField_1);
		
		
		cmbMrMrs.setModel(new DefaultComboBoxModel<String>(new String[] {"Mr.", "Mrs.", "Miss."}));
		cmbMrMrs.setBounds(326, 61, 86, 20);
		panelSignUp.add(cmbMrMrs);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(253, 63, 55, 16);
		panelSignUp.add(lblTitle);
		
		JPanel panelSignIn = new JPanel();
		panelSignIn.setOpaque(false);
		panelSignIn.setBounds(10, 11, 424, 249);
		getContentPane().add(panelSignIn);
		panelSignIn.setLayout(null);
		
		JLabel lblSignIn = new JLabel("Sign In");
		lblSignIn.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSignIn.setBounds(177, 12, 64, 14);
		panelSignIn.add(lblSignIn);
		
		JLabel lblUsername_1 = new JLabel("Username");
		lblUsername_1.setBounds(99, 52, 80, 16);
		panelSignIn.add(lblUsername_1);
		
		JLabel lblPassword_1 = new JLabel("Password");
		lblPassword_1.setBounds(99, 80, 64, 16);
		panelSignIn.add(lblPassword_1);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(199, 50, 114, 20);
		panelSignIn.add(txtUsername);
		txtUsername.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pass = new String(passwordField_2.getPassword());
				
				//username non empty
				if(txtUsername.getText().isEmpty()){
					JOptionPane.showMessageDialog(Auth.this, "Username should not be empty...", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				//password non empty
				}else if(pass.isEmpty()){
					JOptionPane.showMessageDialog(Auth.this, "Password should not be empty...", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//send the request and check the authentication with server...
				User user = APIHandler.getAuthenticated(Auth.this, txtUsername.getText().trim(), pass);
				if (user!=null){
					Auth.this.base.user = user;
					Auth.this.dispose();
				}
			}
		});
		btnSubmit.setBounds(151, 131, 98, 26);
		panelSignIn.add(btnSubmit);
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(199, 78, 114, 20);
		panelSignIn.add(passwordField_2);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(ProjectDetailSelector.class.getResource("/main/resources/background/ProjectDetailSelector.png")));
		lblNewLabel_1.setBounds(0, 0, 444, 271);
		getContentPane().add(lblNewLabel_1);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtName, passwordField, passwordField_1, txtEmail, cmbMrMrs, txtFirstname, txtLastName, btnCreateAccount, txtUsername, passwordField_2, btnSubmit}));
		
		if (purpose.equals("signin")){
			panelSignIn.setVisible(true);
			panelSignUp.setVisible(false);
		}else{
			panelSignIn.setVisible(false);
			panelSignUp.setVisible(true);
		}

	}
}
