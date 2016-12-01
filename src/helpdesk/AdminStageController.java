/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 * FXML Controller class
 *
 * @author shameemah1
 */
public class AdminStageController extends DAO implements Initializable {

    @FXML
    private TreeView<?> tree;
    @FXML
    private AnchorPane details;
    @FXML
    private TableView<AdminStageModel> table;
    @FXML
    private TextField summary;
    @FXML
    private ChoiceBox<String> status;
    @FXML
    private ChoiceBox<?> severity;
    @FXML
    private TextField email;
    @FXML
    private TextField lname;
    @FXML
    private TextField fname;
    @FXML
    private ChoiceBox<?> classification;
    @FXML
    private ChoiceBox<?> type;
    @FXML
    private TextArea internalNotes;
    @FXML
    private TextArea description;
    @FXML
    private ChoiceBox<?> assignee;
    @FXML
    private Button newTicket;
    @FXML
    private Button saveTicket;
    @FXML
    private Button deleteTicket;
    @FXML
    private Button updateTicket;
    @FXML
    private Label messageLabel;
    @FXML
    private ImageView closeIcon;
    @FXML
    private TableColumn<AdminStageModel, Integer> ticketNumCol;
    @FXML
    private TableColumn<AdminStageModel, String> summaryCol;
    @FXML
    private TableColumn<AdminStageModel, String> statusCol;
    @FXML
    private TableColumn<AdminStageModel, String> severityCol;
    @FXML
    private TableColumn<AdminStageModel, String> classificationCol;
    @FXML
    private TableColumn<AdminStageModel, String> typeCol;
    @FXML
    private TableColumn<AdminStageModel, String> internalNotesCol;
    @FXML
    private TableColumn<AdminStageModel, String> descriptionCol;
    @FXML
    private TableColumn<AdminStageModel, String> assigneeCol;
    @FXML
    private Button load;
    @FXML
    private Label ticketNumber;
    @FXML
    private Button editTicket;

    private ObservableList<AdminStageModel>data;
    
    AdminStageModel adm = new AdminStageModel();
    String getbothvalue, finalvalue;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
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
    private void saveButtonFired(ActionEvent event) throws IOException {
        
        //variable to retrieve ticket_id
        String lastid;
        
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
           
         try{
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      this.connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
                      
		      //Insert data into tickets table
		      System.out.println("Inserting records into the table...");
		      PreparedStatement pstmt = connection.prepareStatement(" INSERT INTO s_fuse_ticket_table "
                              + " (summary, status, severity, classification, type, internal_notes, description, assignees) "
                              + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                      
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
                          pstmt.setString(6, internalNotes.getText());
                          if(description.getText().equals("")){
                              alert.setContentText("Description cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt.setString(7, description.getText());
                          }
                          pstmt.setString(8, assignee.getSelectionModel().getSelectedItem().toString());
                          
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
                          messageLabel.setText("Ticket number " + lastid + " created successfully");
                          adm.fadeText(messageLabel);
                      
                          //clear all fields
                          clear();
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

    @FXML
    private void deleteButtonFired(ActionEvent event) {
    }

    @FXML
    private void updateButtonFired(ActionEvent event) {
    }

    @FXML
    private void loadButtonFired(ActionEvent event) {
        try {
            data=FXCollections.observableArrayList();
            //execute query and store result in resultset
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM s_fuse_ticket_table");
            while (rs.next()) {
                //get data from db
                data.add(new AdminStageModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
            
        } catch (SQLException ex) {
            System.out.println("Error loading data");
        }
        
        ticketNumCol.setCellValueFactory(new PropertyValueFactory<>("ticketNum"));
        summaryCol.setCellValueFactory(new PropertyValueFactory<>("summary"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        severityCol.setCellValueFactory(new PropertyValueFactory<>("severity"));
        classificationCol.setCellValueFactory(new PropertyValueFactory<>("classification"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        internalNotesCol.setCellValueFactory(new PropertyValueFactory<>("internalNotes"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        assigneeCol.setCellValueFactory(new PropertyValueFactory<>("assignee"));
        
        table.setItems(null);
        table.setItems(data);
    }

    @FXML
    private void editButtonFired(ActionEvent event) {
        try {
            int value = table.getSelectionModel().getSelectedItem().getTicketNum();
            
            JdbcRowSet rowset = RowSetProvider.newFactory().createJdbcRowSet();
            rowset.setUrl(url);
            rowset.setUsername(username);
            rowset.setPassword(password);
            rowset.setCommand("SELECT * FROM s_fuse_ticket_table WHERE ticket_id = ?");
            rowset.setInt(1, value);
            rowset.execute();
            
            
            
            while(rowset.next()) {
                ticketNumber.setText(rowset.getString(1));
                summary.setText(rowset.getString(2));
                status.setValue(rowset.getString(3));
                
            }
        }catch (SQLException e){
            
        }
    }
    
    public void clear() {
        summary.clear();
        status.getSelectionModel().selectFirst();
        severity.getSelectionModel().select(2);
        email.clear();
        fname.clear();
        lname.clear();
        classification.getSelectionModel().selectFirst();
        type.getSelectionModel().selectFirst();
        internalNotes.clear();
        description.clear();
        assignee.getSelectionModel().select(8);
    }
        
}