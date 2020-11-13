package swapp.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import swapp.core.SwappItem;
import swapp.core.SwappList;


class SwappListDeserializer extends JsonDeserializer<SwappList> {

  private SwappItemDeserializer swappItemDeserializer = new SwappItemDeserializer();
  /*
   * format: { "items": [ ... ] }
   */

  @Override
  public SwappList deserialize(JsonParser parser, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    TreeNode treeNode = parser.getCodec().readTree(parser);
    return deserialize((JsonNode) treeNode);
  }

  SwappList deserialize(JsonNode treeNode){
    if (treeNode instanceof ObjectNode) {
      ObjectNode objectNode = (ObjectNode) treeNode;
      JsonNode nameNode = objectNode.get("username");
      if (! (nameNode instanceof TextNode)) {
        return null;
      }
      String username = nameNode.asText();
      JsonNode itemsNode = objectNode.get("items");
      boolean hasItems = itemsNode instanceof ArrayNode;
      SwappList swappList = new SwappList(username);
      if (hasItems) {
        for (JsonNode elementNode : ((ArrayNode) itemsNode)) {
          SwappItem item = swappItemDeserializer.deserialize(elementNode);
          if (item != null) {
            swappList.addSwappItem(item);
          }
        }
      }
      return swappList;
    }
    return null;
  }   
}
