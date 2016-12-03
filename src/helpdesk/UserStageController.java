/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import static helpdesk.DAO.connection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author shameemah1
 */
public class UserStageController extends DAO implements Initializable {

    @FXML
    private AnchorPane details;
    @FXML
    private TextField summary;
    @FXML
    private ChoiceBox<String> status;
    @FXML
    private ChoiceBox<String> severity;
    @FXML
    private TextField email;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private ChoiceBox<String> classification;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private TextArea description;
    @FXML
    private ChoiceBox<String> assignee;
    @FXML
    private Text ticketNum;
    @FXML
    private Label messageLabel;
    @FXML
    private ImageView closeIcon;
    @FXML
    private Button newButton;
    @FXML
    private Button saveButton;
    
    AdminStageController ad = new AdminStageController();
    AdminStageModel adm = new AdminStageModel();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeIconFired(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void newButtonFired(ActionEvent event) {
        clear();
    }

    @FXML
    private void saveButtonFired(ActionEvent event) {
         //variable to retrieve ticket_id
        String lastid;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
           
         try{
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      this.connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
                      
		      //Insert data into tickets table
		      System.out.println("Inserting records into the table...");
		      PreparedStatement pstmt = connection.prepareStatement(" INSERT INTO s_fuse_ticket_table "
                              + " (summary, status, severity, classification, type, description, assignees) "
                              + " VALUES (?, ?, ?, ?, ?, ?, ?)");
                      
                          if(summary.getText().equals("")){
                              alert.setContentText("Summary cannot be blank");
                              alert.show();
                              throw new SQLException();
                          } else {
                              pstmt.setString(1, summary.getText()); 
                          }
		    	  pstmt.setString(2, status.getSelectionModel().getSelectedItem().toString());
		    	  pstmt.setString(3, severity.getSelectionModel().getSelectedItem().toString());
                          if(classification.getSelectionModel().getSelectedItem().toString().equals("Make a Selection")){
                              alert.setContentText("Please select a classification");
                              alert.show();
                              throw new SQLException();
                          }else {
                               pstmt.setString(4, classification.getSelectionModel().getSelectedItem().toString());
                          }
                          if(type.getSelectionModel().getSelectedItem().toString().equals("Make a Selection")){
                              alert.setContentText("Please select a type");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt.setString(5, type.getSelectionModel().getSelectedItem().toString());
                          }
                          if(description.getText().equals("")){
                              alert.setContentText("Description cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt.setString(6, description.getText());
                          }
                          pstmt.setString(7, assignee.getSelectionModel().getSelectedItem().toString());
                          
                        pstmt.executeUpdate();
                        System.out.println("Inserted records...");
                         
                        //get last inserted ticket_id
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery(" SELECT LAST_INSERT_ID() as last_id FROM s_fuse_ticket_table ");
                        rs.next();
                        lastid = rs.getString("last_id"); 
                        
                        //Insert data into contact_info_table
                        System.out.println("Inserting records into the table...");
                        PreparedStatement pstmt2 = connection.prepareStatement(" INSERT INTO s_fuse_contact_info_table "
                              + " (ticket_number, email, last_name, first_name) "
                              + " VALUES (?, ?, ?, ?)");
		   
                          pstmt2.setString(1, lastid);
                          if((!email.getText().contains("@") && !email.getText().contains(".")) || email.getText().equals("")){
                              alert.setContentText("Enter a valid email");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(2, email.getText());
                          }
                          if(lname.getText().equals("")){
                              alert.setContentText("Last name cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(3, lname.getText());
                          }
                          if(fname.getText().equals("")){
                              alert.setContentText("First name cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(4, fname.getText());
                          }
                          
		    	  pstmt2.executeUpdate();
                          System.out.println("Inserted records...");
                          
                          //get date for date_info table
                          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                          LocalDateTime now = LocalDateTime.now();
                         
                          //insert date_opened into table
                          //Insert data into contact_info_table
                            System.out.println("Inserting records into the table...");
                            PreparedStatement pstmt3 = connection.prepareStatement(" INSERT INTO s_fuse_date_info_table "
                              + " (ticket_number, date_opened) "
                              + " VALUES (?, ?)");
                            
                            pstmt3.setString(1, lastid);
                            pstmt3.setString(2, dtf.format(now));
                            
                            pstmt3.executeUpdate();
                            System.out.println("Inserted records...");
                          
                          //display ticket created
                          messageLabel.setText("Ticket #" + lastid + " created successfully");
                          adm.fadeText(messageLabel);
                      
                          //clear all fields
                          clear();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }
		   System.out.println("Goodbye!");
    }
    
     private void clear() {
        summary.clear();
        status.getSelectionModel().selectFirst();
        severity.getSelectionModel().select(2);
        email.clear();
        fname.clear();
        lname.clear();
        classification.getSelectionModel().selectFirst();
        type.getSelectionModel().selectFirst();
        description.clear();
        assignee.getSelectionModel().select(8);
    }
    
}
