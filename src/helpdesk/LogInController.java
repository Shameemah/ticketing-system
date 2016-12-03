package helpdesk;

//imports
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the FXML Controller class for LogIn.fxml.It initializes
 * the variables and contains the methods that define various actions when
 * buttons are clicked
 * @author Shameemah Fuseini-Codjoe
 * @version NetBeans IDE 8.2 (Build 201609300101)
 */
public class LogInController implements Initializable  {

    @FXML
    public TextField username;
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;
    @FXML
    private PasswordField password;
    @FXML
    private Text errorMessage;
    
    /**
     * This method checks that the username and password are correct, and opens
     * the admin stage or user stage depending on the role of the user logging in
     * @param event
     * @throws IOException 
     */
    @FXML
    private void loginButtonFired(ActionEvent event) throws IOException{
	String userName = this.username.getText();
        String passWord = this.password.getText();
    
        //Instantiate PasswordHash class
        PasswordHash ph = new PasswordHash();
       
	//Get user by username
	LoginModel l = new LoginModel();
	boolean blnUser = l.findByUsername(userName);
	l.close();
        
	//If the user is not in the database
	if(blnUser == false) {
		errorMessage.setText("User does not exist. Try again.");
		return;
	}
        
                //Check if password matche
		try {
                    if(ph.checkPassword(passWord, l.getPassword()))
                    {
                        //check if role is admin and open admin stage
                        if(l.getRole().equals("admin") ) {
                            Parent admin_stage = FXMLLoader.load(getClass().getResource("AdminStage.fxml"));
                            Scene admin_stage_scene = new Scene(admin_stage);
                            admin_stage_scene.setFill(null);
                            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            app_stage.hide();
                            app_stage.setScene(admin_stage_scene);
                            app_stage.show();
                         //check if role is user and open user stage
			}else if (l.getRole().equals("user")){
                            Parent admin_stage = FXMLLoader.load(getClass().getResource("UserStage.fxml"));
                            Scene admin_stage_scene = new Scene(admin_stage);
                            admin_stage_scene.setFill(null);
                            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            app_stage.hide();
                            app_stage.setScene(admin_stage_scene);
                            app_stage.show();
                        }         
                    }else {
                        //if password does not match, show error message
                        errorMessage.setText("Incorrect password. Try again.");
                    }	
		} catch(Exception e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Closes the application when the "Exit" button is clicked
     * @param event 
     */
    @FXML
    private void exitButtonFired(ActionEvent event) {
        System.exit(0);
    }
    
    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
