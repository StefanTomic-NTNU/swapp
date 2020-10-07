package swapp.json;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import swapp.core.SwappItem;
import swapp.core.SwappItemList;

public class SwappItemListModuleTest {

  // {"items":[{"text":"item1","checked":false},{"text":"item2","checked":true}]}

  private static ObjectMapper mapper;

  @BeforeAll
  public static void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new SwappItemModule());
  }

  private final static String ItemsListWithTwoItems = "[{\"itemName\":\"item1\"},{\"itemName\":\"item2\"}]";

  @Test
  public void testSerializers() {
    SwappItemList items = new SwappItemList();
    SwappItem item1 = new SwappItem("item1");
    items.addItem(item1);
    SwappItem item2 = new SwappItem("item2");
    items.addItem(item2);
    try {
      assertEquals(ItemsListWithTwoItems, mapper.writeValueAsString(items));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  @Test
  public void testDeserializers() {
    try {
      SwappItemList deserializedItems = mapper.readValue(ItemsListWithTwoItems, SwappItemList.class);
      SwappItemList items = new SwappItemList();
      SwappItem item1 = new SwappItem("item1");
      items.addItem(item1);
      SwappItem item2 = new SwappItem("item2");
      items.addItem(item2);
      assertEquals(deserializedItems.getItems().get(0).getName(), items.getItems().get(0).getName());
      assertEquals(deserializedItems.getItems().get(1).getName(), items.getItems().get(1).getName());
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  @Test
  public void testSerializersDeserializers() {
    SwappItemList list = new SwappItemList();
    SwappItem item1 = new SwappItem("item1");
    SwappItem item2 = new SwappItem("item2");
    list.addItem(item1);
    list.addItem(item2);
    try {
      String json = mapper.writeValueAsString(list);
      SwappItemList list2 = mapper.readValue(json, SwappItemList.class);
      assertEquals(list.getItems().get(0).getName(), list2.getItems().get(0).getName());
      assertEquals(list.getItems().get(1).getName(), list2.getItems().get(1).getName());
    } catch (JsonProcessingException e) {
      fail();
    }
  }
}
