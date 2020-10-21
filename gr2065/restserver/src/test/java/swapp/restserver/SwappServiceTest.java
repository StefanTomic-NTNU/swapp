package swapp.restserver;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swapp.core.Swappitem;
import swapp.core.SwappItemList;
import swapp.restapi.SwappListService;

public class SwappServiceTest extends JerseyTest {
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected ResourceConfig configure() {
        final SwappConfig config = new SwappConfig();
        if (shouldLog()) {
            enable(TestProperties.LOG_TRAFFIC);
            enable(TestProperties.DUMP_ENTITY);
            config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
        }
        return config;
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new GrizzlyTestContainerFactory();
    }

    private ObjectMapper objectMapper;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        objectMapper = new SwappModuleObjectMapperProvider().getContext(getClass());
    }

    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGet_todo() {
        Response getResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
        assertEquals(200, getResponse.getStatus());
        try {
            SwappItemList swappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappItemList.class);
            Iterator<Swappitem> it = swappList.iterator();
            assertTrue(it.hasNext());
            SwappItem swappItem1 = it.next();
            assertTrue(it.hasNext());
            SwappItem swappItem2 = it.next();
            assertFalse(it.hasNext());
            assertEquals("swapp1", swappItem1.getName());
            assertEquals("swapp2", swappItem2.getName());
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGet_swapp_swapp1() {
        Response getResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH).path("swapp1")
                .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
        assertEquals(200, getResponse.getStatus());
        try {
            SwappItem swappItem = objectMapper.readValue(getResponse.readEntity(String.class),
                    SwappItem.class);
            assertEquals("swapp1", swappItem.getName());
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
    }
}
