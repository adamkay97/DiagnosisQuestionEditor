package Controllers;

import Managers.GridPaneManager;
import Classes.Question;
import Classes.QuestionSet;
import Enums.ButtonTypeEnum;
import Managers.QuestionSetManager;
import Managers.StageManager;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class EditQuestionsContentController implements Initializable 
{
    @FXML private GridPane grdPnQuestionSet;
    @FXML private JFXComboBox cmbBoxQuestionSet;
    
    @FXML private ScrollPane scrlGridHolder;
    
    private QuestionSet currentQuestionSet;
    //private ArrayList<String> languageList;
    private String currentSet;
    
    private GridPaneManager gridPaneManager;
    
    @Override 
    public void initialize(URL url, ResourceBundle rb) 
    {
        setupEditQuestionsForm();
    }    
    
    @FXML public void btnSave_Action(ActionEvent event) {
        gridPaneManager.saveGridPane();
    }
    
    @FXML public void btnAddLanguage_Action(ActionEvent event) {

    }
    
    @FXML public void btnDeleteQuestions_Action(ActionEvent event) 
    {
        
    }
    
    @FXML public void btnBack_Action(ActionEvent event) 
    {
        String msg = "Are you sure you wish to go back? Any unsaved progress will be lost.";
        
        if(allowChange(msg))
            StageManager.loadContentScene(StageManager.START);
    }
    
    private void setupEditQuestionsForm()
    {
        currentSet = QuestionSetManager.getCurrentEditSet();
        
        cmbBoxQuestionSet.getItems().addAll(QuestionSetManager.getQuestionSets());
        cmbBoxQuestionSet.getSelectionModel().select(currentSet);
        cmbBoxQuestionSet.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
        
        currentQuestionSet = QuestionSetManager.getQuestionSet(currentSet);
        
        //Initialise GridPaneManager class for Edit Form
        gridPaneManager = new GridPaneManager(currentQuestionSet, grdPnQuestionSet, true);
        
        setupGridPaneData(false);
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
                    if(allowChange(msg))
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
    
    private void setupGridPaneData(boolean reset) 
    {
        //If grid is to be reset because of a set change remove all nodes and set
        //new question set on GridPaneManager
        /*if(reset)
        {
            gridPaneManager.clearGridPaneContents();
            gridPaneManager.setCurrentQuestionSet(currentQuestionSet);
        }*/
        
        //Use GridPaneManager class to create grid pane
        gridPaneManager.createGridPane();
        
        //Add constraint to grid pane for if the form is maximized
        grdPnQuestionSet.prefWidthProperty().bind(scrlGridHolder.widthProperty());
    }
    
    private boolean allowChange(String message)
    {
        return StageManager.loadPopupMessage("Warning", message, ButtonTypeEnum.YESNO);
    }
    
    
}
