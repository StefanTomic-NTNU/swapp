package swapp.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import javafx.scene.input.KeyCode;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ModuleLayer.Controller;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.json.SwappPersistence;

public class AppTest extends ApplicationTest {

  private SwappAppController appController;
  private SwappPersistence swappPersistence = new SwappPersistence();
  private Parent root;

  @Override
  public void start(final Stage stage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(getClass().getResource("test_SwappApp.fxml"));
    root = loader.load();
    this.appController = loader.getController();
    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  public void setUp() throws IOException {
    DirectSwappAccess access = new DirectSwappAccess("test-swappmodel.json");
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-swappmodel.json"))) {
      access.setModel(swappPersistence.readSwappModel(reader));
      appController.setSwappDataAccess(access);
      appController.init("testUsername1");
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testController_initial() {
    assertNotNull(this.appController);
  }

  @Test
  public void testAdd() throws Exception {
    ListView<SwappItem> listView = (ListView<SwappItem>) root.lookup("#listView");
    SwappItem testItem = new SwappItem("testItem", "testUsername1");
    Collection<SwappItem> swappItems = listView.getItems();
    assertFalse(swappItems.stream().anyMatch(p -> p.allAttributesEquals(testItem)));
    appController.addSwappItem(testItem);
    swappItems = listView.getItems();
    assertTrue(swappItems.stream().anyMatch(p -> p.allAttributesEquals(testItem)));
  }

  @Test
  public void testAddRemove() throws Exception {
    ListView<SwappItem> listView = (ListView<SwappItem>) root.lookup("#listView");
    SwappItem testItem = new SwappItem("testItem", "testUsername1");
    Collection<SwappItem> swappItems = listView.getItems();
    assertFalse(swappItems.stream().anyMatch(p -> p.allAttributesEquals(testItem)));
    appController.addSwappItem(testItem);
    swappItems = listView.getItems();
    assertTrue(swappItems.stream().anyMatch(p -> p.allAttributesEquals(testItem)));
    appController.removeSwappItem(testItem);
    swappItems = listView.getItems();
    assertFalse(swappItems.stream().anyMatch(p -> p.allAttributesEquals(testItem)));
  }

  @Test
  public void testDeleteAllSwappItems() {
    Button deleteAllButton = (Button) root.lookup("#addButton1");
    RadioButton mineRadio = (RadioButton) root.lookup("#mineRadio");
    ListView<SwappItem> listView = (ListView<SwappItem>) root.lookup("#listView");
    clickOn(mineRadio);
    assertEquals(2, listView.getItems().size());
    clickOn(deleteAllButton);
    clickOn(mineRadio);
    assertEquals(0, listView.getItems().size());
  }

  @Test
  public void testFilter() {
    RadioButton newRadio = (RadioButton) root.lookup("#newRadio");
    RadioButton usedRadio = (RadioButton) root.lookup("#usedRadio");
    RadioButton damagedRadio = (RadioButton) root.lookup("#damagedRadio");
    RadioButton allRadio = (RadioButton) root.lookup("#allRadio");
    RadioButton mineRadio = (RadioButton) root.lookup("#mineRadio");
    ListView<SwappItem> listView = (ListView<SwappItem>) root.lookup("#listView");
    clickOn(allRadio);
    assertEquals(3, listView.getItems().size());
    assertNotEquals(2, listView.getItems().size());
    clickOn(usedRadio);
    assertEquals(1, listView.getItems().size());
    clickOn(newRadio);
    assertEquals(2, listView.getItems().size());
    clickOn(damagedRadio);
    assertEquals(0, listView.getItems().size());
  }

  @Test
  public void testSwappItemChanged() throws Exception {
    ListView<SwappItem> listView = (ListView<SwappItem>) root.lookup("#listView");
    RadioButton mineRadio = (RadioButton) root.lookup("#mineRadio");
    clickOn(mineRadio);
    SwappItem selectedItem = listView.getItems().get(0);
    String name = selectedItem.getName();
    String username = selectedItem.getUsername();
    String info = selectedItem.getDescription();
    String status = selectedItem.getStatus();
    String newInfo = "newInfo";
    SwappItem newItem = new SwappItem(name, username, status, newInfo);
    appController.changeSwappItem(newItem);
    Collection<SwappItem> swappItems = listView.getItems();
    assertFalse(swappItems.stream().anyMatch(p -> p.allAttributesEquals(name, status, info, username)));
    assertTrue(swappItems.stream().anyMatch(p -> p.allAttributesEquals(name, status, newInfo, username)));
  }

}
