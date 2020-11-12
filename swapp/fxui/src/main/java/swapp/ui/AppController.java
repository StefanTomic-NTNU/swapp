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

public class AppController {

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
  private MenuItem openButton;

  @FXML
  private MenuItem saveButton;

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

  private ToggleGroup toggleGroup;

  private String username;

  private SwappModel model;

  private final static String swappListWithTwoItems = "{\"lists\":[{\"username\":\"swapp\",\"items\":[{\"itemName\":\"item1\",\"itemUsername\":\"username1\",\"itemStatus\":\"New\",\"itemDescription\":\"info1\"},{\"itemName\":\"item2\",\"itemUsername\":\"username2\",\"itemStatus\":\"New\",\"itemDescription\":\"info2\"}]}]}";

  private SwappPersistence swappPersistence = new SwappPersistence();
  
  private File file = Paths.get(System.getProperty("user.home"), "RemoteSwappItems.json").toFile();

  /** Initializes appcontroller. */
  public AppController() {
    listView = new ListView<SwappItem>();
    filterChoiceBox = new ChoiceBox<>();
    model = new SwappModel();
  }

  void loadSwapp() throws IOException {
    try(Reader reader = new FileReader(file, StandardCharsets.UTF_8)){
      model = swappPersistence.readSwappModel(reader);
    } catch (IOException e) {
      Reader reader = new StringReader(swappListWithTwoItems);
      model = swappPersistence.readSwappModel(reader);
      System.out.println("Couldn't read default-todomodel.json, so rigging TodoModel manually (" + e + ")");
    }
  }

  private void autoSave(){
    Writer writer = null;
    try{
      writer = new FileWriter(file, StandardCharsets.UTF_8);
      swappPersistence.writeSwappModel(model, writer);
    }catch(IOException e){
      e.printStackTrace();
    }
    finally{
      try{
        if (writer!=null) writer.close();
      }
      catch(IOException e){
        e.printStackTrace();
      }
    }
  }

  /**
   * Initialize with lambda expression for listeners of SwappItemList.
   * 
   * @throws IOException
   */
  @FXML
  void initialize() throws IOException {
    inizializeToggleGroup();
    initializeChoiceBox();
    loadSwapp();
    initializeListView();
  }

  public void init(String username) {
    if (!model.hasSwappList(username)) {
      model.addSwappList(username);
    }
    this.username = username;
    for (SwappList swappList : model) {
      swappList.addSwappListListener(p -> {
        updateSwappItems();
        autoSave();
      });
    }
  }

  public void initializeChoiceBox() {
    filterChoiceBox.getItems().add("All");
    filterChoiceBox.getItems().add("New");
    filterChoiceBox.getItems().add("Used");
    filterChoiceBox.getItems().add("Damaged");
    filterChoiceBox.setValue("All");
    filterChoiceBox.getSelectionModel().selectedItemProperty()
        .addListener((v, oldValue, newValue) -> updateSwappItems());
  }

  public void inizializeToggleGroup() {
    toggleGroup = new ToggleGroup();
    newRadio.setToggleGroup(toggleGroup);
    usedRadio.setToggleGroup(toggleGroup);
    damagedRadio.setToggleGroup(toggleGroup);
  }

  public void initializeListView() {
    listView.setCellFactory(list -> new SwappItemListViewCell());
    updateSwappItems();
  }

  public boolean hasSwappItem(String name) {
    return model.getSwappList(username).hasSwappItem(name);
  }

  public void addSwappItem(SwappItem swappItem) {
    model.addSwappItem(swappItem);
  }

  public void removeSwappItem(SwappItem swappItem) {
    model.removeSwappItem(swappItem);
  }

  public void removeSwappItem(String swappItem) {
    model.removeSwappItem(this.username, swappItem);
  }

  @FXML
  void addSwappItemButtonClicked() {
    if (!nameField.getText().isBlank()) {
      SwappItem item = new SwappItem(nameField.getText(), this.username,
          ((RadioButton) toggleGroup.getSelectedToggle()).getText(), descriptionFieldArea.getText());
      if (!hasSwappItem(item.getName())) {
        addSwappItem(item);
        nameField.setText("");
        descriptionFieldArea.setText("");
      }
    }
  }

  @FXML
  void removeSwappItemButtonClicked() {
    SwappItem item = (SwappItem) listView.getSelectionModel().getSelectedItem();
    if (!(item == null)) {
      removeSwappItem(item);
    }
  }

  public void updateSwappItems() {
    listView.getItems().setAll(model.getSwappItemsByStatus(filterChoiceBox.getSelectionModel().getSelectedItem()));
    System.out.println("list changed");
  }

  public SwappList getSwappList(){
    return model.getSwappList(username);
  }

  @FXML
  public void viewSwappItem() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewSwappItem.fxml"));
    Parent root = (Parent) loader.load();
    ViewSwappItemController itemController = loader.getController();
    SwappItem oldItem = (SwappItem) listView.getSelectionModel().getSelectedItem();
    itemController.initSwappitem(oldItem, username);
    Stage stage = new Stage();
    stage.setScene(new Scene(root, 800, 400));
    stage.setTitle("Item");
    stage.showAndWait();
    boolean deleteFlag = itemController.isdelete();
    SwappItem returnetItem = itemController.getSwappItem();
    if (deleteFlag) removeSwappItem(returnetItem);
    else if(getSwappList().isItemChanged(returnetItem)){
      System.out.println(getSwappList().getSwappItems().toString());  
      getSwappList().changeSwappItem(oldItem, returnetItem);
      System.out.println(getSwappList().getSwappItems().toString());
      model.putSwappList(getSwappList());
      System.out.println(getSwappList().getSwappItems().toString());
    }
  }

}
