package main.logic;

import javax.swing.JPasswordField;

public class GeneralValidations {
	
/*	public static boolean nameValidation(String name){
		name = name.trim();
		if (name.matches("[A-Za-z ]+") && name.length()<30){
			return true;
		}else{
			return false;
		}
	}*/
	
	public static boolean usernameValidation(String name){
		name = name.trim();
		if (name.matches("[A-Za-z]+") && name.length()<=30){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean phoneValidation(String phone){
		phone = phone.trim();
		if (phone.matches("^0[0-9]{9}") || phone.matches("^\\+94[0-9]{9}") || phone.matches("[0-9]{9}")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean emailValidation(String email){
		email = email.trim();
		if (email.matches("[A-Za-z0-9.]+@{1}[A-Za-z0-9.]+\\.[a-z]+")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isSamePasswords(JPasswordField pass1, JPasswordField pass2){
		//check for the number of charactors
		int no_char = pass1.getPassword().length;
		
		if (no_char!=pass2.getPassword().length){
			return false;
		}else{
			for (int x=0; x<no_char; x++){
				if(pass1.getPassword()[x]!=pass2.getPassword()[x]){
					return false;
				}
			}
		}
		return true;
	}

}
