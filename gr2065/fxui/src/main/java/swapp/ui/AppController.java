package swapp.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import swapp.json.SwappItemModule;


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


  private SwappItemList swappList;

  /** Initializes appcontroller. */
  public AppController() {
    list = new ListView<SwappItem>();
    swappList = new SwappItemList();
    loadItems();
  }

  void loadItems() {
    getObjectMapper();
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
      SwappItemList list = objectMapper.readValue(reader, SwappItemList.class);
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


  /** Initialize with lambda expression listener SwappItemList. */
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

  private ObjectMapper objectMapper;

  /** Creates objectmapper or returns objectmapper. */
  public ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
      objectMapper.registerModule(new SwappItemModule());
    }
    return objectMapper;
  }

  /*
   * ubrukt kode
   * 
   * private void showExceptionDialog(final String message) { final Alert alert = new
   * Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE); alert.showAndWait(); }
   * 
   * 
   * private void showExceptionDialog(final String message, final Exception e) {
   * showExceptionDialog(message + ": " + e.getLocalizedMessage()); }
   * 
   * 
   * private void showSaveExceptionDialog(final File location, final Exception e) {
   * showExceptionDialog("Oops, problem saving to " + location, e); }
   * 
   * private void showOpenExceptionDialog(final File location, final Exception e) {
   * showExceptionDialog("Oops, problem opening from " + location, e); }
   */


  private void autoSave() {
    Writer writer = null;
    try {
      writer =
          new FileWriter(Paths.get(System.getProperty("user.home"), "items.json").toFile(), 
          StandardCharsets.UTF_8);
      objectMapper.writeValue(writer, swappList);
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
