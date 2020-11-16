package swapp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import swapp.core.SwappItem;

public class AddSwappItemController {
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


  private SwappItem swappItem;

  /**
   * Resests interactive ui elements to default. Adds username to emailTextField.
   */
  public void initSwappitem(String username) {
    cleanText();
    inizializeToggleGroup();
    emailTextField.setText(username);
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
    String title = titleTextField.getText().replaceAll("\\s+", "");
    String newInfo = infoTextField.getText().replaceAll("\\s+", "");
    String newCondition = ((RadioButton) toggleGroup.getSelectedToggle())
        .getText().replaceAll("\\s+", "");
    String newEmail = emailTextField.getText().replaceAll("\\s+", "");
    if (!title.isEmpty() && !newInfo.isEmpty() && !newCondition.isEmpty() && !newEmail.isEmpty()) {
      this.swappItem = new SwappItem(title, newEmail, newCondition, newInfo);
      cleanText();
      Stage stage = (Stage) publishButton.getScene().getWindow();
      stage.close();
    }
  }

  /**
   * Closes stage without publishing any SwappItem.
   */
  @FXML
  public void backButtonPressed() {
    cleanText();
    Stage stage = (Stage) backButton.getScene().getWindow();
    stage.close();
  }

  public SwappItem getSwappItem() {
    return this.swappItem;
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


}
