package swapp.restapi;

import javax.inject.Inject;
import javax.ws.rs.GET;
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

  /**
   * The root resource, i.e. /todo
   *
   * @return the TodoModel
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SwappModel getSwappModel() {
    return this.swappModel;
  }

  /**
   * Returns the TodoList with the provided name
   * (as a resource to support chaining path elements).
   * This supports all requests referring to TodoLists by name.
   * Note that the TodoList needn't exist, since it can be a PUT.
   *
   * @param name the name of the todo list
   */
  @Path("/{name}")
  public SwappListResource getSwappList(@PathParam("name") String name) {
    SwappList swappList = getSwappModel().getSwappList(name);
    LOG.debug("Sub-resource for TodoList " + name + ": " + swappList);
    return new SwappListResource(swappModel, name, swappList);
  }
}