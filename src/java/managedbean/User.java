package managedbean;
import DatabaseConnection.DataConnect;
import DatabaseConnection.Session;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import dao.LoginDAO;
import static dao.LoginDAO.find;
import static dao.LoginDAO.findId;
import static dao.LoginDAO.findUsername;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import stockapi.StockApiBean;
import static stockapi.watchList.Price;



@ManagedBean (name = "User", eager = true)
@SessionScoped
public class User implements Serializable{
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
        
        private String roles;
        
        private List<User> RequestList;
        
        private List<User> ManagersList;
 
   
   
        
        public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
    
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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
        public List<User> getRequestList() {
            List<User> RequestList = new ArrayList<User>();
            String roles = "NOT APPROVED";
                Connection conn = null;
	         Statement statement = null;
                 
                 try {
			conn = DataConnect.getConnection();
                        statement=conn.createStatement();
                        ResultSet rs=statement.executeQuery("SELECT * FROM `User` WHERE roles='"+"NOT APPROVED"+"';");

			while(rs.next()) {
                            User user = new User();
                            //System.out.println(".....here");
                            //System.out.println(rs.getString("Username"));
                            user.setUsername(rs.getString("Username"));
                            RequestList.add(user);
                            //System.out.println(".......listttt"+RequestList);
                        }
                        System.out.println(".......listttt"+RequestList);
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return RequestList;
		} finally {
			DataConnect.close(conn);
		}
            
        return RequestList;
    }

    public void setRequestList(List<User> RequestList) {
        this.RequestList = RequestList;
    }
     public List<User> getManagersList() {
         
            List<User> RequestList = new ArrayList<User>();
            String roles = "NOT APPROVED";
                Connection conn = null;
	         Statement statement = null;
                 
                 try {
			conn = DataConnect.getConnection();
                        statement=conn.createStatement();
                        ResultSet rs=statement.executeQuery("SELECT * FROM `User` WHERE roles='"+"Manager"+"';");

			while(rs.next()) {
                            User user = new User();
                            //System.out.println(".....here");
                            //System.out.println(rs.getString("Username"));
                            user.setUsername(rs.getString("Username"));
                            user.setPhoneNumber(rs.getInt("PhoneNumber"));
                            user.setEmail(rs.getString("Email"));
                            RequestList.add(user);
                            //System.out.println(".......listttt"+RequestList);
                        }
                        System.out.println(".......listttt"+RequestList);
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return RequestList;
		} finally {
			DataConnect.close(conn);
		}
            
        return RequestList;
    }

    public void setManagersList(List<User> ManagersList) {
        this.ManagersList = ManagersList;
    }
    public String selectManger(String username) throws SQLException{
        Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
        String Username=findUsername(uid);
        int i=0;
        Connection con = null;
        PreparedStatement ps = null;
          try {
          con = DataConnect.getConnection();
                        
			ps = (PreparedStatement) con.prepareStatement("INSERT INTO UserToManager(uid,manager,Username) VALUES(?,?,?)");
                          ps.setInt(1,uid );
                          ps.setString(2, username);
                          ps.setString(3, Username);
			 i = ps.executeUpdate();
                         
    }catch (Exception e) {
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
                Message = "Manger Selected";
            return "SelectManager";
        } else
                Message ="Error in Selecting";
            return "SelectManager";
          
    }
    public String editProfile(String firstname, String lastname, String address,int phoneNumber, String email, String Username, String Password){
        System.out.println("in edit profile..."+firstname+lastname+address+phoneNumber+email+Username+Password);
        
//        Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
//                    .getExternalContext()
//                    .getSessionMap().get("id"));
        String uid=findId(Username);
        System.out.println("userid"+uid);
         int i=0;
            Connection con = null;
            PreparedStatement ps = null;
                       		try { 
			con = DataConnect.getConnection();
                        
			ps = (PreparedStatement) con.prepareStatement("UPDATE User SET FirstName=?,LastName=?,Address=?,PhoneNumber=?,Email=?,Username=?,Password=? WHERE id= ?");
                          
                          ps.setString(1,firstname );
                          ps.setString(2, lastname);
                          ps.setString(3, address);
                          ps.setInt(4, phoneNumber);
                          ps.setString(5, email);
                          ps.setString(6, Username);
                          ps.setString(7, Password);
                          ps.setString(8, uid);
                          
			 i = ps.executeUpdate();
                    
                        
                }
                catch (SQLException ex) {
               System.out.println("Login error -->" + ex.getMessage());
            }  finally {
                try {
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }               
             if (i > 0) {
                Message = "Edited";
            return "UserProfile";
        } else
                Message ="Error in Editing";
            return "UserProfile";
    }
       
             //validate logi
        public String ValidateUsernamePassowrd(){
            
            boolean valid = LoginDAO.validate(Username,Password);
            roles = LoginDAO.find(Username);
            String id= LoginDAO.findId(Username);
             System.out.println("......." + roles);
            if(valid || roles.equals("User") || roles.equals("Manager") || roles.equals("Admin")){
                HttpSession session = Session.getSession();
			session.setAttribute("username", Username);
                        session.setAttribute("uid", id);
                        FacesContext fc = FacesContext.getCurrentInstance();
                        User userr=(User) fc.getExternalContext().getSessionMap().get("User");
                      
                        Username = userr.getUsername();
                        
                        System.out.println("......." + Username);
                        System.out.println("......." + roles);
                        
                        if(roles.equals("User")){
                            return "UserDashboard";
                        }
                        else if(roles.equals("Manager")){
                            return "managerDashboard";
                        }
                        else if(roles.equals("Admin")){
                            return "adminDashboard";
                        }
                        else{
                            return "login";
                        }
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
        public String approveRequest(String username) throws SQLException{
            System.out.println("......approve"+username);
            String uid=findId(username);
            int i=0;
            Connection con = null;
            PreparedStatement ps = null;
                       		try { 
			con = DataConnect.getConnection();
                        
			ps = (PreparedStatement) con.prepareStatement("UPDATE User SET roles= ?"+ "WHERE id= ? ");
                          ps.setString(1, "Manager");
                          ps.setString(2, uid);
			 i = ps.executeUpdate();
                    
                        
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
                Message = "Approved";
            return "adminDashboard";
        } else
                Message ="Error in Approving";
            return "adminDashboard";
         }   
  public String declineRequest(String username){
      Message ="Declined";
      return Message;
  }
        //Action Method to register
	public String addUser() {  
             int i=0;        
         Connection con = null;
         PreparedStatement ps = null;
         if(roles.equals("Manager")){
              		try { 
			con = DataConnect.getConnection();
                        
			ps = (PreparedStatement) con.prepareStatement("INSERT INTO User(FirstName,LastName,Address,PhoneNumber,Email,Username,Password,roles) VALUES(?,?,?,?,?,?,?,?)");
                        ps.setString(1, FirstName);
			ps.setString(2, LastName);
                        ps.setString(3, Address );
                        ps.setInt(4, PhoneNumber);
                        ps.setString(5, Email);
                        ps.setString(6, Username);
                        ps.setString(7, Password);
                        ps.setString(8,"NOT APPROVED");
			 i = ps.executeUpdate();
                        System.out.println(i +"NOT APPRoved");
                        
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
                Message = "Request sent";
            return "login";
        } else
                Message ="error signing up";
            return "Register";
             
         }
         else{
  		try { 
			con = DataConnect.getConnection();
                        
			ps = (PreparedStatement) con.prepareStatement("INSERT INTO User(FirstName,LastName,Address,PhoneNumber,Email,Username,Password,roles) VALUES(?,?,?,?,?,?,?,?)");
                        ps.setString(1, FirstName);
			ps.setString(2, LastName);
                        ps.setString(3, Address );
                        ps.setInt(4, PhoneNumber);
                        ps.setString(5, Email);
                        ps.setString(6, Username);
                        ps.setString(7, Password);
                        ps.setString(8,roles);
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
                Message = "Successfully Registered";
            return "login";
        } else
                Message ="error signing up";
            return "Register";
         }	
     }
}