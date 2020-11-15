package swapp.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.RadioButton;
import swapp.core.SwappItem;
import swapp.core.SwappModel;
import swapp.core.SwappList;
import swapp.json.SwappPersistence;
import swapp.ui.RemoteSwappAccess;
import swapp.ui.SwappItemListViewCell;
import swapp.ui.SwappDataAccess;

public class SwappAppController {

  @FXML
  private ListView<SwappItem> listView;

  @FXML
  private ChoiceBox<String> filterChoiceBox;

  @FXML
  private TextField textField;

  @FXML
  private Button addButton;

  @FXML
  private Button removeButton;

  @FXML
  private TextField nameField;

  @FXML
  private TextArea descriptionFieldArea;

  @FXML
  private RadioButton newRadio;
  @FXML
  private RadioButton usedRadio;
  @FXML
  private RadioButton damagedRadio;
  @FXML
  private RadioButton allRadio;
  @FXML
  private RadioButton mineRadio;

  private ToggleGroup toggleGroup;

  private String username;

  //@FXML
  //String endpointUri = "http://localhost:8999/swapp/";

  //private final static String swappListWithTwoItems = "{\"lists\":[{\"username\":\"swapp\",\"items\":[{\"itemName\":\"item1\",\"itemUsername\":\"username1\",\"itemStatus\":\"New\",\"itemDescription\":\"info1\"},{\"itemName\":\"item2\",\"itemUsername\":\"username2\",\"itemStatus\":\"New\",\"itemDescription\":\"info2\"}]}]}";

  private SwappDataAccess swappAccess;

  /** Initializes appcontroller. */
  public SwappAppController() {
    listView = new ListView<SwappItem>();
    //filterChoiceBox = new ChoiceBox<>();
    /**try {
      swappAccess = new RemoteSwappAccess(new URI(endpointUri));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }*/
  }

  public void setSwappDataAccess(SwappDataAccess swappAccess) throws IOException {
    this.swappAccess = swappAccess;
    initializeFXML();
  }

  /**
   * Initialize with lambda expression for listeners of SwappItemList.
   * 
   * @throws IOException
   */
  @FXML
  void initializeFXML() throws IOException {
    inizializeToggleGroup();
    //initializeChoiceBox();
    initializeListView();
  }

  public void init(String username) {
    if (!swappAccess.hasSwappList(username)) {
      swappAccess.addNewSwappList(username);
    }
    this.username = username;
    for (SwappList swappList : swappAccess.getAllSwappLists()) {
      swappList.addSwappListListener(p -> {
        updateSwapp();
        swappAccess.writeData();
      });
    }
  }
  /**
  public void initializeChoiceBox() {
    filterChoiceBox.getItems().add("All");
    filterChoiceBox.getItems().add("New");
    filterChoiceBox.getItems().add("Used");
    filterChoiceBox.getItems().add("Damaged");
    filterChoiceBox.setValue("All");
    filterChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> updateSwapp());
  }*/

  public void inizializeToggleGroup() {
    toggleGroup = new ToggleGroup();
    newRadio.setToggleGroup(toggleGroup);
    usedRadio.setToggleGroup(toggleGroup);
    damagedRadio.setToggleGroup(toggleGroup);
    allRadio.setToggleGroup(toggleGroup);
    mineRadio.setToggleGroup(toggleGroup);
    toggleGroup.selectedToggleProperty().addListener((v, oldValue, newValue) -> updateSwapp());
  }

  public void initializeListView() {
    listView.setCellFactory(list -> new SwappItemListViewCell());
    updateSwapp();
  }

  public void addSwappItem(SwappItem swappItem) throws Exception {
    swappAccess.addSwappItem(swappItem);
    updateSwapp();
  }

  public void removeSwappItem(SwappItem swappItem) throws Exception {
    swappAccess.removeSwappItem(swappItem);
    updateSwapp();
  }

  public void changeSwappItem(SwappItem newItem) throws Exception {
    swappAccess.changeSwappItem(newItem);
    updateSwapp();
  }

  @FXML
  void addSwappItemButtonClicked() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSwappItem.fxml"));
    Parent root = (Parent) loader.load();
    AddSwappItemController itemController = loader.getController();
    itemController.initSwappitem(username);
    Stage stage = new Stage();
    stage.setScene(new Scene(root, 900, 500));
    stage.setTitle("New Item");
    stage.showAndWait();
    SwappItem returnetItem = itemController.getSwappItem();
    if (returnetItem != null){
      //System.out.println(getSwappList().getSwappItems().toString());
      addSwappItem(returnetItem);
      //System.out.println(getSwappList().getSwappItems().toString());
    }
  }

  @FXML
  void removeSwappItemButtonClicked() throws Exception {
    SwappItem item = (SwappItem) listView.getSelectionModel().getSelectedItem();
    if (!(item == null)) {
      removeSwappItem(item);
    }
  }

  @FXML
  void removeAllSwappItems(){
    swappAccess.addNewSwappList(this.username);
    updateSwapp();
    init(this.username);
  }

  public void updateSwapp() {
    String choice = ((RadioButton)toggleGroup.getSelectedToggle()).getText();
    if (choice.equals("Mine"))  {
      listView.getItems().setAll(swappAccess.getSwappItemByUser(this.username));
    } else {
      listView.getItems().setAll(swappAccess.getSwappItemByStatus(choice));
    }
    System.out.println("list changed");
  }

  @FXML
  public void viewSwappItem() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewSwappItem.fxml"));
    Parent root = (Parent) loader.load();
    ViewSwappItemController itemController = loader.getController();
    SwappItem selectedItem = (SwappItem) listView.getSelectionModel().getSelectedItem();
    if(selectedItem!=null){
      System.out.println(selectedItem);
      SwappItem oldItem = swappAccess.getSwappItem(selectedItem);
      System.out.println(swappAccess.getAllSwappItems());
      System.out.println(oldItem);
      itemController.initSwappitem(oldItem, username);
      Stage stage = new Stage();
      stage.setScene(new Scene(root, 900, 530));
      stage.setTitle("Item");
      stage.showAndWait();
      boolean deleteFlag = itemController.isdelete();
      SwappItem returnetItem = itemController.getSwappItem();
      if (deleteFlag){
        removeSwappItem(returnetItem);
      }
      else if (swappAccess.isItemChanged(returnetItem)) {
        //System.out.println(swappAccess.getSwappItem(returnetItem));
        changeSwappItem(returnetItem);
        //System.out.println(swappAccess.getSwappItem(returnetItem));
      }
    }
  }

}
