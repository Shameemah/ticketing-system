/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.animation.FadeTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * @author shameemah1
 */
public class AdminStageModel {
    
    private IntegerProperty ticketNum;
    private StringProperty summary;
    private StringProperty status;
    private StringProperty severity;
    private StringProperty classification;
    private StringProperty type;
    private StringProperty internalNotes;
    private StringProperty description;
    private StringProperty assignee;
    
    //default constructor
    public AdminStageModel () {
        
    }
    
    public AdminStageModel(int ticketNum, String summary, String status, String severity, String classification, String type, String internalNotes, String description, String assignee){
        this.ticketNum=new SimpleIntegerProperty(ticketNum);
        this.summary=new SimpleStringProperty(summary);
        this.status=new SimpleStringProperty(status);
        this.severity=new SimpleStringProperty(severity);
        this.classification=new SimpleStringProperty(classification);
        this.type=new SimpleStringProperty(type);
        this.internalNotes=new SimpleStringProperty(internalNotes);
        this.description=new SimpleStringProperty(description);
        this.assignee=new SimpleStringProperty(assignee);
    }
            
    //getters and setters

    /**
     * @return the ticketNum
     */
    public int getTicketNum() {
        return ticketNum.get();
    }

    /**
     * @param ticketNum the ticketNum to set
     */
    public void setTicketNum(int value) {
        ticketNum.set(value);
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary.get();
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String value) {
       summary.set(value);
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String value) {
        status.setValue(value);
    }

    /**
     * @return the severity
     */
    public String getSeverity() {
        return severity.get();
    }

    /**
     * @param severity the severity to set
     */
    public void setSeverity(String value) {
        severity.set(value);
    }

    /**
     * @return the classification
     */
    public String getClassification() {
        return classification.get();
    }

    /**
     * @param classification the classification to set
     */
    public void setClassification(String value) {
        classification.set(value);
    }

    /**
     * @return the type
     */
    public String getType() {
        return type.get();
    }

    /**
     * @param type the type to set
     */
    public void setType(String value) {
        type.set(value);
    }

    /**
     * @return the internalNotes
     */
    public String getInternalNotes() {
        return internalNotes.get();
    }

    /**
     * @param internalNotes the internalNotes to set
     */
    public void setInternalNotes(String value) {
        internalNotes.set(value);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String value) {
        description.set(value);
    }

    /**
     * @return the assignee
     */
    public String getAssignee() {
        return assignee.get();
    }

    /**
     * @param assignee the assignee to set
     */
    public void setAssignee(String value) {
        assignee.set(value);
    }
    
    public void fadeText(Label label) {
        FadeTransition ft = new FadeTransition(Duration.millis(3000), label);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }
    
  
}
