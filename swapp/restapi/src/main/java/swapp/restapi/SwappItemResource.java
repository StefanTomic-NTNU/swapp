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
public class SwappItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(SwappListResource.class);

    private final SwappModel swappModel;
    private final String name;
    private final SwappList swappList;
    private final SwappItem swappItem;

    /**
     * Initializes this TodoListResource with appropriate context information. Each
     * method will check and use what it needs.
     *
     * @param todoModel the TodoModel, needed for DELETE and rename
     * @param name      the todo list name, needed for most requests
     * @param todoList  the TodoList, or null, needed for PUT
     */
    public SwappItemResource(SwappModel swappModel, SwappList swappList, String name, SwappItem swappItem) {
        this.swappModel = swappModel;
        this.swappList = swappList;
        this.name = name;
        this.swappItem = swappItem;
    }

    /**
     * Gets the corresponding TodoList.
     *
     * @return the corresponding TodoList
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SwappItem getSwappItem() {
        LOG.debug("getSwappItem({})", name);
        return this.swappItem;
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
    public SwappItem putSwappItem(SwappItem newItem) {
        LOG.debug("putTodoList({})", newItem);
        return this.swappModel.changeSwappItem(this.swappItem.getUsername(), this.swappItem, newItem);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public SwappItem deleteSwappItem() {
        LOG.debug("deleteSwappItem({})", name);
        this.swappModel.removeSwappItem(this.swappItem);
        return this.getSwappItem();
    }

}
