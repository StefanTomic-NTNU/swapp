package swapp.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.json.SwappModule;

public class RemoteSwappAccess implements SwappDataAccess {

  private final URI endpointBaseUri;

  private ObjectMapper objectMapper;

  public RemoteSwappAccess(URI endpointBaseUri) {
    this.endpointBaseUri = endpointBaseUri;
    objectMapper = new ObjectMapper().registerModule(new SwappModule());
  }

  private String uriParam(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
  }

  private URI swappListUri(String name) {
    return endpointBaseUri.resolve(uriParam(name));
  }

  private URI swappItemUri(final String path) {
    try {
      return new URI(endpointBaseUri + path);
    } catch (final URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public void writeData() {
  }

  private SwappModel getSwappModel() {
    HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", "application/json").GET().build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      final String responseString = response.body();
      System.out.println("getSwappModel(" + ") response: " + responseString);
      return objectMapper.readValue(responseString, SwappModel.class);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public SwappItem getSwappItem(SwappItem item1) {
    HttpRequest request = HttpRequest.newBuilder(swappItemUri(item1.getUsername() + "/" + item1.getName()))
        .header("Accept", "application/json").GET().build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      System.out.println("getSwappList(" + ") response: " + responseString);
      return objectMapper.readValue(responseString, SwappItem.class);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public SwappList getSwappList(String name) {
    HttpRequest request = HttpRequest.newBuilder(swappListUri(name)).header("Accept", "application/json").GET().build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      System.out.println("getSwappList(" + name + ") response: " + responseString);
      return objectMapper.readValue(responseString, SwappList.class);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void putSwappListAtListLevel(SwappList swappList) {
    try {
      String json = objectMapper.writeValueAsString(swappList);
      HttpRequest request =
          HttpRequest.newBuilder(swappListUri(swappList.getUsername())).header("Accept", "application/json")
              .header("Content-Type", "application/json").PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      Boolean added = objectMapper.readValue(responseString, Boolean.class);
      if (added)
        System.out.println(added);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void putSwappListAtModelLevel(SwappList swappList) {
    try {
      String json = objectMapper.writeValueAsString(swappList);
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", "application/json")
          .header("Content-Type", "application/json").PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      Boolean added = objectMapper.readValue(responseString, Boolean.class);
      if (added)
        System.out.println(added);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeAllSwappItems(String username) {
    addNewSwappList(username);
  }

  @Override
  public void addNewSwappList(String name) {
    SwappList newList = new SwappList(name);
    putSwappListAtModelLevel(newList);
  }

  public void addSwappList(SwappList newList) {
    putSwappListAtModelLevel(newList);
  }

  public void replaceSwappList(SwappList newList) {
    putSwappListAtListLevel(newList);
  }

  @Override
  public void removeSwappItem(SwappItem swappItem) throws Exception {
    try {
      HttpRequest request = HttpRequest.newBuilder(swappItemUri(swappItem.getUsername() + "/" + swappItem.getName()))
          .header("Accept", "application/json").DELETE().build();
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItem removed = objectMapper.readValue(responseString, SwappItem.class);
      if (removed == null)
        throw new Exception();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  // TODO remove exception
  @Override
  public void addSwappItem(SwappItem item) throws Exception {
    try {
      String json = objectMapper.writeValueAsString(item);
      HttpRequest request =
          HttpRequest.newBuilder(swappListUri(item.getUsername())).header("Accept", "application/json")
              .header("Content-Type", "application/json").POST(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItem swappItemRes = objectMapper.readValue(responseString, SwappItem.class);
      if (swappItemRes == null)
        throw new Exception();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void putSwappItem(SwappItem newItem) throws Exception {
    try {
      String json = objectMapper.writeValueAsString(newItem);
      HttpRequest request = HttpRequest.newBuilder(swappItemUri(newItem.getUsername() + "/" + newItem.getName()))
          .header("Accept", "application/json").header("Content-Type", "application/json")
          .PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      if (responseString == null)
        throw new RuntimeException();
      // SwappItem removed = objectMapper.readValue(responseString, SwappItem.class);
      // if (removed==null) throw new Exception();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void changeSwappItem(SwappItem newItem) throws Exception {
    putSwappItem(newItem);
  }

  /**
   * 
   * Notifies that the TodoList has changed, e.g. TodoItems have been mutated, added or removed.
   *
   * @param todoList the TodoList that has changed
   * 
   */

  @Override
  public List<SwappItem> getSwappItemByStatus(String status) {
    return getSwappModel().getSwappItemsByStatus(status);
  }

  @Override
  public List<SwappItem> getAllSwappItems() {
    return getSwappModel().getSwappItems();
  }

  @Override
  public List<SwappItem> getSwappItemByUser(String user) {
    return getSwappList(user).getSwappItems();
  }

  @Override
  public boolean isItemChanged(SwappItem newItem) {
    return getSwappModel().isItemChanged(newItem);
  }

  @Override
  public boolean hasSwappItem(String username, String itemname) {
    return getSwappList(username).hasSwappItem(itemname);
  }

  @Override
  public boolean hasSwappList(String name) {
    return getSwappModel().hasSwappList(name);
  }

  @Override
  public Collection<SwappList> getAllSwappLists() {
    return getSwappModel().getSwappLists();
  }

}
