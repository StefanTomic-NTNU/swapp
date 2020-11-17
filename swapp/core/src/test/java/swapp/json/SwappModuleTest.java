package swapp.json;

import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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

  //TODO Blir ikke tatt i bruk. Fjern?
  private final static String swappListWithTwoItems =
      "{\"lists\":[{\"username\":\"swapp\",\"items\":["
      + "{\"itemName\":\"item1\",\"itemUsername\":\"username1\","
      + "\"itemStatus\":\"New\",\"itemDescription\":\"info1\"},"
      + "{\"itemName\":\"item2\",\"itemUsername\":\"username2\","
      + "\"itemStatus\":\"New\",\"itemDescription\":\"info2\"}]}]}";
  private final static String defaultSwappModel =
      "{\"lists\":[{\"username\":\"username1\",\"items\":["
      + "{\"itemName\":\"item1\",\"itemUsername\":\"username1\","
      + "\"itemStatus\":\"New\",\"itemDescription\":\"info1\"},"
      + "{\"itemName\":\"item2\",\"itemUsername\":\"username1\","
      + "\"itemStatus\":\"New\",\"itemDescription\":\"info2\"}]},"
      + "{\"username\":\"username2\",\"items\":["
      + "{\"itemName\":\"item3\",\"itemUsername\":\"username2\","
      + "\"itemStatus\":\"New\",\"itemDescription\":\"info3\"}]}]}";

  @Test
  public void testSerializers() {
    SwappModel model = new SwappModel();
    SwappList swappList1 = new SwappList("username1");
    SwappList swappList2 = new SwappList("username2");
    model.addSwappList(swappList1);
    model.addSwappList(swappList2);
    swappList1.createAndAddSwappItem("item1", "username1", "New", "info1");
    swappList1.createAndAddSwappItem("item2", "username1", "New", "info2");
    swappList2.createAndAddSwappItem("item3", "username2", "New", "info3");
    try {
      assertEquals(defaultSwappModel, mapper.writeValueAsString(model));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  @Test
  public void testDeserializers() {
    try {
      SwappModel model = mapper.readValue(defaultSwappModel, SwappModel.class);
      Iterator<SwappList> it1 = model.iterator();
      assertTrue(it1.hasNext());
      SwappList swappList = it1.next();
      assertEquals("username1", swappList.getUsername());
      Iterator<SwappItem> iterator = swappList.iterator();
      assertTrue(iterator.hasNext());
      SwappItem swappItem1 = iterator.next();
      assertTrue(swappItem1.allAttributesEquals("item1", "New", "info1", "username1"));
      assertTrue(iterator.hasNext());
      SwappItem swappItem2 = iterator.next();
      assertTrue(swappItem2.allAttributesEquals("item2", "New", "info2", "username1"));
      assertFalse(iterator.hasNext());
      assertTrue(it1.hasNext());
      SwappList list2 = it1.next();
      Iterator<SwappItem> it2 = list2.iterator();
      assertTrue(it2.next().allAttributesEquals("item3", "New", "info3", "username2"));
      assertFalse(it2.hasNext());
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  @Test
  public void testSerializersDeserializers() {
    SwappModel model = new SwappModel();
    SwappList swappList = new SwappList("username1");
    model.addSwappList(swappList);
    SwappItem item1 = new SwappItem("name1", "username1", "New", "info1");
    SwappItem item2 = new SwappItem("name2", "username1", "New", "info2");
    swappList.addSwappItem(item1);
    swappList.addSwappItem(item2);
    try {
      String json = mapper.writeValueAsString(model);
      SwappModel model2 = mapper.readValue(json, SwappModel.class);
      assertTrue(model2.iterator().hasNext());
      SwappList list2 = model.iterator().next();
      assertEquals("username1", swappList.getUsername());
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

  @Test
  public void testSerializersDeserializersSwappItem() throws JsonMappingException, JsonProcessingException {
    String itemString =
        "{\"itemName\":\"item1\",\"itemUsername\":\"username1\",\"itemStatus\":\"New\",\"itemDescription\":\"info1\"}";
    SwappItem item1 = mapper.readValue(itemString, SwappItem.class);
    assertTrue(item1.allAttributesEquals("item1", "New", "info1", "username1"));
  }

}
