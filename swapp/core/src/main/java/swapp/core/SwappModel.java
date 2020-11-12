package swapp.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SwappModel implements Iterable<SwappList> {

    private HashMap<String, SwappList> swappLists = new HashMap<String, SwappList>();

    public boolean hasSwappList(String username) {
        return swappLists.containsKey(username);
    }

    public SwappList addSwappList(String username) {
        return putSwappList(new SwappList(username));
    }

    public SwappList addSwappList(SwappList swappList) {
        return putSwappList(swappList);
    }

    public SwappList putSwappList(SwappList swappList) {
        return swappLists.put(swappList.getUsername(), swappList);
    }

    public List<String> getUsernames() {
        return swappLists.keySet().stream().collect(Collectors.toList());
    }

    public List<SwappItem> getSwappItems() {
        List<SwappItem> swappItems = new ArrayList<SwappItem>();
        swappLists.values().stream().forEach(p -> swappItems.addAll(p.getSwappItems()));
        return swappItems;
    }

    public List<SwappItem> getSwappItemsByStatus(String status) {
        List<SwappItem> swappItems = new ArrayList<SwappItem>();
        swappLists.values().stream().forEach(p -> swappItems.addAll(p.getSwappItemsByStatus(status)));
        return swappItems;
    }

    public List<SwappItem> getSwappItemsByUser(String username) {
        return getSwappList(username).getSwappItems();
    }

    public boolean isValidName(String username) {
        return hasSwappList(username) == false;
    }

    public void changeSwappItem(String username, SwappItem old, SwappItem newItem) {
        getSwappList(username).changeSwappItem(old, newItem);
    }

    @Override
    public Iterator<SwappList> iterator() {
        return swappLists.values().iterator();
    }

    public HashMap<String, SwappList> getHashMap() {
        return swappLists;
    }

    public SwappList getSwappList(String username) {
        return swappLists.get(username);
    }

    public void addSwappItem(SwappItem swappItem) {
        getSwappList(swappItem.getUsername()).addSwappItem(swappItem);
    }

    public void removeSwappItem(SwappItem swappItem) {
        getSwappList(swappItem.getUsername()).removeSwappItem(swappItem);
    }

    public void removeSwappItem(String username, String swappItem) {
        getSwappList(username).removeSwappItem(swappItem);
    }

}