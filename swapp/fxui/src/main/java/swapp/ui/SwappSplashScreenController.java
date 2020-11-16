package swapp.ui;

import java.io.IOException;
import java.lang.String;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


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

  public SwappSplashScreenController() throws URISyntaxException {

  }

  /**
   * User is logged into the App.
   *
   * <p>Info from the usernameTextField is retrieved and set as the username.
   * 
   * <p>SwappApp stage is then loaded and shown. Finaly, SwappSplashScreen is closed.
   *
   * @throws IOException May be thrown by when constructing RemoteSwappAccess.
   * @throws URISyntaxException May be thrown if endpoint uri is bad.
   */
  public void login() throws IOException, URISyntaxException {
    if (endpointUri != null) {
      swappAccess = new RemoteSwappAccess(new URI(endpointUri));
    } else if (filename != null) {
      System.out.println(filename);
      swappAccess = new DirectSwappAccess(filename);
    }
    if (!usernameTextField.getText().replaceAll("\\s+", "").isEmpty()) {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("SwappApp.fxml"));
      Parent root = (Parent) loader.load();
      SwappAppController appController = loader.getController();
      appController.setSwappDataAccess(swappAccess);
      appController.init(usernameTextField.getText().replaceAll("\\s+", ""));
      Stage stage = new Stage();
      stage.setScene(new Scene(root, 900, 600));
      stage.setTitle("Item");
      stage.show();
      stage = (Stage) loginButton.getScene().getWindow();
      stage.close();
    }
  }

}
