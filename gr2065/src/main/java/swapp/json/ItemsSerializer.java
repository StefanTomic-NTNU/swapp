package swapp.json;;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import swapp.core.Item;
import swapp.core.Items;

import java.io.IOException;


public class ItemsSerializer extends JsonSerializer<Items>{

    @Override
    public void serialize(final Items items, final JsonGenerator jsonGen,
                          final SerializerProvider provider) throws IOException {
        jsonGen.writeStartArray(items.getItems().size());
        for (final Item item : items.getItems()) {
            jsonGen.writeObject(item);
        }
        jsonGen.writeEndArray();
    }


}
