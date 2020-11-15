package swapp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SwappModel implements Iterable<SwappList> {

  /**
   * LinkedHashMap which maps usernames to SwappLists.
   */
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

  /**
   * Gets all SwappItems in the Model.
   *
   * @return List of all SwappItems in the Model.
   */
  public List<SwappItem> getSwappItems() {
    List<SwappItem> swappItems = new ArrayList<SwappItem>();
    swappLists.values().stream().forEach(p -> swappItems.addAll(p.getSwappItems()));
    return swappItems;
  }

  /**
   * Gets all SwappItems in the Model who's status equals input.
   *
   * @param status status to compare with.
   * @return List of SwappItems with matching status.
   */
  public List<SwappItem> getSwappItemsByStatus(String status) {
    List<SwappItem> swappItems = new ArrayList<SwappItem>();
    swappLists.values().stream().forEach(p -> swappItems.addAll(p.getSwappItemsByStatus(status)));
    return swappItems;
  }

  public List<SwappItem> getSwappItemsByUser(String username) {
    return getSwappList(username).getSwappItems();
  }

  /**
   * Validates username.
   *
   * @param username username to validate.
   * @return true if username is not already in swappLists. Otherwise return false.
   */
  public boolean isValidName(String username) {
    return hasSwappList(username) == false;
  }

  public SwappItem getSwappItem(SwappItem swappItem) {
    return getSwappList(swappItem.getUsername()).getSwappItem(swappItem.getName());
  }

  /**
   * Sets a SwappItem's status and description to match another SwappItem.
   *
   * @param username username which is associated with the item to change.
   * @param oldItem SwappItem which attributes are changed.
   * @param newItem SwappItem which oldItem's attributes are set to.
   * @throws IllegalArgumentException if oldItem doesn't exist or if names of input match
   */
  public SwappItem changeSwappItem(String username, SwappItem oldItem, SwappItem newItem) {
    getSwappList(username).changeSwappItem(oldItem, newItem);
    return getSwappList(username).getSwappItem(newItem);
  }

  public SwappList createNewSwappList(String name) {
    return new SwappList(name);
  }

  /**
   * Checks if SwappModel contains any SwappItem with all the same attributes as give SwappItem.
   *
   * @param newItem SwappItem to compare with.
   * @return true only if item an item with all the same attributes exists in SwappList. 
   */
  public boolean isItemChanged(SwappItem newItem) {
    return getSwappList(newItem.getUsername()).isItemChanged(newItem);
  }

  @Override
  public Iterator<SwappList> iterator() {
    return swappLists.values().iterator();
  }

  public Collection<SwappList> getSwappLists() {
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
