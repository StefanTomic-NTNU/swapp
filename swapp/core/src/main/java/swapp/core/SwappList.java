package swapp.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SwappList implements Iterable<SwappItem> {

  private List<SwappItem> swappItems = new ArrayList<>();
  private Collection<SwappListListener> swappListListeners = new ArrayList<>();
  private String username;

  public SwappList(String username) {
    this.username = username;
  }

  public SwappList(Collection<SwappItem> items) {
    this.username = items.stream().findFirst().get().getUsername();
    this.addSwappItem(items);
  }

  public SwappList(SwappItem ... items) {
    this.username = List.of(items).stream().findFirst().get().getUsername();
    this.addSwappItem(List.of(items));
  }

  public void addSwappItem(SwappItem newItem){
    if (!hasSwappItem(newItem)){
      swappItems.add(newItem);
      fireSwappListChanged();
    }else throw new IllegalArgumentException();
  }

  public void addSwappItem(Collection<SwappItem> items){
    for (SwappItem item : items){
      if (hasSwappItem(item)) throw new IllegalArgumentException();
      else this.swappItems.add(item);
    }
    fireSwappListChanged();
  }

  public void removeSwappItem(SwappItem item){
    if (hasSwappItem(item)){ 
      swappItems.remove(item);
      fireSwappListChanged();
    }
  }

  public void removeSwappItem(String item){
    if (hasSwappItem(item)){ 
      swappItems.remove(getSwappItem(item));
      fireSwappListChanged();
    }
  }

  public void removeSwappItem(Collection<SwappItem> items) {
    for (SwappItem item : items){
      if (hasSwappItem(item)){
        swappItems.remove(item);
      }
    }
    fireSwappListChanged();
  }

  public boolean hasSwappItem(String name){
    try {
      return getSwappItem(name)!=null;
    } catch (NoSuchElementException e) {return false;}
  }

  public boolean hasSwappItem(SwappItem item){
    try {
      return getSwappItem(item.getName())!=null;
    } catch (NoSuchElementException e) {return false;}
  }

  public SwappItem getSwappItem(SwappItem item) {
    return getSwappItem(item.getName());
  }

  public SwappItem getSwappItem(String name) {
    return swappItems.stream().filter(p->p.nameEquals(name)).findAny().get();
  }

  public boolean isItemChanged(SwappItem newItem){
    if (!swappItems.stream().anyMatch(p->p.allAttributesEquals(newItem))){
      return true;
    }
    return false;
  }

  public List<SwappItem> getSwappItems() {
    return this.swappItems;
  }

  public SwappItem createAndAddSwappItem(String name, String username, String status, String infoString){
    SwappItem newItem = new SwappItem(name, username, status,  infoString);
    addSwappItem(newItem);
    return newItem;
  }

  public List<SwappItem> getSwappItemsByStatus(String status) {
    if (status.equals("All")) return getSwappItems();
    return this.swappItems.stream().filter(p->p.getStatus().equals(status)).collect(Collectors.toList());
  }

  public void changeSwappItem(SwappItem oldItem, SwappItem newItem){
    if (hasSwappItem(oldItem) && isItemChanged(newItem)){
      oldItem.setStatus(newItem.getStatus());
      oldItem.setDescription(newItem.getDescription());
      fireSwappListChanged();
    }
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

  public String getUsername(){
    return this.username;
  }

}
