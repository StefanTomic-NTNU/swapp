package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class SwappItemTest {

  private SwappItem item;

  @BeforeEach
  public void beforeEach() {
    item = new SwappItem("name", "username", "New", "description");
  }

  @Test
  public void testIllegalName() {
    assertThrows(IllegalArgumentException.class, () -> {
      item = new SwappItem(" ", "username");
    });
  }

  @Test
  public void testIllegalStatus() {
    assertThrows(IllegalArgumentException.class, () -> {
      item = new SwappItem("name", "username", "illegalStatus", "description");
    });
  }

  @Test
  public void testGetName() {
    assertEquals(item.getName(), "name");
  }

  @Test
  public void username() {
    assertEquals(item.getUsername(), "username");
  }

  @Test
  public void testGetStatus() {
    assertEquals(item.getStatus(), "New");
  }

  @Test
  public void testGetDescription() {
    assertEquals(item.getDescription(), "description");
  }

  @Test
  public void testSetDescription() {
    item.setDescription("description");
    assertEquals("description", item.getDescription());
    item.setDescription(null);
    assertEquals(SwappItem.defaultDescription, item.getDescription());
  }


  @Test
  public void testToString() {
    SwappItem item = new SwappItem("name", "username");
    assertEquals(item.getName() + "    " + item.getStatus() + "  " + item.getDescription() + "  " + item.getUsername(),
        item.toString());
  }

  @Test
  public void nameEquals() {
    SwappItem item2 = new SwappItem("name", "username2");
    SwappItem item3 = new SwappItem("differentName", "username3");
    assertTrue(item.nameEquals(item2));
    assertFalse(item.nameEquals(item3));
  }

  @Test
  public void testAllAttributesEquals() {
    SwappItem item2 = new SwappItem("name", "username", "New", "description");
    SwappItem item3 = new SwappItem("name", "username3", "New", "differentDescription");
    assertTrue(item.allAttributesEquals(item2));
    assertFalse(item.allAttributesEquals(item3));
  }

}
