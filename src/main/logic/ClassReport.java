package main.logic;

import java.awt.Color;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import main.gui.Base;

public class ClassReport {
    
	private static String FILE = null;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font rank = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static Font small2 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

    public static void createPDF(Base base, String src, Sheet sheet, DefaultTableModel model, String class_name, Sheets sheets){
    	
    	FILE = src.substring(0, src.length()-4) + ".pdf";
    			
    	 try {
             Document document = new Document();
             document.setPageSize(PageSize.A4);
             PdfWriter.getInstance(document, new FileOutputStream(FILE));
             document.open();
             
          //add meta data
             document.addTitle("Class Report");
             document.addSubject("Class: " + class_name);
             document.addAuthor("EZReports");
             document.addCreator("EZReports");
          //meta data end
             
          //add content
             Paragraph para = new Paragraph("Class Report", catFont);
             String produceName = base.user.getFirstName() + " " + base.user.getLastName();
             para.add(new Paragraph("Year: " + sheets.getYear() + "        Term: " + sheets.getTerm() + "        Grade: " + sheets.getGrade() + "        Class: " + class_name + "        Produced by: " + produceName, smallBold));
             para.add(new Paragraph("Date & Time: " + new Date(), smallBold));
             
             addEmptyLine(para, 1);
         	
             // add a table
             PdfPTable table = new PdfPTable(11);
             table.setWidthPercentage(100);

             // t.setBorderColor(BaseColor.GRAY);
             // t.setPadding(4);
             // t.setSpacing(4);
             // t.setBorderWidth(1);

             PdfPCell c1 = new PdfPCell(new Phrase("Index", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);

             c1 = new PdfPCell(new Phrase("Name", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);

             c1 = new PdfPCell(new Phrase(sheet.getColumnNames().get(2), small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             c1 = new PdfPCell(new Phrase(sheet.getColumnNames().get(3), small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             c1 = new PdfPCell(new Phrase(sheet.getColumnNames().get(4), small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             c1 = new PdfPCell(new Phrase("Total", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             c1 = new PdfPCell(new Phrase("Average", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             c1 = new PdfPCell(new Phrase("Z-Score", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             c1 = new PdfPCell(new Phrase("English", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);

             c1 = new PdfPCell(new Phrase("Attendence %", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             c1 = new PdfPCell(new Phrase("Rank", small));
             c1.setHorizontalAlignment(Element.ALIGN_CENTER);
             table.addCell(c1);
             
             table.setHeaderRows(1);
             
             for (Object row: model.getDataVector()){
            	 for (int i=0; i<11; i++){
            		 @SuppressWarnings("rawtypes")
            		 String value = String.valueOf(((Vector) row).get(i));
            		 
            		 if (i!=1){
            			 c1 = new PdfPCell(new Phrase(value, small));
                		 c1.setFixedHeight(30);
            			 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            			 c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            		 }else{
            			 c1 = new PdfPCell(new Phrase(value, small2));
            		 }
            		 c1.setFixedHeight(25);
            		 table.addCell(c1);
            	 }
             }

             para.add(table);
             //table creation end
         	
             
             document.add(para);
          //content end
             
             document.close();
    	 } catch (Exception e) {
             e.printStackTrace();
    	 }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
                paragraph.add(new Paragraph(" "));
        }
    }
    
    @SuppressWarnings("rawtypes")
	public static void createPDFForIndividual(Base base, String src, Sheet sheet, DefaultTableModel model, String class_name, Sheets sheets){
    	FILE = src.substring(0, src.length()-4) + ".pdf";
		
        String produceName = base.user.getFirstName() + " " + base.user.getLastName();
        
    	try {
    		Document document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            
            //add meta data
            document.addTitle("Individual Report");
            document.addSubject("Individual reports");
            document.addAuthor("EZReports");
            document.addCreator("EZReports");
            //meta data end
            
            String subjectName1 = sheet.getColumnNames().get(2);
            String subjectName2 = sheet.getColumnNames().get(3);
            String subjectName3 = sheet.getColumnNames().get(4);
            
            SQLiteController.initializeStatement();
            String sub1AVG = SQLiteController.getSubjectAVG(subjectName1);
            String sub2AVG = SQLiteController.getSubjectAVG(subjectName2);
            String sub3AVG = SQLiteController.getSubjectAVG(subjectName3);

            String sub1MAX = SQLiteController.getSubjectMax(subjectName1);
            String sub2MAX = SQLiteController.getSubjectMax(subjectName2);
            String sub3MAX = SQLiteController.getSubjectMax(subjectName3);
            
            String sub1MIN = SQLiteController.getSubjectMin(subjectName1);
            String sub2MIN = SQLiteController.getSubjectMin(subjectName2);
            String sub3MIN = SQLiteController.getSubjectMin(subjectName3);
            SQLiteController.closeConnection();
            
            int counter = 0;
            for (Object row: model.getDataVector()){
            	 counter+=1;
           		 //add content
                 Paragraph para = new Paragraph("Student Report in year "+sheets.getYear()+" term "+sheets.getTerm()+" grade "+sheets.getGrade(), catFont);
                 para.add(new Paragraph("Date & Time: " + new Date(), smallBold));
                 addEmptyLine(para, 1);
                 
                 para.add(new Paragraph("Year of exam: " + sheets.getYear() + "        Term: " + sheets.getTerm() + "        Grade: " + sheets.getGrade() + "        Class: " + class_name + "        Produced by: " + produceName, small));
                 
                 para.add(new Paragraph("Student name: "+ String.valueOf(((Vector) row).get(1)) + "        Student Index: "+String.valueOf(((Vector) row).get(0)), smallBold)); // put student name

                 para.add(new Paragraph("Rank: " + String.valueOf(((Vector) row).get(10)), rank));
                 addEmptyLine(para, 1);
             	
                 // add a table
                 PdfPTable table = new PdfPTable(5);
                 table.setWidthPercentage(80);

                 PdfPCell c1 = new PdfPCell(new Phrase("Subject Name", small));
                 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                 c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 table.addCell(c1);

                 c1 = new PdfPCell(new Phrase("Subject Avg", small));
                 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                 c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 table.addCell(c1);

                 c1 = new PdfPCell(new Phrase("Subject MAX", small));
                 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                 c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 table.addCell(c1);
                 
                 c1 = new PdfPCell(new Phrase("Subject MIN", small));
                 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                 c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 table.addCell(c1);
                 
                 c1 = new PdfPCell(new Phrase("Student's mark", small));
                 c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                 c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                 table.addCell(c1);
                 
                 table.setHeaderRows(1);
                 
                 addCell(table, subjectName1, true);
                 addCell(table, sub1AVG, true);
                 addCell(table, sub1MAX, true);
                 addCell(table, sub1MIN, true);
                 addCell(table, String.valueOf(((Vector) row).get(2)), true);
                 
                 addCell(table, subjectName2, true);
                 addCell(table, sub2AVG, true);
                 addCell(table, sub2MAX, true);
                 addCell(table, sub2MIN, true);
                 addCell(table, String.valueOf(((Vector) row).get(3)), true);
                 
                 addCell(table, subjectName3, true);
                 addCell(table, sub3AVG, true);
                 addCell(table, sub3MAX, true);
                 addCell(table, sub3MIN, true);
                 addCell(table, String.valueOf(((Vector) row).get(4)), true);
       			 
                 addCell(table, "", false);
                 addCell(table, "", false);
                 addCell(table, "", false);
                 addCell(table, "Average: ", false);
                 addCell(table, String.valueOf(((Vector) row).get(6)), false);
                 
                 addCell(table, "English: ", false);
                 addCell(table, String.valueOf(((Vector) row).get(8)), false);
                 addCell(table, "", false);
                 addCell(table, "Total: ", false);
                 addCell(table, String.valueOf(((Vector) row).get(5)), false);
                 
                 addCell(table, "Attendence: ", false);
                 addCell(table, String.valueOf(((Vector) row).get(9)) + "%", false);
                 addCell(table, "", false);
                 addCell(table, "Z-Score: ", false);
                 addCell(table, String.valueOf(((Vector) row).get(7)), false);
       			 
       			 table.addCell(c1);
       			 para.add(table);
       			 
       			 if (counter%2==0){
       				 document.add(para);
       				 document.newPage();
       			 }else{
       				 addEmptyLine(para, 1);
       				 para.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------", smallBold));
       				 addEmptyLine(para, 1);
       				 document.add(para);
       			 }

       			 
	        }
	        document.close();
    	} catch (Exception e) {
    		e.printStackTrace();
		}
   	}
    
    private static void addCell(PdfPTable table, String data, boolean border){
    	PdfPCell c1 = new PdfPCell(new Phrase(data, small));
    	c1.setFixedHeight(30);
    	c1.setHorizontalAlignment(Element.ALIGN_CENTER);
    	c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
    	if (!border){
    		c1.setBorder(0);
    	}
    	table.addCell(c1);
    }

	
	
	public static DefaultTableModel genReport(Sheets sheets, String class_name){
		SQLiteController.initializeStatement();
		
		//Background to initialize a model
		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel() {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Integer.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		
		Sheet sheet = sheets.getSheets().get(sheets.getClassIds().indexOf((class_name)));
		
		//Identify the subject names
		String sub1 = sheet.getColumnNames().get(2);
		String sub2 = sheet.getColumnNames().get(3);
		String sub3 = sheet.getColumnNames().get(4);
		
		//Getting subject ids
		int subId1 = SQLiteController.getSubjectID(sub1);
		int subId2 = SQLiteController.getSubjectID(sub2);
		int subId3 = SQLiteController.getSubjectID(sub3);
		
		Vector<String> columnNames  = new Vector<String>();
		columnNames.add("Index"); columnNames.add("Name"); columnNames.add(sub1); columnNames.add(sub2); columnNames.add(sub3);
		columnNames.add("Total"); columnNames.add("Average"); columnNames.add("Score"); columnNames.add("English"); columnNames.add("Attendence"); columnNames.add("Rank");
		
		
		//Getting data required
		Vector<Vector<?>> data = new Vector<Vector<?>>();
		
		//Reform the db as required
		SQLiteController.classReport(class_name);
		
		ResultSet rs;
		try {
			//Rank handling variables
			Student previousStd = null;
			int r = 0;
			
			//Handling present people
			rs = SQLiteController.getStdData();
			while (rs.next()){
				//Primary requirements to create a student
				int index = rs.getInt(1);
				Object [] nameAttendence = SQLiteController.getNameAttendence(index);
				String name = (String) nameAttendence[0];
				
				//Create a student
				Student s = new Student(index, name);
				
				//set attendence (In database empty value stays as -1.0)
				Double attendence = Double.valueOf((Double) nameAttendence[1]);
				if (attendence!=-1.0){
					s.setAttendence(attendence);
				}
				
				//Set marks (Every one has values)
				Double mark1 = Double.valueOf(SQLiteController.getMark(index, subId1));
				Double mark2 = Double.valueOf(SQLiteController.getMark(index, subId2));
				Double mark3 = Double.valueOf(SQLiteController.getMark(index, subId3));
				Double markEnglish = Double.valueOf(SQLiteController.getMark(index, 1));
				s.getSubjectMarks()[0] = mark1;
				s.getSubjectMarks()[1] = mark2;
				s.getSubjectMarks()[2] = mark3;
				if (markEnglish!=-1.0){s.getSubjectMarks()[3] = markEnglish;}

				
				//Setting up total, avg, zscore
				Double total = Double.valueOf(rs.getDouble(2));
				Double average = Double.valueOf(rs.getDouble(3));
				Double zscore = Double.valueOf(rs.getDouble(4));
				s.setTotal(total);
				s.setAverage(average);
				s.setZScore(zscore);
				
				//Leave absentees
				if (mark1==-1.0 || mark2==-1.0 || mark3==-1.0){
					continue;
				}

				//Rank processing
				if (previousStd == null){
					r = 1;
					s.setRank(r);
					previousStd = s;
				}else{
					r = r + 1;
					if (previousStd.getZScore()!=zscore){
						s.setRank(r);
						previousStd = s;
					}else{
						s.setRank(previousStd.getRank());
						previousStd = s;
					}
				}
				
				//add student to data vector
				data.add(s.getVector());
			}
			
			
			//Absentees handle
			ArrayList<Student> absentees = new ArrayList<Student>();
			
			rs = SQLiteController.getAbsentees();
			while(rs.next()){
				//Primary requirements to create a student
				int index = rs.getInt(1);
				Object [] nameAttendence = SQLiteController.getNameAttendence(index);
				String name = (String) nameAttendence[0];

				//Create a student
				Student s = new Student(index, name);
				
				//set attendence (In database empty value stays as -1.0)
				Double attendence = (Double) nameAttendence[1];
				if (attendence != -1.0){
					s.setAttendence(attendence);
				}
				
				//Set marks (In database empty value stays as -1.0)
				Double mark1 = Double.valueOf(SQLiteController.getMark(index, subId1));
				Double mark2 = Double.valueOf(SQLiteController.getMark(index, subId2));
				Double mark3 = Double.valueOf(SQLiteController.getMark(index, subId3));
				Double markEnglish = Double.valueOf(SQLiteController.getMark(index, 1));
				if (mark1 != -1.0){ s.getSubjectMarks()[0] = mark1;}
				if (mark2 != -1.0){ s.getSubjectMarks()[1] = mark2;}
				if (mark3 != -1.0){ s.getSubjectMarks()[2] = mark3;}
				if (markEnglish != -1.0){ s.getSubjectMarks()[3] = markEnglish;}
				

				//Set total and average
				if(mark1!=-1.0 || mark2!=-1.0 || mark3!=-1.0){
					Double [] TotAvg = SQLiteController.getTotAvg(index);
					s.setTotal(TotAvg[0]);
					s.setAverage(TotAvg[1]);
				}
				
				//Add student to absentees list
				absentees.add(s);
			}
			
			Collections.sort(absentees); //sort based on total
			
			for (Student s : absentees){
				data.add(s.getVector());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		model.setDataVector(data, columnNames);
		SQLiteController.closeConnection();
		return model;
		
	}
	
	public static void upload(User user, int year, int term, int grade, String classRoom, DefaultTableModel data, JLabel status){
		//initialize or update exam entry
		int exam_id = APIHandler.add_update_exam(user, year, term, grade);
		
		//class name entry
		int class_id = APIHandler.add_update_class(user, classRoom);
		
		//clear previous data
		APIHandler.clear_performance(user, exam_id, class_id);
		
		//add or update subject entries
		String subject1 = data.getColumnName(8); //English
		String subject2 = data.getColumnName(2);
		String subject3 = data.getColumnName(3);
		String subject4 = data.getColumnName(4);
		int sub1_id = APIHandler.add_update_subject(user, subject1);
		int sub2_id = APIHandler.add_update_subject(user, subject2);
		int sub3_id = APIHandler.add_update_subject(user, subject3);
		int sub4_id = APIHandler.add_update_subject(user, subject4);
		
		//set the owner of the data set
		APIHandler.add_update_owner(user, exam_id, class_id);
		
		
		
		//detecting progress
		int tot = data.getDataVector().size();
		int count = 0;
		Color foreColor = Color.WHITE;
		status.setForeground(foreColor);
		
		//lets handle students' data
		for (Object vec: data.getDataVector()){
			count++;
			status.setText(count+"/"+tot+" completed.");
			
			//add_update student
			@SuppressWarnings("unchecked")
			Vector<String> row = ((Vector<String>) vec);
			int student_id = APIHandler.add_update_student(user, row.get(1), row.get(0));
		
			//add marks
			APIHandler.add_update_mark(user, exam_id, student_id, sub1_id, row.get(8));
			APIHandler.add_update_mark(user, exam_id, student_id, sub2_id, row.get(2));
			APIHandler.add_update_mark(user, exam_id, student_id, sub3_id, row.get(3));
			APIHandler.add_update_mark(user, exam_id, student_id, sub4_id, row.get(4));
			
			//add performance
			APIHandler.add_update_performance(user, exam_id, student_id, class_id, row.get(5), row.get(6), row.get(7), row.get(9), row.get(10));
		}
	}
		
		public static void uploadAll(User user, int year, int term, int grade, Sheets sheets, JLabel status){
			//initialize or update exam entry
			int exam_id = APIHandler.add_update_exam(user, year, term, grade);
			
			//detecting progress
			int tot = sheets.getTotalStudents();
			int count = 0;
			Color foreColor = Color.WHITE;
			status.setForeground(foreColor);
		
			for (String classRoom:sheets.getClassIds()){
				//class name entry
				int class_id = APIHandler.add_update_class(user, classRoom);
				
				//clear previous data
				APIHandler.clear_performance(user, exam_id, class_id);
				
				//process data
				DefaultTableModel data = ClassReport.genReport(sheets, classRoom);
				
				//add or update subject entries
				String subject1 = data.getColumnName(8); //English
				String subject2 = data.getColumnName(2);
				String subject3 = data.getColumnName(3);
				String subject4 = data.getColumnName(4);
				int sub1_id = APIHandler.add_update_subject(user, subject1);
				int sub2_id = APIHandler.add_update_subject(user, subject2);
				int sub3_id = APIHandler.add_update_subject(user, subject3);
				int sub4_id = APIHandler.add_update_subject(user, subject4);
				
				//set the owner of the data set
				APIHandler.add_update_owner(user, exam_id, class_id);
				
				//lets handle students' data
				for (Object vec: data.getDataVector()){
					count++;
					status.setText(count+"/"+tot+" completed.");
					
					//add_update student
					@SuppressWarnings("unchecked")
					Vector<String> row = ((Vector<String>) vec);
					int student_id = APIHandler.add_update_student(user, row.get(1), row.get(0));
				
					//add marks
					APIHandler.add_update_mark(user, exam_id, student_id, sub1_id, row.get(8));
					APIHandler.add_update_mark(user, exam_id, student_id, sub2_id, row.get(2));
					APIHandler.add_update_mark(user, exam_id, student_id, sub3_id, row.get(3));
					APIHandler.add_update_mark(user, exam_id, student_id, sub4_id, row.get(4));
					
					//add performance
					APIHandler.add_update_performance(user, exam_id, student_id, class_id, row.get(5), row.get(6), row.get(7), row.get(9), row.get(10));
				}
			}
		}
}
