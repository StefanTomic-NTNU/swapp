package swapp.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;


@SuppressWarnings("serial")
public class SwappModule extends SimpleModule {

  private static final String NAME = "SwappModule";

  /**
   * Initializes this SwappModule with appropriate serializers and deserializers.
   */
  public SwappModule(boolean deepSwappModelSerializer) {
    super(NAME, Version.unknownVersion());
    addSerializer(SwappItem.class, new SwappItemSerializer());
    addSerializer(SwappList.class, new SwappListSerializer());
    addSerializer(SwappModel.class, new SwappModelSerializer(deepSwappModelSerializer));
    addDeserializer(SwappItem.class, new SwappItemDeserializer());
    addDeserializer(SwappList.class, new SwappListDeserializer());
    addDeserializer(SwappModel.class, new SwappModelDeserializer());
  }

  public SwappModule() {
    this(true);
  }
}


