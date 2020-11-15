package swapp.ui;

import java.util.Collection;
import java.util.List;

import swapp.core.SwappItem;
import swapp.core.SwappList;

public interface SwappDataAccess {
    
    public Collection<SwappItem> getAllSwappItems();

    public void addSwappItem(SwappItem swappItem) throws Exception;

    public void removeSwappItem(SwappItem swappItem) throws Exception;

    public void changeSwappItem(SwappItem newItem) throws Exception;

    public void removeAllSwappItems(String username);

    public void addNewSwappList(String username);

    public List<SwappItem> getSwappItemByUser(String username);

    public List<SwappItem> getSwappItemByStatus(String status);

    public boolean isItemChanged(SwappItem swappItem);

    public boolean hasSwappList(String username);

    public Collection<SwappList> getAllSwappLists();

    public SwappItem getSwappItem(SwappItem swappItem);

    public boolean hasSwappItem(String username, String itemname);

    public void writeData();

}