package swapp.json;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import swapp.core.Item;
import swapp.core.Items;

public class ItemsModuleTest {

    // {"items":[{"text":"item1","checked":false},{"text":"item2","checked":true}]}

    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new ItemsModule());
    }

    private final static String ItemsListWithTwoItems = "[{\"itemName\":\"item1\"},{\"itemName\":\"item2\"}]";
    
    @Test
    public void testSerializers() {
        Items items = new Items();
        Item item1 = new Item("item1");
        items.addItem(item1);
        Item item2 = new Item("item2");
        items.addItem(item2);
        try {
            assertEquals(ItemsListWithTwoItems, mapper.writeValueAsString(items));
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void testDeserializers() {
        try {
            Items deserializedItems = mapper.readValue(ItemsListWithTwoItems, Items.class);
            Items items = new Items();
            Item item1 = new Item("item1");
            items.addItem(item1);
            Item item2 = new Item("item2");
            items.addItem(item2);
            assertEquals(deserializedItems.getItems().get(0).getName(), items.getItems().get(0).getName());
            assertEquals(deserializedItems.getItems().get(1).getName(), items.getItems().get(1).getName());
        } catch (JsonProcessingException e) {
            fail();
        }
    }

    @Test
    public void testSerializersDeserializers() {
        Items list = new Items();
        Item item1 = new Item("item1");
        Item item2 = new Item("item2");
        list.addItem(item1);
        list.addItem(item2);
        try {
            String json = mapper.writeValueAsString(list);
            Items list2 = mapper.readValue(json, Items.class);
            assertEquals(list.getItems().get(0).getName(), list2.getItems().get(0).getName());
            assertEquals(list.getItems().get(1).getName(), list2.getItems().get(1).getName());
        } catch (JsonProcessingException e) {
            fail();
        }
    }




}