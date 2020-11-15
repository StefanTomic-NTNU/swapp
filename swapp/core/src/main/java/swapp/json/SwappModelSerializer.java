package swapp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import swapp.core.SwappList;
import swapp.core.SwappModel;

class SwappModelSerializer extends JsonSerializer<SwappModel> {

  private final boolean deep;

  public SwappModelSerializer(boolean deep) {
    this.deep = deep;
  }

  public SwappModelSerializer() {
    this(true);
  }

  /*
   * format: { "lists": [ ... ] }
   */

  @Override
  public void serialize(
      SwappModel model, JsonGenerator jsonGen, SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeArrayFieldStart("lists");
    for (SwappList list : model) {
      if (deep) {
        jsonGen.writeObject(list);
      } else {
        jsonGen.writeStartObject();
        jsonGen.writeStringField("username", list.getUsername());
        // no items!
        jsonGen.writeEndObject();
      }
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }
}
