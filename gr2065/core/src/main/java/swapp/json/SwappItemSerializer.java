package swapp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import swapp.core.SwappItem;


class SwappItemSerializer extends JsonSerializer<SwappItem> {

  public static final String ITEMNAME = "itemName";

  @Override
  public void serialize(
      final SwappItem item, final JsonGenerator jsonGen, final SerializerProvider provider)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeFieldName(ITEMNAME);
    jsonGen.writeString(item.getName());
    jsonGen.writeEndObject();
  }


}
