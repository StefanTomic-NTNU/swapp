package swapp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SwappList implements Iterable<SwappItem> {

  private List<SwappItem> swappItems = new ArrayList<>();
  private Collection<SwappListListener> swappListListeners = new ArrayList<>();
  private String username;

  /**
   * Constructs SwappList with given username.
   *
   * @param username String which the username is set to
   * @throws IllegalArgumentException if username is null or an empty string.
   */
  public SwappList(String username) {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Illegal name for list");
    }
    this.username = username;
  }

  /**
   * Constructs SwappList by adding Collection of SwappItems. SwappItems are validated in the process.
   *
   * @param items SwappItems which are added to the SwappList.
   */
  public SwappList(Collection<SwappItem> items) {
    if (!items.isEmpty()) { 
      this.username = items.stream().findFirst().get().getUsername();
      this.addSwappItem(items);
    } else {
      throw new IllegalArgumentException("Can not initialize with empty list");
    }
  }

  /**
   * Constructs SwappList with given items. SwappItems are validated in the process.
   *
   * @param items SwappItems which are added to the SwappList.
   */
  public SwappList(SwappItem... items) {
    if (items.length > 0) { 
      this.username = List.of(items).stream().findFirst().get().getUsername();
      this.addSwappItem(List.of(items));
    } else {
      throw new IllegalArgumentException("Can not initialize with empty list");
    }
  }

  /**
   * Adds given SwappItem to SwappList. SwappItem is validated beforehand.
   *
   * @param newItem SwappItems which is added to the SwappList.
   */
  public void addSwappItem(SwappItem newItem) {
    validateAddSwappItem(newItem);
    swappItems.add(newItem);
    fireSwappListChanged();
  }

  /**
   * Adds collection of SwappItems to SwappList.
   * 
   * <p>SwappItems are validated before being added.
   *
   * @param items SwappItems to be added.
   */
  public void addSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      validateAddSwappItem(item);
      this.swappItems.add(item);
    }
    fireSwappListChanged();
  }

  
  /**
   * Constructs a SwappItem and then adds it to the SwappList.
   * 
   * <p>SwappItems are validated before being added.
   *
   * @param name        name of SwappItem
   * @param username    username associated with SwappItem
   * @param status      status of SwappItem
   * @param description  description of SwappItem
   */
  public SwappItem createAndAddSwappItem(
      String name, String username, String status, String description) {
    SwappItem newItem = new SwappItem(name, username, status, description);
    addSwappItem(newItem);
    return newItem;
  }

  /**
   * Removes SwappItem from SwappList.
   *
   * <p>SwappItems are validated for removal before being removed.
   *
   * @param item name of SwappItem to be removed.
   */
  public void removeSwappItem(String item) {
    validateRemoveSwappItem(item);
    swappItems.remove(getSwappItem(item));
    fireSwappListChanged();
  }

  /**
   * Removes SwappItem from SwappList.
   * 
   * <p>SwappItems are validated for removal before being removed.
   *
   * @param item SwappItem to be removed.
   */
  public void removeSwappItem(SwappItem item) {
    validateRemoveSwappItem(item);
    swappItems.remove(item);
    fireSwappListChanged();
  }

  /**
   * Removes Collection of SwappItems from SwappList.
   * 
   * <p>SwappItems are validated for removal before being removed.
   *
   * @param items SwappItems to be removed.
   */
  public void removeSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      validateRemoveSwappItem(item);
      swappItems.remove(item);
    }
    fireSwappListChanged();
  }

  /**
   * Validates SwappItem in relation to the SwappList.
   * 
   * <p>The given swappItem is validated against the following criteria:
   * <dl>
   * <dd>-given swappItem may not be null</dd>
   * <dd>-given swappItem may not already be contained in SwappList</dd>
   * <dd>-given swappItem must have same username as SwappList</dd>
   * </dl>
   *
   * @param swappItem SwappItem which is validated.
   * @throws IllegalArgumentException if SwappItem is invalid.
   */
  public void validateAddSwappItem(SwappItem swappItem) {
    if (swappItem == null) {
      throw new IllegalArgumentException("validateAddSwappItem" 
        + " SwappItem can't be null");
    }
    if (hasSwappItem(swappItem)) {
      throw new IllegalArgumentException("validateAddSwappItem" 
        + " SwappItem already exist");
    }
    if (!swappItem.getUsername().equals(this.username)) {
      throw new IllegalArgumentException("validateAddSwappItem" 
        + " item must have same username as the list");
    }
  }

  /**
   * Validates removal of SwappItem.
   *
   * @param swappItem to be validated.
   * @throws IllegalArgumentException if SwappItem is null or doesn't exist in the SwappList.
   */
  public void validateRemoveSwappItem(SwappItem swappItem) {
    if (swappItem == null) {
      throw new IllegalArgumentException("validateRemoveSwappItem " 
        + "SwappItem can't be null");
    }
    if (!hasSwappItem(swappItem)) {
      throw new IllegalArgumentException("validateRemoveSwappItem" 
        + "SwappItem doesn't exist");
    }
  }

  /**
   * Validates removal of SwappItem.
   *
   * @param swappItem name of SwappItem to be validated.
   * @throws IllegalArgumentException if SwappItem's name is null or doesn't exist in the SwappList.
   */
  public void validateRemoveSwappItem(String swappItem) {
    if (swappItem == null) {
      throw new IllegalArgumentException("validateRemoveSwappItem " 
        + "SwappItem can't be null");
    }
    if (!hasSwappItem(swappItem)) {
      throw new IllegalArgumentException("validateRemoveSwappItem" 
        + "SwappItem doesn't exist");
    }
  }

  /**
   * Sets a SwappItem's status and description to match another SwappItem.
   *
   * @param oldItem SwappItem which attributes are changed.
   * @param newItem SwappItem which oldItem's attributes are set to.
   * @throws IllegalArgumentException if oldItem doesn't exist or if names of input match
   */
  public void changeSwappItem(SwappItem oldItem, SwappItem newItem) {
    if (!oldItem.nameEquals(newItem)) {
      throw new IllegalArgumentException("name must be the same for change item, changeSwappItem"); 
    }
    if (!hasSwappItem(oldItem)) {
      throw new IllegalArgumentException("Item doesn't exist changeSwappItem");
    }
    oldItem.setStatus(newItem.getStatus());
    oldItem.setDescription(newItem.getDescription());
    fireSwappListChanged();
  }

  /**
   * Checks SwappList contains any SwappItem with all the same attributes as given SwappItem.
   *
   * @param newItem SwappItem to compare with.
   * @return true only if item an item with all the same attributes exists in SwappList. 
   */
  public boolean isItemChanged(SwappItem newItem) {
    if (swappItems.stream().anyMatch(p -> p.allAttributesEquals(newItem))) {
      return false;
    }
    return true;
  }




  /**
   * Checks if there exists a SwappItem with the given name in SwappList.
   *
   * @param name name of SwappItem to check for.
   * @return true if item exists in SwappList. Return false otherwise.
   */
  public boolean hasSwappItem(String name) {
    return getSwappItem(name) != null;
  }

  /**
   * Checks if SwappItem exists in the SwappList.
   *
   * @param item name of SwappItem to check for.
   * @return true if item exists in SwappList. Return false otherwise.
   */
  public boolean hasSwappItem(SwappItem item) {
    return getSwappItem(item.getName()) != null;
  }


  /**
   * Returns SwappItem with the same name as the given SwappItem.
   *
   * @param name name of SwappItem to get.
   * @return SwappItem. Null if SwappList contains no SwappItem with the same name.
   */
  public SwappItem getSwappItem(String name) {
    if (swappItems.stream().anyMatch(p -> p.nameEquals(name))) {
      return swappItems.stream().filter(p -> p.nameEquals(name)).findFirst().get();
    } else {
      return null;
    }
  }

  /**
   * Returns SwappItem with the same name as the given SwappItem.
   *
   * @param item SwappItem to get.
   * @return SwappItem. Null if SwappList contains no SwappItem with the same name.
   */
  public SwappItem getSwappItem(SwappItem item) {
    return getSwappItem(item.getName());
  }

  public List<SwappItem> getSwappItems() {
    return this.swappItems;
  }

  /**
   * Gets all SwappItems in the SwappList who's status equals input.
   *
   * @param status status to compare with.
   * @return List of SwappItems with matching status.
   */
  public List<SwappItem> getSwappItemsByStatus(String status) {
    if (status.equals("All")) {
      return getSwappItems(); 
    }
    return this.swappItems.stream()
      .filter(p -> p.getStatus().equals(status)).collect(Collectors.toList());
  }

  public String getUsername() {
    return this.username;
  }

  public void addSwappListListener(SwappListListener listener) {
    swappListListeners.add(listener);
  }

  public void removeSwappListListener(SwappListListener listener) {
    swappListListeners.remove(listener);
  }

  protected void fireSwappListChanged() {
    for (SwappListListener listener : swappListListeners) {
      listener.swappListChanged(this);
    }
  }

  @Override
  public Iterator<SwappItem> iterator() {
    return swappItems.iterator();
  }

  

}
