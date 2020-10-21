package swapp.restserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappPersistence;
import swapp.restapi.SwappListService;

public class SwappConfig extends ResourceConfig {
  private SwappItemList swappList;

  /**
   * * Initialize this SwappConfig. * * @param swappList swappList instance to serve
   */
  public SwappConfig(SwappItemList swappList) {
    setSwappList(swappList);
    register(SwappListService.class);
    register(SwappModuleObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(SwappConfig.this.swappList);
      }
    });
  }

  /** * Initialize this TodoConfig with a default TodoModel. */
  public SwappConfig() {
    this(createDefaultSwappList());
  }

  public SwappItemList getSwappList() {
    return swappList;
  }

  public void setSwappList(SwappItemList swappList) {
    this.swappList = swappList;
  }

  private static SwappItemList createDefaultSwappList() {
    SwappPersistence swappPersistence = new SwappPersistence();
    URL url = SwappConfig.class.getResource("default-swapplist.json");
    if (url != null) {
      try (Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
        return swappPersistence.readSwappList(reader);
      } catch (IOException e) {
        System.out.println("Couldn't read default-todomodel.json, so rigging TodoModel manually (" + e + ")");
      }
    }
    SwappItemList swappList = new SwappItemList();
    swappList.addItem(new SwappItem("swapp1"));
    swappList.addItem(new SwappItem("swapp2"));
    return swappList;
  }
}
