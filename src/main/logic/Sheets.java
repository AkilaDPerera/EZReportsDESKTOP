package main.logic;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class Sheets implements Serializable {
	private ArrayList<Sheet> sheets;
	private ArrayList<String> subjects;
	private ArrayList<String> classIds;
	
	
	@SuppressWarnings("rawtypes")
	private final Class[] columnTypes;
	
	private int term;
	private int year;
	private int grade;
	private String projectName;
	
	public Sheets(int year, int grade, int term, String projectName){
		this.year = year;
		this.grade = grade;
		this.term = term;
		this.projectName = projectName;
		
		//create an arraylist
		this.sheets = new ArrayList<Sheet>();
		this.subjects = new ArrayList<String>();
		this.classIds = new ArrayList<String>();
		
		this.columnTypes = new Class[] {Integer.class, String.class, Double.class, Double.class, Double.class, Double.class, Double.class};
	}
	
	public ArrayList<DefaultTableModel> getModels(){
		ArrayList<DefaultTableModel> output = new ArrayList<DefaultTableModel>();
		
		for (Sheet sheet: sheets){
			DefaultTableModel model = new DefaultTableModel(sheet.getData(), sheet.getColumnNames()){
				@SuppressWarnings({ "rawtypes" })
				Class[] columnTypes = Sheets.this.columnTypes;
	
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public Class getColumnClass(int columnIndex) {return columnTypes[columnIndex];}
			};
			output.add(model);
		}
		return output;
	}
	
	public ArrayList<Sheet> getSheets(){
		return sheets;
	}
	
	public ArrayList<String> getSubjects(){
		return subjects;
	}
	
	public ArrayList<String> getClassIds(){
		return classIds;
	}

	public int getTerm() {
		return term;
	}

	public int getYear() {
		return year;
	}

	public int getGrade() {
		return grade;
	}
	
	public String getProjectName(){
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Integer getTotalStudents(){
		int total = 0;
		for (Sheet s: sheets){
			total = total + s.getData().size();
		}
		return total;
	}

}


