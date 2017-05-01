package test.main.logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.junit.Test;

import main.logic.Student;

public class StudentTest {

	@Test
	public void getVector() {
		Student student = new Student(140457, "Akila");
		
		student.getSubjectMarks()[0] = (double) 52.0;
		student.getSubjectMarks()[1] = (double) 53.4;
		student.getSubjectMarks()[2] = (double) 54.5678;
		student.getSubjectMarks()[3] = (double) 50; //English
	
		student.setAttendence(56.59);
		student.setTotal(57.77);
		student.setAverage(89.78);
		student.setZScore(1.1);
		
		student.setRank(1);
		
		Vector<String> expecteds = new Vector<String>();
		expecteds.add("140457"); expecteds.add("Akila"); expecteds.add("52.00"); expecteds.add("53.40");
		expecteds.add("54.57");
		expecteds.add("57.77"); expecteds.add("89.78"); expecteds.add("1.1000"); expecteds.add("50.00");
		expecteds.add("56.59"); expecteds.add("1");
		
		assertArrayEquals(expecteds.toArray(), student.getVector().toArray());
		
	}
	
	@Test
	public void compareTo() {
		//This method performs on absent students for the core subjects
		Student s1 = new Student(140457, "Akila");
		s1.getSubjectMarks()[1] = (double) 20;
		s1.getSubjectMarks()[2] = (double) 30;
		s1.getSubjectMarks()[3] = (double) 50; //English
		s1.setAttendence(56.59);
		s1.setTotal((double) 50);
		
		Student s2 = new Student(140400, "Nimal");
		s2.getSubjectMarks()[1] = (double) 60;
		s2.getSubjectMarks()[2] = (double) 30;
		s2.getSubjectMarks()[3] = (double) 40; //English
		s2.setAttendence(56.59);
		s2.setTotal((double) 90);
		
		Student s3 = new Student(140500, "Kamal");
		s3.getSubjectMarks()[1] = (double) 50;
		s3.getSubjectMarks()[2] = (double) 30;
		s3.getSubjectMarks()[3] = (double) 40; //English
		s3.setAttendence(56.59);
		s3.setTotal((double) 80);
		
		Student [] expected = new Student [] {s2, s3, s1};
		
		ArrayList<Student> actual = new ArrayList<Student>();
		actual.add(s1);
		actual.add(s2);
		actual.add(s3);
		Collections.sort(actual);

		assertArrayEquals(expected, actual.toArray());
		
	}

}
