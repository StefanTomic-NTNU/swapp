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
    String endpointUri;
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
        updateSwappListView();
        swappList.addSwappItemListListener(swappList -> {
            updateSwappListView();
        });
    }

    public void updateSwappListView() {
        listView.getItems().setAll(remoteSwappAccess.getSwappList().getItems());
    }


    void addSwappItemButtonClicked() {
        if (!textField.getText().isBlank()) {
            SwappItem item = new SwappItem(textField.getText());
            SwappItemList newSwappList = new SwappItemList(this.swappList.getItems());
            newSwappList.addItem(item); 
            remoteSwappAccess.addSwappList(newSwappList);
        }
        textField.setText("");
    }


    void removeSwappItemButtonClicked() {
        if (!textField.getText().isBlank()) {
            SwappItem item = new SwappItem(textField.getText());
            SwappItemList newSwappList = new SwappItemList(this.swappList.getItems());
            newSwappList.removeItem(item);
            remoteSwappAccess.removeSwappList(newSwappList);
        }
        textField.setText("");
    }
}
