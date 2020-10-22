package swapp.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
    parent = fxmlLoader.load();
    controller = fxmlLoader.getController();
    try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-swappItemList.json"))) {
      /*
      SwappItemList swappItemList = persistence.readSwappList(reader);
      */
      SwappItemList swappItemList = new SwappItemList();
      swappItemList.addItem(new SwappItem("testItem"));
      this.controller.getItems().setSwappItemlist(swappItemList);
    } catch (IOException ioException) {
      System.err.println("Feil med innlasting av testfil.");
    }
    stage.setScene(new Scene(parent));
    stage.show();
  }

  @BeforeEach
  public void setupList(){
    
  }
  /*
  @BeforeEach
  public void setUpList() {
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-swappItemList.json"))) {
        SwappItemList swappItemList = persistence.readSwappList(reader);
        controller.getItems().setSwappItemlist(swappItemList);
      } catch (IOException ioException) {
    }
  }
*/
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
