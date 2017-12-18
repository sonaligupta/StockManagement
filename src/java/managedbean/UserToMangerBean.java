/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import DatabaseConnection.DataConnect;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import static dao.LoginDAO.findId;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mac
 */
@ManagedBean (name = "Usertomanagerbean", eager = true)
@SessionScoped
public class UserToMangerBean {
    private int uid;
    private String manager;
    private String amount;
    private String Message;
    private String Users;

    public String getUsers() {
        return Users;
    }

    public void setUsers(String Users) {
        this.Users = Users;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
    private List<UserToMangerBean> ReqManagersList;
    private List<UserToMangerBean> UsersList;

    public List<UserToMangerBean> getUsersList() {
        
          List<UserToMangerBean> RequestList = new ArrayList<UserToMangerBean>();
            FacesContext fc = FacesContext.getCurrentInstance();
                        User userr=(User) fc.getExternalContext().getSessionMap().get("User");
                      
                       String managerName = userr.getUsername();
                Connection conn = null;
	         Statement statement = null;
                 
                 try {
			conn = DataConnect.getConnection();
                        statement=conn.createStatement();
                        ResultSet rs=statement.executeQuery("SELECT Username,amount FROM `UserToManager` WHERE manager='"+managerName+"';");

			while(rs.next()) {
                            UserToMangerBean user = new UserToMangerBean();
                            //System.out.println(".....here");
                            //System.out.println(rs.getString("Username"));
                            user.setUsers(rs.getString("Username"));
                            user.setAmount(rs.getString("amount"));
                            RequestList.add(user);
                            //System.out.println(".......listttt"+RequestList);
                        }
                        System.out.println(".......lusersistttt"+RequestList);
		} catch (SQLException ex) {
			System.out.println("DB error -->" + ex.getMessage());
			return RequestList;
		} finally {
			DataConnect.close(conn);
		}
            
        return RequestList;
        
    }

    public void setUsersList(List<UserToMangerBean> UsersList) {
        this.UsersList = UsersList;
    }

    public List<UserToMangerBean> getReqManagersList() {
                    List<UserToMangerBean> RequestList = new ArrayList<UserToMangerBean>();
            Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
                Connection conn = null;
	         Statement statement = null;
                 
                 try {
			conn = DataConnect.getConnection();
                        statement=conn.createStatement();
                        ResultSet rs=statement.executeQuery("SELECT manager FROM `UserToManager` WHERE uid='"+uid+"';");

			while(rs.next()) {
                            UserToMangerBean user = new UserToMangerBean();
                            //System.out.println(".....here");
                            //System.out.println(rs.getString("Username"));
                            user.setManager(rs.getString("manager"));
                           
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
    

    public void setReqManagersList(List<UserToMangerBean> ReqManagersList) {
        this.ReqManagersList = ReqManagersList;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    public String findManagerId(String manager){
        
           Connection con = null;
	PreparedStatement ps = null;
        	try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select id from UserToManager where manager = ?");
			ps.setString(1, manager);

			ResultSet rs = ps.executeQuery();
 
			if (rs.next()) {                         
				return rs.getString(1);
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
                        return "failed";
			
		} finally {
			DataConnect.close(con);
		}
		return "failed";
    }
    public String requestManager(String manager, int amount) throws SQLException{
         System.out.println("..........manager"+manager);
        System.out.println("..........amount"+amount);
        
                  String mid=findManagerId(manager);
                  System.out.println("..........mid"+mid); 
            int i=0;
            Connection con = null;
            PreparedStatement ps = null;
                       		try { 
			con = DataConnect.getConnection();
                        
			ps = (PreparedStatement) con.prepareStatement("UPDATE UserToManager SET amount = ? WHERE id= ? ");
                          ps.setInt(1, amount);
                          ps.setString(2, mid);
			 i = ps.executeUpdate();
                    
                        
                }
                catch (SQLException ex) {
               System.out.println("DB error -->" + ex.getMessage());
            } finally {
                try {
                    con.close();
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }               
             if (i > 0) {
                Message = "Sent Requent";
            return "RequestManager";
        } else
                Message ="Error in sending";
            return "RequestManager";
}
}
