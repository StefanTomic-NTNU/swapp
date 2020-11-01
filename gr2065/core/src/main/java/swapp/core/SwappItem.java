package swapp.core;

public class SwappItem {

  private String name;
  private String status;
  private String description;
  private String contactInfo;

  /**
  * Creates a new SwappItem with the given name.
  */
  public SwappItem(String name, String status, String description, String contactInfo) {
    this.setName(name);
    this.setStatus(status);
    this.setDescription(description); 
    this.setContactInfo(contactInfo);
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

  public String getContactInfo() {
    return contactInfo;
  }

  public void setName(String name) {
    if (!name.isBlank()) {this.name = name;}
  }

  public void setStatus(String status) {
    if (status.equals("Ny") || status.equals("Litt brukt") || status.equals("Godt brukt")) {
      this.status = status;
    }
  }

  public void setDescription(String description) {
    if (!description.isBlank()) {this.description = description;}
  }

  public void setContactInfo(String contactInfo) {
    if (!contactInfo.isBlank()) {this.contactInfo = contactInfo;}
  }

  @Override
  public String toString() {
    return name + "  " + "  " + status + "  " + description + "  " + contactInfo;
  }


  public static void main(String[] args) {
    SwappItem item = new SwappItem("Toyota", "Lager rare lyder ved oppstart", "Til salgs", "magdi@gmail.com");
    System.out.println(item);
  }
}
