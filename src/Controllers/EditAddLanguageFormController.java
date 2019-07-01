package Controllers;

import Enums.ButtonTypeEnum;
import Managers.StageManager;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditAddLanguageFormController implements Initializable 
{
    @FXML private AnchorPane mainAnchorPane;
    @FXML private JFXComboBox<String> cmbBoxLanguage;
    
    private EditQuestionsContentController editController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { }    
    
    @FXML public void btnAdd_Action(ActionEvent event) 
    {
        String language = cmbBoxLanguage.getSelectionModel().getSelectedItem();
        
        if(language != null)
        {
            editController.setNewLanguage(language);
            closeForm();
        }
        else
        {
            String msg = "Please select a Language to add.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }
    
    @FXML public void btnQuit_Action(ActionEvent event) { closeForm(); }
    @FXML public void btnClose_Action(ActionEvent event) { closeForm(); }

    public void setupAddLanguageForm(EditQuestionsContentController controller, ArrayList<String> availableLanguages)
    {
        editController = controller;
        cmbBoxLanguage.getItems().setAll(availableLanguages);
        cmbBoxLanguage.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
    }
    
    private void closeForm()
    {
        Stage currentStage = (Stage)mainAnchorPane.getScene().getWindow();
        currentStage.close();
    }
    
}
