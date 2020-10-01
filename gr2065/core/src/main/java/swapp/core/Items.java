package swapp.core;

import static java.util.Collections.addAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Items {

  final List<Item> items = new ArrayList<>();

  public Items() {

  }

  public Items(final Item... items) {
    this.addItem(items);
  }

  public Items(final Collection<Item> items) {
    this.items.addAll(items);
  }

  public void addItem(Item item) {
    this.items.add(item);
  }

  public void addItem(Item... item) {
    this.items.addAll(List.of(item));
  }

  public void removeItem(Item item) {
    this.items.remove(item);
  }

  public List<Item> getItems() {
    return this.items;
  }
}
