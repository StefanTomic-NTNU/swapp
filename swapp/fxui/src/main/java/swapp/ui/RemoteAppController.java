package swapp.ui;

import java.io.IOException;
import java.net.URI;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.RadioButton;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappPersistence;

public class RemoteAppController {

  @FXML
  private ListView<SwappItem> listView;

  @FXML
  private ChoiceBox<String> filterChoiceBox;
  
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

  @FXML
  private TextField nameField;
  
  @FXML
  private TextArea descriptionFieldArea;

  @FXML
  private TextField contactInfoField;

  @FXML
  private RadioButton newRadio;

  @FXML
  private RadioButton usedRadio;

  @FXML
  private RadioButton damagedRadio;

    private SwappItemList swappList;
    private ToggleGroup toggleGroup;

    @FXML
    String endpointUri = "http://localhost:8999/swapp/";

    private RemoteSwappAccess remoteSwappAccess;

    public RemoteAppController() {
        swappList = new SwappItemList();
        filterChoiceBox = new ChoiceBox<>();
        try {
            remoteSwappAccess = new RemoteSwappAccess(new URI(endpointUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        initializeChoiceBox();
        listView.setCellFactory(list -> new SwappItemListViewCell());
        inizializeToggleGroup();
        updateSwappListView();
        swappList.addSwappItemListListener(swappList -> {
            // updateSwappListView();
        });
    }

    public void initializeChoiceBox() {
        filterChoiceBox.getItems().add("All");
        filterChoiceBox.getItems().add("New");
        filterChoiceBox.getItems().add("Used");
        filterChoiceBox.getItems().add("Damaged");
        filterChoiceBox.setValue("All");
        filterChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> updateSwappListView());
    }

    public void inizializeToggleGroup() {
      toggleGroup = new ToggleGroup();
      newRadio.setToggleGroup(toggleGroup);
      usedRadio.setToggleGroup(toggleGroup);
      damagedRadio.setToggleGroup(toggleGroup);
    }

    public void updateSwappListView() {
        SwappItemList tmp = remoteSwappAccess.getSwappList();
        listView.getItems().setAll(tmp.getItemsByStatus(filterChoiceBox.getSelectionModel().getSelectedItem()));
    }

    @FXML
    void addSwappItemButtonClicked() {
      if (!nameField.getText().isBlank()) {
        String name =nameField.getText();
        String status =((RadioButton)toggleGroup.getSelectedToggle()).getText();
        String description = descriptionFieldArea.getText();
        String contactInfo =contactInfoField.getText();
        SwappItem item = new SwappItem(name, /*statusChoiceBox.getSelectionModel().getSelectedItem().toString()*/ status, description, contactInfo);
        if (!remoteSwappAccess.getSwappList().getItems().contains(item)) {
          remoteSwappAccess.addSwappItem(item);
          updateSwappListView();
          nameField.setText("");
          descriptionFieldArea.setText("");
        }
      }
    }

    @FXML
    void removeSwappItemButtonClicked() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            SwappItem item = (SwappItem) listView.getSelectionModel().getSelectedItem();
            remoteSwappAccess.removeSwappItem(item.getName());
            updateSwappListView();
        }
    }

    @FXML
    public void viewSwappItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewSwappItem.fxml"));
        Parent root = (Parent) loader.load();
        ViewSwappItemController itemController = loader.getController();
        SwappItem swappItem = remoteSwappAccess.getSwappItem(listView.getSelectionModel().getSelectedItem().getName());
        System.out.println(swappItem + "viewSwappItem()");
        itemController.initSwappitem(swappItem);
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Item");
        stage.showAndWait();
        SwappItem newItem = itemController.getSwappItem();
        remoteSwappAccess.putSwappList(newItem);
        updateSwappListView();
    }
}
