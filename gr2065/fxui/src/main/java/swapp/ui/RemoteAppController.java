package swapp.ui;

import java.net.URI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;

public class RemoteAppController {

    @FXML
    private ListView<SwappItem> listView;

    @FXML
    private TextField textField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private ChoiceBox<String> filterChoiceBox;

    private SwappItemList swappList;

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
        updateSwappListView();
        swappList.addSwappItemListListener(swappList -> {
            // updateSwappListView();
        });
    }

    public void initializeChoiceBox(){
        filterChoiceBox.getItems().add("All");
        filterChoiceBox.getItems().add("New");
        filterChoiceBox.getItems().add("Used");
        filterChoiceBox.setValue("All");
        filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> updateSwappListView());
      }

    public void updateSwappListView() {
        SwappItemList tmp = remoteSwappAccess.getSwappList();
        listView.getItems().setAll(tmp.getItemsByStatus(filterChoiceBox.getSelectionModel().getSelectedItem()));
    }

    @FXML
    void addSwappItemButtonClicked() {
        if (!textField.getText().isBlank()) {
            SwappItem item = new SwappItem(textField.getText());
            remoteSwappAccess.addSwappItem(item);
            // remoteSwappAccess.notifySwappListChanged(this.swappList);
            updateSwappListView();
        }
        textField.setText("");
    }

    @FXML
    void removeSwappItemButtonClicked() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            SwappItem item = (SwappItem) listView.getSelectionModel().getSelectedItem();
            remoteSwappAccess.removeSwappItem(item);
            updateSwappListView();
        }
    }
}
