package swapp.core;

public class SwappItem {

  private String name;

  public SwappItem(String name) {
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
