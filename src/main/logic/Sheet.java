package main.logic;

import java.io.Serializable;
import java.util.Vector;

@SuppressWarnings("serial")
public class Sheet implements Serializable {
	
	private Vector<Vector<Object>> data;
	private Vector<String> columnNames;
	private String classID;
	
	public Sheet(Vector<Vector<Object>> data, String classid, String Subject1, String Subject2, String Subject3){
		this.data = data;
		this.classID = classid;
		
		this.columnNames = new Vector<String>(7);
		this.columnNames.addElement("Index");this.columnNames.addElement("Name");this.columnNames.addElement(Subject1);this.columnNames.addElement(Subject2);
		this.columnNames.addElement(Subject3);this.columnNames.addElement("English");this.columnNames.addElement("Attendence %");
	}
	
	public Vector<String> getColumnNames(){
		return columnNames;
	}

	public Vector<Vector<Object>> getData() {
		return data;
	}

	public String getClassID() {
		return classID;
	}

	public void setGradeLetter(String gradeLetter) {
		this.classID = gradeLetter;
	}
	
	public void setData(Vector<Vector<Object>> data) {
		this.data = data;
	}
	
	public String toString(){
		return classID;
	}
}
