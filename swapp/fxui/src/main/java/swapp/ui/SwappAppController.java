package swapp.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import swapp.core.SwappItem;
import swapp.core.SwappList;

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

  private SwappDataAccess swappAccess;

  /** 
   * Initializes appcontroller. 
   */
  public SwappAppController() {
    listView = new ListView<SwappItem>();
  }

  public void setSwappDataAccess(SwappDataAccess swappAccess) throws IOException {
    this.swappAccess = swappAccess;
    initializeFxml();
  }

  //TODO evt skriv javadoc. hvorfor kaster denne en IOException? 
  @FXML
  void initializeFxml() throws IOException {
    initializeToggleGroup();
    // initializeChoiceBox();
    initializeListView();
  }

  /** 
   * Initializes by adding SwappList for user. 
   *
   * @param username Username that is associated with the SwappList.
   */
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
   * Places RadioButtons in toggleGroup and adds listener to update ListView.
   */
  public void initializeToggleGroup() {
    toggleGroup = new ToggleGroup();
    newRadio.setToggleGroup(toggleGroup);
    usedRadio.setToggleGroup(toggleGroup);
    damagedRadio.setToggleGroup(toggleGroup);
    allRadio.setToggleGroup(toggleGroup);
    mineRadio.setToggleGroup(toggleGroup);
    toggleGroup.selectedToggleProperty()
        .addListener((v, oldValue, newValue) -> updateSwapp());
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
    if (returnetItem != null) {
      addSwappItem(returnetItem);
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
  void removeAllSwappItems() {
    swappAccess.addNewSwappList(this.username);
    updateSwapp();
    init(this.username);
  }

  /**
   * Updates listView to match toggleGroup.
   */
  public void updateSwapp() {
    String choice = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
    if (choice.equals("Mine")) {
      listView.getItems().setAll(swappAccess.getSwappItemByUser(this.username));
    } else {
      listView.getItems().setAll(swappAccess.getSwappItemByStatus(choice));
    }
  }

  /**
   * Shows new Stage "ViewSwappItem.fxml".
   *
   * @throws Exception May be thrown if ViewSwappItem.fxml is not loaded properly.
   */
  @FXML
  public void viewSwappItem() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewSwappItem.fxml"));
    Parent root = (Parent) loader.load();
    ViewSwappItemController itemController = loader.getController();
    SwappItem selectedItem = (SwappItem) listView.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      System.out.println(selectedItem);
      SwappItem oldItem = swappAccess.getSwappItem(selectedItem);
      itemController.initSwappitem(oldItem, username);
      Stage stage = new Stage();
      stage.setScene(new Scene(root, 900, 530));
      stage.setTitle("Item");
      stage.showAndWait();
      boolean deleteFlag = itemController.isdelete();
      SwappItem returnetItem = itemController.getSwappItem();
      if (deleteFlag) {
        removeSwappItem(returnetItem);
      } else if (swappAccess.isItemChanged(returnetItem)) {
        changeSwappItem(returnetItem);
      }
    }
  }

}
