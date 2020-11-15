package swapp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SwappList implements Iterable<SwappItem> {

  private List<SwappItem> swappItems = new ArrayList<>();
  private Collection<SwappListListener> swappListListeners = new ArrayList<>();
  private String username;

  public SwappList(String username) {
    if (username == null || username.isEmpty()){
      throw new IllegalArgumentException("Illegal name for list");
    } 
    this.username = username;
  }

  public SwappList(Collection<SwappItem> items) {
    if (!items.isEmpty()){ 
      this.username = items.stream().findFirst().get().getUsername();
      this.addSwappItem(items);
    } else{
      throw new IllegalArgumentException("Can not initialize with empty list");
    }
  }

  public SwappList(SwappItem... items) {
    if (items.length > 0) { 
      this.username = List.of(items).stream().findFirst().get().getUsername();
      this.addSwappItem(List.of(items));
    } else{
      throw new IllegalArgumentException("Can not initialize with empty list");
    }
  }

  public void addSwappItem(SwappItem newItem) {
    validateAddSwappItem(newItem);
    swappItems.add(newItem);
    fireSwappListChanged();
  }

  public void validateAddSwappItem(SwappItem swappItem) {
    if (swappItem == null) {
      throw new IllegalArgumentException("validateAddSwappItem" + " SwappItem can't be null");
    }
    if (hasSwappItem(swappItem)) {
      throw new IllegalArgumentException("validateAddSwappItem" + " SwappItem already exist");
    }
    if (!swappItem.getUsername().equals(this.username)) throw new IllegalArgumentException("validateAddSwappItem" + " item must have same username as the list");
  }

  public void validateRemoveSwappItem(SwappItem swappItem) {
    if (swappItem == null) {
      throw new IllegalArgumentException("validateRemoveSwappItem " + "SwappItem can't be null");
    }
    if (!hasSwappItem(swappItem)) {
      throw new IllegalArgumentException("validateRemoveSwappItem" + "SwappItem doesn't exist");
    }
  }

  public void validateRemoveSwappItem(String swappItem) {
    if (swappItem == null) {
      throw new IllegalArgumentException("validateRemoveSwappItem " + "SwappItem can't be null");
    }
    if (!hasSwappItem(swappItem)) {
      throw new IllegalArgumentException("validateRemoveSwappItem" + "SwappItem doesn't exist");
    }
  }

  public void addSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      validateAddSwappItem(item);
      this.swappItems.add(item);
    }
    fireSwappListChanged();
  }

  public void removeSwappItem(SwappItem item) {
    validateRemoveSwappItem(item);
    swappItems.remove(item);
    fireSwappListChanged();
  }

  public void removeSwappItem(String item) {
    validateRemoveSwappItem(item);
    swappItems.remove(getSwappItem(item));
    fireSwappListChanged();
  }

  public void removeSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      validateRemoveSwappItem(item);
      swappItems.remove(item);
    }
    fireSwappListChanged();
  }

  public boolean hasSwappItem(String name) {
    return getSwappItem(name) != null;
  }

  public boolean hasSwappItem(SwappItem item) {
    return getSwappItem(item.getName()) != null;
  }

  public SwappItem getSwappItem(SwappItem item) {
    return getSwappItem(item.getName());
  }

  public SwappItem getSwappItem(String name) {
    if (swappItems.stream().anyMatch(p -> p.nameEquals(name))) {
      return swappItems.stream().filter(p -> p.nameEquals(name)).findFirst().get();
    } else {
      return null;
    }
  }

  public boolean isItemChanged(SwappItem newItem) {
    if (swappItems.stream().anyMatch(p -> p.allAttributesEquals(newItem))) {
      return false;
    }
    return true;
  }

  public List<SwappItem> getSwappItems() {
    return this.swappItems;
  }

  public SwappItem createAndAddSwappItem(String name, String username, String status, String infoString) {
    SwappItem newItem = new SwappItem(name, username, status, infoString);
    addSwappItem(newItem);
    return newItem;
  }

  public List<SwappItem> getSwappItemsByStatus(String status) {
    if (status.equals("All"))
      return getSwappItems();
    return this.swappItems.stream().filter(p -> p.getStatus().equals(status)).collect(Collectors.toList());
  }

  public void changeSwappItem(SwappItem oldItem, SwappItem newItem) {
    if (!oldItem.nameEquals(newItem)) throw new IllegalArgumentException("name must be the same for change item, changeSwappItem");
    if (!hasSwappItem(oldItem)) throw new IllegalArgumentException("Item doesn't exist changeSwappItem");
    oldItem.setStatus(newItem.getStatus());
    oldItem.setDescription(newItem.getDescription());
    fireSwappListChanged();
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

  public String getUsername() {
    return this.username;
  }

}
