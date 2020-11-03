package swapp.ui;

import java.net.URI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    private SwappItemList swappList;

    @FXML
    String endpointUri = "http://localhost:8999/swapp/";

    private RemoteSwappAccess remoteSwappAccess;

    public RemoteAppController() {
        swappList = new SwappItemList();
        try {
            remoteSwappAccess = new RemoteSwappAccess(new URI(endpointUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        listView.setCellFactory(list -> new SwappItemListViewCell());
        updateSwappListView();
        swappList.addSwappItemListListener(swappList -> {
            //updateSwappListView();
        });
    }

    public void updateSwappListView() {
        SwappItemList tmp = remoteSwappAccess.getSwappList();
        listView.getItems().setAll(tmp.getItems());
    }

    @FXML
    void addSwappItemButtonClicked() {
        if (!textField.getText().isBlank()) {
            SwappItem item = new SwappItem(textField.getText());
            remoteSwappAccess.addSwappItem(item); 
            //remoteSwappAccess.notifySwappListChanged(this.swappList);
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
