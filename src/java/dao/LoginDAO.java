/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import DatabaseConnection.DataConnect;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Sonali
 */
public class LoginDAO {
    public static boolean validate(String user, String password) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select Username, Password, roles from User where Username = ? and Password = ?");
			ps.setString(1, user);
			ps.setString(2, password);
                        

			ResultSet rs = ps.executeQuery();
                        System.out.println("..... RESULT"+ rs);

			if (rs.next()) {
                            
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return false;
		} finally {
			DataConnect.close(con);
		}
		return false;
	}
    
    public static String find(String username){
        Connection con = null;
	PreparedStatement ps = null;
        	try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select roles from User where Username = ?");
			ps.setString(1, username);
			
                        

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
    public static String findId(String username){
        Connection con = null;
	PreparedStatement ps = null;
        	try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select id from User where Username = ?");
			ps.setString(1, username);

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
    
    public static String findUsername(int id){
        Connection con = null;
	PreparedStatement ps = null;
        	try {
			con = DataConnect.getConnection();
			ps = (PreparedStatement) con.prepareStatement("Select Username from User where id = ?");
			ps.setInt(1, id);

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
}
