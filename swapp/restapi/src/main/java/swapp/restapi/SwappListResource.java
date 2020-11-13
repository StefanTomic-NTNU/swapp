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
   * Initializes this TodoListResource with appropriate context information.
   * Each method will check and use what it needs.
   *
   * @param todoModel the TodoModel, needed for DELETE and rename
   * @param name the todo list name, needed for most requests
   * @param todoList the TodoList, or null, needed for PUT
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
    LOG.debug("getTodoList({})", name);
    return this.swappList;
  }

  /**
   * Replaces or adds a TodoList.
   *
   * @param todoListArg the todoList to add
   * @return true if it was added, false if it replaced
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putSwappList(SwappList swappListArg) {
    LOG.debug("putTodoList({})", swappListArg);
    return this.swappModel.putSwappList(swappListArg)==null;
  }

  /**
   * Replaces or adds a TodoList.
   *
   * @param todoListArg the todoList to add
   * @return true if it was added, false if it replaced
   *//**
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SwappList putSwappList(String swappListArg) {
    LOG.debug("putTodoList({})", swappListArg);
    return this.swappModel.addSwappList(swappListArg);
  }/**

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem addSwappItem(SwappItem item) {
    LOG.debug("addSwappItem({})", item.getName());
    swappList.addSwappItem(item);
    return swappList.getSwappItem(item);
  }

  @DELETE
  @Path("/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem deleteSwappItem(@PathParam("name") String name) {
    LOG.debug("deleteSwappItem({})", name);
    SwappItem deletedItem = swappList.getSwappItem(name);
    swappList.removeSwappItem(name);
    return deletedItem;
  }


  @GET
  @Path("/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public SwappItem getSwappItem(@PathParam("name") String name){
    LOG.debug("getSwappItem({})", name);
    return this.swappList.getSwappItem(name);
  }  */


}
