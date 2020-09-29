package swapp.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import swapp.core.Item;
import swapp.core.Items;


public class ItemsModule extends Module {

    @Override
    public String getModuleName() {
        return "ItemsModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    private final SimpleSerializers serializers = new SimpleSerializers();
    private final SimpleDeserializers deserializers = new SimpleDeserializers();

    public ItemsModule() {
        serializers.addSerializer(Item.class, new ItemSerializer());
        serializers.addSerializer(Items.class, new ItemsSerializer());
        deserializers.addDeserializer(Item.class, new ItemDeserializer());
        deserializers.addDeserializer(Items.class, new ItemsDeserializer());
    }

    @Override
    public void setupModule(final SetupContext context) {
        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
    }
}

