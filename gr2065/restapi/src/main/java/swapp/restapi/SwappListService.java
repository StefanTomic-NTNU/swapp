package swapp.restapi;

import javax.inject.Inject;
import javax.ws.rs.GET;
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

    /** * The root resource, i.e. /swapp * 
     * 
     * * @return the SwappItemList
     * 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SwappItemList getSwappList() {
        return swappList;
    }

    /**
     * Returns the SwappItem with the provided name 
     * (as a resource to support
     * chaining path elements). * This supports all requests referring to SwappItems
     * by name.
     * Note that the SwappItem needn't exist, since it can be a PUT. *
     * * @param name the name of the SwappItem
     */

    @Path("/{name}")
    public SwappItemResource getSwappItem(@PathParam("name") String name) {
        SwappItem swappItem = getSwappList().getSwappItem(name);
        LOG.debug("Sub-resource for SwappItem " + name + ": " + swappItem);
        return new SwappItemResource(swappList, name, swappItem);
    }
}
