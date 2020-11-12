package swapp.restserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.core.SwappItem;
import swapp.json.SwappPersistence;
import swapp.restapi.SwappModelService;

public class SwappConfig extends ResourceConfig {

  private SwappModel swappModel;

  /**
   * Initialize this TodoConfig.
   *
   * @param todoModel todoModel instance to serve
   */
  public SwappConfig(SwappModel swappModel) {
    setSwappModel(swappModel);
    register(SwappModelService.class);
    register(SwappModuleObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(SwappConfig.this.swappModel);
      }
    });
  }

  /**
   * Initialize this TodoConfig with a default TodoModel.
   */
  public SwappConfig() {
    this(createDefaultSwappModel());
  }

  public SwappModel getSwappModel() {
    return swappModel;
  }

  public void setSwappModel(SwappModel swappModel) {
    this.swappModel = swappModel;
  }

  private static SwappModel createDefaultSwappModel() {
    SwappPersistence swappPersistence = new SwappPersistence();
    URL url = SwappConfig.class.getResource("default-todomodel.json");
    if (url != null) {
      try (Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
        return swappPersistence.readSwappModel(reader);
      } catch (IOException e) {
        System.out.println("Couldn't read default-todomodel.json, so rigging TodoModel manually ("
            + e + ")");
      }
    }
    SwappModel swappModel = new SwappModel();
    SwappList list1 = new SwappList(new SwappItem("name1", "swapp1", "New", "info1"), new SwappItem("name2", "swapp1", "New", "info1"));
    SwappList list2 = new SwappList(new SwappItem("name3", "swapp2", "New", "info1"), new SwappItem("name4", "swapp2", "New", "info1"));
    swappModel.addSwappList(list1);
    swappModel.addSwappList(list2);    
    return swappModel;
  }
}
