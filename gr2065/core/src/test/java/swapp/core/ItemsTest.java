package swapp.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;


public class ItemsTest{

    @Test
    public void testCreateEmptyItems(){
        Items items = new Items();
        List<Item> itemslist = items.items;
        assertEquals(itemslist.size(),0);
    }


    @Test
    public void testCreatePopulatedItems(){
        Item item1 = new Item("item1");
        Item item2 = new Item("item2");
        Items items = new Items(item1,item2);
        List<Item> itemslist = items.items;
        assertEquals(itemslist.get(0).getName(),"item1");
        assertEquals(itemslist.get(1).getName(),"item2");
    }

    @Test
    public void testGetItems(){
        Item item1 = new Item("item1");
        Items items = new Items(item1);
        assertEquals(items.getItems().get(0).getName(),"item1");
    }

    @Test
    public void testAddOneItem(){
        Items items = new Items();
        Item item = new Item("name");
        items.addItem(item);
        List<Item> itemlist = new ArrayList<>();
        itemlist.add(item);
        assertEquals(items.getItems(), itemlist);
    }

    @Test
    public void testAddMultipleItems(){
        Items items = new Items();
        Item item1 = new Item("item1");
        Item item2 = new Item("item2");
        items.addItem(item1, item2);
        List<Item> itemlist = new ArrayList<>();
        itemlist.add(item1);
        itemlist.add(item2);
        assertEquals(items.getItems(), itemlist);
    }

    @Test
    public void testRemoveItem(){
        Item item1 = new Item("item1");
        Items items = new Items(item1);
        items.removeItem(item1);
        assertEquals(items.getItems().size(),0);;
    }
}