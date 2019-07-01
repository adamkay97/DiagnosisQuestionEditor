package Controllers;

import Enums.ButtonTypeEnum;
import Managers.QuestionSetManager;
import Managers.StageManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CreateInformationContentController implements Initializable 
{
    @FXML private TextArea txtInformation;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    { 
        String info = QuestionSetManager.getCurrentCreateSet().getInformation();
        
        if(info != null)
            txtInformation.setText(info);
    }
    
    @FXML public void btnNext_Action(ActionEvent event) 
    {
        String info = txtInformation.getText();
        
        if(!info.equals(""))
        {
            QuestionSetManager.getCurrentCreateSet().setInformation(info);
            StageManager.loadContentScene(StageManager.CREATEQUESTIONS);
        }
        else
        {
            String msg = "Please enter text for the Information section.";
            StageManager.loadPopupMessage("Warning", msg, ButtonTypeEnum.OK);
        }
    }
    
    @FXML public void btnBack_Action(ActionEvent event) 
    {
        StageManager.loadContentScene(StageManager.CREATESET);
    }
}
