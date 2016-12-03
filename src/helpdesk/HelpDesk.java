/***********************************************************************************************************
 * Name: Shameemah Fuseini-Codjoe                                                                          *
 * Date: 12/03/2016                                                                                        *
 * File Name: Fuseini-Codjoe_FinalProject.docx                                                             *
 * Source File Name: HelpDesk.java                                                                         *
 * Title: Final Project                                                                                    *
 *                                                                                                         *
 * Program Description: This program is designed for the IIT OTS Support Desk. It is a ticketing system    *
 *                      that allows users to report trouble tickets based on problems they are facing.     *
 *                                                                                                         *
 ***********************************************************************************************************/

package helpdesk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * HelpDesk holds the main method, as well as the start method, which initializes
 * and loads the Login screen
 * @author Shameemah Fuseini-Codjoe
 * @version NetBeans IDE 8.2 (Build 201609300101)
 */
public class HelpDesk extends Application {
    
    /**
     * The start method initializes and loads the Login screen
     * @param stage the JavaFx stage to be initialized
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.TRANSPARENT);
        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method launches the command line arguments
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
