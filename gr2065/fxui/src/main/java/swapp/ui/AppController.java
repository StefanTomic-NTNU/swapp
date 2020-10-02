package swapp.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URL;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


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

    public AppController(){
        list = new ListView<Item>();
        items = new Items();
        loadItems();
    }



    void loadItems() {  
        getObjectMapper();
        Reader reader = null;
        try {
            try {
                reader = new FileReader(Paths.get(System.getProperty("user.home"), "items.json").toFile(), StandardCharsets.UTF_8);
            } catch (IOException ioex1) {
                System.err.println("Fant ingen fil lokalt. Laster inn eksempelfil..");
                URL url = getClass().getResource("items.json");
                if (url != null) {
                    reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
                } else {
                    System.err.println("Fant ingen eksempelfil. Parser string direkte..");
                    String exampleText = "{{\"itemName\":\"eksempelgjenstand1\"},{\"itemName\":\"eksempelgjenstand2\"}}";
                    reader = new StringReader(exampleText);
                }
            }
            items = objectMapper.readValue(reader, Items.class);
        } catch(IOException ioex2) {
            System.err.println("Legger til gjenstander direkte..");
            items.addItem(new Item("eksempelgjenstand1"));
            items.addItem(new Item("eksempelgjenstand2"));
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch(IOException ioex3) {}
        }
    }


    @FXML
    void initialize() {
        updateItems();
    }


    @FXML
    void handleClickMeButtonAction() {
        if (!textField.getText().isBlank()){
            Item item = new Item(textField.getText());
            items.addItem(item);
            updateItems();
        }
        textField.setText("");
    }

    public void updateItems(){
        list.getItems().setAll(items.getItems());
    }

    public void setItems(final Items items){
        this.items = items;
        updateItems();
    }

    public Items getItems(){
        return items;
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

    private void showOpenExceptionDialog(final File location, final Exception e) {
        showExceptionDialog("Oops, problem opening from " + location, e);
    }



    private void saveItems() {
        try {
            Writer writer = new FileWriter(Paths.get(System.getProperty("user.home"), "items.json").toFile(), StandardCharsets.UTF_8);
            objectMapper.writeValue(writer, items);
        } catch(IOException ioex) {
            System.err.println("Feil med fillagring.");
        }
    }

    @FXML
    void handleSaveAction() {saveItems();}

    @FXML
    void handleOpenAction() {loadItems();}
    
}
