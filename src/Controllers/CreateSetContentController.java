package Controllers;

import Classes.Question;
import Classes.QuestionSet;
import Enums.ButtonTypeEnum;
import Managers.QuestionSetManager;
import Managers.StageManager;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
        cmbBoxLanguage.getItems().addAll(QuestionSetManager.getQuestionLanguages());
        cmbBoxLanguage.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
    }    
    
    @FXML public void btnCreate_Action(ActionEvent event) 
    {
        if(validateUserInput())
            createQuestionSet();
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
    
    private void createQuestionSet()
    {
        String setName = txtSetName.getText();
        int numOfQs = Integer.parseInt(txtNumOfQuestions.getText());
        ArrayList<String> languages = new ArrayList<>();
        
        listViewLanguages.getItems().forEach((lang) -> {
            languages.add(lang);
        });
        
        QuestionSet questionSet = new QuestionSet(setName, numOfQs, new HashMap<>(), languages);
        
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Forms/CreateQuestionsContent.fxml"));
            Parent root = (Parent)loader.load();
            
            CreateQuestionsContentController createQuestions = loader.<CreateQuestionsContentController>getController();
            createQuestions.setupCreateQuestionsContent(questionSet);
            
            StageManager.loadContentSceneParent(root);
        } 
        catch (IOException ex) 
        {
            System.out.println("Error when loading Create Questions content - " + ex.getMessage());
        }
    }
    
    private boolean validateUserInput()
    {
        if(txtSetName.getText().equals(""))
        {
            StageManager.loadPopupMessage("Warning", "Please enter a valid Set Name.", ButtonTypeEnum.OK);
            return false;
        }
        
        if(!isNumeric(txtNumOfQuestions.getText()))
        {
            StageManager.loadPopupMessage("Warning", "Please enter a valid number of questions.", ButtonTypeEnum.OK);
            return false;
        }
        
        if(listViewLanguages.getItems().isEmpty())
        {
            StageManager.loadPopupMessage("Warning", "Please add at least one active language "
                                                    + "for the question set.", ButtonTypeEnum.OK);
            return false;
        }
        
        return true;
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
