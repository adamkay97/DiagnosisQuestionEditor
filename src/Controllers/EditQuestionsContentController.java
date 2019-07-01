package Controllers;

import Managers.GridPaneManager;
import Classes.QuestionSet;
import Enums.ButtonTypeEnum;
import Managers.DatabaseManager;
import Managers.QuestionSetManager;
import Managers.StageManager;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditQuestionsContentController implements Initializable 
{
    @FXML private GridPane grdPnQuestionSet;
    @FXML private ScrollPane scrlGridHolder;
    @FXML private JFXComboBox cmbBoxQuestionSet;
    
    private GridPaneManager gridPaneManager;
    private QuestionSet currentQuestionSet;
    private String currentSet;
    
    private String newLanguage;
    
    @Override 
    public void initialize(URL url, ResourceBundle rb) 
    {
        setupEditQuestionsForm();
    }    
    
    @FXML public void btnSave_Action(ActionEvent event) 
    {
        gridPaneManager.saveGridPane();
    }
    
    @FXML public void btnAddLanguage_Action(ActionEvent event) 
    {
        addLanguage();
    }
    
    @FXML public void btnDeleteQuestions_Action(ActionEvent event) 
    {
        String msg;
        
        if(!currentSet.equals(QuestionSetManager.DEFAULTSET))
        {
            msg = "Are you sure you wish to delete this Question Set? This action will be irreversible.";
        
            if(popupConfirmation(msg))
            {
                deleteQuestionSet();

                msg = currentSet +" Question Set has been successfully deleted.";
                StageManager.loadPopupMessage("Information", msg, ButtonTypeEnum.OK);
                
                QuestionSetManager.setCurrentEditSet(QuestionSetManager.DEFAULTSET);
                StageManager.loadContentScene(StageManager.EDITQUESTIONS);
            }
        }
        else
        {
            msg = QuestionSetManager.DEFAULTSET + " is the default Question Set and cannot be deleted.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        } 
    }
    
    @FXML public void btnBack_Action(ActionEvent event) 
    {
        String msg = "Are you sure you wish to go back? Any unsaved progress will be lost.";
        
        if(popupConfirmation(msg))
            StageManager.loadContentScene(StageManager.START);
    }
    
    private void setupEditQuestionsForm()
    {
        currentSet = QuestionSetManager.getCurrentEditSet();
        newLanguage = "";
        
        cmbBoxQuestionSet.getItems().addAll(QuestionSetManager.getQuestionSets());
        cmbBoxQuestionSet.getSelectionModel().select(currentSet);
        cmbBoxQuestionSet.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
        
        currentQuestionSet = QuestionSetManager.getQuestionSet(currentSet);
        
        //Initialise GridPaneManager class for Edit Form
        gridPaneManager = new GridPaneManager(currentQuestionSet, grdPnQuestionSet, true);
        
        setupGridPaneData();
        createComboBoxListener();
    }
    
    private void createComboBoxListener()
    {
        //If combobox value changed and user ensures changes are saved reset the grid pane for new set
        cmbBoxQuestionSet.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldSet, String newSet) 
            {
                if(newSet != null && !newSet.equals(currentSet))
                {
                    String msg = "Are you sure you wish to change Question Set? Any unsaved progress will be lost.";
                    if(popupConfirmation(msg))
                    {
                        //When the question set is changed, set name on SetManager and reload whole content page
                        QuestionSetManager.setCurrentEditSet(newSet);
                        StageManager.loadContentScene(StageManager.EDITQUESTIONS);
                    }
                    else
                        cmbBoxQuestionSet.setValue(oldSet);
                }
            }    
        });
    }
    
    private void addLanguage()
    {
        ArrayList<String> availableLanguages = new ArrayList<>();
        
        //Loop through all languages
        QuestionSetManager.getQuestionLanguages().forEach((language) -> {
            //If language doesnt exist in current question sets active language list
            //add it to list to be passed to add language form.
            if(!currentQuestionSet.getActiveLanguages().contains(language))
                availableLanguages.add(language);
        });
        
        if(!availableLanguages.isEmpty())
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(StageManager.ADDLANGUAGE));
                Parent root = (Parent)loader.load();

                EditAddLanguageFormController addLanguageForm = loader.<EditAddLanguageFormController>getController();
                addLanguageForm.setupAddLanguageForm(this, availableLanguages);
                
                StageManager.loadFormParent(root, new Stage());
                
                if(!newLanguage.equals(""))
                {
                    gridPaneManager.addNewLanguageColumns(newLanguage);
                    currentQuestionSet.addNewActiveLanguage(newLanguage);
                    gridPaneManager.setCurrentQuestionSet(currentQuestionSet);
                }
            }
            catch(IOException ex)
            {
                System.out.println("Error when loading Add Language Form - " + ex.getMessage());
            }
        }
        else
        {
            String msg = "This Question Set contains all available languages. If you wish to "
                        + "add more languages please use the Manage Languages form found on the Start page.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }
    
    private void setupGridPaneData() 
    {        
        //Use GridPaneManager class to create grid pane
        gridPaneManager.createGridPane();
        
        //Add constraint to grid pane for if the form is maximized
        grdPnQuestionSet.prefWidthProperty().bind(scrlGridHolder.widthProperty());
    }
    
    private void deleteQuestionSet()
    {
        DatabaseManager dbManager = new DatabaseManager();
            
        if(dbManager.connect())
        {
            dbManager.deleteAllSetData(currentSet);
            
            dbManager.loadQuestionSetList();
            dbManager.loadQuestionSetMap();
            
            dbManager.disconnect();
        }
    }
    
    private boolean popupConfirmation(String message)
    {
        return StageManager.loadPopupMessage("Warning", message, ButtonTypeEnum.YESNO);
    }
    
    public void setNewLanguage(String language) { newLanguage = language; }
}
