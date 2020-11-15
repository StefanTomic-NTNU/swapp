package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SwappModelTest {

    private SwappModel swappModel;
    private SwappList list1, list2;

    @BeforeEach
    public void setUp() {
        swappModel = new SwappModel();
        list1 = new SwappList("swapp1");
        list2 = new SwappList("swapp2");
    }

    @Test
    public void testGetSwappItems() {
        swappModel.addSwappList(list1);
        swappModel.addSwappList(list2);
        SwappItem item1 = new SwappItem("name", "swapp1", "New", "info");
        SwappItem item2 = new SwappItem("name2", "swapp1", "New", "info");
        SwappItem item3 = new SwappItem("name3", "swapp2", "New", "info");
        swappModel.addSwappItem(item1);
        swappModel.addSwappItem(item2);
        swappModel.addSwappItem(item3);
        List<SwappItem> items = swappModel.getSwappItems();
        assertEquals(items.size(), 3);
        assertEquals(items.get(0).getName(), "name");
        assertEquals(items.get(1).getName(), "name2");
        assertEquals(items.get(2).getName(), "name3");
    }

    @Test
    public void testGetSwappItemsByStatus() {
        swappModel.addSwappList(list1);
        swappModel.addSwappList(list2);
        SwappItem item1 = new SwappItem("name", "swapp1", "Used", "info");
        SwappItem item2 = new SwappItem("name2", "swapp1", "New", "info");
        SwappItem item3 = new SwappItem("name3", "swapp2", "Used", "info");
        swappModel.addSwappItem(item1);
        swappModel.addSwappItem(item2);
        swappModel.addSwappItem(item3);
        List<SwappItem> items = swappModel.getSwappItemsByStatus("Used");
        assertEquals(items.size(), 2);
        assertEquals(items.get(0).getName(), "name");
        assertEquals(items.get(1).getName(), "name3");
    }

    @Test
    public void testAddSwapplist() {
        SwappModel swappModel = new SwappModel();
        SwappList list1 = new SwappList(new SwappItem("name1", "swapp1", "New", "info1"),
                new SwappItem("name2", "swapp1", "New", "info1"));
        SwappList list2 = new SwappList(new SwappItem("name3", "swapp2", "New", "info1"),
                new SwappItem("name4", "swapp2", "New", "info1"));
        swappModel.addSwappList(list1);
        swappModel.addSwappList(list2);
        Iterator<SwappList> it = swappModel.iterator();
        assertTrue(it.hasNext());

    }

    
    @Test
    public void testHasSwappList() {
        swappModel.addSwappList(list1);
        String username = list1.getUsername();
        assertTrue(swappModel.hasSwappList(username));
        assertFalse(swappModel.hasSwappList(list2.getUsername()));
    }

    @Test
    public void isValidName() {
        swappModel.addSwappList(list1);
        assertTrue(swappModel.isValidName(list2.getUsername()));
        assertFalse(swappModel.isValidName(list1.getUsername()));
    }


}
