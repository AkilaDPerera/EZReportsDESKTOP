package main.logic;

import java.io.FileOutputStream;
import java.util.Date;


import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import main.gui.Base;


public class SubjectReport {
	private static String FILE = null;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static Font small2 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLDITALIC);
    private static Font small3 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);
    
    String subject;
	String subjectMax = null;
	String subjectMin = null;
	String subjectAvg = null;
	String subjectSD = null;
	//draw the curve
	int [] data = null;
	
	public SubjectReport(String subject){
		this.subject = subject;
		
		SQLiteController.initializeStatement();
		this.subjectMax = SQLiteController.getSubjectMax(subject);
		this.subjectMin = SQLiteController.getSubjectMin(subject);
		this.subjectAvg = SQLiteController.getSubjectAVG(subject);
		this.subjectSD = SQLiteController.getSubjectSD(subject);
		
		int [] distribution = new int[20];
		for (int i=0; i<20; i++){
			distribution[i] = SQLiteController.getCountInRange(subject, i*5, (i+1)*5);
		}
		data = distribution;
	
		SQLiteController.closeConnection();
	}

	public String getSubjectMax() {
		return subjectMax;
	}

	public String getSubjectMin() {
		return subjectMin;
	}

	public String getSubjectAvg() {
		return subjectAvg;
	}

	public String getSubjectSD() {
		return subjectSD;
	}

	public int[] getData() {
		return data;
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
                paragraph.add(new Paragraph(" "));
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
	
	public void createPDFForSubject(Base base, String src, Sheets sheets, String subject, String imgURL){
    	FILE = src.substring(0, src.length()-4) +"_" + subject + ".pdf";
    	
        String produceName = base.user.getFirstName() + " " + base.user.getLastName();
		
    	try {
    		Document document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            
            //add meta data
            document.addTitle("Subject Report");
            document.addSubject(this.subject+" reports");
            document.addAuthor("EZReports");
            document.addCreator("EZReports");
            //meta data end
            

       		 //add content
             Paragraph para = new Paragraph("Subject Report - "+this.subject, catFont);
             para.add(new Paragraph("Date & Time: " + new Date(), smallBold));
             addEmptyLine(para, 1);
             
             Paragraph p = new Paragraph("Year of exam: " + sheets.getYear() + "        Term: " + sheets.getTerm() + "        Grade: " + sheets.getGrade() + "        Produced by: " + produceName, smallBold);
             para.add(p);
             addEmptyLine(para, 1);
         	
             // add a table
             PdfPTable table = new PdfPTable(2);
             table.setWidthPercentage(50);
             
             addCell(table, "Subject MAX: ", true);
             addCell(table, this.subjectMax, true);
             
             addCell(table, "Subject MIN: ", true);
             addCell(table, this.subjectMin, true);
             
             addCell(table, "Subject AVG: ", true);
             addCell(table, this.subjectAvg, true);
             
             addCell(table, "Standard Deviation: ", true);
             addCell(table, this.subjectSD, true);
             
   			 para.add(table);
   			 
   			 addEmptyLine(para, 1);
   			 Paragraph e = new Paragraph("Distribution of marks of subject "+subject, small2);
  			 e.setAlignment(Element.ALIGN_CENTER);
  			 para.add(e);
   			 
   			 Image img = Image.getInstance(imgURL);
   			 img.setRotation((float) Math.PI/2);
   			 
   			 Paragraph ending = new Paragraph("Marks ranges", small3);
   			 ending.setAlignment(Element.ALIGN_CENTER);
   			 
   			 document.add(para);
   			 document.add(img);
   			 document.add(ending);
   			 
   			 document.close();
   			 
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
