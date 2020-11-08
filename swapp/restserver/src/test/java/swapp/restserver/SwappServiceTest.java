package swapp.restserver;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.Iterator;

import javax.ws.rs.client.Entity;
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
import swapp.core.SwappItem;
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
  public void testGet_swapp() {
    Response getResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    try {
      SwappItemList swappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappItemList.class);
      Iterator<SwappItem> it = swappList.iterator();
      assertTrue(it.hasNext());
      SwappItem swappItem1 = it.next();
      assertTrue(it.hasNext());
      SwappItem swappItem2 = it.next();
      assertEquals(swappItem1.getName(), "swapp1");
      assertEquals(swappItem1.getStatus(), "New");
      assertEquals(swappItem1.getDescription(), "bla bla");
      assertEquals(swappItem1.getContactInfo(), "contactInfo");
      assertFalse(it.hasNext());
      assertEquals("swapp2", swappItem2.getName());
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetPutAndDelete() throws JsonProcessingException {
    SwappItemList other = new SwappItemList(new SwappItem("swapp1put", "New", "bla bla", "contactInfo"), new SwappItem("swapp2put", "Used", "bla bla", "contactInfo"));
    Response putResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(Entity.entity(objectMapper.writeValueAsString(other), MediaType.APPLICATION_JSON));
    assertEquals(200, putResponse.getStatus());
    try {
      SwappItemList newSwappList = objectMapper.readValue(putResponse.readEntity(String.class), SwappItemList.class);
      Iterator<SwappItem> it = newSwappList.iterator();
      assertTrue(it.hasNext());
      SwappItem swappItem1 = it.next();
      assertTrue(it.hasNext());
      SwappItem swappItem2 = it.next();
      assertFalse(it.hasNext());
      assertEquals(swappItem1.getName(), "swapp1put");
      assertEquals(swappItem1.getStatus(), "New");
      assertEquals(swappItem1.getDescription(), "bla bla");
      assertEquals(swappItem1.getContactInfo(), "contactInfo");
      assertEquals(swappItem2.getName(), "swapp2put");
      assertEquals(swappItem2.getStatus(), "Used");
      assertEquals(swappItem2.getDescription(), "bla bla");
      assertEquals(swappItem2.getContactInfo(), "contactInfo");
      Response deleteResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
        .path("swapp1put")
        .request("application/json; charset=UTF-8")
        .delete();
      Response getResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
      SwappItemList getSwappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappItemList.class);
      assertTrue(getSwappList.getSwappItems().size() == 1);
      //String deletedItem = objectMapper.readValue(deleteResponse.readEntity(String.class), String.class);
      //assertEquals("swapp1put", deletedItem);
      assertTrue(getSwappList.getSwappItems().get(0).getName().equals("swapp2put"));

    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }

@Test
  public void testPost() throws JsonProcessingException {
    SwappItem swappItem = new SwappItem("testPost", "New", "postDescription", "postContactInfo");
    Response postResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .post(Entity.entity(objectMapper.writeValueAsString(swappItem), MediaType.APPLICATION_JSON));
    assertEquals(200, postResponse.getStatus());
    try {
      SwappItem swappItem2 = objectMapper.readValue(postResponse.readEntity(String.class), SwappItem.class);
      assertTrue(swappItem2.allAttributesEquals(swappItem));
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }


  @Test
  public void testGetSwappItem() throws JsonProcessingException {
    Response getResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH).path("swapp1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    try {
      SwappItem swappItem = objectMapper.readValue(getResponse.readEntity(String.class), SwappItem.class);
      assertEquals("swapp1", swappItem.getName());
      assertNotEquals(swappItem.getName(), "swapp2");
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
    
  }


}
