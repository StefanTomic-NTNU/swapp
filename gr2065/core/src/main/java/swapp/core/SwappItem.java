package swapp.core;

public class SwappItem {

  private String name;

  /**
  * Creates a new SwappItem with the given name.
  */
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
