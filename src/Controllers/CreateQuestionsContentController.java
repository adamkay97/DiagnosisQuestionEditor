package Controllers;

import Managers.GridPaneManager;
import Classes.Question;
import Classes.QuestionSet;
import Enums.ButtonTypeEnum;
import Managers.DatabaseManager;
import Managers.StageManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class CreateQuestionsContentController implements Initializable 
{
    @FXML private Label lblSetName;

    @FXML private GridPane grdPnQuestionSet;
    @FXML private ScrollPane scrlGridHolder;
    
    //private QuestionSet currentQuestionSet;
    private GridPaneManager gridPaneManager;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML public void btnSave_Action(ActionEvent event) 
    {
        gridPaneManager.saveGridPane();
    }
    
    @FXML public void btnBack_Action(ActionEvent event) 
    {
        String msg = "Are you sure you wish to go back? Your progress will not be saved.";
        if(allowChange(msg))
            StageManager.loadContentScene(StageManager.CREATESET);
    }
    
    public void setupCreateQuestionsContent(QuestionSet questionSet)
    {
        //currentQuestionSet = questionSet;
        lblSetName.setText(questionSet.getSetName());
        
        //Use GridPaneCreator class to create grid pane for Create Form
        gridPaneManager = new GridPaneManager(questionSet, grdPnQuestionSet, false);
        gridPaneManager.createGridPane();
        
        //Add constraint to grid pane for if the form is maximized
        grdPnQuestionSet.prefWidthProperty().bind(scrlGridHolder.widthProperty());
    }
    
    private boolean allowChange(String message)
    {
        return StageManager.loadPopupMessage("Warning", message, ButtonTypeEnum.YESNO);
    }
}
