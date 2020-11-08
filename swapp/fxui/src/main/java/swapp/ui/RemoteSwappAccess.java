package swapp.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import swapp.core.SwappItem;
import swapp.core.SwappItemList;
import swapp.json.SwappItemModule;

public class RemoteSwappAccess {

  private final URI endpointBaseUri;

  private ObjectMapper objectMapper;

  private SwappItemList swappList;
  private SwappItem swappItem;

  public RemoteSwappAccess(URI endpointBaseUri) {
    this.endpointBaseUri = endpointBaseUri;
    objectMapper = new ObjectMapper().registerModule(new SwappItemModule());
  }

  public SwappItemList getSwappList() {
    if (swappList == null) {
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", "application/json").GET().build();
      try {
        final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
            HttpResponse.BodyHandlers.ofString());
        final String responseString = response.body();
        this.swappList = objectMapper.readValue(responseString, SwappItemList.class);
        System.out.println("TodoModel: " + this.swappList);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return this.swappList;
  }

  private String uriParam(String s) {
    return URLEncoder.encode(s, StandardCharsets.UTF_8);
  }

  private URI swapptUri(String name) {
    return endpointBaseUri.resolve(uriParam(name));
  }

  public SwappItem getSwappItem(String name) {
    if (!this.swappList.getSwappItem(name).equals(this.swappItem)){
      final HttpRequest request = HttpRequest.newBuilder(swapptUri(name)).header("Accept", "application/json").GET()
          .build();
      try {
        final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
            HttpResponse.BodyHandlers.ofString());
        String responseString = response.body();
        System.out.println("getTodoList(" + name + ") response: " + responseString);
        SwappItem newSwappItem = objectMapper.readValue(responseString, SwappItem.class);
        this.swappItem = newSwappItem;
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return this.swappItem;
  }



  private void putSwappList(SwappItemList newSwappList) {
    try {
      String json = objectMapper.writeValueAsString(newSwappList);
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri).header("Accept", "application/json")
          .header("Content-Type", "application/json").PUT(BodyPublishers.ofString(json)).build();
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItemList swappListRes = objectMapper.readValue(responseString, SwappItemList.class);
      this.swappList.putSwappList(swappListRes);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }


  public void addSwappItem(SwappItem item) {
    
    try {
      String json = objectMapper.writeValueAsString(item);
      HttpRequest request = HttpRequest.newBuilder(endpointBaseUri)
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json))
        .build();
      final HttpResponse<String> response = HttpClient.newBuilder()
        .build()
        .send(request,HttpResponse.BodyHandlers.ofString());
      String responseString = response.body();
      SwappItem swappItemRes = objectMapper.readValue(responseString, SwappItem.class);
      this.swappList.addItem(swappItemRes);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

/*
  public void addSwappItem(SwappItem other) {
    this.swappList.addItem(other);
    notifySwappListChanged(this.swappList);
  }
*/
  public void removeSwappItem(SwappItem other) {
    this.swappList.removeItem(other);
    notifySwappListChanged(this.swappList);
    ;
  }

  /**
   * Notifies that the TodoList has changed, e.g. TodoItems have been mutated,
   * added or removed.
   *
   * @param todoList the TodoList that has changed
   */
  public void notifySwappListChanged(SwappItemList other) {
    putSwappList(other);
  }
}
