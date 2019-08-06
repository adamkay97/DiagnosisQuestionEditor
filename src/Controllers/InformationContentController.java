package Controllers;

import Enums.ButtonTypeEnum;
import Managers.DatabaseManager;
import Managers.QuestionSetManager;
import Managers.StageManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class InformationContentController implements Initializable 
{
    @FXML private TextArea txtInformation;
    @FXML private Label lblHeader;
    @FXML private Button btnNext;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    { 
        String info;
        
        if(QuestionSetManager.getInEdit())
        {
            info = QuestionSetManager.getQuestionSet(QuestionSetManager.getCurrentEditSet()).getInformation();
            lblHeader.setText("Edit Questions");
            btnNext.setText("Save");
        }
        else
            info = QuestionSetManager.getCurrentCreateSet().getInformation();
        
        if(info != null)
            txtInformation.setText(info);
    }
    
    @FXML public void btnNext_Action(ActionEvent event) 
    {
        String info = txtInformation.getText();
        
        if(!info.equals(""))
        {
            if(QuestionSetManager.getInEdit())
            {
                DatabaseManager dbManager = new DatabaseManager();
                if(dbManager.connect())
                {
                    String setName = QuestionSetManager.getCurrentEditSet();
                    dbManager.deleteQuestionInfo(setName);
                    dbManager.resetSequenceID("RichTextData");
                    dbManager.writeQuestionInformation(setName, info);
                    dbManager.disconnect();
                    
                    StageManager.loadContentScene(StageManager.EDITQUESTIONS);
                }
            }
            else
            {
                QuestionSetManager.getCurrentCreateSet().setInformation(info);
                StageManager.loadContentScene(StageManager.SCORING);
            }   
        }
        else
        {
            String msg = "Please enter text for the Information section.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }
    
    @FXML public void btnBack_Action(ActionEvent event) 
    {
        if(QuestionSetManager.getInEdit())
            StageManager.loadContentScene(StageManager.EDITQUESTIONS);
        else
            StageManager.loadContentScene(StageManager.CREATESET);
    }
}
