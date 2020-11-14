package swapp.ui;

import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import swapp.core.SwappItem;

public class SplashScreenRemoteController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    String endpointUri = "http://localhost:8999/swapp/";

    private RemoteSwappAccess remoteSwappAccess;

    /**
     * @throws URISyntaxException
     * @FXML public void login() throws IOException, URISyntaxException { FXMLLoader
     *       loader = new FXMLLoader(getClass().getResource("RemoteApp.fxml"));
     *       Parent root = (Parent) loader.load(); RemoteAppController appController
     *       = loader.getController();
     *       appController.init(usernameTextField.getText()); Stage stage = new
     *       Stage(); stage.setScene(new Scene(root, 800, 400));
     *       stage.setTitle("Item"); stage.show(); stage = (Stage)
     *       loginButton.getScene().getWindow(); stage.close(); }
     */

    public SplashScreenRemoteController() throws URISyntaxException {
        try {
            remoteSwappAccess = new RemoteSwappAccess(new URI(endpointUri));
          } catch (Exception e) {
            e.printStackTrace();
          }
        System.out.println(remoteSwappAccess.getAllSwappItem());
    }

    public void login() {
        String name = usernameTextField.getText();
        SwappItem item1 = new SwappItem("item1", name, "New", "info1");
        System.out.println(item1.toString());
        System.out.println(remoteSwappAccess.getSwappItem(item1).toString()!=null);
    
    }

}
