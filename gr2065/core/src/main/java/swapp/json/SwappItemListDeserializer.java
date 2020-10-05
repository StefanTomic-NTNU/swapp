package swapp.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;


public class SwappItemListDeserializer extends JsonDeserializer<SwappItemList> {

  private final SwappItemDeserializer itemDeserializer = new SwappItemDeserializer();

  @Override
  public SwappItemList deserialize(final JsonParser jsonParser, final DeserializationContext deserContext)
      throws IOException, JsonProcessingException {
    final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
    if (jsonNode instanceof ArrayNode) {
      final ArrayNode itemsArray = (ArrayNode) jsonNode;
      final Collection<SwappItem> items = new ArrayList<>(itemsArray.size());
      for (final JsonNode itemNode : itemsArray) {
        final SwappItem item = itemDeserializer.deserialize(itemNode);
        items.add(item);
      }
      return new SwappItemList(items);
    }
    return null;
  }

}
