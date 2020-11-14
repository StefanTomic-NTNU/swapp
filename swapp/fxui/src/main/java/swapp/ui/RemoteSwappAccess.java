package swapp.ui;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import swapp.core.SwappItem;
import swapp.core.SwappList;
import swapp.core.SwappModel;
import swapp.json.SwappModule;

public class RemoteSwappAccess {

  private final URI endpointBaseUri;

  private ObjectMapper objectMapper;

  private SwappModel swappModel;

  public RemoteSwappAccess(URI endpointBaseUri) {
    this.endpointBaseUri = endpointBaseUri;
    objectMapper = new ObjectMapper().registerModule(new SwappModule());
    this.swappModel = getSwappModel();
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

  private SwappModel getSwappModel() {
    if (swappModel == null) {
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", "application/json").GET().build();
      try {
        final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
            HttpResponse.BodyHandlers.ofString());
        final String responseString = response.body();
        this.swappModel = objectMapper.readValue(responseString, SwappModel.class);
        System.out.println("TodoModel: " + this.swappModel);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return swappModel;
  }

  public SwappItem getSwappItem(SwappItem item1) {
    HttpRequest request = HttpRequest.newBuilder(swappItemUri(item1.getUsername() + "/" + item1.getName()))
        .header("Accept", "application/json").GET().build();
    try {
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      System.out.println("getSwappList(" + ") response: " + responseString);
      SwappItem swappItem = objectMapper.readValue(responseString, SwappItem.class);
      return swappItem;
      // this.swappModel.putSwappList(swappItem);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public SwappList getSwappList(String name) {
    SwappList oldSwappList = this.swappModel.getSwappList(name);
    // if existing list has no todo items, try to (re)load
    if (oldSwappList == null) {
      HttpRequest request = HttpRequest.newBuilder(swappListUri(name)).header("Accept", "application/json").GET()
          .build();
      try {
        final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
            HttpResponse.BodyHandlers.ofString());
        String responseString = response.body();
        System.out.println("getSwappList(" + name + ") response: " + responseString);
        SwappList swappList = objectMapper.readValue(responseString, SwappList.class);
        this.swappModel.putSwappList(swappList);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return oldSwappList;
  }

  private void putSwappListAtListLevel(SwappList swappList) {
    try {
      String json = objectMapper.writeValueAsString(swappList);
      HttpRequest request = HttpRequest.newBuilder(swappListUri(swappList.getUsername()))
          .header("Accept", "application/json").header("Content-Type", "application/json")
          .PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      Boolean added = objectMapper.readValue(responseString, Boolean.class);
      if (added != null)
        swappModel.putSwappList(swappList);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void putSwappListAtModelLevel(SwappList swappList) {
    try {
      String json = objectMapper.writeValueAsString(swappList);
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", "application/json")
          .header("Content-Type", "application/json").PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      Boolean added = objectMapper.readValue(responseString, Boolean.class);
      if (added != null)
        swappModel.putSwappList(swappList);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

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

  public void removeSwappItem(SwappItem swappItem) {
    try {
      HttpRequest request = HttpRequest.newBuilder(swappItemUri(swappItem.getUsername() + "/" + swappItem.getName()))
          .header("Accept", "application/json").DELETE().build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItem removed = objectMapper.readValue(responseString, SwappItem.class);
      if (removed != null) {
        swappModel.removeSwappItem(swappItem);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void addSwappItem(SwappItem item) {
    try {
      String json = objectMapper.writeValueAsString(item);
      HttpRequest request = HttpRequest.newBuilder(swappListUri(item.getUsername()))
          .header("Accept", "application/json").header("Content-Type", "application/json")
          .POST(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItem swappItemRes = objectMapper.readValue(responseString, SwappItem.class);
      if (swappItemRes != null)
        swappModel.addSwappItem(swappItemRes);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void putSwappItem(SwappItem newItem) {
    try {
      String json = objectMapper.writeValueAsString(newItem);
      HttpRequest request = HttpRequest.newBuilder(swappItemUri(newItem.getUsername() + "/" + newItem.getName()))
          .header("Accept", "application/json").PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItem removed = objectMapper.readValue(responseString, SwappItem.class);
      if (removed != null) {
        swappModel.removeSwappItem(newItem);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void changeSwappItem(SwappItem newItem){
    putSwappItem(newItem);
  }

  /**
   * 
   * Notifies that the TodoList has changed, e.g. TodoItems have been mutated,
   * added or removed.
   *
   * @param todoList the TodoList that has changed
   * 
   */
  
  public List<SwappItem> getSwappItemByStatus(String status) {
    return getSwappModel().getSwappItemsByStatus(status);
  }

  public List<SwappItem> getAllSwappItem() {
    return getSwappModel().getSwappItems();
  }

  public List<SwappItem> getSwappItemByUser(String user) {
    return getSwappList(user).getSwappItems();
  }

  public boolean isItemChanged(SwappItem newItem) {
    return getSwappModel().isItemChanged(newItem);
  }

  public boolean hasSwappItem(SwappItem item) {
    return getSwappList(item.getUsername()).hasSwappItem(item);
  }

  public boolean hasSwappList(String name) {
    return getSwappModel().hasSwappList(name);
  }

  public Collection<SwappList> getAllSwappLists(){
    return getSwappModel().getSwappLists();
  }

}
