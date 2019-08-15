package QuestionEditor;

import Managers.DatabaseManager;
import Managers.LogManager;
import Managers.SettingsManager;
import Managers.StageManager;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.stage.Stage;


public class QuestionEditor extends Application 
{    
    private final LogManager logManager = new LogManager();
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        setLocalisedDBPath();
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
            
            dbManager.disconnect();
        }
    }

    private void setLocalisedDBPath()
    {
        try
        {
            //Get directory of current FYP class
            Path path = Paths.get(new File(QuestionEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
            
            //Go up two directories
            path = path.getParent().getParent();
            
            //Append the Database location to the path
            String dbPath = path.resolve("Database/DiagnosisData.db").toString();
            
            SettingsManager.setDBConnString(dbPath);
        } 
        catch (URISyntaxException ex) 
        {
            logManager.ErrorLog("Failed when getting localised DatabasePath");
        }
    }
}
