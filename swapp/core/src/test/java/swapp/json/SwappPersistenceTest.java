package swapp.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;

public class SwappPersistenceTest {

  private SwappPersistence swappPersistence = new SwappPersistence();

  @Test
  public void testSerializersDeserializers() {
    SwappModel model = new SwappModel();
    SwappList swappList = new SwappList("username1");
    model.addSwappList(swappList);
    SwappItem item1 = swappList.createAndAddSwappItem("name1", "username1", "New", "info1");
    SwappItem item2 = swappList.createAndAddSwappItem("name2", "username1", "New", "info2"); 
    try{
      StringWriter writer = new StringWriter();
      swappPersistence.writeSwappModel(model, writer);
      String json = writer.toString();
      SwappModel model2 = swappPersistence.readSwappModel(new StringReader(json));
      assertTrue(model2.iterator().hasNext());
      SwappList list2 = model.iterator().next();
      assertEquals("username1", list2.getUsername());
      Iterator<SwappItem> it = list2.iterator();
      assertTrue(it.hasNext());
      assertTrue(it.next().allAttributesEquals(item1));
      assertTrue(it.hasNext());
      assertTrue(it.next().allAttributesEquals(item2));
      assertFalse(it.hasNext());
    } catch (IOException e) {
      fail();
    }






    /**
    list.addSwappItem(item1, item2);
    SwappItem nextItem;
    try {
      StringWriter writer = new StringWriter();
      swappPersistence.writeSwappList(list, writer);
      String json = writer.toString();
      SwappList list2 = swappPersistence.readSwappList(new StringReader(json));
      Iterator<SwappItem> it = list2.iterator();
      assertTrue(it.hasNext());
      nextItem = it.next();
      assertTrue(nextItem.allAttributesEquals(item1));
      nextItem = it.next();
      assertTrue(nextItem.allAttributesEquals(item2));
    } catch (IOException e) {
      fail();
    }*/

  }

}
