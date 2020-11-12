package swapp.ui;

import javafx.scene.control.ListCell;
import swapp.core.SwappItem;

public class SwappItemListViewCell extends ListCell<SwappItem> {
    @Override
    protected void updateItem(SwappItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getName() + "\t" + item.getStatus() + "\t" + item.getDescription() + "\t" +item.getUsername() );
        } else {
            setText("");
        }
    }
}
