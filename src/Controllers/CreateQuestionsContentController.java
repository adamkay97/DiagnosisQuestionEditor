package Controllers;

import Managers.GridPaneManager;
import Classes.QuestionSet;
import Enums.ButtonTypeEnum;
import Managers.DatabaseManager;
import Managers.QuestionSetManager;
import Managers.StageManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class CreateQuestionsContentController implements Initializable 
{
    @FXML private Label lblSetName;

    @FXML private GridPane grdPnQuestionSet;
    @FXML private ScrollPane scrlGridHolder;
    
    //private QuestionSet currentQuestionSet;
    private GridPaneManager gridPaneManager;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        QuestionSet questionSet = QuestionSetManager.getCurrentCreateSet();
        
        //currentQuestionSet = questionSet;
        lblSetName.setText(questionSet.getSetName());
        
        //Use GridPaneCreator class to create grid pane for Create Form
        gridPaneManager = new GridPaneManager(questionSet, grdPnQuestionSet, false);
        gridPaneManager.createGridPane();
        
        //Add constraint to grid pane for if the form is maximized
        grdPnQuestionSet.prefWidthProperty().bind(scrlGridHolder.widthProperty());
    }

    @FXML public void btnSave_Action(ActionEvent event) 
    {
        gridPaneManager.saveGridPane();
        
        DatabaseManager dbManager = new DatabaseManager();
        if(dbManager.connect())
        {
            String info = QuestionSetManager.getCurrentCreateSet().getInformation();
            String setName = QuestionSetManager.getCurrentCreateSet().getSetName();
            
            dbManager.writeQuestionInformation(setName, info);
            dbManager.disconnect();
        }
    }
    
    @FXML public void btnBack_Action(ActionEvent event) 
    {
        String msg = "Are you sure you wish to go back? Your progress will not be saved.";
        if(allowChange(msg))
            StageManager.loadContentScene(StageManager.CREATEINFO);
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
