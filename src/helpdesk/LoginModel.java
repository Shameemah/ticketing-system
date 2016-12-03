package helpdesk;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains the getters and setters for the different fields displayed in the LogIn
 * GUI, and the findByUsername() method, which finds a user by their username
 * in the login table
 * @author Shameemah Fuseini-Codjoe
 * @version NetBeans IDE 8.2 (Build 201609300101)
 */
public class LoginModel extends DAO {
    
        //variables
	private String role;
        private String password;
        private int id;
        
        /**
         * 
         * @return the password
         */
        public String getPassword() {
		return password;
	}
	
        /**
         * 
         * @param password the password to set
         */
        public void setPassword(String password) {
		this.password = password;
	}
        
        /**
         * 
         * @return the role
         */
	public String getRole() {
		return role;
	}

        /**
         * 
         * @param role the role to set
         */
	public void setRole(String role) {
		this.role = role;
	}
        
        /**
         * 
         * @return the id
         */
        public int getId() {
            return id;
        }
        
        /**
         * 
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }
                

        /**
         * Queries for data from all columns that match the username entered and stores that in a result set, and
         * sets the role, password and id
         * @param username
         * @return true or false
         */
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