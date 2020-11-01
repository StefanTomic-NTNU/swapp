package swapp.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappPersistence;


public class AppController {

  @FXML
  private ListView<SwappItem> list;

//  @FXML
//  private TextField textField;

  @FXML
  private Button addButton;

  @FXML
  private Button removeButton;

  @FXML
  private MenuItem openButton;

  @FXML
  private MenuItem saveButton;

  @FXML
  private TextField nameField;

  @FXML
  private ChoiceBox statusChoiceBox;
  
  @FXML
  private TextArea descriptionFieldArea;

  @FXML
  private TextField contactInfoField;

  
  private SwappPersistence swappPersistence = new SwappPersistence();

  private SwappItemList swappList;

  private File file = Paths.get(System.getProperty("user.home"), "items.json").toFile();

  private final static String SwappItemListWithTwoItems = 
    "[{\"itemName\":\"name1\",\"itemStatus\":\"Ny\""
    + ",\"itemDescription\":\"description1\",\"itemContactInfo\":\"contactInfo1\"},"
    + "{\"itemName\":\"name2\",\"itemStatus\":\"Ny\""
    + ",\"itemDescription\":\"description2\",\"itemContactInfo\":\"contactInfo2\"}]";

  /** Initializes appcontroller. */
  public AppController() {
    list = new ListView<SwappItem>();
    swappList = new SwappItemList();
    loadItems();
  }

  public void setFile(File file) {
    this.file = file;
    loadItems();
  }

  void loadItems() {
    Reader reader = null;
    try {
      try {
        reader =
            new FileReader(file, 
            StandardCharsets.UTF_8);
      } catch (IOException ioex1) {
        System.err.println("Fant ingen fil lokalt. Laster inn eksempelfil..");
        URL url = getClass().getResource("items.json");
        if (url != null) {
          reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        } else {
          System.err.println("Fant ingen eksempelfil. Parser string direkte..");
          String exampleText = SwappItemListWithTwoItems;
          reader = new StringReader(exampleText);
        }
      }
      /* For å printe ut fil til konsoll:
      BufferedReader reader2 =
            new BufferedReader(reader);
      String linje;
      while ((linje = reader2.readLine()) != null) {
        System.out.println(linje);
      }
      */
      SwappItemList list = swappPersistence.readSwappList(reader);
      swappList.setSwappItemlist(list);
    } catch (IOException ioex2) {
      System.err.println("Legger til gjenstander direkte..");
      swappList.addItem(new SwappItem("name1", "Ny", "description1", "contactInfo1"));
      swappList.addItem(new SwappItem("name2", "Ny", "description2", "contactInfo2"));
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException ioex3) {
        System.err.println("Problem med reader..");
      }
    }
  }


  /** Initialize with lambda expression for listeners of SwappItemList. */
  @FXML
  void initialize() {
    statusChoiceBox.getItems().addAll("Ny", "Litt brukt", "Godt brukt");
    updateSwappItems();
    swappList.addSwappItemListListener(swappList -> {
      updateSwappItems();
      autoSave();
    });
  }


  @FXML
  void addSwappItemButtonClicked() {
    if (!nameField.getText().isBlank()) {
      SwappItem item = new SwappItem(nameField.getText(), statusChoiceBox.getSelectionModel().getSelectedItem().toString(), descriptionFieldArea.getText(), contactInfoField.getText());
      swappList.addItem(item);
    }
    nameField.setText("");
  }

  @FXML
  void removeSwappItemButtonClicked() {
    SwappItem item = (SwappItem) list.getSelectionModel().getSelectedItem();
    if (!(item == null)) {
      swappList.removeItem(item);
    }
  }

  public void updateSwappItems() {
    list.getItems().setAll(swappList.getItems());
  }


  public SwappItemList getItems() {
    return swappList;
  }

  private void autoSave() {
    Writer writer = null;
    try {
      writer =
          new FileWriter(file, 
          StandardCharsets.UTF_8);
      swappPersistence.writeSwappList(swappList, writer);
    } catch (IOException ioex) {
      System.err.println("Feil med fillagring.");
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        System.err.println("Feil med fillagring..");
      }
    }
  }

  /*
   * Metoder for dokumentmetafor:
   * 
   * @FXML void handleSaveAction() {saveItems();}
   * 
   * @FXML void handleOpenAction() {loadItems();}
   */
}
