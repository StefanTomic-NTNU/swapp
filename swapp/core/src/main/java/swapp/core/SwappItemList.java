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
    this.addSwappItem(items);
  }

  public SwappItemList(Collection<SwappItem> items) {
    this.addSwappItem(items);
  }

  public void setSwappItemList(SwappItemList swappList) {
    this.items = new ArrayList<>();
    addSwappItem(swappList.getSwappItems());
  }

  public void addSwappItem(SwappItem... item) {

    this.items.addAll(List.of(item));
    fireSwappItemListChanged();
  }

  /**
  * Adds collection of SwapItems to SwappItemList.
  */
  public void addSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      if (!this.items.contains(item)) {
        this.addSwappItem(item);
      } else {throw new IllegalArgumentException("Duplicate SwappItem in SwappItemList");}
    }
  }

  public void removeSwappItem(SwappItem... item) {
    this.items.removeAll(List.of(item));
    fireSwappItemListChanged();
  }

  /**
  * Removes multiple SwappItems from SwappItemList.
  */
  public void removeSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      this.removeSwappItem(item);
    }
  }

  public void removeSwappItem(String name) {
    int j=0;
    for (int i=0; i<this.items.size(); i++) {
      if (items.get(i).getName().equals(name)) {
        j=i;
      }
    }
    this.items.remove(j);
    fireSwappItemListChanged();
  }

  public SwappItemList putSwappItemList(SwappItemList other){
    if (!getSwappItems().equals(other.getSwappItems())){
      setSwappItemList(other);
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

  public List<SwappItem> getSwappItems() {
    return this.items;
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

  public List<SwappItem> getSwappItemsByStatus(String status) {
    if (status.equals("All")) return getSwappItems();
    return this.items.stream().filter(s->s.getStatus().equals(status)).collect(Collectors.toList());
  }

  @Override
  public Iterator<SwappItem> iterator() {
    return items.iterator();
  }

}
