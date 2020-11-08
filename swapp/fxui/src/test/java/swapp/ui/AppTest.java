package swapp.ui;

import java.io.File;
import javafx.scene.input.KeyCode;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ModuleLayer.Controller;
import java.nio.file.Paths;
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
    controller.setFile(Paths.get(System.getProperty("user.home"), "test-swappItemList.json").toFile());
    try(Reader reader = new InputStreamReader(getClass().getResourceAsStream("test-swappItemList.json"))) {
      controller.getSwappItemList().setSwappItemList(persistence.readSwappList(reader));
    } catch (IOException ioException) {
      System.err.println("Feil med innlasting av testfil.");
    }
    stage.setScene(new Scene(parent));
    stage.show();
  }
/**
  Button addButton;
  TextField nameField;
  TextField contactInfoField;
  TextArea descriptionFieldArea;
  RadioButton newRadio;
  RadioButton usedRadio;
  RadioButton damagedRadio;
  ListView<SwappItem> listView;
  
  @BeforeEach
  public void setUp() {
    addButton = (Button) parent.lookup("#addButton");
    nameField = (TextField) parent.lookup("#nameField");
    contactInfoField = (TextField) parent.lookup("#contactInfoField");
    descriptionFieldArea = (TextArea) parent.lookup("#descriptionFieldArea");
    newRadio = (RadioButton) parent.lookup("#newRadio");
    usedRadio = (RadioButton) parent.lookup("#usedRadio");
    damagedRadio = (RadioButton) parent.lookup("#damagedRadio");
    listView = (ListView) parent.lookup("#listView");
  }

  private String testName;
  private String testDescription;
  private String testContactInfo;

  @Test
  public void testAdd() {
    setUp();
    testName = "testName";
    testDescription = "Bla bla bla";
    testContactInfo = "kontaktinfo@email.no";
    clickOn(nameField).write(testName);
    clickOn(usedRadio);
    clickOn(descriptionFieldArea).write(testDescription);
    if (contactInfoField.getText().isBlank()) {
      clickOn(contactInfoField).write(testContactInfo);
    }
    clickOn(addButton);
    Assertions.assertTrue(listView.getItems().get(listView.getItems().size()-1)
    .toString().equals(testName + "  " + "  " + "Used" + "  " + 
    testDescription + "  " + testContactInfo));
  }

  /*
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
    Assertions.assertEquals(list.getSwappItems().get(list.getSwappItems().size() - 1).getName(), item2.getName());
    int lengthBeforeRemoval = list.getSwappItems().size();
    list.getSelectionModel().selectLast();
    clickOn("#removeButton");
    Assertions.assertEquals(lengthBeforeRemoval-1, list.getSwappItems().size());
    //Assertions.assertNotEquals(list.getSwappItems().get(list.getSwappItems().size() - 1).getName(), item2.getName());

  }
  */

}
