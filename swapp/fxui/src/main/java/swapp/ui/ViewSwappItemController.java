package swapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import swapp.core.SwappItem;

public class ViewSwappItemController {
  @FXML
  private TextField titleTextField;

  @FXML
  private TextArea infoTextField;

  @FXML
  private TextField emailTextField;

  @FXML
  private Button backButton;

  @FXML
  private Button publishButton;

  @FXML
  private Button deleteButton;

  @FXML
  private RadioButton newRadio;

  @FXML
  private RadioButton usedRadio;

  @FXML
  private RadioButton damagedRadio;

  private ToggleGroup toggleGroup;

  private boolean deleteFlag;

  private SwappItem swappItem;

  /**
   * Initializes stage based on weather or not SwappItem is user's.
   * 
   * <p>If SwappItem is user's: Resests interactive ui elements to default. 
   * Adds username to emailTextField.
   * 
   * <p>If SwappItem is not user's: Sets Text- and AreaFields as well as RadioButtons
   * to non-editable. Disables buttons for deleteButton and publishButton. 
   */
  public void initSwappitem(SwappItem swappItem, String username) {
    cleanText();
    inizializeToggleGroup();
    if (swappItem != null) {
      this.swappItem = swappItem;
      setTextAndToggle();
      if (!swappItem.getUsername().equals(username)) {
        deleteButton.setDisable(true);
        publishButton.setDisable(true);
        infoTextField.setEditable(false);
        newRadio.setDisable(true);
        usedRadio.setDisable(true);
        damagedRadio.setDisable(true);
      }
    }
  }

  /**
   * Places RadioButtons in toggleGroup and selects RadioButton "New" as default.
   */
  public void inizializeToggleGroup() {
    toggleGroup = new ToggleGroup();
    newRadio.setToggleGroup(toggleGroup);
    usedRadio.setToggleGroup(toggleGroup);
    damagedRadio.setToggleGroup(toggleGroup);
    toggleGroup.selectToggle(newRadio);
  }

  /**
   * Sets Text and RadioButton to match SwappItem's attributes.
   */
  public void setTextAndToggle() {
    titleTextField.setText(swappItem.getName());
    infoTextField.setText(swappItem.getDescription());
    emailTextField.setText(swappItem.getUsername());
    switch (swappItem.getStatus()) {
      case "New":
        toggleGroup.selectToggle(newRadio);
        break;
      case "Used":
        toggleGroup.selectToggle(usedRadio);
        break;
      case "Damaged":
        toggleGroup.selectToggle(damagedRadio);
        break;
      default:
        break;
    }
  }

  /**
   * Clears all Text- and AreaFields.
   */
  public void cleanText() {
    titleTextField.setText("");
    infoTextField.setText("");
    emailTextField.setText("");
  }

  /**
   * Constructs SwappItem based on ui input. Closes stage.
   */
  @FXML
  public void publishSwappItem() {
    // String title = titleTextField.getText();
    String newInfo = infoTextField.getText().replaceAll("\\s+", "");
    String newCondition = ((RadioButton) toggleGroup.getSelectedToggle())
        .getText().replaceAll("\\s+", "");
    // String newEmail = emailTextField.getText();
    if (!newInfo.isEmpty() && !newCondition.isEmpty()) {
      SwappItem newItem = new SwappItem(
          this.swappItem.getName(), this.swappItem.getUsername(), newCondition, newInfo);
      this.swappItem = newItem;
      setTextAndToggle();
      cleanText();
      Stage stage = (Stage) publishButton.getScene().getWindow();
      stage.close();
    }
  }

  /**
   * Closes stage without publishing or deleting any SwappItem.
   */
  @FXML
  public void backButtonPressed() {
    cleanText();
    Stage stage = (Stage) backButton.getScene().getWindow();
    stage.close();
  }

  /**
   * Closes and sets deleteButton flag to true.
   */
  @FXML
  public void deleteItem() {
    if (swappItem != null) {
      deleteFlag = true;
    }
    setTextAndToggle();
    cleanText();
    Stage stage = (Stage) deleteButton.getScene().getWindow();
    stage.close();
  }

  public SwappItem getSwappItem() {
    return this.swappItem;
  }

  public boolean isdelete() {
    return deleteFlag;
  }

}
