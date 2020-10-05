package swapp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;


public class SwappItemListSerializer extends JsonSerializer<SwappItemList> {

  @Override
  public void serialize(final SwappItemList items, final JsonGenerator jsonGen, final SerializerProvider provider)
      throws IOException {
    jsonGen.writeStartArray(items.getItems().size());
    for (final SwappItem item : items.getItems()) {
      jsonGen.writeObject(item);
    }
    jsonGen.writeEndArray();
  }


}
