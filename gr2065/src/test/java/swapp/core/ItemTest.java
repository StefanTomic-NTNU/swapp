package swapp.core;

import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


public class ItemTest extends ApplicationTest {

    @Test
    public void testBlankName(){
        Item item = new Item(" ");
        assertEquals(item.getName(), null);
    }
    
    @Test
    public void testGetname(){
        Item item = new Item("name");
        assertEquals(item.getName(), "name");
    }

}
