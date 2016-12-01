/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author shameemah1
 */
public class DAO {
    
    protected static Connection connection;
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://www.papademas.net:3306/tickets";
    String username = "fp411";
    String password = "411";
    Statement stmt = null;
    PreparedStatement pstmt = null;
    
    //instantiate PasswordHash object
    PasswordHash ph = new PasswordHash();

    
    public DAO() {
    	try {
            connection = DriverManager.getConnection(url, username, password);
        } catch(SQLException e) {
            System.out.println("Error creating connection to database: " + e);
            System.exit(-1);
        } catch(Exception e) {
            
        }
    }
    
       public void createTables() {
		
		   try{
                      //Register JDBC driver
                      Class.forName(DRIVER);
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
		      
		      //Execute a query
		      System.out.println("Creating table in given database...");
		      stmt = connection.createStatement();
		      
		      String sql = "CREATE TABLE s_fuse_login_table " +
		                   "(id INTEGER not NULL AUTO_INCREMENT, " +
		                   " username VARCHAR(255), " + 
		                   " password VARCHAR(60), " + 
                                   " role VARCHAR(255), " +
		                   " PRIMARY KEY ( id ))"; 

		      stmt.executeUpdate(sql);
		      System.out.println("Created login table in given database...");
                      
                      String sqlA = "CREATE TABLE s_fuse_ticket_table " +
		                   "(ticket_id INTEGER not NULL AUTO_INCREMENT, " +
		                   " summary VARCHAR(255) not NULL, " + 
                                   " status VARCHAR(25) not NULL, " + 
                                   " severity VARCHAR(10) not NULL, " + 
                                   " classification VARCHAR(25) not NULL, " + 
                                   " type VARCHAR(25) not NULL, " + 
                                   " internal_notes VARCHAR(300), " + 
                                   " description VARCHAR(500) not NULL, " + 
                                   " assignees VARCHAR(25) not NULL, " + 
		                   " PRIMARY KEY ( ticket_id ), " +
                                   " FOREIGN KEY ( user_id ) REFERENCES s_fuse_login_table ( id ))";
                      
                      stmt.executeUpdate(sqlA);
		      System.out.println("Created ticket table in given database...");
                      
                      String sqlB = "CREATE TABLE s_fuse_contact_info_table " +
                                   "(contact_id INTEGER not NULL AUTO_INCREMENT, " +
		                   " ticket_number INTEGER not NULL, " +
                                   " email VARCHAR(50) not NULL, " +
		                   " last_name VARCHAR(50) not NULL, " +
                                   " first_name VARCHAR(50) not NULL, " +
		                   " PRIMARY KEY ( contact_id ), " +
                                   " FOREIGN KEY ( ticket_number ) REFERENCES s_fuse_ticket_table ( ticket_id ))"; 

		      stmt.executeUpdate(sqlB);
		      System.out.println("Created contact info table in given database...");
                      
                      String sqlC = "CREATE TABLE s_fuse_date_info_table " +
                                   "(date_id INTEGER not NULL AUTO_INCREMENT, " +
		                   " ticket_number INTEGER not NULL, " +
		                   " date_opened DATE, " +
                                   " date_closed DATE, " +
		                   " PRIMARY KEY ( date_id ), " +
                                   " FOREIGN KEY ( ticket_number ) REFERENCES s_fuse_ticket_table ( ticket_id ))"; 

		      stmt.executeUpdate(sqlC);
		      System.out.println("Created date info table in given database...");
                      
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		         {
		            connection.close();
		         }
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(connection!=null)
		         {
		            connection.close();
		         }
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
       }
        
        public void insertRecords()
	{
		try{
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      this.connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
		      
		      //Execute a query
		      System.out.println("Inserting records into the table...");
		      PreparedStatement pstmt = connection.prepareStatement("INSERT INTO s_fuse_login_table(id, username, password, role) VALUES (?, ?, ?, ?)");
		   
                          pstmt.setString(1, "1");
		    	  pstmt.setString(2, "jpapademas");
		    	  pstmt.setString(3, ph.hashPassword("theboss"));
                          pstmt.setString(4, "admin");
		    	  pstmt.executeUpdate();
                          
                          pstmt.setString(1, "2");
		    	  pstmt.setString(2, "sfuseini");
		    	  pstmt.setString(3, ph.hashPassword("itmd411"));
                          pstmt.setString(4, "user");
		    	  pstmt.executeUpdate();
		      
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		         {
		            connection.close();
		         }
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(connection!=null)
		         {
		            connection.close();
		         }
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
		}//end main

	public void close() {
    	try {
            connection.close();
            connection = null;
        } catch(SQLException e) {
            System.out.println("Error closing connection: " + e);
        }
	}
}
