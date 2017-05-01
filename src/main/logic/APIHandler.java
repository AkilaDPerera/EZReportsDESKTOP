package main.logic;

import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class APIHandler {
	public static String baseURL = "http://127.0.0.1:8000/api/";
	
	public static ArrayList<String> getUsers(){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.get(baseURL+ "user/")
					.basicAuth("app", "maadpAa@452263293")
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			
			int no_users = ja.length();
			
			ArrayList<String> users = new ArrayList<String>();
			for (int t=0; t<no_users; t++){
				users.add((String) ja.getJSONObject(t).get("username"));
			}
			return users;
			
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static User getAuthenticated(JDialog com, String username, String password){
		try {
			String url = baseURL + "user/?username=" + username + "&password=" + password;
			
			HttpResponse<JsonNode> jsonResponse = Unirest.get(url)
					.basicAuth("app", "maadpAa@452263293")
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			
			int no_users = ja.length();
			
			//check whether we got any response
			if(no_users==0){
				//wrong auth
				JOptionPane.showMessageDialog(com, "Wrong authentication info... Try again", "Authentication failed...", JOptionPane.ERROR_MESSAGE);
				return null;
			}else{
				//successful
				User u = new User(username, password, (String) ja.getJSONObject(0).get("email"), String.valueOf(ja.getJSONObject(0).get("id")), 
						(String) ja.getJSONObject(0).get("first_name"), (String) ja.getJSONObject(0).get("last_name"));
				return u;
			}

		} catch (UnirestException e) {
			//internet fails
			JOptionPane.showMessageDialog(com, "<html>Something wrongs with your internet connection. <br>Check your internet conectivity and try again...</html>", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

	}
	
	public static User registerUser(String username, String password, String email, String first, String last){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "user/")
					.field("username", username)
					.field("password", password)
					.field("email", email)
					.field("first_name", first)
					.field("last_name", last)
					.basicAuth("app", "maadpAa@452263293")
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			User u = new User(username, password, (String) ja.getJSONObject(0).get("email"), String.valueOf(ja.getJSONObject(0).get("id")), 
					(String) ja.getJSONObject(0).get("first_name"), (String) ja.getJSONObject(0).get("last_name"));
			return u;
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer add_update_exam(User user, int year, int term, int grade){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "exam/")
					.field("year", year)
					.field("term", term)
					.field("grade", grade)
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			return (Integer) ja.getJSONObject(0).get("id");
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer add_update_class(User user, String classroom){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "classroom/")
					.field("name", classroom)
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			return (Integer) ja.getJSONObject(0).get("id");
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer add_update_subject(User user, String subject){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "subject/")
					.field("name", subject)
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			return (Integer) ja.getJSONObject(0).get("id");
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer add_update_owner(User user, int exam_id, int class_id){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "rowner/")
					.field("exam_id", exam_id)
					.field("classRoom_id", class_id)
					.field("user_id", user.getId())
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			return (Integer) ja.getJSONObject(0).get("id");
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer add_update_student(User user, String name, String index){
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "student/")
					.field("name", name)
					.field("index", Integer.valueOf(index.trim()))
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			return (Integer) ja.getJSONObject(0).get("index");
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer add_update_mark(User user, int exam_id, int student_id, int subject_id, String mark){
		Double score = ABNAChecker(mark);
		
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "mark/")
					.field("exam_id", exam_id)
					.field("student_id", student_id)
					.field("subject_id", subject_id)
					.field("mark", score)
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			return (Integer) ja.getJSONObject(0).get("id");
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer add_update_performance(User user, int exam_id, int student_id, int classRoom_id, String total, String avg, String z, String attendence, String rank){
		Double tot = ABNAChecker(total);
		Double average = ABNAChecker(avg);
		Double zscore = ABNAChecker(z);
		Double atte = ABNAChecker(attendence);
		Integer r =  (int) (ABNAChecker(rank)/1);
		
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post(baseURL+ "performance/")
					.field("exam_id", exam_id)
					.field("student_id", student_id)
					.field("classRoom_id", classRoom_id)
					.field("total", tot)
					.field("average", average)
					.field("zscore", zscore)
					.field("attendence", atte)
					.field("rank", r)
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
			org.json.JSONArray ja = jsonResponse.getBody().getArray();
			return (Integer) ja.getJSONObject(0).get("id");
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void clear_performance(User user, int exam_id, int classRoom_id){
		try {
			Unirest.get(baseURL+ "performance/")
					.queryString("exam_id", String.valueOf(exam_id))
					.queryString("classRoom_id", String.valueOf(classRoom_id))
					.basicAuth(user.getName(), user.getPassword())
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
	
	private static Double ABNAChecker(String value){
		Double score = 0.0;
		if (value.equals("AB")){
			score = -1.0;
		}else if(value.equals("NA")){
			score = -2.0;
		}else{
			score = Double.valueOf(value);
		}
		return score;
	}
	
	
	

}