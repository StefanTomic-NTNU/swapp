package swapp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import swapp.core.SwappItem;


class SwappItemSerializer extends JsonSerializer<SwappItem> {

  public static final String ITEMNAME = "itemName";
  public static final String ITEMUSERNAME = "itemUsername";
  public static final String ITEMSTATUS = "itemStatus";
  public static final String ITEMDESCRIPTION = "itemDescription";

  @Override
  public void serialize(
      SwappItem item, JsonGenerator jsonGen, final SerializerProvider serializerProvider)
      throws IOException {
    jsonGen.writeStartObject();
    jsonGen.writeStringField(ITEMNAME, item.getName());
    jsonGen.writeStringField(ITEMUSERNAME, item.getUsername());
    jsonGen.writeStringField(ITEMSTATUS, item.getStatus());
    jsonGen.writeStringField(ITEMDESCRIPTION, item.getDescription());
    jsonGen.writeEndObject();
  }

}
