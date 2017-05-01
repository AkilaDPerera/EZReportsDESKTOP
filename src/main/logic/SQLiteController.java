package main.logic;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JLabel;

import org.sqlite.SQLiteException;

import main.gui.support.StatusBarMessage;

public class SQLiteController {
	private static String tempURL = "jdbc:sqlite:"+System.getProperty("java.io.tmpdir")+"db.db";
	private static Connection connection = null;
	private static java.sql.Statement statement = null;

	public static void initializeStatement(){//this is public because of testing purpose otherwise no modifier is enough
		try {
			if (connection == null || connection.isClosed()){
				connection = DriverManager.getConnection(tempURL);
			}
			
			if(statement==null || statement.isClosed()){
				statement = connection.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeConnection(){//this is public because of testing purpose otherwise no modifier is enough
		try {
			if (statement!=null || !statement.isClosed()){
				statement.close();
			}
			if(connection!=null || !connection.isClosed()){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static int getClassID(String class_name){
		try {
			PreparedStatement getClassId = (PreparedStatement) connection.prepareStatement("SELECT id FROM 'class' WHERE `name`=?;");
			getClassId.setString(1, class_name);
			ResultSet rs = (getClassId.executeQuery());
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return -4;
	}
	
	private static Double[] getAVGnSTDD(String subject_name){
		try {
			PreparedStatement getSubjectID = (PreparedStatement) connection.prepareStatement("SELECT id FROM subject WHERE subject.name=?;");
			PreparedStatement getAvg = (PreparedStatement) connection.prepareStatement("SELECT AVG(mark.mark) FROM mark WHERE mark.subject_id=? AND mark.mark!=-1.0;");
			PreparedStatement getVariance = (PreparedStatement) connection.prepareStatement("SELECT SUM((mark.mark - ?)*(mark.mark - ?))/COUNT(mark.mark) FROM mark WHERE mark.subject_id=? AND mark.mark!=-1.0;");
		
			//Finding the associated subject id
			getSubjectID.setString(1, subject_name);
			ResultSet rs = getSubjectID.executeQuery();
			rs.next();
			int subject_id = rs.getInt(1);
			
			//Finding the subject avg
			getAvg.setInt(1, subject_id);
			rs = getAvg.executeQuery();
			rs.next();
			Double average = rs.getDouble(1);
			
			//Finding the variance
			getVariance.setDouble(1, average);
			getVariance.setDouble(2, average);
			getVariance.setInt(3, subject_id);
			rs = getVariance.executeQuery();
			rs.next();
			Double variance = rs.getDouble(1);

			//Returning values
			return new Double[]{average, Math.sqrt(variance)};
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; 
	}

	public static void initializeTempDB(){
		System.out.println(tempURL);
		
		initializeStatement();
		String [] initialize = new String[] {
			"DROP TABLE IF EXISTS student;",
			"DROP TABLE IF EXISTS mark;",
			"DROP TABLE IF EXISTS subject;",
			"DROP TABLE IF EXISTS class;",
			"CREATE TABLE `student` (`id` INTEGER NOT NULL UNIQUE, `name` TEXT NOT NULL, `class_id`	INTEGER NOT NULL, `attendence` REAL, PRIMARY KEY(id));",
			"CREATE TABLE `mark` (`id` INTEGER NOT NULL,`subject_id` INTEGER NOT NULL,`mark` REAL NOT NULL);",
			"CREATE TABLE `subject` (`id` INTEGER NOT NULL UNIQUE,`name` TEXT NOT NULL UNIQUE,PRIMARY KEY(id));",
			"CREATE TABLE `class` (`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL UNIQUE);",
			"INSERT INTO subject ('name') VALUES ('english');",
		};
			
		for (String query: initialize){
			try {
				statement.execute(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		closeConnection();
	}

	public static String getDataToDB(Sheets sheets, JLabel status){
		initializeStatement();
		
		String log="";
		PreparedStatement addClass = null;
		PreparedStatement addSubject = null;
		PreparedStatement addStudent = null;
		PreparedStatement addMark = null;
		try{
			addClass = (PreparedStatement) connection.prepareStatement("INSERT INTO 'class' ('name') VALUES (?);");
			addSubject = (PreparedStatement) connection.prepareStatement("INSERT INTO 'subject' ('name') VALUES (?);");
			addStudent = (PreparedStatement) connection.prepareStatement("INSERT INTO 'student' ('id', 'name', 'class_id', 'attendence') VALUES (?, ?, ?, ?);");
			addMark = (PreparedStatement) connection.prepareStatement("INSERT INTO 'mark' ('id', 'subject_id', 'mark') VALUES (?, ?, ?);");
			
			//ADD SUBJECT
			for (String subject: sheets.getSubjects()){
				addSubject.setString(1, subject);
				addSubject.executeUpdate();
			}
			
			//ADD CLASS
			for (String classID: sheets.getClassIds()){
				addClass.setString(1, classID);
				addClass.executeUpdate();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
			
		//Handling sheet data
		int total = sheets.getTotalStudents();
		int count = 0;
		Color foreColor = Color.WHITE;

		
		ArrayList<String> empty = new ArrayList<String>(); empty.add(""); empty.add(null);
		for (Sheet sheet: sheets.getSheets()){
			status.setForeground(foreColor);
			Vector<String> subjects = sheet.getColumnNames();
			String sub1Name = subjects.get(2);
			String sub2Name = subjects.get(3);
			String sub3Name = subjects.get(4);
			
			String className = sheet.getClassID();

			int classId = getClassID(sheet.getClassID());
			int sub1Id = getSubjectID(sub1Name);
			int sub2Id = getSubjectID(sub2Name);
			int sub3Id = getSubjectID(sub3Name);

			//Row data
			int rowCounter = 0;
			for (Vector<?> row: sheet.getData()){
				count += 1;
				//display progress
				status.setText(count +"/"+ total + " completed." );
				
				rowCounter += 1;
				
				if (empty.contains(row.get(0)) || empty.contains(row.get(1))){
					for (int x=0; x<7; x++){
						if (!empty.contains(row.get(x))){
							log += "Sheet: " + className + "; Row: " + rowCounter + "\n";
							break;
						}
					}
					continue;
				}
				
				int index; String name; double sub1; double sub2; double sub3; double subEng; double attendence;
				
				if (empty.contains(row.get(2))){sub1 = -1.0;}else{sub1 = (Double) row.get(2);}
				if (empty.contains(row.get(3))){sub2 = -1.0;}else{sub2 = (Double) row.get(3);}
				if (empty.contains(row.get(4))){sub3 = -1.0;}else{sub3 = (Double) row.get(4);}
				if (empty.contains(row.get(5))){subEng = -1.0;}else{subEng = (Double) row.get(5);}
				if (empty.contains(row.get(6))){attendence = Double.valueOf("-1.0");}else{attendence = (Double) row.get(6);}

				index = Integer.valueOf(String.valueOf(row.get(0)).trim());
				name = (String) row.get(1);
				
				try {
					addStudent.setInt(1, index);
					addStudent.setString(2, name);
					addStudent.setInt(3, classId);
					addStudent.setDouble(4, attendence);
					addStudent.executeUpdate();
				
					addMark.setInt(1, index);
					addMark.setInt(2, sub1Id);
					addMark.setDouble(3, sub1);
					addMark.executeUpdate();
					
					addMark.setInt(1, index);
					addMark.setInt(2, sub2Id);
					addMark.setDouble(3, sub2);
					addMark.executeUpdate();
					
					addMark.setInt(1, index);
					addMark.setInt(2, sub3Id);
					addMark.setDouble(3, sub3);
					addMark.executeUpdate();
					
					addMark.setInt(1, index);
					addMark.setInt(2, 1);
					addMark.setDouble(3, subEng);
					addMark.executeUpdate();
				} catch (SQLiteException ex){
					log += "Sheet: " + className + "; Row: " + rowCounter + "(Duplicate entry)\n";
					continue;
				} catch (SQLException e) {
				}
			}
		}
		new StatusBarMessage(status, "Processing completed...").run();

		closeConnection();
		return log;
	}

	public static void processData(Sheets sheets){
		initializeStatement();

		try {
			statement.execute("DROP TABLE IF EXISTS temp_subject_details;");
			statement.execute("CREATE TABLE `temp_subject_details` (`subject_id` INTEGER, `stdd` REAL NOT NULL, `avg` REAL NOT NULL, PRIMARY KEY(subject_id));");
			PreparedStatement addEntry = (PreparedStatement) connection.prepareStatement("INSERT INTO 'temp_subject_details' ('subject_id', 'stdd', 'avg') VALUES (?, ?, ?);");
			
			for (String subject_name: sheets.getSubjects()){
				int sub_id = getSubjectID(subject_name);
				Double [] sub_details = getAVGnSTDD(subject_name);
				
				addEntry.setInt(1, sub_id);
				addEntry.setDouble(2, sub_details[1]);
				addEntry.setDouble(3, sub_details[0]);
				addEntry.execute();
			}
			statement.execute("DROP VIEW IF EXISTS temp_scores;");
			statement.execute("CREATE VIEW temp_scores AS SELECT mark.id, student.class_id, mark.subject_id, mark.mark, ((mark.mark-avg)/stdd) AS 'ZScore' FROM mark, temp_subject_details, student WHERE mark.mark!=-1.0 AND mark.subject_id=temp_subject_details.subject_id AND mark.id=student.id;");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		closeConnection();
	}

	public static void classReport(String class_name){
		int class_id = getClassID(class_name);
		try {
			statement.execute("DROP VIEW IF EXISTS class_report;");
			statement.execute("DROP VIEW IF EXISTS absentees;");
			String query = "CREATE VIEW class_report AS SELECT id, SUM(mark) AS 'total', AVG(mark) AS 'average', SUM(ZScore) AS 'Z' FROM temp_scores WHERE class_id='"+ class_id +"' GROUP BY id ORDER BY Z DESC;";
			statement.execute(query);
			query = "CREATE VIEW absentees AS SELECT DISTINCT student.id FROM mark, student WHERE student.id = mark.id AND student.class_id='"+ class_id +"' AND mark.mark=-1.0;";
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet getStdData(){
		try {
			return statement.executeQuery("SELECT * FROM class_report;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ResultSet getAbsentees(){
		try {
			return statement.executeQuery("SELECT * FROM absentees;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static int getSubjectID(String subject_name){
		try {
			PreparedStatement getSubjectId = (PreparedStatement) connection.prepareStatement("SELECT id FROM 'subject' WHERE `name`=?;");
			getSubjectId.setString(1, subject_name);
			ResultSet rs = (getSubjectId.executeQuery());
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return -3;
	}
	


	public static double getMark(int index, int subId){
		try {
			PreparedStatement getMark = (PreparedStatement) connection.prepareStatement("SELECT mark.mark FROM mark WHERE mark.id=? AND mark.subject_id=?;");
			getMark.setInt(1, index);
			getMark.setInt(2, subId);
			ResultSet rs = getMark.executeQuery();
			rs.next();
			return rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -2.0;
	}

	public static Object[] getNameAttendence(int index){
		try {
			PreparedStatement getNameAttedence = (PreparedStatement) connection.prepareStatement("SELECT student.name, student.attendence FROM student WHERE student.id=?;");
			getNameAttedence.setInt(1, index);
			ResultSet rs = getNameAttedence.executeQuery();
			rs.next();
			return new Object[] {rs.getString(1), rs.getDouble(2)};
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Double[] getTotAvg(int index){
		try {
			PreparedStatement getTotAvg = (PreparedStatement) connection.prepareStatement("SELECT class_report.total, class_report.average FROM class_report WHERE class_report.id=?;");
			getTotAvg.setInt(1, index);
			ResultSet rs = getTotAvg.executeQuery();
			rs.next();
			return new Double[] {rs.getDouble(1), rs.getDouble(2)};
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Subject report
	public static String getSubjectMax(String subject){
		int subjectId = getSubjectID(subject);
		
		try {
			PreparedStatement getSubMax = (PreparedStatement) connection.prepareStatement("SELECT MAX(mark) FROM temp_scores WHERE subject_id=?;");
			getSubMax.setInt(1, subjectId);
			ResultSet rs = getSubMax.executeQuery();
			rs.next();
			return (new DecimalFormat("0.00")).format(rs.getDouble(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSubjectMin(String subject){
		int subjectId = getSubjectID(subject);
		
		try {
			PreparedStatement getSubMin = (PreparedStatement) connection.prepareStatement("SELECT MIN(mark) FROM temp_scores WHERE subject_id=?;");
			getSubMin.setInt(1, subjectId);
			ResultSet rs = getSubMin.executeQuery();
			rs.next();
			return (new DecimalFormat("0.00")).format(rs.getDouble(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSubjectAVG(String subject){
		int subjectId = getSubjectID(subject);
		
		try {
			PreparedStatement getSubAVG = (PreparedStatement) connection.prepareStatement("SELECT avg FROM temp_subject_details WHERE subject_id=?;");
			getSubAVG.setInt(1, subjectId);
			ResultSet rs = getSubAVG.executeQuery();
			rs.next();
			return (new DecimalFormat("0.00")).format(rs.getDouble(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSubjectSD(String subject){
		int subjectId = getSubjectID(subject);
		
		try {
			PreparedStatement getSubSD = (PreparedStatement) connection.prepareStatement("SELECT stdd FROM temp_subject_details WHERE subject_id=?;");
			getSubSD.setInt(1, subjectId);
			ResultSet rs = getSubSD.executeQuery();
			rs.next();
			return (new DecimalFormat("0.00")).format(rs.getDouble(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Subject report
	public static Integer getCountInRange(String subject, int start, int end){
		int subjectId = getSubjectID(subject);
		
		try {
			PreparedStatement getCount = (PreparedStatement) connection.prepareStatement("SELECT count(id) FROM temp_scores WHERE subject_id=? AND mark>? AND mark<=?;");
			getCount.setInt(1, subjectId);
			getCount.setInt(2, start);
			getCount.setInt(3, end);
			ResultSet rs = getCount.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}




