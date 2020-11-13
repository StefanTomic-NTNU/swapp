package swapp.restserver;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.restapi.SwappModelService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.Writer;
//import java.lang.invoke.PolymorphicSignature;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


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
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try {
      SwappModel swappModel = objectMapper.readValue(getResponse.readEntity(String.class), SwappModel.class);
      System.out.println(swappModel.getSwappItems());
      Iterator<SwappList> it = swappModel.iterator();
      assertTrue(it.hasNext());
      SwappList swappList1 = it.next();
      assertTrue(it.hasNext());
      SwappList swappList2 = it.next();
      assertFalse(it.hasNext());
      assertEquals("username1", swappList1.getUsername());
      assertEquals("username2", swappList2.getUsername());
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  } 

  
  @Test 
  public void testGet_swapp_swapp1() {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH)
        .path("username1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try {
      SwappList swappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappList.class);
      System.out.println(swappList.getSwappItems());
      assertEquals("username1", swappList.getUsername());
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }
/**
  @Test
  public void testGetSwappItem() throws JsonProcessingException {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("swapp1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    try {
      SwappItem swappItem = objectMapper.readValue(getResponse.readEntity(String.class), SwappItem.class);
      assertEquals("item1", swappItem.getName());
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    } 
  }*/

/**
  @Test
  public void testGetPutAndDelete() throws JsonProcessingException {
    SwappList other = new SwappList(new SwappItem("swapp1put", "New", "bla bla", "contactInfo"), new SwappItem("swapp2put", "Used", "bla bla", "contactInfo"));
    Response putResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(Entity.entity(objectMapper.writeValueAsString(other), MediaType.APPLICATION_JSON));
    assertEquals(200, putResponse.getStatus());
    try {
      SwappList newSwappList = objectMapper.readValue(putResponse.readEntity(String.class), SwappList.class);
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
      SwappList getSwappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappList.class);
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
    
  }*/


}
