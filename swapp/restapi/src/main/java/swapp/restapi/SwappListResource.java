package swapp.restapi;

import javax.ws.rs.Consumes;
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
import swapp.core.SwappList;
import swapp.core.SwappModel;

/**
 * Used for all requests referring to SwappLists by name.
 */
public class SwappListResource {

  private static final Logger LOG = LoggerFactory.getLogger(SwappListResource.class);

  private final SwappModel swappModel;
  private final String name;
  private final SwappList swappList;
  private final SaveHelper saveHelper;

  /**
   * Initializes this SwappListResource with appropriate context information. Each method will check
   * and use what it needs.
   *
   * @param swappModel the swappModel, needed for DELETE and rename
   * @param name      the swapp list name, needed for most requests
   * @param swappList  the swappList, or null, needed for PUT
   * @param saveHelper  the saveHelper, needed for saving on DELETE, PUT and POST-request
   */
  public SwappListResource(SwappModel swappModel, String name, 
      SwappList swappList, SaveHelper saveHelper) {
    this.swappModel = swappModel;
    this.name = name;
    this.swappList = swappList;
    this.saveHelper = saveHelper;
  }

  /**
   * Gets the corresponding SwappList.
   *
   * @return the corresponding SwappList
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SwappList getSwappList() {
    LOG.debug("getSwappList({})", name);
    return this.swappList;
  }

  /**
   * Creates new swappItem belonging to this SwappList.
   *
   * @param item the SwappItem to add
   * @return item
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem addSwappItem(SwappItem item) {
    LOG.debug("addSwappItem({})", item.getName());
    this.swappModel.addSwappItem(item);
    saveHelper.write(this.swappModel);
    return item;
  }

  /**
   * Replaces or adds a SwappList.
   *
   * @param swappListArg the SwappList to add
   * @return true if it was added, false if it replaced
   */
  // Delete all items
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putSwappList(SwappList swappListArg) {
    LOG.debug("putSwappList({})", swappListArg);
    boolean added = this.swappModel.putSwappList(swappListArg) == null;
    saveHelper.write(this.swappModel);
    return added;
  }

  /**
   * Returns the SwappItem with the provided name (as a resource to support chaining path elements).
   * This supports all requests referring to SwappItems by name. Note that the SwappItem needn't
   * exist, since it can be a PUT.
   *
   * @param name the name of the swappItem
   */
  @Path("/{name}")
  public SwappItemResource getSwappItem(@PathParam("name") String name) {
    SwappItem swappItem = getSwappList().getSwappItem(name);
    LOG.debug("Sub-resource for SwappItem " + name + ": " + swappItem);
    return new SwappItemResource(this.swappModel, name, swappItem, this.saveHelper);
  }

}
