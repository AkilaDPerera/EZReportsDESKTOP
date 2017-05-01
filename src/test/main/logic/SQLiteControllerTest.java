package test.main.logic;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JLabel;

import org.junit.Test;

import main.logic.SQLiteController;
import main.logic.Sheet;
import main.logic.Sheets;
import main.logic.Student;

public class SQLiteControllerTest {

	@Test
	public void ClassReport() {
		Sheets sheets = new Sheets(2016, 13, 2, "test");
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<Object> v = new Vector<Object>();
		v.add("140457");
		v.add("Akila");
		v.add(96.01);
		v.add(95.0);
		v.add(87.98);
		v.add(56.04);
		v.add(80.0);
		
		data.add(v);
		
		sheets.getSheets().add(new Sheet(data, "class A", "Maths", "Chem", "Physics"));
		sheets.getSubjects().add("Maths");
		sheets.getSubjects().add("Chem");
		sheets.getSubjects().add("Physics");
		sheets.getClassIds().add("class A");
		
		
		SQLiteController.initializeTempDB();
		
		SQLiteController.getDataToDB(sheets, new JLabel());
		SQLiteController.processData(sheets);

		SQLiteController.initializeStatement();
		
		SQLiteController.classReport("class A");
		ResultSet rs = SQLiteController.getStdData();

		try {
			rs.next();
			
			Student s = new Student(rs.getInt(1), "Akila");
			s.setTotal(rs.getDouble(2));
			s.setAverage(rs.getDouble(3));
			s.setZScore(rs.getDouble(4));
			
			String [] expected = new String [] {"140457", "Akila", "AB", "AB", "AB", "278.99", "93.00", "0.0000", "AB", "NA", "NA"};
			
			assertArrayEquals(expected, s.getVector().toArray());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		SQLiteController.closeConnection();
		
	}

}
