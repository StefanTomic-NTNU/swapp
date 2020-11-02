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

@Path(SwappListService.SWAPP_LIST_SERVICE_PATH)
public class SwappListService {
  public static final String SWAPP_LIST_SERVICE_PATH = "swapp";
  private static final Logger LOG = LoggerFactory.getLogger(SwappListService.class);
  
  @Inject
  private SwappItemList swappList;

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
    return this.swappList.putSwappList(swappItemList);
  }
  



}
