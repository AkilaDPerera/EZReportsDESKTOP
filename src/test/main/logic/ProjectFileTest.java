package test.main.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import main.logic.ProjectFile;

public class ProjectFileTest {

	@Test
	public void isValidePath() {
		String filepath1 = ProjectFile.isValidePath("c:/program/a.pdf");
		String filepath2 = ProjectFile.isValidePath("c:/program/a.ezr");
		String filepath3 = ProjectFile.isValidePath("c:/program/a");
		String [] expected = new String[] {null, "c:/program/a.ezr", "c:/program/a.ezr"};
		String[] actual = new String[] {filepath1, filepath2, filepath3};
		assertArrayEquals(expected, actual);
	}

}
