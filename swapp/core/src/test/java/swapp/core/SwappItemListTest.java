package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;

import java.util.List;
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
    item1 = new SwappItem("name1", "Ny", "description1", "contactInfo1");
    item2 = new SwappItem("name2", "Ny", "description2", "contactInfo2");
  }

  @Test
  public void testCreateEmptyItemList() {
    List<SwappItem> itemslist = swappItemList.getItems();
    assertEquals(itemslist.size(), 0);
  }


  @Test
  public void testCreatePopulatedItemList() {
    //SwappItem item1 = new SwappItem("name1", "status1", "description1", "contactInfo1");
    //SwappItem item2 = new SwappItem("name2", "status2", "description2", "contactInfo2");
    swappItemList.addItem(item1, item2);
    List<SwappItem> itemslist = swappItemList.getItems();
    assertEquals(itemslist.get(0).getName(), "name1");
    assertEquals(itemslist.get(1).getName(), "name2");
  }

  @Test
  public void testCreateListPopulatedItemList() {
    //SwappItem item1 = new SwappItem("name1", "status1", "description1", "contactInfo1");
    //SwappItem item2 = new SwappItem("name2", "status2", "description2", "contactInfo2");
    SwappItemList swappItemList2 = new SwappItemList(item1, item2);
    List<SwappItem> itemslist = swappItemList2.getItems();
    assertEquals(itemslist.get(0).getName(), "name1");
    assertEquals(itemslist.get(1).getName(), "name2");
  }

  @Test
  public void testGetItems() {
    //SwappItem item1 = new SwappItem("name1", "status1", "description1", "contactInfo1");
    swappItemList.addItem(item1);
    assertEquals(swappItemList.getItems().get(0).getName(), "name1");
  }

  @Test
  public void testSetSwappItemList() {
    //SwappItem item1 = new SwappItem("name1", "status1", "description1", "contactInfo1");
    //SwappItem item2 = new SwappItem("name2", "status2", "description2", "contactInfo2");
    swappItemList.setSwappItemlist(new SwappItemList(item1, item2));
    assertEquals(swappItemList.getItems().get(0).getName(), "name1");
    assertEquals(swappItemList.getItems().get(1).getName(), "name2");
  }

  @Test
  public void testAddOneItem() {
    //SwappItem item = new SwappItem("name");
    swappItemList.addItem(item1);
    List<SwappItem> itemlist = new ArrayList<>();
    itemlist.add(item1);
    assertEquals(swappItemList.getItems(), itemlist);
  }

  @Test
  public void testAddMultipleItems() {
    //SwappItem item1 = new SwappItem("item1");
    //SwappItem item2 = new SwappItem("item2");
    swappItemList.addItem(item1, item2);
    List<SwappItem> itemlist = new ArrayList<>();
    itemlist.add(item1);
    itemlist.add(item2);
    assertEquals(swappItemList.getItems(), itemlist);
  }

  @Test
  public void testRemoveItem() {
    //SwappItem item1 = new SwappItem("item1");
    swappItemList.addItem(item1);
    swappItemList.removeItem(item1);
    assertEquals(swappItemList.getItems().size(), 0);;
  }

  @Test
  public void testRemoveMultipleItems() {
    //SwappItem item1 = new SwappItem("item1");
    //SwappItem item2 = new SwappItem("item2");
    swappItemList.addItem(item1, item2);
    List<SwappItem> itemlist = new ArrayList<>();
    itemlist.add(item1);
    itemlist.add(item2);
    swappItemList.removeItem(itemlist);
    assertEquals(swappItemList.getItems().size(), 0);
  }

  private int receivedNotificationCount = 0;

  @Test
  public void testFireSwappItemListChanged_addItemAndReceiveNotification() {
    swappItemList.addSwappItemListListener(list -> {
      receivedNotificationCount++;
    });
    assertEquals(0, receivedNotificationCount);
    //SwappItem item = new SwappItem("name");
    swappItemList.addItem(item1);
    assertEquals(1, receivedNotificationCount);
    assertEquals(swappItemList.getItems().size(), 1);
    swappItemList.removeItem(item1);
    assertEquals(2, receivedNotificationCount);
    assertEquals(swappItemList.getItems().size(), 0);
  }

  // Mockito test
  @Test
  public void testFireSwappItemListChanged_addItemAndMockReceiveNotification() {
    SwappItemListListener listener = mock(SwappItemListListener.class);
    swappItemList.addSwappItemListListener(listener);
    verify(listener, times(0)).swappListChanged(swappItemList);
    //SwappItem item = new SwappItem("name");
    swappItemList.addItem(item1);
    verify(listener, times(1)).swappListChanged(swappItemList);
    swappItemList.removeItem(item1);
    verify(listener, times(2)).swappListChanged(swappItemList);
  }

  @Test
  public void testRemoveSwappItemListListener() {
    //SwappItemList swappItemList = new SwappItemList();
    SwappItemListListener listener = list -> {
      receivedNotificationCount++;
    };
    //SwappItem item1 = new SwappItem("item1");
    //SwappItem item2 = new SwappItem("item2");
    swappItemList.addSwappItemListListener(listener);
    assertTrue(receivedNotificationCount == 0);
    swappItemList.addItem(item1);
    assertTrue(receivedNotificationCount == 1);
    swappItemList.removeSwappItemListListener(listener);
    swappItemList.addItem(item2);
    assertTrue(receivedNotificationCount == 1);
  }
  

  
  @Test
  public void testIterator() {
    //SwappItem item1 = new SwappItem("item1");
    //SwappItem item2 = new SwappItem("item2");
    swappItemList.addItem(item1, item2);
    for (SwappItem item : swappItemList) {
      assertTrue(item.getName().equals("name1") || item.getName().equals("name2"));
    }
  }
  
}
