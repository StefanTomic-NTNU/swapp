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
import swapp.core.Item;
import swapp.core.Items;
import swapp.json.ItemsModule;

public class AppController {

  @FXML
  private ListView<Item> list;

  @FXML
  private TextField textField;

  @FXML
  private Button addButton;

  @FXML
  private MenuItem openButton;

  @FXML
  private MenuItem saveButton;


  private Items items;

  public AppController() {
    items = new Items();
  }

  @FXML
  void handleClickMeButtonAction() {
    if (!textField.getText().isBlank()) {
      Item item = new Item(textField.getText());
      items.addItem(item);
      updateItems();
    }
    textField.setText("");
  }

  public void updateItems() {
    list.getItems().setAll(items.getItems());
  }

  public void setItems(final Items items) {
    this.items = items;
    updateItems();
  }

  public Items getItems() {
    return items;
  }

  // File menu items
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
        setItems(getObjectMapper().readValue(input, Items.class));
      } catch (final IOException e) {
        showExceptionDialog("Oops, problem when opening " + selection, e);
      }
    }
  }

  private ObjectMapper objectMapper;

  public ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
      objectMapper.registerModule(new ItemsModule());
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
