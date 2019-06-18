package questioneditor;

import Managers.DatabaseManager;
import Managers.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class QuestionEditor extends Application 
{    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        loadDatabaseData();
        StageManager.loadForm(StageManager.MAIN, stage);
    }
    
    private void loadDatabaseData()
    {
        DatabaseManager dbManager = new DatabaseManager();
        
        if(dbManager.connect())
        {
            dbManager.loadLanguageList();
            dbManager.loadQuestionSetList();
            dbManager.loadQuestionSetMap();
        }
    }

    
    
}
