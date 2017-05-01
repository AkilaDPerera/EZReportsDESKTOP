package main.logic;

public class User {
	private String username;
	private String password;
	private String email;
	private String id;
	private String firstName;
	private String lastName;
	
	public User(String name, String password, String email, String id, String first, String last){
		this.username = name;
		this.email = email;
		this.password = password;
		this.id = id;
		this.firstName = first;
		this.lastName = last;
	}

	public String getName() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	
	
}
