package test.main.logic;

import static org.junit.Assert.*;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.junit.Test;

import main.logic.Sheet;
import main.logic.Sheets;

public class SheetsTest {

	@Test
	public void SheetsSheetTest() {
		Sheets sheets = new Sheets(2016, 13, 2, "test");
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		Vector<Object> v = new Vector<Object>();
		v.add("140457");
		v.add("Akila");
		v.add("96.01");
		v.add("95.00");
		v.add("87.98");
		v.add("56.04");
		v.add("80.00");
		
		data.add(v);
		
		sheets.getSheets().add(new Sheet(data, "class A", "Maths", "Chem", "Physics"));
		
		DefaultTableModel model = sheets.getModels().get(0);
		
		assertEquals("140457", (String) model.getValueAt(0, 0));
	}

}
