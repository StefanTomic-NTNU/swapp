package swapp.ui;

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
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappPersistence;


public class AppController {

  @FXML
  private ListView<SwappItem> list;

  @FXML
  private TextField textField;

  @FXML
  private Button addButton;

  @FXML
  private Button removeButton;

  @FXML
  private MenuItem openButton;

  @FXML
  private MenuItem saveButton;

  private SwappPersistence swappPersistence = new SwappPersistence();

  private SwappItemList swappList;

  /** Initializes appcontroller. */
  public AppController() {
    list = new ListView<SwappItem>();
    swappList = new SwappItemList();
    loadItems();
  }

  void loadItems() {
    Reader reader = null;
    try {
      try {
        reader =
            new FileReader(Paths.get(System.getProperty("user.home"), "items.json").toFile(), 
            StandardCharsets.UTF_8);
      } catch (IOException ioex1) {
        System.err.println("Fant ingen fil lokalt. Laster inn eksempelfil..");
        URL url = getClass().getResource("items.json");
        if (url != null) {
          reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        } else {
          System.err.println("Fant ingen eksempelfil. Parser string direkte..");
          String exampleText = 
              "{{\"itemName\":\"eksempelgjenstand1\"},{\"itemName\":\"eksempelgjenstand2\"}}";
          reader = new StringReader(exampleText);
        }
      }
      SwappItemList list = swappPersistence.readSwappList(reader);
      swappList.setSwappItemlist(list);
    } catch (IOException ioex2) {
      System.err.println("Legger til gjenstander direkte..");
      swappList.addItem(new SwappItem("eksempelgjenstand1"));
      swappList.addItem(new SwappItem("eksempelgjenstand2"));
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
    updateSwappItems();
    swappList.addSwappItemListListener(swappList -> {
      updateSwappItems();
      autoSave();
    });
  }


  @FXML
  void addSwappItemButtonClicked() {
    if (!textField.getText().isBlank()) {
      SwappItem item = new SwappItem(textField.getText());
      swappList.addItem(item);
    }
    textField.setText("");
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
          new FileWriter(Paths.get(System.getProperty("user.home"), "items.json").toFile(), 
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
