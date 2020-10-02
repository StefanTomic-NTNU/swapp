package swapp.core;

public class Item {

  private String name;

  public Item(String name) {
    if (!name.isBlank()) {
      this.name = name;
    }

  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
