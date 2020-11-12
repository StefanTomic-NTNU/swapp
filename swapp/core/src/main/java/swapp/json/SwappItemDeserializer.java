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

  private static final int ARRAY_JSON_NODE_SIZE = 4;

  @Override
  public SwappItem deserialize(
      final JsonParser jsonParser, final DeserializationContext deserContext)
      throws IOException, JsonProcessingException {

    final JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
    return deserialize(jsonNode);

    //TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
    //return deserialize((JsonNode)treeNode);
  }


  /**
  * Deserializes jsonNode.
  */
  
  public SwappItem deserialize(final JsonNode jsonNode) throws JsonProcessingException {
    if (jsonNode instanceof ObjectNode) {
      final ObjectNode objectNode = (ObjectNode) jsonNode;
      final String itemName = objectNode.get(SwappItemSerializer.ITEMNAME).asText();
      final String itemUsername = objectNode.get(SwappItemSerializer.ITEMUSERNAME).asText();
      final String itemStatus = objectNode.get(SwappItemSerializer.ITEMSTATUS).asText();
      final String itemDescription = objectNode.get(SwappItemSerializer.ITEMDESCRIPTION).asText();
      return new SwappItem(itemName, itemUsername, itemStatus, itemDescription);
    } else if (jsonNode instanceof ArrayNode) {
      final ArrayNode itemArray = (ArrayNode) jsonNode;
      if (itemArray.size() == ARRAY_JSON_NODE_SIZE) {
        final String itemName = itemArray.get(0).asText();
        final String itemUsername = itemArray.get(1).asText();
        final String itemStatus = itemArray.get(2).asText();
        final String itemDescription = itemArray.get(3).asText();
        return new SwappItem(itemName, itemUsername, itemStatus, itemDescription);
      }
    }
    return null;
  }

  
//Dette er mer likt måten Hallvard gjør det på
/*
  public SwappItem deserialize(JsonNode jsonNode) {
    if (jsonNode instanceof ObjectNode) {
      String itemName ="";
      String itemStatus ="";
      String itemDescription ="";
      String itemContactInfo ="";
      ObjectNode objectNode = (ObjectNode) jsonNode;
      JsonNode nameNode = objectNode.get("itemName");
      if (nameNode instanceof TextNode) {
        itemName = nameNode.asText();
      }
      JsonNode statusNode = objectNode.get("itemStatus");
      if (statusNode instanceof TextNode) {
        itemStatus = statusNode.asText();
      }
      JsonNode descriptionNode = objectNode.get("itemDescription");
      if (descriptionNode instanceof TextNode) {
        itemDescription = descriptionNode.asText();
      }
      JsonNode contactInfoNode = objectNode.get("itemContactInfo");
      if (contactInfoNode instanceof TextNode) {
        itemContactInfo = contactInfoNode.asText();
      }
      return new SwappItem(itemName, itemStatus, itemDescription, itemContactInfo);
    }
    return null;
  } 
*/


}
