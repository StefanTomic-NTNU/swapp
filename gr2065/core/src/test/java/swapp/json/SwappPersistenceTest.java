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

    private SwappItemList swappItemList;
    private SwappItem item1;
    private SwappItem item2;

    @BeforeEach
    public void beforeEach() {
      swappItemList = new SwappItemList();
      item1 = new SwappItem("name1", "Gis bort", "description1", "contactInfo1");
      item2 = new SwappItem("name2", "Gis bort", "description2", "contactInfo2");
    }

    /*
    @Test
    public void testSerializersDeserializers(){
        //SwappItemList list = new SwappItemList();
        //SwappItem item1 = new SwappItem("name");
        //SwappItem item2 = new SwappItem("name2");
        list.addItem(item1, item2);
        try {
            StringWriter writer = new StringWriter();
            swappPersistence.writeSwappList(list, writer);
            String json = writer.toString();
            SwappItemList list2 = swappPersistence.readSwappList(new StringReader(json));
            Iterator<SwappItem> it = list2.iterator();
            assertTrue(it.hasNext());
            assertEquals(it.next().getName(), item1.getName());
            assertTrue(it.hasNext());
            assertEquals(it.next().getName(), item2.getName());
            assertFalse(it.hasNext());
          } catch (IOException e) {
            fail();
          }
      
        
    }
    */
    
}
