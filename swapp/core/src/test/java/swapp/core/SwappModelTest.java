package swapp.core;

import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SwappModelTest {

  private SwappModel swappModel;
  private SwappList list1, list2;
  private SwappItem item1, item2, item3;

  @BeforeEach
  public void setUp() {
    swappModel = new SwappModel();
    list1 = new SwappList("username1");
    list2 = new SwappList("username2");
    item1 = new SwappItem("name1", "username1");
    item2 = new SwappItem("name2", "username2");
    item3 = new SwappItem("name3", "username2");
  }

  @Test
  public void testGetSwappItems() {
    swappModel.addSwappList(list1);
    swappModel.addSwappList(list2);
    swappModel.addSwappItem(item1);
    swappModel.addSwappItem(item2);
    swappModel.addSwappItem(item3);
    List<SwappItem> items = swappModel.getSwappItems();
    assertEquals(items.size(), 3);
    assertEquals("name1", items.get(0).getName());
    assertEquals("name2", items.get(1).getName());
    assertEquals("name3", items.get(2).getName());
  }

  @Test
  public void testGetSwappItemsByStatus() {
    swappModel.addSwappList(list1);
    swappModel.addSwappList(list2);
    SwappItem item1 = new SwappItem("name1", "username1", "Used", "description");
    SwappItem item2 = new SwappItem("name2", "username2", "New", "description");
    SwappItem item3 = new SwappItem("name3", "username2", "Used", "description");
    swappModel.addSwappItem(item1);
    swappModel.addSwappItem(item2);
    swappModel.addSwappItem(item3);
    List<SwappItem> items = swappModel.getSwappItemsByStatus("Used");
    assertEquals(2, items.size());
    assertEquals("name1", items.get(0).getName());
    assertEquals("name3", items.get(1).getName());
  }

  @Test
  public void testAddSwapplist() {
    SwappModel swappModel = new SwappModel();
    list1.addSwappItem(item1);
    list2.addSwappItem(item2);
    list2.addSwappItem(item3);
    swappModel.addSwappList(list1);
    swappModel.addSwappList(list2);
    Iterator<SwappList> it = swappModel.iterator();
    assertTrue(it.hasNext());
    it.next();
    it.next();
    assertFalse(it.hasNext());
  }


  @Test
  public void testHasSwappList() {
    swappModel.addSwappList(list1);
    String username = list1.getUsername();
    assertTrue(swappModel.hasSwappList(username));
    assertFalse(swappModel.hasSwappList(list2.getUsername()));
  }

  @Test
  public void testIsValidName() {
    swappModel.addSwappList(list1);
    assertTrue(swappModel.isValidName(list2.getUsername()));
    assertFalse(swappModel.isValidName(list1.getUsername()));
  }

  @Test
  public void testChangeSwappItemAndIsChanged() {
    SwappItem oldItem = new SwappItem("name", "username1", "New", "oldDescription");
    SwappItem newItem = new SwappItem("name", "username1", "New", "newDescription");
    list1.addSwappItem(oldItem);
    swappModel.addSwappList(list1);
    assertFalse(list1.getSwappItems().get(0).allAttributesEquals(newItem));
    assertTrue(swappModel.isItemChanged(newItem));
    swappModel.changeSwappItem("username1", oldItem, newItem);
    assertTrue(list1.getSwappItems().get(0).allAttributesEquals(newItem));
    assertFalse(swappModel.isItemChanged(newItem));
  }


}
