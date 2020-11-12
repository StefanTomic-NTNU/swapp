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
import swapp.core.SwappList;
import swapp.core.SwappModel;

public class SwappModuleTest {

  private static ObjectMapper mapper;

  @BeforeAll
  public static void setUp() {
    mapper = new ObjectMapper();
    mapper.registerModule(new SwappModule());
  }

  /*
   * private final static String ItemsListWithTwoItems =
   * "[{\"itemName\":\"name1\",\"itemUsername\":\"username1\"" +
   * ",\"itemDescription\":\"\",\"itemStatus\":\"anonymous@email.com\"}," +
   * "{\"itemName\":\"name2\",\"itemUsername\":\"New\"" +
   * ",\"itemDescription\":\"\",\"itemStatus\":\"anonymous@email.com\"}]";
   */

  private final static String swappListWithTwoItems = "{\"lists\":[{\"username\":\"swapp\",\"items\":[{\"itemName\":\"item1\",\"itemUsername\":\"username1\",\"itemStatus\":\"New\",\"itemDescription\":\"info1\"},{\"itemName\":\"item2\",\"itemUsername\":\"username2\",\"itemStatus\":\"New\",\"itemDescription\":\"info2\"}]}]}";

  @Test
  public void testSerializers() {
    SwappModel model = new SwappModel();
    SwappList swappList = new SwappList("swapp");
    model.addSwappList(swappList);
    SwappItem swappItem1 = swappList.createAndAddSwappItem("item1", "username1", "New", "info1");
    SwappItem swappItem2 = swappList.createAndAddSwappItem("item2", "username2", "New", "info2");
    try {
      assertEquals(swappListWithTwoItems, mapper.writeValueAsString(model));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  @Test
  public void testDeserializers() {
    try {
      SwappModel model = mapper.readValue(swappListWithTwoItems, SwappModel.class);
      assertTrue(model.iterator().hasNext());
      SwappList swappList = model.iterator().next();
      assertEquals("swapp", swappList.getUsername());
      Iterator<SwappItem> iterator = swappList.iterator();
      assertTrue(iterator.hasNext());
      SwappItem swappItem1 = iterator.next();
      assertTrue(swappItem1.allAttributesEquals("item1", "New", "info1", "username1"));
      assertTrue(iterator.hasNext());
      SwappItem swappItem2 = iterator.next();
      assertTrue(swappItem2.allAttributesEquals("item2", "New", "info2", "username2"));
      assertFalse(iterator.hasNext());
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  @Test
  public void testSerializersDeserializers() {
    SwappModel model = new SwappModel();
    SwappList swappList = new SwappList("swapp");
    model.addSwappList(swappList);
    SwappItem item1 = new SwappItem("name1", "username1", "New", "info1");
    SwappItem item2 = new SwappItem("name2", "username2", "New", "info2");
    swappList.addSwappItem(item1);
    swappList.addSwappItem(item2);
    try {
      String json = mapper.writeValueAsString(model);
      SwappModel model2 = mapper.readValue(json, SwappModel.class);
      assertTrue(model2.iterator().hasNext());
      SwappList list2 = model.iterator().next();
      assertEquals("swapp", swappList.getUsername());
      Iterator<SwappItem> it = list2.iterator();
      assertTrue(it.hasNext());
      assertTrue(it.next().allAttributesEquals(item1));
      assertTrue(it.hasNext());
      assertTrue(it.next().allAttributesEquals(item2));
      assertFalse(it.hasNext());
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  // TODO legg til flere tester?
  /*
   * Forstår ikke hensikten med en slik test
   * 
   * @Test public void testDeserializeSwappItemAsArrayNode() { try { SwappItem[]
   * items = {item1}; ArrayNode arr = mapper.valueToTree(item1);
   * SwappItemDeserializer des = new SwappItemDeserializer(); SwappItem item3 =
   * des.deserialize(arr); System.err.println(item1); System.err.println(item3);
   * assertEquals(item1, item3); } catch (JsonProcessingException e) { fail(); } }
   */
  /**
   * @Test public void testSerializersDeserializers() { try { String json =
   *       mapper.writeValueAsString(items); SwappList items2 =
   *       mapper.readValue(json, SwappList.class);
   *       assertTrue(items.getSwappItems().get(0).allAttributesEquals(items2.getSwappItems().get(0)));
   *       assertTrue(items.getSwappItems().get(1).allAttributesEquals(items2.getSwappItems().get(1)));
   *       } catch (JsonProcessingException e) { fail(); }
   * 
   *       }
   */
}
