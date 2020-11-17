package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SwappListTest {

  private SwappList swappList;

  @BeforeEach
  public void setUp() {
    swappList = new SwappList("username");
  }

  @Test
  public void testCreateAndAddItem() {
    swappList.createAndAddSwappItem("name", "username", "New", "infoString");
    swappList.createAndAddSwappItem("name2", "username", "New", "infoString2");
    Iterator<SwappItem> it = swappList.iterator();
    assertTrue(it.hasNext());
    assertEquals(it.next().getName(), "name");
    assertTrue(it.hasNext());
    assertEquals(it.next().getName(), "name2");
    assertFalse(it.hasNext());
  }

  @Test
  public void testGetSwappItemsByStatus() {
    swappList.createAndAddSwappItem("name", "username", "New", "infoString");
    swappList.createAndAddSwappItem("name2", "username", "Used", "infoString2");
    swappList.createAndAddSwappItem("name3", "username", "New", "infoString");
    swappList.createAndAddSwappItem("name4", "username", "New", "infoString2");
    List<SwappItem> usedItems = swappList.getSwappItemsByStatus("Used");
    assertEquals(usedItems.size(), 1);
    assertEquals(usedItems.get(0).getName(), "name2");
    List<SwappItem> newItems = swappList.getSwappItemsByStatus("New");
    assertEquals(newItems.size(), 3);
    assertEquals(newItems.get(0).getName(), "name");
    assertEquals(newItems.get(1).getName(), "name3");
  }

  @Test
  public void hasSwappItem() {
    SwappItem item1 = new SwappItem("name", "username", "New", "info");
    SwappItem item2 = new SwappItem("name2", "username", "New", "info");
    swappList.addSwappItem(item1);
    assertTrue(swappList.hasSwappItem(item1.getName()));
    assertFalse(swappList.hasSwappItem(item2.getName()));
  }

  @Test
  public void testGetSwappItem() {
    SwappItem item1 = new SwappItem("name", "username", "New", "info");
    SwappItem item2 = new SwappItem("name2", "username", "New", "info");
    SwappItem item3 = new SwappItem("name3", "username", "New", "info");
    swappList.addSwappItem(item1);
    swappList.addSwappItem(item2);
    SwappItem item4 = new SwappItem("name", "username", "New", "info");
    SwappItem item5 = swappList.getSwappItem(item4);
    assertTrue(item5.allAttributesEquals(item1));
    assertFalse(item5.allAttributesEquals(item2));
  }

  @Test
  public void isItemChanged() {
    SwappItem item1 = new SwappItem("name", "username", "New", "info");
    swappList.addSwappItem(item1);
    SwappItem item2 = new SwappItem("name", "username", "New", "info");
    SwappItem item3 = new SwappItem("name", "username", "Used", "new_info");
    assertFalse(swappList.isItemChanged(item2));
    assertTrue(swappList.isItemChanged(item3));
  }

  @Test
  public void testChangeSwappItem() {
    SwappItem item1 = new SwappItem("name", "username", "New", "info");
    SwappItem item2 = new SwappItem("name", "username", "Damaged", "new info");
    swappList.addSwappItem(item1);
    assertTrue(swappList.getSwappItem(item1.getName()).allAttributesEquals(item1));
    swappList.changeSwappItem(item1, item2);
    assertTrue(swappList.getSwappItem(item1.getName()).allAttributesEquals(item2));
  }

  private int receivedNotificationCount = 0;

  @Test
  public void testFireswappListChanged_addSwappItemAndReceiveNotification() {
    SwappItem item1 = new SwappItem("name", "username", "New", "info");
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

  // Mockito test
  @Test
  public void testFireswappListChanged_addSwappItemAndMockReceiveNotification() {
    SwappItem item1 = new SwappItem("name", "username", "New", "info");
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
    SwappItem item1 = new SwappItem("name", "username", "New", "info");
    SwappItem item2 = new SwappItem("name2", "username", "Damaged", "new info");
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
