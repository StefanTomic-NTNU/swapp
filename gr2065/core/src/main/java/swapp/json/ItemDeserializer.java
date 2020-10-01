package swapp.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import swapp.core.Item;

import java.io.IOException;


public class ItemDeserializer extends JsonDeserializer<Item>{

    private static final int ARRAY_JSON_NODE_SIZE = 1;

    @Override
    public Item deserialize(final JsonParser jsonParser,
                               final DeserializationContext deserContext) throws IOException, JsonProcessingException {
        final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        return deserialize(jsonNode);
    }

    public Item deserialize(final JsonNode jsonNode) throws JsonProcessingException {
        if (jsonNode instanceof ObjectNode) {
            final ObjectNode objectNode = (ObjectNode) jsonNode;
            final String itemName =
                    objectNode.get(ItemSerializer.ITEMNAME).asText();
            return new Item(itemName);
        } else if (jsonNode instanceof ArrayNode) {
            final ArrayNode itemArray = (ArrayNode) jsonNode;
            if (itemArray.size() == ARRAY_JSON_NODE_SIZE) {
                final String itemName = itemArray.get(0).asText();
                return new Item(itemName);
            }
        }
        return null;
    }


}
