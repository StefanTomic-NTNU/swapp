package swapp.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;


public class SwappItemModule extends Module {

  @Override
  public String getModuleName() {
    return "SwappItemModule";
  }

  @Override
  public Version version() {
    return Version.unknownVersion();
  }

  private final SimpleSerializers serializers = new SimpleSerializers();
  private final SimpleDeserializers deserializers = new SimpleDeserializers();

  /**
  * adds serializers and deserializers to module. 
  */
  public SwappItemModule(boolean deepSwappItemSerializer) {
    serializers.addSerializer(SwappItem.class, new SwappItemSerializer());
    serializers.addSerializer(SwappItemList.class, new SwappItemListSerializer());
    deserializers.addDeserializer(SwappItem.class, new SwappItemDeserializer());
    deserializers.addDeserializer(SwappItemList.class, new SwappItemListDeserializer());
  }

  public SwappItemModule(){
    this(true);
  }

  @Override
  public void setupModule(final SetupContext context) {
    context.addSerializers(serializers);
    context.addDeserializers(deserializers);
  }
}

