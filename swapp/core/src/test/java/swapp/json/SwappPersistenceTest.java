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
import swapp.core.SwappItemList;

public class SwappPersistenceTest {

    private SwappPersistence swappPersistence = new SwappPersistence();

    private SwappItemList list;
    private SwappItem item1;
    private SwappItem item2;

    @BeforeEach
    public void beforeEach() {
      list = new SwappItemList();
      item1 = new SwappItem("name1");
      item2 = new SwappItem("name2");
    }
    
    @Test
    public void testSerializersDeserializers(){
        list.addSwappItem(item1, item2);
        SwappItem nextItem;
        try {
            StringWriter writer = new StringWriter();
            swappPersistence.writeSwappList(list, writer);
            String json = writer.toString();
            SwappItemList list2 = swappPersistence.readSwappList(new StringReader(json));
            Iterator<SwappItem> it = list2.iterator();
            assertTrue(it.hasNext());
            nextItem = it.next();
            assertEquals(nextItem, item1);
            nextItem = it.next();
            assertEquals(nextItem, item2);
            /* Hvis vi skal endre på equals() igjen kan dette være nyttig:
            nextItem = it.next();
            assertEquals(nextItem.getName(), item1.getName());
            assertEquals(nextItem.getStatus(), item1.getStatus());
            assertEquals(nextItem.getDescription(), item1.getDescription());
            assertEquals(nextItem.getContactInfo(), item1.getContactInfo());
            assertTrue(it.hasNext());
            nextItem = it.next();
            assertEquals(nextItem.getName(), item2.getName());
            assertEquals(nextItem.getStatus(), item2.getStatus());
            assertEquals(nextItem.getDescription(), item2.getDescription());
            assertEquals(nextItem.getContactInfo(), item2.getContactInfo());
            assertFalse(it.hasNext());
            */
          } catch (IOException e) {
            fail();
          }
      
        
    }
    
    
}
