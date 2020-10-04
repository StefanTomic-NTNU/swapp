package swapp.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import swapp.core.SwappItem;
import swapp.ui.AppController;

public class AppTest extends ApplicationTest {

  private Parent parent;
  private AppController controller;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("App.fxml"));
    parent = fxmlLoader.load();
    controller = fxmlLoader.getController();
    stage.setScene(new Scene(parent));
    stage.show();
  }


  @Test
  public void testAddition() {
    final Button addButton = (Button) parent.lookup("#addButton");
    final TextField textField = (TextField) parent.lookup("#textField");
    final ListView<SwappItem> list = (ListView) parent.lookup("#list");
    String testText;
    for (int i = 0; i < 3; i++) {
      testText = "Gjenstand " + (i + 1);
      clickOn(textField).write(testText);
      // textField.setText(testText);
      clickOn(addButton);
      Assertions.assertTrue(list.getItems().get(i).toString().equals(testText));
    }
  }

}
