package swapp.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.core.SwappItem;

/**
 * Used for all requests referring to TodoLists by name.
 */
public class SwappListResource {

  private static final Logger LOG = LoggerFactory.getLogger(SwappListResource.class);

  private final SwappModel swappModel;
  private final String name;
  private final SwappList swappList;

  /**
   * Initializes this TodoListResource with appropriate context information. Each
   * method will check and use what it needs.
   *
   * @param todoModel the TodoModel, needed for DELETE and rename
   * @param name      the todo list name, needed for most requests
   * @param todoList  the TodoList, or null, needed for PUT
   */
  public SwappListResource(SwappModel swappModel, String name, SwappList swappList) {
    this.swappModel = swappModel;
    this.name = name;
    this.swappList = swappList;
  }

  /**
   * Gets the corresponding TodoList.
   *
   * @return the corresponding TodoList
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SwappList getSwappList() {
    LOG.debug("getSwappList({})", name);
    return this.swappList;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem addSwappItem(SwappItem item) {
    LOG.debug("addSwappItem({})", item.getName());
    this.swappModel.addSwappItem(item);
    return item;
  }

  /**
   * Replaces or adds a TodoList.
   *
   * @param todoListArg the todoList to add
   * @return true if it was added, false if it replaced
   */
  // Delete all items
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putSwappList(SwappList swappListArg) {
    LOG.debug("putSwappList({})", swappListArg);
    return this.swappModel.putSwappList(swappListArg) == null;
  }

  /**
   * Replaces or adds a TodoList.
   *
   * @param todoListArg the todoList to add
   * @return true if it was added, false if it replaced
   */
  /**
   * @PUT
   * @Produces(MediaType.APPLICATION_JSON) public boolean putSwappList() { return
   *                                       putSwappList(new SwappList(name)); }
   */

  @Path("/{name}")
  public SwappItemResource getSwappItem(@PathParam("name") String name) {
    SwappItem swappItem = getSwappList().getSwappItem(name);
    LOG.debug("Sub-resource for SwappItem " + name + ": " + swappItem);
    return new SwappItemResource(this.swappModel, this.swappList, name, swappItem);
  }

}
