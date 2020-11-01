package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class SwappItemTest {

  private SwappItem item1;

  @BeforeEach
  public void beforeEach() {
    item1 = new SwappItem("name", "Gis bort", "description", "contactInfo");
  }

  @Test
  public void testBlankName() {
    SwappItem item2 = new SwappItem(" ", " "," "," ");
    assertEquals(item2.getName(), null);
  }

  @Test
  public void testGetName() {
    assertEquals(item1.getName(), "name");
  }

  @Test
  public void testGetStatus() {
    assertEquals(item1.getStatus(), "Gis bort");
  }

  @Test
  public void testGetDescription() {
    assertEquals(item1.getDescription(), "description");
  }

  @Test
  public void testGetContactInfo() {
    assertEquals(item1.getContactInfo(), "contactInfo");
  }

  /*
    @Test
    public void testToString(){
        SwappItem item = new SwappItem("name");
        assertEquals("name", item.toString());
    }
    */
}
