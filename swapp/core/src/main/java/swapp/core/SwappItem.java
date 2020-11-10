package swapp.core;


public class SwappItem {

  private String name;
  private String status;
  private String description;
  private String contactInfo;
  public final static String defaultStatus = "New";
  public final static String defaultDescription = "";
  public final static String defaultContactInfo = "anonymous@email.com";
  

  public SwappItem(String name, String status, String description, String contactInfo) {
    this.setName(name);
    this.setStatus(status);
    this.setDescription(description); 
    this.setContactInfo(contactInfo);
  }

  public SwappItem(String name) {
    this.setName(name);
    this.setStatus(defaultStatus);
    this.setDescription(defaultDescription); 
    this.setContactInfo(defaultContactInfo);
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
    if (!name.isBlank()) {
      this.name = name;
    }
    else {
      throw new IllegalArgumentException("Name cannot be blank");
    }
  }

  public void setStatus(String status) {
    if (status.equals("New") || status.equals("Used") || status.equals("Damaged")) {
      this.status = status;
    } else {
      throw new IllegalArgumentException("Status must be either 'New', 'Used', or 'Damaged'");
    }
  }

  public void setDescription(String description) {
    if (!(description == null)) {
      this.description = description;
    } else {
      this.description = "";
    }
  }

  public void setContactInfo(String contactInfo) {
    if (!(contactInfo == null)) {
      this.contactInfo = contactInfo;
    } else {
      this.contactInfo = "";
    }
  }

  public boolean allAttributesEquals(String name, String status, String description, String contactInfo) {
    return (name.equals(this.name) && 
      status.equals(this.status) &&
      description.equals(this.description) &&
      contactInfo.equals(this.contactInfo));
  }

  public boolean allAttributesEquals(SwappItem other) {
    return allAttributesEquals(other.getName(), other.getStatus(), other.getDescription(), other.getContactInfo());
  }

  public boolean nameEquals(String name) {
    return getName().equals(name);
  }

  public boolean nameEquals(SwappItem other) {
    return nameEquals(other.getName());
  }

  @Override
  public String toString() {
    return name + "    " + status + "  " + description + "  " + contactInfo;
  }

  /*
  //TODO se på Equals metode
  //Virker som at spotbugs ønsker at equals sammenligner hashkoden til objektene. Dette ser også ut til å være god kodeskikk
  //Tror at måten det er satt opp nå vil to objekter med samme navn få samme hash uansett hva de andre param. er.
  //Vet ikke om dette er så lurt..
  //Men sånn som koden er satt opp nå er f.eks SwappItemSerializer avhengig av at equals gir true hviss name er likt.

  @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof SwappItem)) return false;
    SwappItem swappOther = (SwappItem) other;
    if ((this.getName() == null) ? (swappOther.getName() != null) : !this.getName().equals(swappOther.getName())) {
      return false;
    }
    return true;
  }
  */

}
