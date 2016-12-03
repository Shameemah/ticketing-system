package helpdesk;
//imports
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * This is the FXML Controller class for AdminStage.fxml.It initializes
 * the variables and contains the methods that define various actions when
 * buttons are clicked
 * @author Shameemah Fuseini-Codjoe
 * @version NetBeans IDE 8.2 (Build 201609300101)
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
    
    /**
     * Closes the application when the "close" icon is clicked
     * @param event 
     */
    @FXML
    private void closeIconFired(MouseEvent event) {
        System.exit(0);
    }

    /**
     * Clears all fields in the GUI when the "New" button is selected
     * @param event 
     */
    @FXML
    private void newButtonFired(ActionEvent event) {
        clear();
    }

    /**
     * Saves the data entered in the GUI to the database when the "Save" button
     * is clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    public void saveButtonFired(ActionEvent event) throws IOException {
        
        //variable to retrieve ticket_id
        String lastid;
        
        //error alert dialog
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
                      
                          //summary cannot be left blank
                          if(summary.getText().equals("")){
                              alert.setContentText("Summary cannot be blank");
                              alert.show();
                              throw new SQLException();
                          } else {
                              pstmt.setString(1, summary.getText()); 
                          }
		    	  pstmt.setString(2, status.getSelectionModel().getSelectedItem().toString());
		    	  pstmt.setString(3, severity.getSelectionModel().getSelectedItem().toString());
                          //classification cannot be left unselected
                          if(classification.getSelectionModel().getSelectedItem().toString().equals("Make a Selection")){
                              alert.setContentText("Please select a classification");
                              alert.show();
                              throw new SQLException();
                          }else {
                               pstmt.setString(4, classification.getSelectionModel().getSelectedItem().toString());
                          }
                          //type cannot be left unselected
                          if(type.getSelectionModel().getSelectedItem().toString().equals("Make a Selection")){
                              alert.setContentText("Please select a type");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt.setString(5, type.getSelectionModel().getSelectedItem().toString());
                          }
                          pstmt.setString(6, internalNotes.getText());
                          //description cannot be left blank
                          if(description.getText().equals("")){
                              alert.setContentText("Description cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt.setString(7, description.getText());
                          }
                          pstmt.setString(8, assignee.getSelectionModel().getSelectedItem().toString());
                          
                        //execute update
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
                          //email cannot be blank and must be in the correct format. must contain @ and .
                          if((!email.getText().contains("@") && !email.getText().contains(".")) || email.getText().equals("")){
                              alert.setContentText("Enter a valid email");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(2, email.getText());
                          }
                          //last name cannot be blank
                          if(lname.getText().equals("")){
                              alert.setContentText("Last name cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(3, lname.getText());
                          }
                          //first name cannot be blank
                          if(fname.getText().equals("")){
                              alert.setContentText("First name cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(4, fname.getText());
                          }
                          
                          //execute update
		    	  pstmt2.executeUpdate();
                          System.out.println("Inserted records...");
                          
                          //get current date for date_info table
                          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                          LocalDateTime now = LocalDateTime.now();
                         
                          //insert date_opened into table
                            System.out.println("Inserting records into the table...");
                            PreparedStatement pstmt3 = connection.prepareStatement(" INSERT INTO s_fuse_date_info_table "
                              + " (ticket_number, date_opened) "
                              + " VALUES (?, ?)");
                            
                            pstmt3.setString(1, lastid);
                            pstmt3.setString(2, dtf.format(now));
                            
                            //execute update
                            pstmt3.executeUpdate();
                            System.out.println("Inserted records...");
                          
                          //display ticket created and fade away
                          messageLabel.setText("Ticket #" + lastid + " created successfully");
                          adm.fadeText(messageLabel);
                      
                          //clear all fields
                          clear();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      e.printStackTrace();
		   }
		   System.out.println("Goodbye!");
    }

    /**
     * Deletes the selected row in the database when the "Delete" button is clicked
     * @param event 
     */
    @FXML
    private void deleteButtonFired(ActionEvent event) {
        
        //get the ticket number of the selected row
        int value = table.getSelectionModel().getSelectedItem().getTicketNum();

        //alert user to confirm on whether or not they'd like to go ahead with deletion
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You are deleting a ticket");
        alert.setContentText("Are you sure you want to delete ticket #" +value + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        //if the user clicks OK, go ahead and delete
        if(result.get() == ButtonType.OK)
        {
            try{
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      this.connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
                      
                      //Delete selected ticket number from date_info table first
		      System.out.println("Deleting records from the table...");
		      PreparedStatement pstmt = connection.prepareStatement(" DELETE FROM s_fuse_date_info_table "
                              + " WHERE ticket_number=?");
                      
                      pstmt.setInt(1, value);
                      pstmt.executeUpdate();
                      System.out.println("Deleted records...");
                      
                      //Delete selected ticket number from contact_info table next
		      System.out.println("Deleting records from the table...");
		      PreparedStatement pstmt2 = connection.prepareStatement(" DELETE FROM s_fuse_contact_info_table "
                              + " WHERE ticket_number=?");
                      
                      pstmt2.setInt(1, value);
                      pstmt2.executeUpdate();
                      System.out.println("Deleted records...");
                      
		      //Finally delete records from ticket table
		      System.out.println("Deleting records from  the table...");
		      PreparedStatement pstmt3 = connection.prepareStatement(" DELETE FROM s_fuse_ticket_table "
                              + " WHERE ticket_id=?");
                      
                      pstmt3.setInt(1, value);
                      //execute update
                      pstmt3.executeUpdate();
                      System.out.println("Deleted records...");
                        
                          //display ticket deleted and fade away
                          messageLabel.setText("Ticket #" + value + " deleted successfully");
                          adm.fadeText(messageLabel);
                      
                          //clear all fields
                          clear();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      e.printStackTrace();
		   }//end try
		   System.out.println("Goodbye!");
        } else {
            alert.close();
        }
           
         
    }
    
    /**
     * Updates the specific row in the database when the data of that row is edited
     * by the user and the "Update" button is clicked
     * @param event 
     */
    @FXML
    private void updateButtonFired(ActionEvent event) {
        
        //error alert dialog
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        
        //get the ticket number of the selected row
        int value = table.getSelectionModel().getSelectedItem().getTicketNum();
           
         try{
		      //Open a connection
		      System.out.println("Connecting to a selected database...");
		      this.connection = DriverManager.getConnection(url, username, password);
		      System.out.println("Connected database successfully...");
                      
		      //Update data of selected row
		      System.out.println("Updating record in the table...");
		      PreparedStatement pstmt = connection.prepareStatement(" UPDATE s_fuse_ticket_table "
                              + " SET summary=?, status=?, severity=?, classification=?, type=?, internal_notes=?, description=?, assignees=? "
                              + " WHERE ticket_id=?");
                      
                          //summary cannot be left blank
                          if(summary.getText().equals("")){
                              alert.setContentText("Summary cannot be blank");
                              alert.show();
                              throw new SQLException();
                          } else {
                              pstmt.setString(1, summary.getText()); 
                          }
		    	  pstmt.setString(2, status.getSelectionModel().getSelectedItem().toString());
		    	  pstmt.setString(3, severity.getSelectionModel().getSelectedItem().toString());
                          //classification must be selected
                          if(classification.getSelectionModel().getSelectedItem().toString().equals("Make a Selection")){
                              alert.setContentText("Please select a classification");
                              alert.show();
                              throw new SQLException();
                          }else {
                               pstmt.setString(4, classification.getSelectionModel().getSelectedItem().toString());
                          }
                          //type must be selected
                          if(type.getSelectionModel().getSelectedItem().toString().equals("Make a Selection")){
                              alert.setContentText("Please select a type");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt.setString(5, type.getSelectionModel().getSelectedItem().toString());
                          }
                          pstmt.setString(6, internalNotes.getText());
                          //description cannot be left blank
                          if(description.getText().equals("")){
                              alert.setContentText("Description cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt.setString(7, description.getText());
                          }
                          pstmt.setString(8, assignee.getSelectionModel().getSelectedItem().toString());
                          pstmt.setInt(9, value);
                        
                        //execute update
                        pstmt.executeUpdate();
                        System.out.println("Updated records...");
                        
                        //Update data in contact_info_table
                        System.out.println("Updating records in contact_info table...");
                        PreparedStatement pstmt2 = connection.prepareStatement(" UPDATE s_fuse_contact_info_table "
                              + " SET email=?, last_name=?, first_name=? "
                              + " WHERE ticket_number=?");
		  
                          //email cannot be blank and must be in correct format
                          if((!email.getText().contains("@") && !email.getText().contains(".")) || email.getText().equals("")){
                              alert.setContentText("Enter a valid email");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(1, email.getText());
                          }
                          //last name cannot be blank
                          if(lname.getText().equals("")){
                              alert.setContentText("Last name cannot be blank");
                              alert.show();
                              throw new SQLException();
                          }else {
                              pstmt2.setString(2, lname.getText());
                          }
                          //first name cannot be blank
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
                          
                          //get current date for date_info table
                          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                          LocalDateTime now = LocalDateTime.now();
                         
                          //if the status is changed to closed, insert date_closed into date_info_table
                          if(status.getSelectionModel().getSelectedItem().equals("Closed"))
                          {
                            System.out.println("Updating records in date_info table...");
                            PreparedStatement pstmt3 = connection.prepareStatement(" UPDATE s_fuse_date_info_table "
                              + " SET date_closed=? "
                              + " WHERE ticket_number=?");
                            
                            pstmt3.setString(1, dtf.format(now));
                            pstmt3.setInt(2, value);
                            
                            //execute update
                            pstmt3.executeUpdate();
                            System.out.println("Updated records...");   
                            
                            //display ticket closed and fade away
                            messageLabel.setText("Ticket #" + value + " closed successfully");
                            adm.fadeText(messageLabel);
                          } else {
                             //display ticket updated and fade away
                             messageLabel.setText("Ticket #" + value + " updated successfully");
                             adm.fadeText(messageLabel);
                          }
                          //clear all fields
                          clear();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      e.printStackTrace();
		   }//end try
		   System.out.println("Goodbye!");
        
    }

    /**
     * Loads the data from the tickets table in the database to the JavaFX table in the GUI
     * for the user to view when the "Load" button is selected 
     * It also calls the ticketReport() method
     * @param event 
     */
    @FXML
    private void loadButtonFired(ActionEvent event) {
        try {
            //create observableArrayList
            data=FXCollections.observableArrayList();
            
            //execute query and store result in resultset
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM s_fuse_ticket_table");
            
            while (rs.next()) {
                //add data from database to observableArrayList
                data.add(new AdminStageModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        //set the cell value factory
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
        
        //add data from observableArrayList to the table
        table.setItems(data);
        
        //call the ticketReport() method to show report on the console
        ticketReport();
    }

    /**
     * Populates the GUI form with the data from the row selected in th JavaFX table
     * so that the user may make edits when the "Edit" button is fired
     * @param event 
     */
    @FXML
    private void editButtonFired(ActionEvent event) {
        try {
            //get the ticket number of the selected row
            int value = table.getSelectionModel().getSelectedItem().getTicketNum();
            
            //Initialize rowset and execute query
            JdbcRowSet rowset = RowSetProvider.newFactory().createJdbcRowSet();
            rowset.setUrl(url);
            rowset.setUsername(username);
            rowset.setPassword(password);
            rowset.setCommand("SELECT * FROM s_fuse_ticket_table WHERE ticket_id = ?");
            rowset.setInt(1, value);
            rowset.execute();
            
            //get values from rowset and set them to corresponding GUI fields
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
            
            //Initialize rowset and execute query
            JdbcRowSet rowset1 = RowSetProvider.newFactory().createJdbcRowSet();
            rowset1.setUrl(url);
            rowset1.setUsername(username);
            rowset1.setPassword(password);
            rowset1.setCommand("SELECT email, last_name, first_name FROM s_fuse_contact_info_table WHERE ticket_number = ?");
            rowset1.setInt(1, value);
            rowset1.execute();
            
            //get values from rowset and set them to corresponding GUI fields
            while(rowset1.next()) {
                email.setText(rowset1.getString(1));
                lname.setText(rowset1.getString(2));
                fname.setText(rowset1.getString(3));
            }
            
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Clears all the fields in the GUI
     */
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
    
    /**
     * This method computes the ratio of open tickets to closed tickets, shows
     * all high priority open tickets, and the duration of all open tickets
     */
    private void ticketReport() {
        
        //initialize variables
        int openCount=0, closedCount=0, highPId=0, openTicketNum=0;   
        String highPDesc = "", openTicketDesc="";
        Date dateOpened;
        
        try {
            //query to count the number of open tickets and store in result set
            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(status) AS openCount FROM s_fuse_ticket_table WHERE status='Open'");
            while(rs.next()){
                openCount = rs.getInt("openCount");
            }
            //query to count the number of closed tickets and store in result set
            ResultSet rs1 = connection.createStatement().executeQuery("SELECT COUNT(status) AS closedCount FROM s_fuse_ticket_table WHERE status='Closed'");
            while(rs1.next()){
                closedCount = rs1.getInt("closedCount");
            }
            //Display ratio of open tickets to closed tickets
            System.out.println("Ratio of Open tickets:Closed tickets");
            System.out.println(openCount + ":" + closedCount + "\n");
            //header for high priority tickets
            System.out.println("High Priority Tickets");
            
            //Query for open tickets with a high severity and store in result set
            ResultSet rs2 = connection.createStatement().executeQuery("SELECT ticket_id, description FROM s_fuse_ticket_table WHERE severity='Severity 1'"
                    + " AND status='Open'");
            while(rs2.next()){
                highPId = rs2.getInt("ticket_id");
                highPDesc = rs2.getString("description");
                //display tickets and their descriptions
                System.out.println("Ticket #" + highPId + ": " + highPDesc);
            }
            System.out.println(" ");
            //header for all open tickets and their duration
            System.out.println("Open Tickets Duration");
            
            //Query for all open tickets and the dates they were opened
            ResultSet rs3 = connection.createStatement().executeQuery("SELECT s_fuse_date_info_table.ticket_number, s_fuse_date_info_table.date_opened, s_fuse_ticket_table.description "
                    + " FROM s_fuse_date_info_table, s_fuse_ticket_table "
                    + " WHERE s_fuse_date_info_table.ticket_number=s_fuse_ticket_table.ticket_id AND "
                    + " s_fuse_ticket_table.status='Open'");
            while(rs3.next()){
                //get ticket number, ticket description and the date opened
                openTicketNum = rs3.getInt("ticket_number");
                dateOpened = rs3.getDate("date_opened");
                openTicketDesc = rs3.getString("description");
                //Display tickets, description and date opened
                System.out.println("Ticket #" + openTicketNum + ": " + openTicketDesc );
                System.out.println("Opened since " + dateOpened + "\n");
            }
             
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
        
}
