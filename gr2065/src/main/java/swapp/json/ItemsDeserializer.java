package swapp.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import swapp.core.Item;
import swapp.core.Items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class ItemsDeserializer extends JsonDeserializer<Items>{

    private final ItemDeserializer itemDeserializer = new ItemDeserializer();

    @Override
    public Items deserialize(final JsonParser jsonParser,
                                final DeserializationContext deserContext) throws IOException, JsonProcessingException {
        final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        if (jsonNode instanceof ArrayNode) {
            final ArrayNode itemsArray = (ArrayNode) jsonNode;
            final Collection<Item> items = new ArrayList<>(itemsArray.size());
            for (final JsonNode itemNode : itemsArray) {
                final Item item = itemDeserializer.deserialize(itemNode);
                items.add(item);
            }
            return new Items(items);
        }
        return null;
    }

}
