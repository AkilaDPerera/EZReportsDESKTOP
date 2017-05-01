package main.gui.support;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

public class FileChoose {

	public static String[] CreateFile(Component component){
		JFileChooser fc = new JFileChooser();
		
		javax.swing.filechooser.FileFilter ezrFilter = new javax.swing.filechooser.FileFilter() {
			@Override
			public String getDescription() {
				return "EZReports files (*.ezr)";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".ezr") || !f.getAbsolutePath().contains(".")){
					return true;
				}else{
					return false;
				}
			}
		};
		
		fc.setFileFilter(ezrFilter);
		int result = fc.showDialog(component, "Create");

		if (result == 1){
			return null;
		}else{
			return new String [] {
					fc.getSelectedFile().getAbsolutePath(),
					fc.getSelectedFile().getName()
					};
		}
//		abspath, filename
	}
	
	public static String[] OpenFile(Component component){
		JFileChooser fc = new JFileChooser();
		javax.swing.filechooser.FileFilter ezrFilter = new javax.swing.filechooser.FileFilter() {
			@Override
			public String getDescription() {
				return "EZReports files (*.ezr)";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".ezr") || !f.getAbsolutePath().contains(".")){
					return true;
				}else{
					return false;
				}
			}
		};
		fc.setFileFilter(ezrFilter);
		int result = fc.showDialog(component, "Open");

		if (result == 1){
			return null;
		}else{
			return new String [] {
					fc.getSelectedFile().getAbsolutePath(),
					fc.getSelectedFile().getName()
					};
		}
		//abspath, filename
	}
	
	public static String[] SaveAsFile(Component component){
		JFileChooser fc = new JFileChooser();
		javax.swing.filechooser.FileFilter ezrFilter = new javax.swing.filechooser.FileFilter() {
			@Override
			public String getDescription() {
				return "EZReports files (*.ezr)";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".ezr") || !f.getAbsolutePath().contains(".")){
					return true;
				}else{
					return false;
				}
			}
		};
		fc.setFileFilter(ezrFilter);
		int result = fc.showDialog(component, "Save");

		if (result == 1){
			return null;
		}else{
			return new String [] {
					fc.getSelectedFile().getAbsolutePath(),
					fc.getSelectedFile().getName()
					};
		}
		//abspath, filename
	}
}
