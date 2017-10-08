package managedbean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean (name = "User", eager = true)
@RequestScoped
public class User {
        private String Message;
       
	private String FirstName;
        
	private String LastName;
       
	private String Address;
        
	private Integer PhoneNumber;
        
	private String Email;
        
	private String Username;
        
	private String Password;
        
	public boolean rememberMe;
	
	//Action Method to register
	public String addUser() {  
            
            Message=Username + " " + "got Registered Successfully, Please sign In";
		return "Register";
		
	}
        public String login(){
                  
    	if(Username.equals("Admin") && Password.equals("Admin")) {
    		Message ="Successfully logged-in.";
    		return "login";
    	} else {
    		Message ="There is an error in signing up, Please Sign In again!";
    		return "login";
    	}
    }
        

	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public Integer getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(Integer phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
       public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
        public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
}