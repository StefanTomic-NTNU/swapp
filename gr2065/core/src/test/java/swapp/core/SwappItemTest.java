package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class SwappItemTest {

    @Test
    public void testBlankName(){
        SwappItem item = new SwappItem(" ");
        assertEquals(item.getName(), null);
    }
    
    @Test
    public void testGetname(){
        SwappItem item = new SwappItem("name");
        assertEquals(item.getName(), "name");
    }

    @Test
    public void testToString(){
        SwappItem item = new SwappItem("name");
        assertEquals("name", item.toString());
    }

}
