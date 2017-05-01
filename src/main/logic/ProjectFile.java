package main.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Pattern;

public class ProjectFile {
	
	public static boolean saveFile(String filePath, Sheets sheets){
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(sheets);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (oos!=null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
		return true;
	}

	public static Sheets loadFile(String filePath){
		Sheets sheets = null;
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream(filePath));
			sheets = (Sheets) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sheets;
	}
	
	public static String isValidePath(String path){
		if(Pattern.compile("\\.\\S*$").matcher(path).find()){
			if(Pattern.compile("\\.ezr$").matcher(path).find()){
				return path;
			}else{
				return null;
			}
		}else{
			return path+".ezr";
		}
	}
}
