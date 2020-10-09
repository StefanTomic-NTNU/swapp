package swapp.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    int listLength = list.getItems().size();
    for (int i = listLength; i < listLength + 3; i++) {
      testText = "Gjenstand " + (i + 1);
      clickOn(textField).write(testText);
      clickOn(addButton);
      Assertions.assertTrue(list.getItems().get(i).toString().equals(testText));
    }

  }

  // TODO look through cell see ep.8 todolist
  @Test
  public void testRemove() {
    final ListView<SwappItem> list = lookup("#list").query();
    final Button addButton = (Button) parent.lookup("#addButton");
    final TextField textField = (TextField) parent.lookup("#textField");
    SwappItem item = new SwappItem("testRemoveName");
    SwappItem item2 = new SwappItem("testRemoveName2");
    clickOn(textField).write(item.getName());
    clickOn(addButton);
    clickOn(textField).write(item2.getName());
    clickOn(addButton);
    Assertions.assertEquals(list.getItems().get(list.getItems().size() - 1).getName(), item2.getName());
    list.getSelectionModel().selectLast();
    clickOn("#removeButton");
    Assertions.assertNotEquals(list.getItems().get(list.getItems().size() - 1).getName(), item2.getName());

  }

}
