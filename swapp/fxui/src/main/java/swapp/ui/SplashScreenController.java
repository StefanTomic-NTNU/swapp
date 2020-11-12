package swapp.ui;

import java.io.IOError;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SplashScreenController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    public void login() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
        Parent root = (Parent) loader.load();
        AppController appController = loader.getController();
        appController.init(usernameTextField.getText());
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Item");
        stage.show();
        stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

}
