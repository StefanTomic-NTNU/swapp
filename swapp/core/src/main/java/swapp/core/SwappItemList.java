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


  public void addSwappItem(SwappItem... item) {
    for (SwappItem newItem : List.of(item)) {
      for (SwappItem oldItem : this.items) {
        if (newItem.nameEquals(oldItem)) {
          throw new IllegalArgumentException("Duplicate SwappItem in SwappItemList");
        }
      }
    }
    this.items.addAll(List.of(item));
    fireSwappItemListChanged();
  }

  public void addSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items) {
      addSwappItem(item);
    }
  }


  public void setSwappItemList(SwappItemList items) {
    this.items.clear();
    addSwappItem(items.getSwappItems());
  }

  public SwappItemList putSwappItemList(SwappItemList other){
    setSwappItemList(other);
    return this; 
  }


  public void removeSwappItem(SwappItem... item) {
    removeSwappItem(List.of(item));
  }

  public void removeSwappItem(Collection<SwappItem> items) {
    this.items.removeAll(items);
    fireSwappItemListChanged();
  }

  public void removeSwappItem(String name) {
    int j=-1;
    for (int i=0; i<this.items.size(); i++) {
      if (items.get(i).getName().equals(name)) {
        j=i;
      }
    } 
    if (j != -1) {
    this.items.remove(j);
    fireSwappItemListChanged();
    }
  }


  public List<SwappItem> getSwappItems() {
    return this.items;
  }

  public SwappItem getSwappItem(SwappItem item) {
    return getSwappItem(item.getName());
  }

  public SwappItem getSwappItem(String name) {
    return items.stream().filter(x->x.nameEquals(name)).findFirst().get();
  }

  public boolean hasSwappItem(String name){
    return getSwappItem(name)!=null;
  }

  public List<SwappItem> getSwappItemsByStatus(String status) {
    if (status.equals("All")) return getSwappItems();
    return this.items.stream().filter(s->s.getStatus().equals(status)).collect(Collectors.toList());
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
