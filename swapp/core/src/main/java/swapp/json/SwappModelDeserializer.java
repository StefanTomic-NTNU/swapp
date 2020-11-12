package swapp.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import swapp.core.SwappList;
import swapp.core.SwappModel;

class SwappModelDeserializer extends JsonDeserializer<SwappModel> {

    private SwappListDeserializer swappListDeserializer = new SwappListDeserializer();
    /*
     * format: { "lists": [ ... ] }
     */
  
    @Override
    public SwappModel deserialize(JsonParser parser, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
      TreeNode treeNode = parser.getCodec().readTree(parser);
      return deserialize((JsonNode) treeNode);
    }
  
    SwappModel deserialize(JsonNode treeNode) throws JsonProcessingException {
      if (treeNode instanceof ObjectNode) {
        ObjectNode objectNode = (ObjectNode) treeNode;
        SwappModel model = new SwappModel();
        JsonNode itemsNode = objectNode.get("lists");
        if (itemsNode instanceof ArrayNode) {
          for (JsonNode elementNode : ((ArrayNode) itemsNode)) {
            SwappList list = swappListDeserializer.deserialize(elementNode);
            if (list != null) {
              model.addSwappList(list);
            }
          }
        }
        return model;
      }
      return null;
    }   
  }
  