package swapp.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import swapp.core.SwappItem;

public class SwappItemListViewCell extends ListCell<SwappItem> {

    @FXML
    private Text titleText;

    @FXML
    private Text statusText;

    @FXML
    private Text usernameText;

    @FXML
    private HBox hbox;

    private FXMLLoader mLLoader;
    
    @Override
    protected void updateItem(SwappItem swappItem, boolean empty) {
        super.updateItem(swappItem, empty);

        if (empty || swappItem == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("item.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            titleText.setText(swappItem.getName());
            statusText.setText(swappItem.getStatus());
            usernameText.setText(swappItem.getUsername());
            setText(null);
            setGraphic(hbox);
        }

    }

}
