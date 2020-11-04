package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class SwappItemTest {

  private SwappItem item1;

  @BeforeEach
  public void beforeEach() {
    item1 = new SwappItem("name", "New", "description", "contactInfo");
  }

  @Test
  public void testBlankName() {
    assertThrows(IllegalArgumentException.class, () -> {
        SwappItem item2 = new SwappItem(" ");
    });
  }

  @Test
  public void testGetName() {
    assertEquals(item1.getName(), "name");
  }

  @Test
  public void testGetStatus() {
    assertEquals(item1.getStatus(), "New");
  }

  @Test
  public void testGetDescription() {
    assertEquals(item1.getDescription(), "description");
  }

  @Test
  public void testGetContactInfo() {
    assertEquals(item1.getContactInfo(), "contactInfo");
  }

  
    @Test
    public void testToString(){
        SwappItem item = new SwappItem("name");
        assertEquals(item.getName() + "  " + "  " + 
        item.getStatus() + "  " + item.getDescription() + 
        "  " + item.getContactInfo(), item.toString());
    }

    @Test
    public void testEquals(){
        SwappItem item1 = new SwappItem("name", "New", "description1", "contactInfo1");
        SwappItem item2 = new SwappItem("name", "New", "description2", "contactInfo2");
        SwappItem item3 = new SwappItem("name3", "New", "description2", "contactInfo1");
        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }
    
}
