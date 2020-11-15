package swapp.ui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import swapp.core.SwappItem;

public class ViewSwappItemController {
    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea infoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button backButton;

    @FXML
    private Button publishButton;

    @FXML
    private Button deleteButton;

    @FXML
    private RadioButton newRadio;

    @FXML
    private RadioButton usedRadio;

    @FXML
    private RadioButton damagedRadio;

    private ToggleGroup toggleGroup;

    private boolean deleteFlag;

    private SwappItem swappItem;

    public void initSwappitem(SwappItem swappItem, String username) {
        cleanText();
        inizializeToggleGroup();
        if (swappItem != null) {
            this.swappItem = swappItem;
            setTextAndToggle();
            if (!swappItem.getUsername().equals(username)) {
                deleteButton.setDisable(true);
                publishButton.setDisable(true);
                infoTextField.setEditable(false);
                newRadio.setDisable(true);
                usedRadio.setDisable(true);
                damagedRadio.setDisable(true);
            }
        }
    }

    public void inizializeToggleGroup() {
        toggleGroup = new ToggleGroup();
        newRadio.setToggleGroup(toggleGroup);
        usedRadio.setToggleGroup(toggleGroup);
        damagedRadio.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(newRadio);
    }

    public void setTextAndToggle() {
        titleTextField.setText(swappItem.getName());
        infoTextField.setText(swappItem.getDescription());
        emailTextField.setText(swappItem.getUsername());
        switch (swappItem.getStatus()) {
            case "New":
                toggleGroup.selectToggle(newRadio);
                break;
            case "Used":
                toggleGroup.selectToggle(usedRadio);
                break;
            case "Damaged":
                toggleGroup.selectToggle(damagedRadio);
                break;
            default:
                break;
        }
    }

    public void cleanText() {
        titleTextField.setText("");
        infoTextField.setText("");
        emailTextField.setText("");
    }

    @FXML
    public void publishSwappItem() {
        // String title = titleTextField.getText();
        String newInfo = infoTextField.getText();
        String newCondition = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
        // String newEmail = emailTextField.getText();
        SwappItem newItem = new SwappItem(this.swappItem.getName(), this.swappItem.getUsername(), newCondition,
                newInfo);
        this.swappItem = newItem;
        setTextAndToggle();
        cleanText();
        Stage stage = (Stage) publishButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void backButtonPressed() {
        cleanText();
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void deleteItem() {
        if (swappItem != null) {
            deleteFlag = true;
        }
        setTextAndToggle();
        cleanText();
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    public SwappItem getSwappItem() {
        return this.swappItem;
    }

    public boolean isdelete() {
        return deleteFlag;
    }

}
