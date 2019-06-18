package Controllers;

import Classes.Question;
import Enums.ButtonTypeEnum;
import Managers.QuestionSetManager;
import Managers.StageManager;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class EditQuestionsContentController implements Initializable 
{
    @FXML private AnchorPane mainAnchorPane;
    @FXML private Button btnDeleteQuestions;
    @FXML private Button btnSave;
    @FXML private Button btnCreateQuestions;
    @FXML private GridPane grdPnQuestionSet;
    @FXML private JFXComboBox cmbBoxQuestionSet;
    @FXML private Label lblHeader;
    
    @FXML private ScrollPane scrlGridHolder;
    @FXML private Label lblQNumber;
    @FXML private TextArea txtQText;
    
    private ArrayList<String> languageList;
    private String currentSet;
    
    @Override 
    public void initialize(URL url, ResourceBundle rb) 
    {
        setupEditQuestionsForm();
    }    
    
    @FXML public void btnSave_Action(ActionEvent event) {

    }
    
    @FXML public void btnAddLanguage_Action(ActionEvent event) {

    }
    
    @FXML public void btnDeleteQuestions_Action(ActionEvent event) 
    {
        
    }
    
    private void setupEditQuestionsForm()
    {
        currentSet = "M-CHAT-R/F";
        
        cmbBoxQuestionSet.getItems().addAll(QuestionSetManager.getQuestionSets());
        cmbBoxQuestionSet.getSelectionModel().select(currentSet);
        cmbBoxQuestionSet.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
        
        languageList = QuestionSetManager.getSetLanguages(currentSet);
        
        setupGridPaneData(currentSet);
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
                    if(allowSetChange())
                    {
                        currentSet = newSet;
                        languageList = QuestionSetManager.getSetLanguages(currentSet);
                        
                        setupGridPaneData(currentSet);
                    }
                    else
                        cmbBoxQuestionSet.setValue(oldSet);
                }
            }    
        });
    }
    
    private void setupGridPaneData(String setName) 
    {
        //Get question set from QuestionSetManager
        HashMap<Integer, Question> questionMap = QuestionSetManager.getQuestionSet(setName);
        int qCount = questionMap.size();
        
        //HashMap for the labels containing the question number
        HashMap<Integer, Label> qNumMap = new HashMap<>();
        //HashMap for the text areas containing the Robot behaviour name
        HashMap<Integer, TextArea> qBehaviourMap = new HashMap<>();
        
        //HashMap containing the language and HashMap with the TextAreas that hold the question text 
        HashMap<String, HashMap<Integer, TextArea>> qTextMap = new HashMap<>();
        //HashMap containing the language and HashMap with the TextAreas that hold the question robot instructions 
        HashMap<String, HashMap<Integer, TextArea>> qInstructionsMap = new HashMap<>();
        
        //For each question
        for (int i = 1; i <= qCount; i++) 
        {
            //Create Label for Question Number
            Label qNumber = new Label(Integer.toString(i));
            qNumber.setStyle("-fx-font: 24px \"Berlin Sans FB\";");
            qNumMap.put(i, qNumber);
            
            //Create TextArea for NAO Behvaiour Name
            String text = questionMap.get(i).getQuestionBehaviour();
            TextArea behaviourText = createTextArea(text);
            qBehaviourMap.put(i, behaviourText);
        }
        
        //For each language have seperate maps containing TextAreas
        for(String language : languageList)
        {
            HashMap<Integer, TextArea> textMap = new HashMap<>();
            HashMap<Integer, TextArea> instrMap = new HashMap<>();
            
            //For each question
            for (int i = 1; i <= qCount; i++) 
            {
                //Get the text and instruction for that language 
                String tex = questionMap.get(i).getQuestionText(language);
                String ins = questionMap.get(i).getQuestionInstructions(language);
                
                //Create TextAreas for both text and instruction
                TextArea text = createTextArea(tex);
                textMap.put(i, text);
                
                TextArea instr = createTextArea(ins);
                instrMap.put(i, instr);
            }
            //Put the map of TextAreas in main map with the language its associated with
            qTextMap.put(language, textMap);
            qInstructionsMap.put(language, instrMap);
        }
        
        try
        {
            int index = 0;
            
            //Create Columns in grid pane with Headers for the Question Number and Behaviour Name
            //Also create column constraint for the size of the columns (Same size as label for allignment purposes
            grdPnQuestionSet.add(createHeaderLabel("Question Number", 200), index++, 0);
            grdPnQuestionSet.getColumnConstraints().add(createColConstraint(200));
            
            grdPnQuestionSet.add(createHeaderLabel("NAO Behaviour Name", 250), index++, 0);
            grdPnQuestionSet.getColumnConstraints().add(createColConstraint(250));
            
            //Create columns and headers for each different text/instruction language
            for(String language : languageList)
            {
                grdPnQuestionSet.add(createHeaderLabel("Text - " + language, 450), index++, 0);
                grdPnQuestionSet.getColumnConstraints().add(createColConstraint(450));
                
                grdPnQuestionSet.add(createHeaderLabel("Instructions - " + language, 450), index++, 0);
                grdPnQuestionSet.getColumnConstraints().add(createColConstraint(450));
            }
            
            //for each question
            for (int i = 1; i <= qCount; i++) 
            {
                index = 0;
                
                //Add label and behaviour from maps to the current row 
                grdPnQuestionSet.add(qNumMap.get(i), index++, i);
                grdPnQuestionSet.add(qBehaviourMap.get(i), index++, i);
                
                //Add text and instruction for each language to the current row
                for (int j = 0; j < languageList.size(); j++) {
                    grdPnQuestionSet.add(qTextMap.get(languageList.get(j)).get(i), index++, i);
                    grdPnQuestionSet.add(qInstructionsMap.get(languageList.get(j)).get(i), index++, i);
                }
                
                //Create row constraint for the height of the row
                RowConstraints rowCon = new RowConstraints();
                rowCon.setMinHeight(75);
                rowCon.setPrefHeight(75);
                rowCon.setMaxHeight(75);
                grdPnQuestionSet.getRowConstraints().add(rowCon);
                
                //Set allignment of question number label so that it is centered
                GridPane.setHalignment(qNumMap.get(i), HPos.CENTER);
            }
            //Add constraint to grid pane for if the form is maximized
            grdPnQuestionSet.prefWidthProperty().bind(scrlGridHolder.widthProperty());
        }
        catch(Exception ex)
        {
            System.out.println("Failed when setting up gridpane - " + ex.getMessage());
        } 
    }
    
    private boolean allowSetChange()
    {
        String message = "Are you sure you wish to change Question Set? Any unsaved progress will be lost.";
        return StageManager.loadPopupMessage("Warning", message, ButtonTypeEnum.YESNO);
    }
    
    private Label createHeaderLabel(String text, double size)
    {
        Label header = new Label();
        header.setText(text);
        header.setPrefWidth(size);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
        header.setUnderline(true);
        
        return header;
    }
    
    private TextArea createTextArea(String text)
    {
        TextArea qText = new TextArea();
        qText.setText(text);
        qText.setWrapText(true);
        qText.setStyle("-fx-font: 18px \"Berlin Sans FB\";");
        
        return qText;
    }
    
    private ColumnConstraints createColConstraint(double size)
    {
        ColumnConstraints colCon = new ColumnConstraints();
        colCon.setMinWidth(size);
        colCon.setPrefWidth(size);
        colCon.setMaxWidth(size);
        
        return colCon;
    }
}
