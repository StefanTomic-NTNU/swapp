package swapp.ui;

import java.io.IOError;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SplashScreenRemoteController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    public void login() throws IOException, URISyntaxException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RemoteApp.fxml"));
        Parent root = (Parent) loader.load();
        RemoteAppController appController = loader.getController();
        appController.init(usernameTextField.getText());
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Item");
        stage.show();
        stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

}
