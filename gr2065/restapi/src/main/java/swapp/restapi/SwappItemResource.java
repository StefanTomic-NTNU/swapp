package swapp.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;

/** * Used for all requests referring to TodoLists by name. */
public class SwappItemResource {
    private static final Logger LOG = LoggerFactory.getLogger(SwappItemResource.class);
    private final SwappItemList swappList;
    private final String name;
    private final SwappItem swappItem;

    /**
     * * Initializes this TodoListResource with appropriate context information. *
     * Each method will check and use what it needs. * * @param todoModel the
     * TodoModel, needed for DELETE and rename * @param name the todo list name,
     * needed for most requests * @param todoList the TodoList, or null, needed for
     * PUT
     */
    public SwappItemResource(SwappItemList swappList, String name, SwappItem swappItem) {
        this.swappList = swappList;
        this.name = name;
        this.swappItem = swappItem;
    }

    private void checkSwappItem() {
        if (this.swappItem == null) {
            throw new IllegalArgumentException("No SwappItem named \"" + name + "\"");
        }
    }

    /** * Gets the corresponding TodoList. * * @return the corresponding TodoList */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SwappItem getSwappItem() {
        checkSwappItem();
        LOG.debug("getSwappItem({})", name);
        return this.swappItem;
    }

    /**
     * * Replaces or adds a TodoList. * * @param todoListArg the todoList to add
     * * @return true if it was added, false if it replaced
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putSwappItem(SwappItem swappItemArg) {
        LOG.debug("putSwappItem({})", swappItemArg);
        return this.swappList.putSwappItem(swappItemArg) == null;
    }

    /** * Adds a TodoList with the given name, if it doesn't exist already. */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public boolean putSwappItem() {
        return putSwappItem(null);
    }

    /** * Renames the TodoList. * * @param newName the newName */
    @POST
    @Path("/rename")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean renameSwappItem(@QueryParam("newName") String newName) {
        checkSwappItem();
        if (this.swappList.getSwappItem(newName) != null) {
            throw new IllegalArgumentException("A TodoList named \"" + newName + "\" already exists");
        }
        this.swappItem.setName(newName);
        return true;
    }

    /** * Removes the TodoList. */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeSwappItem() {
        checkSwappItem();
        this.swappList.removeItem(this.swappItem);
        return true;
    }
}
