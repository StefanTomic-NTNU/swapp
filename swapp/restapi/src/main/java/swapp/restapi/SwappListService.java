package swapp.restapi;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.File;
import java.io.Writer;
//import java.lang.invoke.PolymorphicSignature;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappPersistence;

@Path(SwappListService.SWAPP_LIST_SERVICE_PATH)
public class SwappListService {
  public static final String SWAPP_LIST_SERVICE_PATH = "swapp";
  private static final Logger LOG = LoggerFactory.getLogger(SwappListService.class);
  
  @Inject
  private SwappItemList swappList;

  private File file = Paths.get(System.getProperty("user.home"), "RemoteSwappItems.json").toFile();
  
  private void updateServer(SwappItemList swappItemList){
    //adds value to default-swapplist.json
  Writer writer = null;
  SwappPersistence swappPersistence = new SwappPersistence();
  try {
    writer = new FileWriter(file, StandardCharsets.UTF_8);
    swappPersistence.writeSwappList(swappList, writer);
  } catch (IOException ioex) {
    System.err.println("Feil med fillagring.");
  } finally {
    try {
      if (writer != null) {
        writer.close();
      }
    } catch (IOException e) {
      System.err.println("Feil med fillagring..");
    }
  }
}

  /**
   * The root resource, i.e. /swapp 
   * 
   * @return the SwappItemList
   * 
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItemList getSwappList() {
    return swappList;
  }

  @PUT
  @Consumes
  @Produces
  public SwappItemList putSwappList(SwappItemList swappItemList){
    LOG.debug("putSwappList({})", swappItemList);
    SwappItemList newSwappItemlist = this.swappList.putSwappList(swappItemList);
    updateServer(newSwappItemlist);
    return newSwappItemlist;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem addSwappItem(SwappItem item) {
    LOG.debug("addSwappItem({})", item.getName());
    swappList.addItem(item);
    updateServer(swappList);
    return swappList.getSwappItem(item);
  }

  @DELETE
  @Path("/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public String deleteSwappItem(@PathParam("name") String name) {
    LOG.debug("deleteSwappItem({})", name);
    swappList.deleteSwappItem(name);
    updateServer(swappList);
    return name;
  }


  @GET
  @Path("/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem getSwappItem(@PathParam("name") String name){
    LOG.debug("getSwappItem({})", name);
    return this.swappList.getSwappItem(name);
  }  


}
