package Controllers;

import Enums.ButtonTypeEnum;
import Managers.QuestionSetManager;
import Managers.StageManager;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class CreateSetContentController implements Initializable 
{
    @FXML private JFXTextField txtSetName;
    @FXML private JFXTextField txtNumOfQuestions;
    @FXML private JFXComboBox<String> cmbBoxLanguage;
    @FXML private ListView<String> listViewLanguages;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        cmbBoxLanguage.getItems().addAll(QuestionSetManager.getAllLanguages());
        cmbBoxLanguage.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
    }    
    
    @FXML public void btnCreate_Action(ActionEvent event) 
    {
        
    }
    
    @FXML public void btnAddLanguage_Action(ActionEvent event) 
    {
        String language = cmbBoxLanguage.getValue();
        
        if(!listViewLanguages.getItems().contains(language) && language != null)
            listViewLanguages.getItems().add(language);
        else
        {
            String msg = "This language has already been added to the list.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }

    @FXML public void btnRemoveLanguage_Action(ActionEvent event) 
    {
        String language = listViewLanguages.getSelectionModel().getSelectedItem();
        
        if(language != null)
            listViewLanguages.getItems().remove(language);
        else
        {
            String msg = "Please select a language to remove from the list.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }
    
    @FXML public void btnBack_Action(ActionEvent event) 
    {
        StageManager.loadContentScene(StageManager.START);
    }
    
    private boolean isNumeric(String number) 
    {
        //Parse user input to see if the age input is numeric or not
        try {
            int i = Integer.parseInt(number);
        } 
        catch (NumberFormatException | NullPointerException ex) {
            return false;
        }
        return true;
    }
    
}
