package swapp.ui;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import swapp.core.SwappItem;
import java.lang.String;

public class SwappSplashScreenController {

  @FXML
  private TextField usernameTextField;

  @FXML
  private Button loginButton;

  @FXML
  String endpointUri;

  @FXML
  String filename;


  private SwappDataAccess swappAccess;

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

  public SwappSplashScreenController() throws URISyntaxException {
  
  }

  public void login() throws IOException, URISyntaxException {
    if (endpointUri!=null){ 
      swappAccess = new RemoteSwappAccess(new URI(endpointUri));
    }else if (filename != null){
      System.out.println(filename);
      swappAccess = new DirectSwappAccess(filename);
    } 
    FXMLLoader loader = new FXMLLoader(getClass().getResource("SwappApp.fxml"));
    Parent root = (Parent) loader.load();
    SwappAppController appController = loader.getController();
    appController.setSwappDataAccess(swappAccess);
    appController.init(usernameTextField.getText());
    Stage stage = new Stage();
    stage.setScene(new Scene(root, 900, 600));
    stage.setTitle("Item");
    stage.show();
    stage = (Stage) loginButton.getScene().getWindow();
    stage.close();
  }

}
