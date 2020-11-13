package swapp.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.TreeNode;
import java.io.IOException;
import swapp.core.SwappItem;

class SwappItemDeserializer extends JsonDeserializer<SwappItem> {

  @Override
  public SwappItem deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);  
  }

  SwappItem deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      String itemName ="";
      String username ="";
      String itemStatus ="";
      String itemDescription ="";
      ObjectNode objectNode = (ObjectNode) jsonNode;
      JsonNode nameNode = objectNode.get("itemName");
      if (nameNode instanceof TextNode) {
        itemName = nameNode.asText();
      }
      JsonNode usernameNode = objectNode.get("itemUsername");
      if (usernameNode instanceof TextNode) {
        username = usernameNode.asText();
      }
      JsonNode statusNode = objectNode.get("itemStatus");
      if (statusNode instanceof TextNode) {
        itemStatus = statusNode.asText();
      }
      JsonNode descriptionNode = objectNode.get("itemDescription");
      if (descriptionNode instanceof TextNode) {
        itemDescription = descriptionNode.asText();
      }
      return new SwappItem(itemName, username, itemStatus, itemDescription);
    }
    return null;
  }
}
