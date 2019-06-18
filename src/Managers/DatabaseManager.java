package Managers;

//import Classes.FormText;
//import Classes.PopupText;
import Classes.Language;
import Classes.Question;
import Classes.QuestionSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager 
{
    //private final String DBCONNSTRING = "/Database/FYP_Database.db";
    private final String DBCONNSTRING = "C:/Users/Adam/Documents/Degree/Third Year/Final Project/Application/FYP_Database.db";
    private Connection conn;
    
    public boolean connect() 
    {
        //Connects to SQLite database stored in the root of the application
        conn = null;
        try 
        {
            //conn = DriverManager.getConnection("jdbc:sqlite::resource:" + getClass().getResource(DBCONNSTRING));
            conn = DriverManager.getConnection("jdbc:sqlite:" + DBCONNSTRING);
            System.out.println("A connection to the SQLite db has been established.");    
            return true;
        } 
        catch (SQLException e) 
        {    
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public void loadQuestionSetMap()
    {
        ArrayList<String> questionSets = QuestionSetManager.getQuestionSets();
        HashMap<String, QuestionSet> questionSetsMap = new HashMap<>();
        
        for(String setName : questionSets)
        {
            ArrayList<String> languageList = QuestionSetManager.getSetLanguages(setName);
            HashMap<Integer, Question> questionList = loadQuestionList(setName, languageList);
            
            questionSetsMap.put(setName, new QuestionSet(questionList));
        }
        QuestionSetManager.setQuestionSetsMap(questionSetsMap);
    }
    
    private HashMap<Integer, Question> loadQuestionList(String diagnosisName, ArrayList<String> languageList)
    {
        HashMap<Integer, Question> questionMap = new HashMap<>();
        
        String query = "SELECT * FROM QuestionList WHERE DiagnosisName = ?";
        
        //Loads all the questions in to the questionMap so it can be used 
        //in the questionaire form for the specific diagnosis.
        try(PreparedStatement pstmt = conn.prepareStatement(query))
        {    
            pstmt.setString(1, diagnosisName);
            ResultSet results = pstmt.executeQuery();
            
            while(results.next())
            {
                HashMap<String, String> texts = new HashMap<>();
                HashMap<String, String> instructions = new HashMap<>();
                
                int qNumber = results.getInt("QuestionNumber");
                String qBehaviour = results.getString("BehaviourName");
                
                for(String language : languageList)
                {
                    String text = results.getString("QuestionText-"+language);
                    texts.put(language, text);
                    String instruction = results.getString("QuestionInstruction-"+language);
                    instructions.put(language, instruction);
                }
                
                Question q = new Question(qNumber, qBehaviour, texts, instructions);
                
                questionMap.put(qNumber, q);
            }
            return questionMap;
        }
        catch(SQLException ex)
        {
            System.out.println("Error when reading questions from the db - " + ex.getMessage());
        }
        return null;
    }
    
    public void loadQuestionSetList()
    {
        ArrayList<String> questionSetList = new ArrayList<>();
        
        String query = "SELECT DISTINCT DiagnosisName FROM QuestionList";
        
        try(Statement stmt = conn.createStatement(); 
                ResultSet results = stmt.executeQuery(query)) 
        {    
            while(results.next())
            {
                String name = results.getString("DiagnosisName");
                questionSetList.add(name);
            }
            QuestionSetManager.setQuestionSets(questionSetList);
        }
        catch(SQLException ex)
        {
            System.out.println("Error when reading the Question Sets from the db - " + ex.getMessage());
        }
    }
    
    public void loadLanguageList()
    {
        ArrayList<Language> languageList = new ArrayList<>();
        
        String query = "SELECT * FROM Languages";
        
        try(Statement stmt = conn.createStatement(); 
                ResultSet results = stmt.executeQuery(query)) 
        {    
            while(results.next())
            {
                ArrayList<String> questionSets = new ArrayList<>();
                
                String lang = results.getString("Language");
                String sets = results.getString("ActiveQuestionSets");
                
                //If the active sets is empty skip and add empty list to language object
                if(!sets.equals(""))
                {
                    //If sets contains multiple active sets create new array list
                    //Else just add the singluar set to the list
                    if(sets.contains(","))
                    {
                        //Split the sets and create new array list of different sets used
                        String[] setsArray = sets.split(",");
                        questionSets = new ArrayList<>(Arrays.asList(setsArray));
                    }
                    else
                        questionSets.add(sets);
                }
                
                //Create new language object and add to language list
                Language language = new Language(lang, questionSets);
                languageList.add(language);
            }
            //Set language list on QuestionSetManager
            QuestionSetManager.setLanguageList(languageList);
        }
        catch(SQLException ex)
        {
            System.out.println("Error when reading the Languages from the db - " + ex.getMessage());
        }
    }
    
    /*public User loadUser(int userId)
    {
        String query = "SELECT * FROM Users WHERE UserID = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setInt(1, userId);
            ResultSet results = pstmt.executeQuery();
            
            while(results.next())
            {
                String uname = results.getString("Username");
                String pword = results.getString("HashPassword");
                String fname = results.getString("FirstName");
                String lname = results.getString("LastName");
                User user = new User(userId, uname, pword, fname, lname);
                
                return user;
            }
        }
        catch(SQLException ex)
        {
             System.out.println("Error when reading the user from the db - " + ex.getMessage());
        }
        return null;
    }*/
    
    /*public ArrayList<String> loadInformationData(String pageName)
    {
        ArrayList<String> mchatInfo = new ArrayList<>();
        
        String query = "SELECT * FROM RichTextData WHERE PageName = ?";
        String language = LanguageManager.getLanguage();
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, pageName);
            ResultSet results = pstmt.executeQuery();
            
            while(results.next())
            {
                String infoHeader = results.getString("InfoHeading-"+language);
                String infoText = results.getString("InfoText-"+language);
                mchatInfo.add(infoHeader + "%" + infoText);
            }
            return mchatInfo;
        }
        catch(SQLException ex)
        {
             System.out.println("Error when reading the MCHAT information from the db - " + ex.getMessage());
        }
        return null;
    }*/
    
    public void writeNewLanguage(String language)
    {
        String query = "INSERT INTO Languages (Language) VALUES (?)";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, language);
            pstmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Error when writing new Language to the db - " + ex.getMessage());
        }
    }
    
    public void deleteLanguage(String language)
    {
        String query = "DELETE FROM Languages WHERE Language = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, language);
            pstmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Error when deleting a Language from the db - " + ex.getMessage());
        }
    }
    
    public void decreaseSequenceID(String tableName)
    {
        int seq = getLastInsertedRowID(tableName);
        
        String query = "UPDATE sqlite_sequence SET seq = ? WHERE name = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setInt(1, seq-1);
            pstmt.setString(2, tableName);
            pstmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Error when getting the next child Id from the db - " + ex.getMessage());
        }
    }
    
    public int getLastInsertedRowID(String tableName)
    {
        String query = "SELECT seq FROM sqlite_sequence WHERE name = ?";
        int id = 0;
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, tableName);
            ResultSet results = pstmt.executeQuery();
            
            while(results.next())
                id = results.getInt("seq");
        }
        catch(SQLException ex)
        {
            System.out.println("Error when getting the next child Id from the db - " + ex.getMessage());
        }
        return id;
    }
    
    private ArrayList<String> getFormNames()
    {
        String query = "SELECT DISTINCT Form FROM FormText";
        ArrayList<String> formNames = new ArrayList<>();
        
        try(Statement stmt = conn.createStatement(); 
                ResultSet results = stmt.executeQuery(query)) 
        {    
            while(results.next())
            {
                String formName = results.getString("Form");
                formNames.add(formName);
            }
            return formNames;
        }
        catch(SQLException ex)
        {
             System.out.println("Error when reading the Form Names in FormText from the db - " + ex.getMessage());
        }
        return null;
    }
    
    public void disconnect()
    {
        try 
        {
            if (conn != null)
            {
                conn.close();
                System.out.println("Successfully disconnected from the SQLite db.");
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }
}
