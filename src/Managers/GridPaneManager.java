package Managers;

import Classes.Question;
import Classes.QuestionSet;
import Enums.ButtonTypeEnum;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class GridPaneManager 
{
    private QuestionSet currentQuestionSet;
    private final GridPane grdPnQuestionSet;
    private final boolean isEditContent;
    
    public GridPaneManager(QuestionSet questionSet, GridPane gridPane, boolean isEdit)
    {
        currentQuestionSet = questionSet;
        grdPnQuestionSet = gridPane;
        isEditContent = isEdit;
    }
    
    //------------------------------------------------
    //      Methods for creating the GridPane
    //------------------------------------------------
    public void createGridPane()
    {
        //Get question set from QuestionSetManager
        HashMap<Integer, Question> questionMap = currentQuestionSet.getQuestionSet();
        int qCount = currentQuestionSet.getNumberOfQuestions();
        ArrayList<String> languageList = currentQuestionSet.getActiveLanguages();
        
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
            
            //If this class is being used within the Edit form get the question behaviour name
            //Else its left blank for user to add in Create form
            String text = "";
            if(isEditContent)
                text = questionMap.get(i).getQuestionBehaviour();
            
            //Create TextArea for NAO Behvaiour Name
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
                //Get the text and instruction for that language if in Edit form
                //Else leave blank for Create form.
                String tex = ""; String ins = "";
                if(isEditContent)
                {
                    tex = questionMap.get(i).getQuestionText(language);
                    ins = questionMap.get(i).getQuestionInstructions(language);
                }
                
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
            
            grdPnQuestionSet.add(createHeaderLabel("NAO Behaviour Name \n(Required)", 250), index++, 0);
            grdPnQuestionSet.getColumnConstraints().add(createColConstraint(250));
            
            //Create columns and headers for each different text/instruction language
            for(String language : languageList)
            {
                String header = String.format("Text - %s (Required)", language);
                grdPnQuestionSet.add(createHeaderLabel(header, 450), index++, 0);
                grdPnQuestionSet.getColumnConstraints().add(createColConstraint(450));
                
                header = String.format("Instructions - %s", language);
                grdPnQuestionSet.add(createHeaderLabel(header , 450), index++, 0);
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
                grdPnQuestionSet.getRowConstraints().add(createRowConstraint(125));
                
                //Set allignment of question number label so that it is centered
                GridPane.setHalignment(qNumMap.get(i), HPos.CENTER);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Failed when setting up gridpane - " + ex.getMessage());
        } 
    }
    
    public void clearGridPaneContents()
    {
        while(grdPnQuestionSet.getRowConstraints().size() > 0){
            grdPnQuestionSet.getRowConstraints().remove(0);
        }

        while(grdPnQuestionSet.getColumnConstraints().size() > 0){
            grdPnQuestionSet.getColumnConstraints().remove(0);
        }
        
        grdPnQuestionSet.getChildren().clear();
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
    
    private RowConstraints createRowConstraint(double size)
    {
        RowConstraints rowCon = new RowConstraints();
        rowCon.setMinHeight(size);
        rowCon.setPrefHeight(size);
        rowCon.setMaxHeight(size);
        
        return rowCon;
    }
    
    //-------------------------------------------------------------
    //      Methods for saving information from the GridPane
    //-------------------------------------------------------------
    public void saveGridPane()
    {
        String msg;
        if(saveQuestionData())
        {
            if(writeQuestionSetToDB())
            {
                msg = "The Question Set has been successfully saved to the database";
                StageManager.loadPopupMessage("Information", msg, ButtonTypeEnum.OK);
                StageManager.loadContentScene(StageManager.START);
            }
            else
            {
                msg = "There was an error when saving the question set to the database";
                StageManager.loadPopupMessage("Error", msg, ButtonTypeEnum.OK);
            }
        }
    }
    
    private boolean saveQuestionData()
    {
        HashMap<Integer, Question> questionMap = new HashMap<>();
        
        try
        {
            for (int i = 1; i <= currentQuestionSet.getNumberOfQuestions(); i++) 
            {
                HashMap<String, String> questionText = new HashMap<>();
                HashMap<String, String> instructionText = new HashMap<>();

                String behaviourName = getTextByRowColumn(i, 1);
                if(!checkBlankText(behaviourName)) return false;

                int index = 2;
                for(String lang : currentQuestionSet.getActiveLanguages())
                {
                    String text = getTextByRowColumn(i, index);
                    String instr = getTextByRowColumn(i, index+1);

                    if(!checkBlankText(text)) return false;

                    questionText.put(lang, text);
                    instructionText.put(lang, instr);

                    index += 2;
                }

                Question question = new Question(i, behaviourName, questionText, instructionText);
                questionMap.put(i, question);
            }

            currentQuestionSet.setQuestionSet(questionMap);
        }
        catch(Exception ex)
        {
            String msg = "Failed when reading data from the GridPane - " + ex.getMessage();
            StageManager.loadPopupMessage("Error", msg, ButtonTypeEnum.OK);
            return false;
        }
        
        return true;
    }
    
    private boolean writeQuestionSetToDB()
    {
        boolean success = true;
        DatabaseManager dbManager = new DatabaseManager();
        
        if(dbManager.connect())
        {
            //If this class is being used by the Edit form, drop the current version of the table so that a new
            //one can be created with the new data or columns
            if(isEditContent)
                dbManager.deleteQuestionSetTable(currentQuestionSet.getSetName());
            
            //If the new question set table is successfully created in the database
            if(dbManager.createNewSetTable(currentQuestionSet))
            {
                //If the new questions are successfully written to the new question set table
                if(dbManager.writeNewQuestionSet(currentQuestionSet))
                {
                    //If this is being used on the Create Form write the new set name to the set names table
                    //And write the set name and language to the Set Language link table.
                    if(!isEditContent)
                    {
                        dbManager.writeNewSetName(currentQuestionSet.getSetName());
                        dbManager.writeNewSetLanguages(currentQuestionSet);
                    }
                    
                    //Reload the question set list and set map so they contain the new question sets
                    dbManager.loadQuestionSetList();
                    dbManager.loadQuestionSetMap();
                }
                else
                {
                    //If the questions fail to be written to the table drop the new table
                    dbManager.deleteQuestionSetTable(currentQuestionSet.getSetName());
                    success = false;
                }  
            }
            else
                success = false;
            
            dbManager.disconnect();
        }
        return success;
    }
    
    private String getTextByRowColumn(int row, int col)
    {
        String text = "";
        
        for(Node node : grdPnQuestionSet.getChildren())
        {
            if(GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null)
            {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                
                if(rowIndex == row && colIndex == col){
                    TextArea textArea = (TextArea)node;
                    text = textArea.getText();
                    break;
                }
            }    
        }
        return text;
    }
    
    private boolean checkBlankText(String text)
    {
        if(text.equals(""))
        {
            String msg = "Please ensure all required fields are filled in.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
            return false;
        }
        else
            return true;
    }
    
    private Label createHeaderLabel(String text, double size)
    {
        Label header = new Label();
        header.setText(text);
        header.setPrefWidth(size);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-font: 20px \"Berlin Sans FB\";");
        header.setUnderline(true);
        //header.setWrapText(true);
        
        return header;
    }
    
    public void setCurrentQuestionSet(QuestionSet qSet) { currentQuestionSet = qSet; }
    
}


