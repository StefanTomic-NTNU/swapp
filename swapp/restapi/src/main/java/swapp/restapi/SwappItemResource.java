package swapp.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swapp.core.SwappItem;
import swapp.core.SwappModel;

/**
 * Used for all requests referring to SwappItem by name.
 */
public class SwappItemResource {

  private static final Logger LOG = LoggerFactory.getLogger(SwappListResource.class);

  private final SwappModel swappModel;
  private final String name;
  private final SwappItem swappItem;
  private final SaveHelper saveHelper;

  /**
   * Initializes this SwappItemResource with appropriate context information. Each method will check
   * and use what it needs.
   *
   * @param swappModel the swappModel, needed for DELETE and rename
   * @param name      the swapp item name, needed for most requests
   * @param swappItem  the swappItem, or null, needed for PUT
   * @param saveHelper  the saveHelper, needed for saving on DELETE, PUT and POST-request
   */
  public SwappItemResource(SwappModel swappModel, String name, SwappItem swappItem,
      SaveHelper saveHelper) {
    this.swappModel = swappModel;
    this.name = name;
    this.swappItem = swappItem;
    this.saveHelper = saveHelper;
  }

  /**
   * Gets the corresponding SwappItem.
   *
   * @return the corresponding SwappItem
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem getSwappItem() {
    LOG.debug("getSwappItem({})", name);
    return this.swappItem;
  }

  /**
   * Adds a SwappItem.
   *
   * @param newItem swappItem that will be added
   * @return newItem
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem putSwappItem(SwappItem newItem) {
    LOG.debug("putSwappItem({})", newItem);
    this.swappModel.changeSwappItem(this.swappItem.getUsername(), this.swappItem, newItem);
    saveHelper.write(this.swappModel);
    return newItem;
  }

  /**
   * Deletes the SwappItem.
   *
   * @return the SwappItem
   */
  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem deleteSwappItem() {
    LOG.debug("deleteSwappItem({})", name);
    this.swappModel.removeSwappItem(this.swappItem);
    saveHelper.write(this.swappModel);
    return this.swappItem;
  }

}
