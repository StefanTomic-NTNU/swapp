package swapp.ui;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swapp.core.SwappList;
import swapp.ui.RemoteSwappAccess;

public class RemoteSwappAccessTest {

  private WireMockConfiguration config;
  private WireMockServer wireMockServer;

  private RemoteSwappAccess swappModelAccess;
  private final static String defaultSwappModel = "{\"lists\":[{\"username\":\"username1\",\"items\":[{\"itemName\":\"item1\",\"itemUsername\":\"username1\",\"itemStatus\":\"New\",\"itemDescription\":\"info1\"},{\"itemName\":\"item2\",\"itemUsername\":\"username1\",\"itemStatus\":\"New\",\"itemDescription\":\"info2\"}]},{\"username\":\"username2\",\"items\":[{\"itemName\":\"item3\",\"itemUsername\":\"username2\",\"itemStatus\":\"New\",\"itemDescription\":\"info3\"}]}]}";
  

  @BeforeEach
  public void startWireMockServerAndSetup() throws URISyntaxException {
    config = WireMockConfiguration.wireMockConfig().port(8089);
    wireMockServer = new WireMockServer(config.portNumber());
    wireMockServer.start();
    WireMock.configureFor("localhost", config.portNumber());
    swappModelAccess = new RemoteSwappAccess(new URI("http://localhost:" + wireMockServer.port() + "/swapp"));
  }

  @Test
  public void testGetTodoListNames() {
    stubFor(get(urlEqualTo("/swapp"))
        .withHeader("Accept", equalTo("application/json"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(defaultSwappModel)
        )
    );
    Collection<SwappList> swappLists = swappModelAccess.getAllSwappLists();
    assertEquals(2, swappLists.size());
    Iterator<SwappList> it = swappLists.iterator();
    assertEquals(it.next().getUsername(), "username1");
    assertEquals(it.next().getUsername(), "username2");
  }

  @AfterEach
  public void stopWireMockServer() {
    wireMockServer.stop();
  }
}
