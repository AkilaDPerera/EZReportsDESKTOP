package main.logic;

import java.text.DecimalFormat;
import java.util.Vector;

public class Student implements Comparable<Student>{
	private int index;
	private String name;
	private Double [] subjectMarks = new Double[]{null, null, null, null};
	
	private Double attendence=null;
	
	private Double total=null;
	private Double average=null;
	private Double zscore=null;
	
	private Integer rank=null;
	
	private DecimalFormat twoDecimal;
	private DecimalFormat fourDecimal;
	
	
	public Student(int index, String name){
		this.index = index;
		this.name = name;
		
		twoDecimal = new DecimalFormat("0.00");
		fourDecimal = new DecimalFormat("0.0000");
	}
	
	public Double getZScore(){
		return this.zscore;
	}
	
	public Integer getRank(){
		return this.rank;
	}
	
	public Double getTotal(){
		return this.total;
	}
	
	public Double[] getSubjectMarks(){
		return this.subjectMarks;
	}
	
	public void setZScore(Double zscore){
		this.zscore = zscore;
	}
	
	public void setRank(Integer rank){
		this.rank = rank;
	}
	
	public void setAttendence(Double attendence){
		this.attendence = attendence;
	}
	
	public void setTotal(Double total){
		this.total = total;
	}
	
	public void setAverage(Double average){
		this.average = average;
	}
	
	private String getSubMark(int subNum){
		Double temp = this.subjectMarks[subNum];
		if (temp!=null){
			return twoDecimal.format(temp);
		}else{
			return "AB";
		}
	}
	
	private String getTotalAs(){
		Double temp = this.total;
		if (temp!=null){
			return twoDecimal.format(temp);
		}else{
			return "NA";
		}
	}
	
	private String getAvgAs(){
		Double temp = this.average;
		if (temp!=null){
			return twoDecimal.format(temp);
		}else{
			return "NA";
		}
	}
	
	private String getZScoreAs(){
		Double temp = this.zscore;
		if (temp!=null){
			return fourDecimal.format(temp);
		}else{
			return "NA";
		}
	}
	
	private String getAttendenceAs(){
		Double temp = this.attendence;
		if (temp!=null){
			return twoDecimal.format(temp);
		}else{
			return "NA";
		}
	}
	
	private String getRankAs(){
		Integer temp = this.rank;
		if (temp!=null){
			return String.valueOf(temp);
		}else{
			return "NA";
		}
	}
	
	public Vector<String> getVector(){
		Vector<String> v = new Vector<String>();
		v.add(String.valueOf(this.index));
		v.add(this.name);
		
		v.add(getSubMark(0));
		v.add(getSubMark(1));
		v.add(getSubMark(2));
		
		v.add(getTotalAs());
		v.add(getAvgAs());
		v.add(getZScoreAs());
		
		v.add(getSubMark(3));
		v.add(getAttendenceAs());
		v.add(getRankAs());
		
		return v;
	}


	@Override
	public int compareTo(Student o) {
		//Comparing from the Z-Score is done at the DB
		//Comparing absentees by total
		double myTot = 0;
		double otherTot = 0;
		
		if (this.total == null){myTot = 0;}else{myTot = this.total;}
		
		if (o.getTotal() == null){otherTot = 0;}else{otherTot = o.getTotal();}
		
		if(myTot>= otherTot){
			return -1;
		}else{
			return 1;
		}
	}

}
