package swapp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SwappItemList implements Iterable<SwappItem> {

  private List<SwappItem> items = new ArrayList<>();
  private Collection<SwappItemListListener> swappItemListListeners = new ArrayList<>();

  public SwappItemList() {
  }

  public SwappItemList(final SwappItem... items) {
    this.addItem(items);
  }

  public SwappItemList(Collection<SwappItem> items) {
    this.addItem(items);
  }

  public void setSwappItemlist(SwappItemList swappList) {
    this.items = new ArrayList<>();
    addItem(swappList.getItems());
  }

  public void addItem(SwappItem... item) {
    this.items.addAll(List.of(item));
    fireSwappItemListChanged();
  }

  /**
  * Adds collection of SwapItems to SwappItemList.
  */
  public void addItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      this.addItem(item);
    }
  }

  public void removeItem(SwappItem... item) {
    this.items.removeAll(List.of(item));
    fireSwappItemListChanged();
  }

  /**
  * Removes multiple SwappItems from SwappItemList.
  */
  public void removeItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      this.removeItem(item);
    }
  }

  public SwappItemList putSwappList(SwappItemList other){
    if (!getItems().equals(other.getItems())){
      setSwappItemlist(other);
    }return this; 
  }

  public SwappItem getSwappItem(SwappItem swappItem){
    if (items.contains(swappItem)) return swappItem;  
    else return null;
  }

  public boolean hasSwappItem(String name){
    return getSwappItem(name)!=null;
  }

  public SwappItem getSwappItem(String name){
    return items.stream().filter(x->x.getName().equals(name)).findFirst().get();
  }

  public List<SwappItem> getItems() {
    return this.items;
  }

  public SwappItemList getSwappList(){
    return this;
  }

  public boolean isvalidName(String name){
    return !name.isBlank();
  }

  public void addSwappItemListListener(SwappItemListListener listener) {
    swappItemListListeners.add(listener);
  }

  public void removeSwappItemListListener(SwappItemListListener listener) {
    swappItemListListeners.remove(listener);
  }

  protected void fireSwappItemListChanged() {
    for (SwappItemListListener listener : swappItemListListeners) {
      listener.swappListChanged(this);
    }
  }

  @Override
  public Iterator<SwappItem> iterator() {
    return items.iterator();
  }

}
