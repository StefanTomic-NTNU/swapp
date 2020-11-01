package swapp.json;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import swapp.core.SwappItem;
import swapp.core.SwappItemList;

public class SwappItemListModuleTest {

  // {"items":[{"text":"item1","checked":false},{"text":"item2","checked":true}]}

  private static ObjectMapper mapper;
  private SwappItemList items;
  private SwappItem item1;
  private SwappItem item2;


  @BeforeAll
  public static void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new SwappItemModule());
  }

  @BeforeEach
  public void beforeEach() {
    items = new SwappItemList();
    item1 = new SwappItem("name1", "Gis bort", "description1", "contactInfo1");
    item2 = new SwappItem("name2", "Gis bort", "description2", "contactInfo2");
  }

  //private final static String ItemsListWithTwoItems = "[{\"itemName\":\"item1\"},{\"itemName\":\"item2\"}]";
  //private final static String ItemsListWithTwoItems = 
  //  "[{\"itemName\":\"name1\"}{\"itemStatus\":\"Gis bort\"}{\"itemDescription\":\"description1\"}{\"itemContactInfo\":\"contactInfo1\"},{\"itemName\":\"name2\"}{\"itemStatus\":\"Gis bort\"}{\"itemDescription\":\"description2\"}{\"itemContactInfo\":\"contactInfo2\"}]";
  private final static String ItemsListWithTwoItems = 
    "[{\"itemName\":\"name1\",\"itemStatus\":\"Gis bort\""
    + ",\"itemDescription\":\"description1\",\"itemContactInfo\":\"contactInfo1\"},"
    + "{\"itemName\":\"name2\",\"itemStatus\":\"Gis bort\""
    + ",\"itemDescription\":\"description2\",\"itemContactInfo\":\"contactInfo2\"}]"; 
  
  @Test
  public void testSerializers() {
    //SwappItemList items = new SwappItemList();
    //SwappItem item1 = new SwappItem("item1");
    items.addItem(item1);
    //SwappItem item2 = new SwappItem("item2");
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
      //SwappItemList items = new SwappItemList();
      //SwappItem item1 = new SwappItem("item1");
      items.addItem(item1);
      //SwappItem item2 = new SwappItem("item2");
      items.addItem(item2);
      assertEquals(deserializedItems.getItems().get(0).getName(), items.getItems().get(0).getName());
      assertEquals(deserializedItems.getItems().get(1).getName(), items.getItems().get(1).getName());
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  /*
  @Test
  public void testDeserializeSwappItemAsArrayNode() {
    try {
      //SwappItem item = new SwappItem("item");
      SwappItem[] items = {item1};
      ArrayNode arr = mapper.valueToTree(items);
      SwappItemDeserializer des = new SwappItemDeserializer();
      SwappItem item2 = des.deserialize(arr);
      System.err.println(item2);
      assertEquals(item.getName(), item2.getName());
    } catch (JsonProcessingException e) {
      fail();
    }
  }
  */

  /*
  @Test
  public void testDeserializeSwappItemAsTextNode() {
    try {
      //SwappItem item = new SwappItem("item");
      TextNode text = mapper.valueToTree(item.getName());
      SwappItemDeserializer des = new SwappItemDeserializer();
      SwappItem item2 = des.deserialize(text);
      assertEquals(null, item2);
    } catch (JsonProcessingException e) {
      fail();
    }
  }
  */

  @Test
  public void testSerializersDeserializers() {
    //SwappItemList list = new SwappItemList();
    //SwappItem item1 = new SwappItem("item1");
    //SwappItem item2 = new SwappItem("item2");
    items.addItem(item1);
    items.addItem(item2);
    try {
      String json = mapper.writeValueAsString(items);
      SwappItemList items2 = mapper.readValue(json, SwappItemList.class);
      assertEquals(items.getItems().get(0).getName(), items2.getItems().get(0).getName());
      assertEquals(items.getItems().get(1).getName(), items2.getItems().get(1).getName());
    } catch (JsonProcessingException e) {
      fail();
    }

  }
}
