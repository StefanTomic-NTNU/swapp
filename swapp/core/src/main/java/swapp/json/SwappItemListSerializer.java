package swapp.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;


class SwappItemListSerializer extends JsonSerializer<SwappItemList> {

  @Override
  public void serialize(
      final SwappItemList items, final JsonGenerator jsonGen, final SerializerProvider provider)
      throws IOException {
    jsonGen.writeStartArray(items.getSwappItems().size());
    for (final SwappItem item : items.getSwappItems()) {
      jsonGen.writeObject(item);
    }
    jsonGen.writeEndArray();
  }


}
