package Controllers;

import Managers.QuestionSetManager;
import Managers.StageManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StartContentController implements Initializable 
{
    @FXML private AnchorPane mainAnchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}    

    @FXML public void btnCreate_Action(ActionEvent event) 
    {
        StageManager.loadContentScene(StageManager.CREATESET);
    }

    @FXML public void btnEdit_Action(ActionEvent event) 
    {
        //Set M-CHAT-R/F as the default question set to be editted
        QuestionSetManager.setCurrentEditSet(QuestionSetManager.DEFAULTSET);
        StageManager.loadContentScene(StageManager.EDITQUESTIONS);
    }
    
    @FXML public void btnLanguages_Action(ActionEvent event) 
    {
        StageManager.loadForm(StageManager.MANAGELANGS, new Stage());
    }
    
    @FXML public void btnQuit_Action(ActionEvent event) 
    {
        Stage currentStage = (Stage)mainAnchorPane.getScene().getWindow();
        currentStage.close();
    }

}
