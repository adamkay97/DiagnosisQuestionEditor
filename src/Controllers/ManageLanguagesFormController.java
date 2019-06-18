package Controllers;

import Classes.Language;
import Enums.ButtonTypeEnum;
import Managers.DatabaseManager;
import Managers.QuestionSetManager;
import Managers.StageManager;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ManageLanguagesFormController implements Initializable 
{
    @FXML private AnchorPane mainAnchorPane;
    @FXML private JFXTextField txtLanguage;
    @FXML private ListView<String> listViewLanguages;
    
    private ArrayList<Language> languagesList;
    private ArrayList<String> languages;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        languagesList = QuestionSetManager.getLanguageList();
        languages = QuestionSetManager.getAllLanguages();
        
        listViewLanguages.getItems().addAll(languages);
    }    
    
    @FXML public void btnAdd_Action(ActionEvent event) 
    {
        String language = txtLanguage.getText();
        
        //If the language isnt empty
        if(!language.equals(""))
        {
            //If the lanaguage doesnt already exist
            if(!languages.contains(language))
                addNewLanguage(language);
            else
            {
                String msg = "Please enter a valid language that is also not in use.";
                StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
            }
        }
    }
    
    @FXML public void btnRemoveLanguage_Action(ActionEvent event) 
    {
        String language = listViewLanguages.getSelectionModel().getSelectedItem();
        
        //If there is an item selected
        if(language != null)
        {
            //Check this language is not being used by any question sets
            if(!checkLanguageActive(language))
                removeLanguage(language);
            else
            {
                String msg = "This language cannot be removed as it is being used by a question set.";
                StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
            }
        }
        else
        {
            String msg = "Please select a language to remove from the list.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }

    @FXML public void btnClose_Action(ActionEvent event) 
    {
        Stage currentStage = (Stage)mainAnchorPane.getScene().getWindow();
        currentStage.close();
    }
    
    private void addNewLanguage(String language)
    {
        DatabaseManager dbManager = new DatabaseManager();
        
        if(dbManager.connect())
        {
            //Write the new language to the database then reload the language list
            dbManager.writeNewLanguage(language);
            dbManager.loadLanguageList();
            
            //Add the language to the list view
            listViewLanguages.getItems().add(language);
            
            dbManager.disconnect();
        }
    }
    
    private void removeLanguage(String language)
    {
        DatabaseManager dbManager = new DatabaseManager();
        
        if(dbManager.connect())
        {
            //Delete the language from the database then reload the language list
            dbManager.deleteLanguage(language);
            dbManager.loadLanguageList();
            
            //Remove the language from the list view
            listViewLanguages.getItems().remove(language);
            
            dbManager.disconnect();
        }
    }
     
    
    private boolean checkLanguageActive(String langName)
    {     
        for(Language language : languagesList)
        {
            if(language.getLanguage().equals(langName))
            {
                //If language to be removed is being used in a question return true
                if(!language.getActiveQuestionSets().isEmpty())
                    return true;
            }
        }
        return false;
    }
}
