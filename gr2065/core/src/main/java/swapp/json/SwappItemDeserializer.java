package swapp.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import swapp.core.SwappItem;

public class SwappItemDeserializer extends JsonDeserializer<SwappItem> {

  private static final int ARRAY_JSON_NODE_SIZE = 1;

  @Override
  public SwappItem deserialize(final JsonParser jsonParser, final DeserializationContext deserContext)
      throws IOException, JsonProcessingException {
    final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
    return deserialize(jsonNode);
  }

  public SwappItem deserialize(final JsonNode jsonNode) throws JsonProcessingException {
    if (jsonNode instanceof ObjectNode) {
      final ObjectNode objectNode = (ObjectNode) jsonNode;
      final String itemName = objectNode.get(SwappItemSerializer.ITEMNAME).asText();
      return new SwappItem(itemName);
    } else if (jsonNode instanceof ArrayNode) {
      final ArrayNode itemArray = (ArrayNode) jsonNode;
      if (itemArray.size() == ARRAY_JSON_NODE_SIZE) {
        // Kode som er endret ved utvidelse av json-tester:
        return deserialize(itemArray.get(0));
        /* Kode som ikke ser ut til å ha fungert: 
         *
         * final String itemName = itemArray.get(0).asText();
         * return new SwappItem(itemName);
         */
      }
    }
    return null;
  }


}
