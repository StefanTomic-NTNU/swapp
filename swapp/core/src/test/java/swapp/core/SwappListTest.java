package swapp.core;

import java.util.List;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SwappListTest {

  private SwappList swappList;
  private SwappItem item1, item2;

  @BeforeEach
  public void setUp() {
    swappList = new SwappList("username");
    item1 = new SwappItem("name1", "username");
    item2 = new SwappItem("name2", "username");
  }

  @Test
  public void testCreateAndAddItem() {
    swappList.createAndAddSwappItem("name3", "username", "Used", "description");
    swappList.createAndAddSwappItem("name4", "username", "New", "description");
    Iterator<SwappItem> it = swappList.iterator();
    assertTrue(it.hasNext());
    assertEquals(it.next().getName(), "name3");
    assertTrue(it.hasNext());
    assertEquals(it.next().getName(), "name4");
    assertFalse(it.hasNext());
  }

  @Test
  public void testAddTwoOfSameItem() {
    swappList.addSwappItem(item1);
    SwappItem item1Duplicate = new SwappItem("name1", "username");
    assertThrows(IllegalArgumentException.class, () -> {
      swappList.addSwappItem(item1Duplicate);
    });
  }

  @Test
  public void testAdditemWithDifferentUsername() {
    swappList.addSwappItem(item1);
    SwappItem illegalItem = new SwappItem("name1", "differentUsername");
    assertThrows(IllegalArgumentException.class, () -> {
      swappList.addSwappItem(illegalItem);
    });
  }


  @Test
  public void testGetSwappItemsByStatus() {
    swappList.createAndAddSwappItem("name1", "username", "New", "description");
    swappList.createAndAddSwappItem("name2", "username", "Used", "description");
    swappList.createAndAddSwappItem("name3", "username", "New", "description");
    List<SwappItem> usedItems = swappList.getSwappItemsByStatus("Used");
    assertEquals(1, usedItems.size());
    assertEquals("name2", usedItems.get(0).getName());
    List<SwappItem> newItems = swappList.getSwappItemsByStatus("New");
    assertEquals(2, newItems.size());
    assertEquals(newItems.get(0).getName(), "name1");
    assertEquals(newItems.get(1).getName(), "name3");
  }

  @Test
  public void hasSwappItem() {
    swappList.addSwappItem(item1);
    assertTrue(swappList.hasSwappItem(item1));
    assertFalse(swappList.hasSwappItem(item2));
  }

  @Test
  public void testGetSwappItem() {
    swappList.addSwappItem(item1);
    swappList.addSwappItem(item2);
    SwappItem item4 = new SwappItem("name1", "username");
    SwappItem gottenItem = swappList.getSwappItem(item4);
    assertTrue(gottenItem.allAttributesEquals(item1));
    assertFalse(gottenItem.allAttributesEquals(item2));
  }

  @Test
  public void isItemChanged() {
    swappList.addSwappItem(item1);
    SwappItem item3 = new SwappItem("name1", "username");
    SwappItem item4 = new SwappItem("differentName", "username");
    assertFalse(swappList.isItemChanged(item3));
    assertTrue(swappList.isItemChanged(item4));
  }

  @Test
  public void testChangeSwappItem() {
    SwappItem item1 = new SwappItem("name", "username", "New", "oldDescription");
    SwappItem item2 = new SwappItem("name", "username", "Damaged", "newDescription");
    swappList.addSwappItem(item1);
    assertTrue(swappList.getSwappItem(item1).allAttributesEquals(item1));
    swappList.changeSwappItem(item1, item2);
    assertTrue(swappList.getSwappItem(item1).allAttributesEquals(item2));
  }

  private int receivedNotificationCount = 0;

  @Test
  public void testFireswappListChanged_addSwappItemAndReceiveNotification() {
    SwappItem item1 = new SwappItem("name", "username", "New", "description");
    swappList.addSwappListListener(list -> {
      receivedNotificationCount++;
    });
    assertEquals(0, receivedNotificationCount);
    swappList.addSwappItem(item1);
    assertEquals(1, receivedNotificationCount);
    assertEquals(swappList.getSwappItems().size(), 1);
    swappList.removeSwappItem(item1);
    assertEquals(2, receivedNotificationCount);
    assertEquals(swappList.getSwappItems().size(), 0);
  }

  @Test
  public void testFireswappListChanged_addSwappItemAndMockReceiveNotification() {
    SwappItem item1 = new SwappItem("name", "username", "New", "description");
    SwappListListener listener = mock(SwappListListener.class);
    swappList.addSwappListListener(listener);
    verify(listener, times(0)).swappListChanged(swappList);
    swappList.addSwappItem(item1);
    verify(listener, times(1)).swappListChanged(swappList);
    swappList.removeSwappItem(item1);
    verify(listener, times(2)).swappListChanged(swappList);
  }

  @Test
  public void testRemoveswappListListener() {
    SwappItem item1 = new SwappItem("name", "username", "New", "description");
    SwappItem item2 = new SwappItem("name2", "username", "Damaged", "newDescription");
    SwappListListener listener = list -> {
      receivedNotificationCount++;
    };
    swappList.addSwappListListener(listener);
    assertTrue(receivedNotificationCount == 0);
    swappList.addSwappItem(item1);
    assertTrue(receivedNotificationCount == 1);
    swappList.removeSwappListListener(listener);
    swappList.addSwappItem(item2);
    assertTrue(receivedNotificationCount == 1);
  }
}
