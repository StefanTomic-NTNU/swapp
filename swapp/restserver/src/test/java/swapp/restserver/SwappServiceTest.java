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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.json.SwappPersistence;
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

  private SwappModel defaultModel = new SwappModel();
  private SwappList defaultList1;
  private SwappList defaultList2;

  @Override
  protected ResourceConfig configure() {
    final SwappConfig config = new SwappConfig(createDefaultSwappModel(), false);
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

  private static SwappModel createDefaultSwappModel() {
    SwappModel swappModel = new SwappModel();
    swappModel.addSwappList(new SwappList(new SwappItem("item1", "username1", "New", "info1"),
        new SwappItem("item2", "username1", "New", "info2")));
    swappModel.addSwappList(new SwappList(new SwappItem("item3", "username2", "New", "info3")));
    return swappModel;
  }

  @Override
  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    objectMapper = new SwappModuleObjectMapperProvider().getContext(getClass());
    defaultModel = new SwappModel();
    defaultList1 = new SwappList(new SwappItem("item1", "username1", "New", "info1"),
        new SwappItem("item2", "username1", "New", "info2"));
    defaultList2 = new SwappList(new SwappItem("item3", "username2", "New", "info3"));
    defaultModel.addSwappList(defaultList1);
    defaultModel.addSwappList(defaultList2);
  }

  @AfterEach
  public void tearDown() throws Exception {
    super.tearDown();
  }

  private SwappModel getDefaultSwappModel() throws JsonMappingException, JsonProcessingException {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    return objectMapper.readValue(getResponse.readEntity(String.class), SwappModel.class);
  }

  private SwappList getDefaultSwappList(String username) throws JsonMappingException, JsonProcessingException {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path(username)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    return objectMapper.readValue(getResponse.readEntity(String.class), SwappList.class);
  }

  @Test
  public void testPost() throws JsonProcessingException {
    SwappItem postItem = new SwappItem("item8", "username2", "New", "info3");
    Response postResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username2")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .post(Entity.entity(objectMapper.writeValueAsString(postItem), MediaType.APPLICATION_JSON));
    assertEquals(200, postResponse.getStatus());
    defaultList2.addSwappItem(postItem);
    checkSwappList(defaultList2, getDefaultSwappList("username2"));
  }

  @Test
  public void testGetSwappItem() throws JsonMappingException, JsonProcessingException {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1/item1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    SwappItem swappItem = objectMapper.readValue(getResponse.readEntity(String.class), SwappItem.class);
    assertTrue(swappItem.allAttributesEquals("item1", "New", "info1", "username1"));
  }

  @Test
  public void testGetPutSwappItem() throws JsonMappingException, JsonProcessingException {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1/item1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    SwappItem swappItem = objectMapper.readValue(getResponse.readEntity(String.class), SwappItem.class);
    assertTrue(swappItem.allAttributesEquals("item1", "New", "info1", "username1"));
    SwappItem changedItem = new SwappItem("item1", "username1", "Used", "newInfo");
    Response putResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1/item1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(Entity.entity(objectMapper.writeValueAsString(changedItem), MediaType.APPLICATION_JSON));
    assertEquals(200, putResponse.getStatus());
    getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1/item1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    swappItem = objectMapper.readValue(getResponse.readEntity(String.class), SwappItem.class);
    assertTrue(swappItem.allAttributesEquals("item1", "Used", "newInfo", "username1"));
    assertFalse(swappItem.allAttributesEquals("item1", "New", "info1", "username1"));
  }

  @Test
  public void testDeleteSwappItem() throws JsonMappingException, JsonProcessingException {
    Response deleteResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1/item1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").delete();
    assertEquals(200, deleteResponse.getStatus());
    SwappItem swappItem = objectMapper.readValue(deleteResponse.readEntity(String.class), SwappItem.class);
    defaultList1.removeSwappItem("item1");
    checkSwappList(getDefaultSwappList("username1"), defaultList1);
    assertTrue(swappItem.allAttributesEquals("item1", "New", "info1", "username1"));
  }

  @Test
  public void testGet_swapp() {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    try {
      SwappModel swappModel = objectMapper.readValue(getResponse.readEntity(String.class), SwappModel.class);
      checkSwappModel(defaultModel, swappModel);
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void test_PutNewList() throws JsonProcessingException {
    SwappList newList = new SwappList(new SwappItem("newName", "username3", "New", "newInfo"),
        new SwappItem("newName2", "username3", "New", "newInfo2"));
    Response putResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(Entity.entity(objectMapper.writeValueAsString(newList), MediaType.APPLICATION_JSON));
    assertEquals(200, putResponse.getStatus());
    boolean res = objectMapper.readValue(putResponse.readEntity(String.class), Boolean.class);
    assertEquals(res, true);
    defaultModel.addSwappList(newList);
    checkSwappModel(defaultModel, getDefaultSwappModel());
  }

  @Test
  public void testGet_swapp_swapp_list() {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    try {
      SwappList swappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappList.class);
      System.out.println(swappList.getSwappItems());
      assertEquals("username1", swappList.getUsername());
    } catch (JsonProcessingException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void test_PutAT_List_Level_swapp() throws JsonProcessingException {
    SwappList newList = new SwappList(new SwappItem("newName", "username1", "New", "newInfo"),
        new SwappItem("newName2", "username1", "New", "newInfo2"));
    Response putResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(Entity.entity(objectMapper.writeValueAsString(newList), MediaType.APPLICATION_JSON));
    assertEquals(200, putResponse.getStatus());
    boolean res = objectMapper.readValue(putResponse.readEntity(String.class), Boolean.class);
    assertEquals(res, false);
  }

  @Test
  public void test_Get_Put_at_List_Level_Get_swapp() throws JsonProcessingException {
    Response getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username2")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    SwappList swappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappList.class);
    assertEquals("username2", swappList.getUsername());
    Iterator<SwappItem> it1 = swappList.iterator();
    assertTrue(it1.hasNext());
    SwappItem swappItem1 = it1.next();
    assertEquals(swappItem1.getName(), "item3");
    assertFalse(it1.hasNext());
    SwappList newList = new SwappList(new SwappItem("newName", "username2", "New", "newInfo"),
        new SwappItem("newName2", "username2", "New", "newInfo2"));
    Response putResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username1")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(Entity.entity(objectMapper.writeValueAsString(newList), MediaType.APPLICATION_JSON));
    assertEquals(200, putResponse.getStatus());
    boolean res = objectMapper.readValue(putResponse.readEntity(String.class), Boolean.class);
    assertEquals(res, false);
    getResponse = target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("username2")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8").get();
    assertEquals(200, getResponse.getStatus());
    swappList = objectMapper.readValue(getResponse.readEntity(String.class), SwappList.class);
    assertEquals("username2", swappList.getUsername());
    Iterator<SwappItem> it2 = swappList.iterator();
    assertTrue(it2.hasNext());
    SwappItem swappItem2 = it2.next();
    assertEquals(swappItem2.getName(), "newName");
    assertTrue(it2.hasNext());
    SwappItem swappItem3 = it2.next();
    assertEquals(swappItem3.getName(), "newName2");
    assertFalse(it1.hasNext());
  }

  public void checkSwappList(SwappList toCheckList, SwappList correctList) {
    int lentoCheckList = toCheckList.getSwappItems().size();
    int lencorrectkList = toCheckList.getSwappItems().size();
    assertEquals(lencorrectkList, lentoCheckList);
    assertTrue(lentoCheckList > 0);
    int i;
    Iterator<SwappItem> it1 = toCheckList.iterator();
    Iterator<SwappItem> it2 = correctList.iterator();
    assertTrue(it1.hasNext());
    assertTrue(it2.hasNext());
    for (i = 0; i < lencorrectkList; i++) {
      assertTrue(it1.hasNext());
      assertTrue(it2.hasNext());
      assertTrue(it1.next().allAttributesEquals(it2.next()));
    }
    assertFalse(it1.hasNext());
    assertFalse(it2.hasNext());
  }

  public void checkSwappModel(SwappModel model1, SwappModel model2) {
    int lenmodel1 = model1.getSwappLists().size();
    int lenmodel2 = model1.getSwappLists().size();
    assertEquals(lenmodel1, lenmodel2);
    assertTrue(lenmodel1 > 0);
    int i;
    Iterator<SwappList> it1 = model1.iterator();
    Iterator<SwappList> it2 = model2.iterator();
    assertTrue(it1.hasNext());
    assertTrue(it2.hasNext());
    for (i = 0; i < lenmodel1; i++) {
      assertTrue(it1.hasNext());
      assertTrue(it2.hasNext());
      SwappList list1 = it1.next();
      SwappList list2 = it2.next();
      assertEquals(list1.getUsername(), list2.getUsername());
      checkSwappList(list1, list2);
    }
    assertFalse(it1.hasNext());
    assertFalse(it2.hasNext());
  }

  /**
   * public void checkSwappModel(SwappModel toCheckItem, SwappList correctItem) {
   * int lentoCheckItem = toCheckItem.getSwappItems().size(); int lencorrectkItem
   * = toCheckItem.getSwappItems().size(); assertEquals(lencorrectkItem,
   * lentoCheckItem); assertTrue(lentoCheckItem > 0); int i; Iterator<SwappItem>
   * it1 = toCheckItem.iterator(); Iterator<SwappItem> it2 =
   * correctItem.iterator(); assertTrue(it1.hasNext()); assertTrue(it2.hasNext());
   * for (i = 0; i < lencorrectkItem; i++) { assertTrue(it1.hasNext());
   * assertTrue(it2.hasNext());
   * assertTrue(it1.next().allAttributesEquals(it2.next())); }
   * assertFalse(it1.hasNext()); assertFalse(it2.hasNext()); }
   */

  /**
   * @Test public void testGetSwappItem() throws JsonProcessingException {
   *       Response getResponse =
   *       target(SwappModelService.SWAPP_MODEL_SERVICE_PATH).path("swapp1")
   *       .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER
   *       + "=UTF-8").get(); assertEquals(200, getResponse.getStatus()); try {
   *       SwappItem swappItem =
   *       objectMapper.readValue(getResponse.readEntity(String.class),
   *       SwappItem.class); assertEquals("item1", swappItem.getName()); } catch
   *       (JsonProcessingException e) { fail(e.getMessage()); } }
   */

  /**
   * @Test public void testGetPutAndDelete() throws JsonProcessingException {
   *       SwappList other = new SwappList(new SwappItem("swapp1put", "New", "bla
   *       bla", "contactInfo"), new SwappItem("swapp2put", "Used", "bla bla",
   *       "contactInfo")); Response putResponse =
   *       target(SwappListService.SWAPP_LIST_SERVICE_PATH)
   *       .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER
   *       + "=UTF-8") .put(Entity.entity(objectMapper.writeValueAsString(other),
   *       MediaType.APPLICATION_JSON)); assertEquals(200,
   *       putResponse.getStatus()); try { SwappList newSwappList =
   *       objectMapper.readValue(putResponse.readEntity(String.class),
   *       SwappList.class); Iterator<SwappItem> it = newSwappList.iterator();
   *       assertTrue(it.hasNext()); SwappItem swappItem1 = it.next();
   *       assertTrue(it.hasNext()); SwappItem swappItem2 = it.next();
   *       assertFalse(it.hasNext()); assertEquals(swappItem1.getName(),
   *       "swapp1put"); assertEquals(swappItem1.getStatus(), "New");
   *       assertEquals(swappItem1.getDescription(), "bla bla");
   *       assertEquals(swappItem1.getContactInfo(), "contactInfo");
   *       assertEquals(swappItem2.getName(), "swapp2put");
   *       assertEquals(swappItem2.getStatus(), "Used");
   *       assertEquals(swappItem2.getDescription(), "bla bla");
   *       assertEquals(swappItem2.getContactInfo(), "contactInfo"); Response
   *       deleteResponse = target(SwappListService.SWAPP_LIST_SERVICE_PATH)
   *       .path("swapp1put") .request("application/json; charset=UTF-8")
   *       .delete(); Response getResponse =
   *       target(SwappListService.SWAPP_LIST_SERVICE_PATH)
   *       .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER
   *       + "=UTF-8").get(); SwappList getSwappList =
   *       objectMapper.readValue(getResponse.readEntity(String.class),
   *       SwappList.class); assertTrue(getSwappList.getSwappItems().size() == 1);
   *       //String deletedItem =
   *       objectMapper.readValue(deleteResponse.readEntity(String.class),
   *       String.class); //assertEquals("swapp1put", deletedItem);
   *       assertTrue(getSwappList.getSwappItems().get(0).getName().equals("swapp2put"));
   * 
   *       } catch (JsonProcessingException e) { fail(e.getMessage()); } }
   * 
   * @Test public void testPost() throws JsonProcessingException { SwappItem
   *       swappItem = new SwappItem("testPost", "New", "postDescription",
   *       "postContactInfo"); Response postResponse =
   *       target(SwappListService.SWAPP_LIST_SERVICE_PATH)
   *       .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER
   *       + "=UTF-8")
   *       .post(Entity.entity(objectMapper.writeValueAsString(swappItem),
   *       MediaType.APPLICATION_JSON)); assertEquals(200,
   *       postResponse.getStatus()); try { SwappItem swappItem2 =
   *       objectMapper.readValue(postResponse.readEntity(String.class),
   *       SwappItem.class);
   *       assertTrue(swappItem2.allAttributesEquals(swappItem)); } catch
   *       (JsonProcessingException e) { fail(e.getMessage()); } }
   * 
   * 
   * @Test public void testGetSwappItem() throws JsonProcessingException {
   *       Response getResponse =
   *       target(SwappListService.SWAPP_LIST_SERVICE_PATH).path("swapp1")
   *       .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER
   *       + "=UTF-8").get(); assertEquals(200, getResponse.getStatus()); try {
   *       SwappItem swappItem =
   *       objectMapper.readValue(getResponse.readEntity(String.class),
   *       SwappItem.class); assertEquals("swapp1", swappItem.getName());
   *       assertNotEquals(swappItem.getName(), "swapp2"); } catch
   *       (JsonProcessingException e) { fail(e.getMessage()); }
   * 
   *       }
   */

}
