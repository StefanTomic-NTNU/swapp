package swapp.core;

public class SwappItem {

  
  /**
   * Name of the SwappItem.
   */
  private String name;
  /**
   * Current status of SwappItem. May only be "New", "Used" or "Damaged"
   */
  private String status;
  /**
   * A longer description of the SwappItem.
   */
  private String description;
  private String username;

  /**
   * Default status which is used when status isn't specified in constructor.
   */
  public static final String defaultStatus = "New";
  /**
   * Default description which is used when status isn't specified in constructor.
   */
  public static final String defaultDescription = "No info";
  

  /**
   * Constructs SwappItem. 
   * 
   * <p>Attributes are set by setters which may throw exceptions.
   *
   * @param name        name of SwappItem
   * @param username    username associated with SwappItem
   * @param status      status of SwappItem
   * @param description description of SwappItem
   */
  public SwappItem(String name, String username, String status, String description) {
    this.setName(name);
    this.setUsername(username);
    this.setStatus(status);
    this.setDescription(description);
  }

  /**
   * Constructs new SwappItem with default status and description.
   * 
   * <p>Default status is "New" and default description is "No info".
   *
   * @param name     SwappItem name
   * @param username Username of SwappItem owner
   */
  public SwappItem(String name, String username) {
    this.setName(name);
    this.setStatus(defaultStatus);
    this.setDescription(defaultDescription);
    this.setUsername(username);
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public String getDescription() {
    return description;
  }

  public String getUsername() {
    return username;
  }

  /**
   * Sets new SwappItem name.
   *
   * @param name New name
   * @throws IllegalArgumentException Name cannot be blank
   */
  public void setName(String name) {
    if (!name.isBlank()) {
      this.name = name.replaceAll("\\s", "");
    } else {
      throw new IllegalArgumentException("Name cannot be blank");
    }
  }

  /**
   * Sets SwappItem status.
   *
   * @param status String which the status is set to
   * @throws IllegalArgumentException Status must be either 'New', 'Used', or 'Damaged'
   */
  public void setStatus(String status) {
    if (status.equals("New") || status.equals("Used") || status.equals("Damaged")) {
      this.status = status;
    } else {
      throw new IllegalArgumentException("Status must be either 'New', 'Used', or 'Damaged'");
    }
  }

  /**
   * Sets new SwappItem description
   *
   * <p>If input is null, the description is set to default description "No info".
   *
   * @param description String which the description is set to
   */
  public void setDescription(String description) {
    if (!(description == null)) {
      this.description = description;
    } else {
      this.description = defaultDescription;
    }
  }

  /**
   * Sets the username of the SwappItem.
   *
   * @param username String which the username is set to
   */
  public void setUsername(String username) {
    if (!(username == null) && !username.isEmpty()) {
      this.username = username.replaceAll("\\s", "");;
    }
  }

  /**
   * Compares all of a SwappItems attributes to the corresponing parameters.
   *
   * @param name        String which is compared to the SwappItem's name
   * @param status      String which is compared to the SwappItem's status
   * @param description String which is compared to the SwappItem's description
   * @param username    String which is compared to the SwappItem's username
   * @return True if all the attributes match the corresponding parameters, otherwise false
   */
  public boolean allAttributesEquals(
      String name, String status, String description, String username) {
    return (name.equals(this.name) 
      && status.equals(this.status) 
      && description.equals(this.description)
      && username.equals(this.username));
  }


  /**
   * Compares all of a SwappItems attributes to attributes of another SwappItem.
   *
   * @param other SwappItem with which to compare.
   * @return True if all the attributes match, otherwise false.
   */
  public boolean allAttributesEquals(SwappItem other) {
    return allAttributesEquals(
      other.getName(), other.getStatus(), other.getDescription(), other.getUsername());
  }

  /**
   * Compares the name of a SwappItems to a String.
   *
   * @param name String with which to compare the SwappItem's name
   * @return True if the names match. False if they don't.
   */
  public boolean nameEquals(String name) {
    return getName().equals(name);
  }

  /**
   * Compares the names of two SwappItems.
   *
   * @param other SwappItem with which to compare.
   * @return True if the names match. False if they don't.
   */
  public boolean nameEquals(SwappItem other) {
    return nameEquals(other.getName());
  }

  @Override
  public String toString() {
    return name + "    " + status + "  " + description + "  " + username;
  }


}
