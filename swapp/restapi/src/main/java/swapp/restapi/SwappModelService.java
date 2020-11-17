package swapp.restapi;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swapp.core.SwappList;
import swapp.core.SwappModel;

@Path(SwappModelService.SWAPP_MODEL_SERVICE_PATH)
public class SwappModelService {

  public static final String SWAPP_MODEL_SERVICE_PATH = "swapp";

  private static final Logger LOG = LoggerFactory.getLogger(SwappModelService.class);

  @Inject
  private SwappModel swappModel;

  @Inject
  private SaveHelper saveHelper;

  public void updateServer(SwappModel swappModel) {
    saveHelper.write(swappModel);
  }

  /**
   * The root resource, i.e. /swapp
   *
   * @return the swappModel
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SwappModel getSwappModel() {
    LOG.debug("getSwappModel({})");
    return this.swappModel;
  }

  /**
   * Returns the swappList with the provided name (as a resource to support chaining path elements).
   * This supports all requests referring to swappLists by name. Note that the swappList needn't
   * exist, since it can be a PUT.
   *
   * @param name the name of the swappList
   */
  @Path("/{name}")
  public SwappListResource getSwappList(@PathParam("name") String name) {
    SwappList swappList = getSwappModel().getSwappList(name);
    LOG.debug("Sub-resource for SwappList " + name + ": " + swappList);
    return new SwappListResource(swappModel, name, swappList, this.saveHelper);
  }

  /**
   * Creates new anwapplist
   *
   * @param swappListArg swapplist
   * @return true if added, otherwise false
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putSwappList(SwappList swappListArg) {
    LOG.debug("putSwappList({})", swappListArg);
    boolean added = this.swappModel.putSwappList(swappListArg) == null;
    saveHelper.write(this.swappModel);
    return added;
  }

}
