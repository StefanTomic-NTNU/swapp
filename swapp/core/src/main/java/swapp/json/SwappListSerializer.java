package swapp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import swapp.core.SwappItem;
import swapp.core.SwappList;


class SwappListSerializer extends JsonSerializer<SwappList> {

  @Override
  public void serialize(
      final SwappList swappList, final JsonGenerator jsonGen, final SerializerProvider provider)
      throws IOException {
    jsonGen.writeStartObject();
    if (swappList.getUsername()!=null){
      jsonGen.writeStringField("username", swappList.getUsername());
    }
    jsonGen.writeArrayFieldStart("items");
    for (final SwappItem item : swappList) {
      jsonGen.writeObject(item);
    }
    jsonGen.writeEndArray();
    jsonGen.writeEndObject();
  }


}
