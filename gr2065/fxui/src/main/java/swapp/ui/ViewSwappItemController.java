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

    private SwappItem swappItem;

    public void initSwappitem(SwappItem swappItem) {
        if (swappItem != null) {
            this.swappItem = swappItem;
            setText();
        }
    }

    public void setText() {
        titleTextField.setText(swappItem.getName());
        infoTextField.setText(swappItem.getName());
        statusTextField.setText(swappItem.getName());
        emailTextField.setText(swappItem.getName());
    }

    @FXML
    public void backButtonPressed() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();

    }

}
