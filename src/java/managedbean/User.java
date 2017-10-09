package managedbean;
import DatabaseConnection.DataConnect;
import DatabaseConnection.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import dao.LoginDAO;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;



@ManagedBean (name = "User", eager = true)
@SessionScoped
public class User {
        private int Id;
        private String Message;
       
	private String FirstName;
        
	private String LastName;
       
	private String Address;
        
	private Integer PhoneNumber;
        
	private String Email;
        
	private String Username;
        
	private String Password;
        
	public boolean rememberMe;
       
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
        
        
        
      //validate login
        public String ValidateUsernamePassowrd(){
            boolean valid = LoginDAO.validate(Username,Password);
            if(valid){
                HttpSession session = Session.getSession();
			session.setAttribute("username", Username);
                        FacesContext fc = FacesContext.getCurrentInstance();
                        User user=(User) fc.getExternalContext().getSessionMap().get("User");
                        Username = user.getUsername();
			return "main";
            }
            else {
                FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
			return "login";
            }
        }
        //logout event, invalidate session
	public String logout() {
		HttpSession session = Session.getSession();
		session.invalidate();
		return "login";
	}
        //Action Method to register
	public String addUser() {  
             int i=0;        
         Connection con = null;
		PreparedStatement ps = null;

		try { 
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("INSERT INTO User(FirstName,LastName,Address,PhoneNumber,Email,Username,Password ) VALUES(?,?,?,?,?,?,?)");
                        ps.setString(1, FirstName );
			ps.setString(2, LastName);
                        ps.setString(3, Address);
                        ps.setInt(4, PhoneNumber);
                        ps.setString(5, Email);
                        ps.setString(6, Username);
                        ps.setString(7, Password);
                        
			 i = ps.executeUpdate();
                         System.out.println(i +"hooo");
                }
			catch (Exception e) {
                System.out.println(e);
            } finally {
                try {
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (i > 0) {
                Message="Successfully Registered";
            return "login";
        } else
                Message="error signing up";
            return "Register";
		
     }
           
}