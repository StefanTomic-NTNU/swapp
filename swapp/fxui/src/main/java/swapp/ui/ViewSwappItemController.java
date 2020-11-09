package swapp.ui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import swapp.core.SwappItem;

public class ViewSwappItemController {
    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea infoTextField;

    @FXML
    private TextField statusTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button backButton;

    @FXML
    private Button publishButton;

    @FXML
    private Button deleteButton;

    private boolean deleteFlag;


    private SwappItem swappItem;

    public void initSwappitem(SwappItem swappItem) {
        if (swappItem != null) {
            this.swappItem = swappItem;
            setText();
        }
    }

    public void setText() {
        titleTextField.setText(swappItem.getName());
        infoTextField.setText(swappItem.getDescription());
        statusTextField.setText(swappItem.getStatus());
        emailTextField.setText(swappItem.getContactInfo());
    }

    @FXML
    public void publishSwappItem(){
        String title = titleTextField.getText();
        String newInfo = infoTextField.getText();
        String newstatus = statusTextField.getText();
        String newEmail = emailTextField.getText();
        SwappItem newItem = new SwappItem(title, newstatus, newInfo, newEmail);
        this.swappItem = newItem;
        setText();
        Stage stage = (Stage) publishButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void backButtonPressed() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void deleteItem(){
        if (swappItem!=null){
            deleteFlag = true;
        }
        setText();
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    public SwappItem getSwappItem(){
        return this.swappItem;
    }

    public boolean isdelete(){
        return deleteFlag;
    }

}
