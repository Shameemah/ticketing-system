/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

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
 *
 * @author shameemah1
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
    
    //defining action when login button is clicked
    @FXML
    private void loginButtonFired(ActionEvent event) throws IOException{
	String userName = this.username.getText();
        String passWord = this.password.getText();
    
        //Instantiate PasswordHash class
        PasswordHash ph = new PasswordHash();
       
	//Get user
	LoginModel l = new LoginModel();
	boolean blnUser = l.findByUsername(userName);
	l.close();
        
	//If user not found
	if(blnUser == false) {
		errorMessage.setText("User does not exist. Try again.");
		return;
	}
        
	//Set user as Logged in user + valid user by role
		try {
                    if(ph.checkPassword(passWord, l.getPassword()))
                    {
                        if(l.getRole().equals("admin") ) {
                            Parent admin_stage = FXMLLoader.load(getClass().getResource("AdminStage.fxml"));
                            Scene admin_stage_scene = new Scene(admin_stage);
                            admin_stage_scene.setFill(null);
                            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            app_stage.hide();
                            app_stage.setScene(admin_stage_scene);
                            app_stage.show();
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
                        errorMessage.setText("Incorrect password. Try again.");
                    }	
		} catch(Exception e) {
			e.printStackTrace();
		}
        
    }
    
    //defining action when exit button is clicked
    @FXML
    private void exitButtonFired(ActionEvent event) {
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
