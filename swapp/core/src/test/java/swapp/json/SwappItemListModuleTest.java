package swapp.json;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import swapp.core.SwappItem;
import swapp.core.SwappItemList;

public class SwappItemListModuleTest {

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
    item1 = new SwappItem("name1");
    item2 = new SwappItem("name2");
    items.addItem(item1);
    items.addItem(item2);
  }

  private final static String ItemsListWithTwoItems = 
    "[{\"itemName\":\"name1\",\"itemStatus\":\"Ny\""
    + ",\"itemDescription\":null,\"itemContactInfo\":\"anonymous@email.com\"},"
    + "{\"itemName\":\"name2\",\"itemStatus\":\"Ny\""
    + ",\"itemDescription\":null,\"itemContactInfo\":\"anonymous@email.com\"}]"; 
  
  @Test
  public void testSerializers() {
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
      assertEquals(deserializedItems.getItems().get(0), items.getItems().get(0));
      assertEquals(deserializedItems.getItems().get(1), items.getItems().get(1));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  /* Forstår ikke hensikten med en slik test
  @Test
  public void testDeserializeSwappItemAsArrayNode() {
    try {
      SwappItem[] items = {item1};
      ArrayNode arr = mapper.valueToTree(item1);
      SwappItemDeserializer des = new SwappItemDeserializer();
      SwappItem item3 = des.deserialize(arr);
      System.err.println(item1);
      System.err.println(item3);
      assertEquals(item1, item3);
    } catch (JsonProcessingException e) {
      fail();
    }
  }
  */
  

  /*Testen funker men gir ingen mer test-coverage
  @Test
  public void testDeserializeSwappItemAsObjectNode() {
    try {
      ObjectNode item1Text = mapper.valueToTree(item1);
      SwappItemDeserializer des = new SwappItemDeserializer();
      SwappItem desItem = des.deserialize(item1Text);
      assertEquals(item1, desItem);
    } catch (JsonProcessingException e) {
      fail();
    }
  }
  */
  
  

  @Test
  public void testSerializersDeserializers() {
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
