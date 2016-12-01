/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class UserStageController implements Initializable {

    @FXML
    private AnchorPane details;
    @FXML
    private TableView<?> table;
    @FXML
    private TextField summary;
    @FXML
    private ChoiceBox<?> status;
    @FXML
    private ChoiceBox<?> severity;
    @FXML
    private TextField email;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private ChoiceBox<?> classification;
    @FXML
    private ChoiceBox<?> type;
    @FXML
    private TextArea description;
    @FXML
    private ChoiceBox<?> assignee;
    @FXML
    private Text ticketNum;
    @FXML
    private Button newTicket;
    @FXML
    private Button saveTicket;
    @FXML
    private Button deleteTicket;
    @FXML
    private Button searchTicket;
    @FXML
    private Label messageLabel;
    @FXML
    private ImageView closeIcon;

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
    
}
