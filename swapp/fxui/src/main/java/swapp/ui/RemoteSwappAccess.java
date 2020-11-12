package swapp.ui;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

  private String uriParam(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
  }

  private URI swappListUri(String name) {
    return endpointBaseUri.resolve(uriParam(name));
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
        System.out.println("getTodoList(" + name + ") response: " + responseString);
        SwappList swappList = objectMapper.readValue(responseString, SwappList.class);
        this.swappModel.putSwappList(swappList);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return oldSwappList;
  }

  private void putSwappList(SwappList swappList) {
    try {
      String json = objectMapper.writeValueAsString(swappList);
      HttpRequest request = HttpRequest.newBuilder(swappListUri(swappList.getUsername()))
          .header("Accept", "application/json").header("Content-Type", "application/json")
          .PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappList newList = objectMapper.readValue(responseString, SwappList.class);
      swappModel.putSwappList(newList);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void addSwappList(String name) {
    if(!hasSwappList(name)){
      SwappList newList = new SwappList(name);
      putSwappList(newList); 
    }
  } 

  public void removeSwappItem(SwappItem swappItem) {
    try {
      HttpRequest request = HttpRequest.newBuilder(swappListUri(swappItem.getName()))
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
      HttpRequest request = HttpRequest.newBuilder(swappListUri(item.getName())).header("Accept", "application/json")
          .header("Content-Type", "application/json").POST(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItem swappItemRes = objectMapper.readValue(responseString, SwappItem.class);
      swappModel.addSwappItem(swappItemRes);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 
   * Notifies that the TodoList has changed, e.g. TodoItems have been mutated,
   * added or removed.
   *
   * @param todoList the TodoList that has changed
   * 
   */
  public void notifySwappListChanged(SwappList other) {
    putSwappList(other);
  }

  public List<SwappItem> getSwappItemByStatus(String status){
    return getSwappModel().getSwappItemsByStatus(status);
  }

  public List<SwappItem> getSwappItemByUser(String user){
    return getSwappList(user).getSwappItems();
  }

  public boolean isItemChanged(SwappItem newItem){
    return getSwappList(newItem.getName()).isItemChanged(newItem);
  }

  public void changeSwappItem(SwappItem oldItem, SwappItem newItem){
    if (isItemChanged(newItem)){
      getSwappList(oldItem.getUsername()).changeSwappItem(oldItem, newItem);
      putSwappList(getSwappList(newItem.getName()));
    }
  }

  public boolean hasSwappItem(SwappItem item){
    return getSwappList(item.getUsername()).hasSwappItem(item);
  }

  public boolean hasSwappList(String name){
    return swappModel.hasSwappList(name);
  }

}
