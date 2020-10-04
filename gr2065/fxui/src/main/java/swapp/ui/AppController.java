package swapp.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
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

  public AppController() {
    swappList = new SwappItemList();
  }

  /**Initialize with lambda expression listener SwappItemList */
  @FXML
  public void initialize(){
    updateSwappItems();
    swappList.addSwappItemListListener(swappList -> updateSwappItems());
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
    if (!(item==null)) {
      swappList.removeItem(item);
    }
  }

  public void updateSwappItems() {
    list.getItems().setAll(swappList.getItems());
  }

  public SwappItemList getItems() {
    return swappList;
  }

  // File menu swappList
  private FileChooser fileChooser;

  private FileChooser getFileChooser() {
    if (fileChooser == null) {
      fileChooser = new FileChooser();
    }
    return fileChooser;
  }

  @FXML
  void handleOpenAction(final ActionEvent event) {
    final FileChooser fileChooser = getFileChooser();
    final File selection = fileChooser.showOpenDialog(null);
    if (selection != null) {
      try (InputStream input = new FileInputStream(selection)) {
        swappList.setSwappItemlist(getObjectMapper().readValue(input, SwappItemList.class));
      } catch (final IOException e) {
        showExceptionDialog("Oops, problem when opening " + selection, e);
      }
    }
  }

  private ObjectMapper objectMapper;

  public ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
      objectMapper.registerModule(new SwappItemModule());
    }
    return objectMapper;
  }

  private void showExceptionDialog(final String message) {
    final Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.CLOSE);
    alert.showAndWait();
  }

  private void showExceptionDialog(final String message, final Exception e) {
    showExceptionDialog(message + ": " + e.getLocalizedMessage());
  }

  private void showSaveExceptionDialog(final File location, final Exception e) {
    showExceptionDialog("Oops, problem saving to " + location, e);
  }

  @FXML
  void handleSaveAction() {
    final FileChooser fileChooser = getFileChooser();
    final File selection = fileChooser.showSaveDialog(null);
    if (selection != null) {
      try (OutputStream outputStream = new FileOutputStream(selection, false)) {
        getObjectMapper().writeValue(outputStream, getItems());
      } catch (final IOException e) {
        showSaveExceptionDialog(selection, e);
      }
    }
  }

}
