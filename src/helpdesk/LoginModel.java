/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shameemah1
 */
public class LoginModel extends DAO {
	private String role;
        private String password;
        private int id;
        
        public String getPassword() {
		return password;
	}
	
        public void setPassword(String password) {
		this.password = password;
	}
        
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
                

	public Boolean findByUsername(String username) {
		 
	String query = "SELECT * FROM s_fuse_login_table WHERE username = ?;";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
            	        this.role=resultSet.getString("role");
            		this.password=resultSet.getString("password");
                        this.id=resultSet.getInt("id");
                    if (username.equals(resultSet.getString("username"))){
                    	setRole(this.role);
                        setPassword(this.password);
                        setId(this.id);
                    	return true;
                    }
            }
        } catch(SQLException e){
            System.out.println("Error Finding User by Username: " + e);
        }
        return false;
    
    }
}