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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private ChoiceBox<String> severity;
    @FXML
    private TextField email;
    @FXML
    private TextField lname;
    @FXML
    private TextField fname;
    @FXML
    private ChoiceBox<String> classification;
    @FXML
    private ChoiceBox<String> type;
    @FXML
    private TextArea internalNotes;
    @FXML
    private TextArea description;
    @FXML
    private ChoiceBox<String> assignee;
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
    public void saveButtonFired(ActionEvent event) throws IOException {
        
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

    @FXML
    private void deleteButtonFired(ActionEvent event) {
        
        int value = table.getSelectionModel().getSelectedItem().getTicketNum();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You are deleting a ticket");
        alert.setContentText("Are you sure you want to delete ticket #" +value + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK)
        {
            try{
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      this.connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
                      
                      //Insert data into tickets table
		      System.out.println("Deleting records into the table...");
		      PreparedStatement pstmt = connection.prepareStatement(" DELETE FROM s_fuse_date_info_table "
                              + " WHERE ticket_number=?");
                      
                      pstmt.setInt(1, value);
                      pstmt.executeUpdate();
                      System.out.println("Deleted records...");
                      
                      //Insert data into tickets table
		      System.out.println("Deleting records into the table...");
		      PreparedStatement pstmt2 = connection.prepareStatement(" DELETE FROM s_fuse_contact_info_table "
                              + " WHERE ticket_number=?");
                      
                      pstmt2.setInt(1, value);
                      pstmt2.executeUpdate();
                      System.out.println("Deleted records...");
                      
		      //Insert data into tickets table
		      System.out.println("Deleting records into the table...");
		      PreparedStatement pstmt3 = connection.prepareStatement(" DELETE FROM s_fuse_ticket_table "
                              + " WHERE ticket_id=?");
                      
                      pstmt3.setInt(1, value);
                      pstmt3.executeUpdate();
                      System.out.println("Deleted records...");
                        
                          //display ticket deleted
                          messageLabel.setText("Ticket #" + value + " deleted successfully");
                          adm.fadeText(messageLabel);
                      
                          //clear all fields
                          clear();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      System.out.println("Error loading table");
		   }//end try
		   System.out.println("Goodbye!");
        } else {
            alert.close();
        }
           
         
    }

    @FXML
    private void updateButtonFired(ActionEvent event) {
        String lastid;
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        
        int value = table.getSelectionModel().getSelectedItem().getTicketNum();
           
         try{
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      this.connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
                      
		      //Insert data into tickets table
		      System.out.println("Updating records into the table...");
		      PreparedStatement pstmt = connection.prepareStatement(" UPDATE s_fuse_ticket_table "
                              + " SET summary=?, status=?, severity=?, classification=?, type=?, internal_notes=?, description=?, assignees=? "
                              + " WHERE ticket_id=?");
                      
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
                          pstmt.setInt(9, value);
                          
                        pstmt.executeUpdate();
                        System.out.println("Updated records...");
                        
                        //Insert data into contact_info_table
                        System.out.println("Updating records in contact_info table...");
                        PreparedStatement pstmt2 = connection.prepareStatement(" UPDATE s_fuse_contact_info_table "
                              + " SET email=?, last_name=?, first_name=? "
                              + " WHERE ticket_number=?");
		  
                          if((!email.getText().contains("@") && !email.getText().contains(".")) || email.getText().equals("")){
                              alert.setContentText("Enter a valid email");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(1, email.getText());
                          }
                          if(lname.getText().equals("")){
                              alert.setContentText("Last name cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(2, lname.getText());
                          }
                          if(fname.getText().equals("")){
                              alert.setContentText("First name cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(3, fname.getText());
                          }
                          pstmt2.setInt(4, value);
                          
		    	  pstmt2.executeUpdate();
                          System.out.println("Updated records...");
                          
                          //get date for date_info table
                          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                          LocalDateTime now = LocalDateTime.now();
                         
                          //insert date_closed into table
                          if(status.getSelectionModel().getSelectedItem().equals("Closed"))
                          {
                            System.out.println("Updating records in date_info table...");
                            PreparedStatement pstmt3 = connection.prepareStatement(" UPDATE s_fuse_date_info_table "
                              + " SET date_closed=? "
                              + " WHERE ticket_number=?");
                            
                            pstmt3.setString(1, dtf.format(now));
                            pstmt3.setInt(2, value);
                            
                            pstmt3.executeUpdate();
                            System.out.println("Updated records...");   
                             //display ticket updated
                            messageLabel.setText("Ticket #" + value + " closed successfully");
                            adm.fadeText(messageLabel);
                          } else {
                             //display ticket updated
                             messageLabel.setText("Ticket #" + value + " updated successfully");
                             adm.fadeText(messageLabel);
                          }
                          //clear all fields
                          clear();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      System.out.println("Error loading table");
		   }//end try
		   System.out.println("Goodbye!");
        
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
            ex.printStackTrace();
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
        
        ticketReport();
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
                severity.setValue(rowset.getString(4));
                classification.setValue(rowset.getString(5));
                type.setValue(rowset.getString(6));
                internalNotes.setText(rowset.getString(7));
                description.setText(rowset.getString(8));
                assignee.setValue(rowset.getString(9));
            }
            
            JdbcRowSet rowset1 = RowSetProvider.newFactory().createJdbcRowSet();
            rowset1.setUrl(url);
            rowset1.setUsername(username);
            rowset1.setPassword(password);
            rowset1.setCommand("SELECT email, last_name, first_name FROM s_fuse_contact_info_table WHERE ticket_number = ?");
            rowset1.setInt(1, value);
            rowset1.execute();
            
            while(rowset1.next()) {
                email.setText(rowset1.getString(1));
                lname.setText(rowset1.getString(2));
                fname.setText(rowset1.getString(3));
            }
            
        }catch (SQLException e){
            
        }
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
        internalNotes.clear();
        description.clear();
        assignee.getSelectionModel().select(8);
    }
    
    private void ticketReport() {
        int openCount=0, closedCount=0;   
        try {
            //execute query and store result in resultset
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(status) AS openCount FROM s_fuse_ticket_table WHERE status='Open'");
            while(rs.next()){
                openCount = rs.getInt("openCount");
            }
            //execute query and store result in resultset
            ResultSet rs1 = connection.createStatement().executeQuery("SELECT COUNT(status) AS closedCount FROM s_fuse_ticket_table WHERE status='Closed'");
            while(rs1.next()){
                closedCount = rs1.getInt("closedCount");
            }
            System.out.println("Ratio of Open tickets:Closed tickets");
            System.out.println(openCount + ":" + closedCount + "\n");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        
}
