package swapp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SwappModel implements Iterable<SwappList> {

    private LinkedHashMap<String, SwappList> swappLists = new LinkedHashMap<String, SwappList>();

    public boolean hasSwappList(String username) {
        return swappLists.containsKey(username);
    }

    public SwappList addNewSwappList(String username) {
        return putSwappList(new SwappList(username));
    }

    public boolean hasSwappItem(String username, String itemName) {
        return getSwappList(username).hasSwappItem(itemName);
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

    public SwappItem getSwappItem(SwappItem swappItem){
        return getSwappList(swappItem.getUsername()).getSwappItem(swappItem.getUsername());
    }

    public SwappItem changeSwappItem(String username, SwappItem old, SwappItem newItem) {
        getSwappList(username).changeSwappItem(old, newItem);
        return getSwappList(username).getSwappItem(newItem);
    }

    public SwappList createNewSwappList(String name){
        return new SwappList(name);
    }

    public boolean isItemChanged(SwappItem newItem){
        return getSwappList(newItem.getUsername()).isItemChanged(newItem);
    }

    @Override
    public Iterator<SwappList> iterator() {
        return swappLists.values().iterator();
    }

    public Collection<SwappList> getSwappLists(){
        return swappLists.values();
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