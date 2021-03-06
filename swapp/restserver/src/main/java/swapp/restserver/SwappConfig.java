package swapp.restserver;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.json.SwappPersistence;
import swapp.restapi.SaveHelper;
import swapp.restapi.SwappModelService;

public class SwappConfig extends ResourceConfig {

  private SwappModel swappModel;
  private SaveHelper saveHelper;
  private String fileString;
  private static File file = 
      Paths.get(System.getProperty("user.home"), "RemoteSwappItems.json").toFile();

  /**
   * Initialize this SwappConfig.
   *
   * @param swappModel swappModel instance to serve
   */
  public SwappConfig(SwappModel swappModel, boolean test) {
    setSwappModel(swappModel);
    saveHelper = new SaveHelper();
    if (test) {
      fileString = "TestSwappItems.json";
    } else {
      fileString = "RemoteSwappItems.json";
    }
    setSaveHelper(fileString);
    register(SwappModelService.class);
    register(SwappModuleObjectMapperProvider.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(SwappConfig.this.swappModel);
        bind(SwappConfig.this.saveHelper);
      }
    });
  }

  private void setSaveHelper(String fileString) {
    this.saveHelper.setFilePath(fileString);
  }

  /**
   * Initialize this SwappConfig with a default SwappModel.
   */
  public SwappConfig() {
    this(createDefaultSwappModel(), false);
  }

  public SwappModel getSwappModel() {
    return swappModel;
  }

  public void setSwappModel(SwappModel swappModel) {
    this.swappModel = swappModel;
  }

  private static SwappModel createDefaultSwappModel() {
    SwappPersistence swappPersistence = new SwappPersistence();
    try (Reader reader = new FileReader(file, StandardCharsets.UTF_8)) {
      return swappPersistence.readSwappModel(reader);
    } catch (IOException e) {
      System.out.println(
          "Couldn't read default-swappmodel.json, so rigging SwappModel manually (" + e + ")"
      );
    }
    SwappModel swappModel = new SwappModel();
    swappModel.addSwappList(new SwappList(new SwappItem("item1", "username1", "New", "info1"),
        new SwappItem("item2", "username1", "New", "info2")));
    swappModel.addSwappList(new SwappList(new SwappItem("item3", "username2", "New", "info3")));
    return swappModel;
  }
}
