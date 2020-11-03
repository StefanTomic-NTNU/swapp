package swapp.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ModuleLayer.Controller;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappPersistence;

public class AppTest extends ApplicationTest {

  private Parent parent;
  private AppController controller;
  private SwappPersistence persistence = new SwappPersistence();

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("test_App.fxml"));
    this.parent = fxmlLoader.load();
    this.controller = (AppController)fxmlLoader.getController();
    this.controller.setFile(Paths.get(System.getProperty("user.home"), "test-swappItemList.json").toFile());
    try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-swappItemList.json"))) {
    this.controller.getItems().setSwappItemlist(persistence.readSwappList(reader));
    } catch (IOException ioException) {
      System.err.println("Feil med innlasting av testfil.");
    }
    stage.setScene(new Scene(parent));
    stage.show();
  }

  /*
  @BeforeEach
  public void setupList(){
    
  }
  */

  @Test
  public void testAddition() {
    final Button addButton = (Button) parent.lookup("#addButton");
    final TextField textField = (TextField) parent.lookup("#textField");
    final ListView<SwappItem> list = (ListView<SwappItem>) parent.lookup("#list");
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
    int lengthBeforeRemoval = list.getItems().size();
    list.getSelectionModel().selectLast();
    clickOn("#removeButton");
    Assertions.assertEquals(lengthBeforeRemoval-1, list.getItems().size());
    //Assertions.assertNotEquals(list.getItems().get(list.getItems().size() - 1).getName(), item2.getName());

  }

}
