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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SwappItemListTest {

  private SwappItemList swappItemList;
  private SwappItem item1;
  private SwappItem item2;

  @BeforeEach
  public void beforeEach() {
    swappItemList = new SwappItemList();
    item1 = new SwappItem("name1");
    item2 = new SwappItem("name2");
  }

  @Test
  public void testCreateEmptyItemList() {
    List<SwappItem> itemslist = swappItemList.getSwappItems();
    assertEquals(itemslist.size(), 0);
  }


  @Test
  public void testCreatePopulatedItemList() {
    swappItemList.addSwappItem(item1, item2);
    List<SwappItem> itemslist = swappItemList.getSwappItems();
    assertEquals(itemslist.get(0).getName(), "name1");
    assertEquals(itemslist.get(1).getName(), "name2");
  }

  @Test
  public void testCreateCollectionPopulatedItemList() {
    SwappItemList swappItemList2 = new SwappItemList(item1, item2);
    List<SwappItem> itemslist = swappItemList2.getSwappItems();
    assertEquals(itemslist.get(0).getName(), "name1");
    assertEquals(itemslist.get(1).getName(), "name2");
  }

  

  @Test
  public void testSetSwappItemList() {
    swappItemList.setSwappItemList(new SwappItemList(item1, item2));
    assertEquals(swappItemList.getSwappItems().get(0).getName(), "name1");
    assertEquals(swappItemList.getSwappItems().get(1).getName(), "name2");
  }

  @Test
  public void testSetSwappItemListWithDuplicate() {
    SwappItem item3 = new SwappItem("name1");
    assertThrows(IllegalArgumentException.class, () -> {
        swappItemList.setSwappItemList(new SwappItemList(item1, item3));
    });
  }


  @Test
  public void testAddSwappItems() {
    swappItemList.addSwappItem(item1, item2);
    List<SwappItem> itemlist = new ArrayList<>();
    itemlist.add(item1);
    itemlist.add(item2);
    assertEquals(swappItemList.getSwappItems(), itemlist);
  }


  @Test
  public void testRemoveSwappItems() {
    swappItemList.addSwappItem(item1, item2);
    List<SwappItem> itemlist = new ArrayList<>();
    itemlist.add(item1);
    itemlist.add(item2);
    swappItemList.removeSwappItem(itemlist);
    assertEquals(swappItemList.getSwappItems().size(), 0);
  }

  @Test
  public void testRemoveSwappItemByName() {
    swappItemList.addSwappItem(item1);
    swappItemList.removeSwappItem(item1.getName());
    assertThrows(NoSuchElementException.class, () -> {
        swappItemList.getSwappItem(item1.getName());
    });
    assertFalse(swappItemList.hasSwappItem(item1.getName()));
  }

  @Test
  public void testGetSwappItems() {
    swappItemList.addSwappItem(item1);
    assertEquals(swappItemList.getSwappItems().get(0).getName(), "name1");
  }

  @Test
  public void testGetAndHasSwappItem() {
    swappItemList.addSwappItem(item1);
    assertEquals(item1, swappItemList.getSwappItem(item1));
    assertTrue(swappItemList.hasSwappItem(item1.getName()));
    assertFalse(swappItemList.hasSwappItem(item2.getName()));
  }

  @Test
  public void testGetSwappItemsByStatus() {
    item1 = new SwappItem("name1", "New", "", "");
    item2 = new SwappItem("name2", "Used", "", "");
    swappItemList.addSwappItem(item1, item2);
    List<SwappItem> itemlist = new ArrayList<>();
    itemlist.add(item1);
    itemlist.add(item2);
    assertTrue(item1.allAttributesEquals(swappItemList.getSwappItemsByStatus("New").get(0)));
    assertEquals(itemlist, swappItemList.getSwappItemsByStatus("All"));
  }

  private int receivedNotificationCount = 0;

  @Test
  public void testFireSwappItemListChanged_addSwappItemAndReceiveNotification() {
    swappItemList.addSwappItemListListener(list -> {
      receivedNotificationCount++;
    });
    assertEquals(0, receivedNotificationCount);
    //SwappItem item = new SwappItem("name");
    swappItemList.addSwappItem(item1);
    assertEquals(1, receivedNotificationCount);
    assertEquals(swappItemList.getSwappItems().size(), 1);
    swappItemList.removeSwappItem(item1);
    assertEquals(2, receivedNotificationCount);
    assertEquals(swappItemList.getSwappItems().size(), 0);
  }

  // Mockito test
  @Test
  public void testFireSwappItemListChanged_addSwappItemAndMockReceiveNotification() {
    SwappItemListListener listener = mock(SwappItemListListener.class);
    swappItemList.addSwappItemListListener(listener);
    verify(listener, times(0)).swappListChanged(swappItemList);
    swappItemList.addSwappItem(item1);
    verify(listener, times(1)).swappListChanged(swappItemList);
    swappItemList.removeSwappItem(item1);
    verify(listener, times(2)).swappListChanged(swappItemList);
  }

  @Test
  public void testRemoveSwappItemListListener() {
    SwappItemListListener listener = list -> {
      receivedNotificationCount++;
    };
    swappItemList.addSwappItemListListener(listener);
    assertTrue(receivedNotificationCount == 0);
    swappItemList.addSwappItem(item1);
    assertTrue(receivedNotificationCount == 1);
    swappItemList.removeSwappItemListListener(listener);
    swappItemList.addSwappItem(item2);
    assertTrue(receivedNotificationCount == 1);
  }
  

  
  @Test
  public void testIterator() {
    swappItemList.addSwappItem(item1, item2);
    for (SwappItem item : swappItemList) {
      assertTrue(item.allAttributesEquals(item1) || item.allAttributesEquals(item2));
    }
  }
  
}
