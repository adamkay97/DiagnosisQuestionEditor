package Managers;

import Classes.Question;
import Classes.QuestionSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
            
            questionSetsMap.put(setName, new QuestionSet(setName, questionList.size(), questionList, languageList));
        }
        QuestionSetManager.setQuestionSetsMap(questionSetsMap);
    }
    
    private HashMap<Integer, Question> loadQuestionList(String diagnosisName, ArrayList<String> languageList)
    {
        HashMap<Integer, Question> questionMap = new HashMap<>();
        
        String query = String.format("SELECT * FROM `%s`", diagnosisName);
        
        //Loads all the questions in to the questionMap so it can be used 
        //in the questionaire form for the specific diagnosis.
        try(Statement stmt = conn.createStatement(); 
                ResultSet results = stmt.executeQuery(query))
        {
            while(results.next())
            {
                HashMap<String, String> texts = new HashMap<>();
                HashMap<String, String> instructions = new HashMap<>();
                
                int qNumber = results.getInt("QuestionID");
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
        
        String query = "SELECT * FROM QuestionSets";
        
        try(Statement stmt = conn.createStatement(); 
                ResultSet results = stmt.executeQuery(query)) 
        {    
            while(results.next())
            {
                String name = results.getString("SetName");
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
        ArrayList<String> languageList = new ArrayList<>();
        
        String query = "SELECT * FROM QuestionLanguages";
        
        try(Statement stmt = conn.createStatement(); 
                ResultSet results = stmt.executeQuery(query)) 
        {    
            while(results.next())
            {                
                String lang = results.getString("Language");
                languageList.add(lang);
            }
            //Set language list on QuestionSetManager
            QuestionSetManager.setQuestionLanguages(languageList);
        }
        catch(SQLException ex)
        {
            System.out.println("Error when reading the Languages from the db - " + ex.getMessage());
        }
    }
    
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
    
    public boolean createNewSetTable(QuestionSet questionSet)
    {
        boolean success = true;
        
        //Start query with two fixed columns (id and Behaviour Name)
        String query = "CREATE TABLE `" + questionSet.getSetName() + "` "
        + "(`QuestionID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
        + "`BehaviourName` TEXT NOT NULL";
        
        //For each active language append to query string the Question Text and Instruction with language.
        for(String lang : questionSet.getActiveLanguages())
        {
            query += String.format(", `QuestionText-%s` TEXT NOT NULL, "
                                 + "`QuestionInstruction-%s` TEXT NOT NULL", lang, lang);
        }
        query += ")"; 
        
        try(Statement stmt = conn.createStatement())
        {
            stmt.executeUpdate(query);
        }
        catch(SQLException ex)
        {
            System.out.println("Error when creating new Question Set table in the db - " + ex.getMessage());
            success = false;
        }
        return success;
    }
    
    public boolean writeNewQuestionSet(QuestionSet questionSet)
    {
        ArrayList<String> langList = questionSet.getActiveLanguages();
        
        String query = "INSERT INTO `" + questionSet.getSetName() + "` (`BehaviourName`";
        
        //For each active language append to query string the Question Text and Instruction with language.
        for(String lang : langList)
            query += String.format(", `QuestionText-%s`, `QuestionInstruction-%s`", lang, lang);
        
        query += ") VALUES (?";
        
        for(String lang : langList)
            query += ", ?, ?";
        
        query += ")";
        
        HashMap<Integer, Question> qMap = questionSet.getQuestionSet();
        for (int i = 1; i <= questionSet.getNumberOfQuestions(); i++) 
        {
            Question q = qMap.get(i);
            try(PreparedStatement pstmt = conn.prepareStatement(query)) 
            {    
                pstmt.setString(1, q.getQuestionBehaviour());
                
                int index = 2;
                for(String lang : langList)
                {
                    pstmt.setString(index, q.getQuestionText(lang));
                    pstmt.setString(index+1, q.getQuestionInstructions(lang));
                    index += 2;
                }
                
                System.out.println(pstmt.toString());
                pstmt.executeUpdate();
            }
            catch(SQLException ex)
            {
                System.out.println("Error when inserting new question row to "
                                + "new question set in the db - " + ex.getMessage());
                return false;
            }
        }
        return true;
    }
    
    public void writeNewLanguage(String language)
    {
        String query = "INSERT INTO QuestionLanguages (Language) VALUES (?)";
        
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
    
    public void writeNewSetName(String setName)
    {
        String query = "INSERT INTO QuestionSets (SetName) VALUES (?)";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, setName);
            pstmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Error when writing new Question Set Name to the db - " + ex.getMessage());
        }
    }
    
    public void writeNewSetLanguages(QuestionSet qSet)
    {
        String query = "INSERT INTO QuestionSetLanguages (QuestionSetID, QuestionLanguageID) VALUES " +
                       "((SELECT QuestionSetID FROM QuestionSets WHERE SetName = ?), " +
                       "(SELECT QuestionLanguageID FROM QuestionLanguages WHERE Language = ?))";
        
        qSet.getActiveLanguages().forEach((language) -> 
        {
            try(PreparedStatement pstmt = conn.prepareStatement(query)) 
            {    
                pstmt.setString(1, qSet.getSetName());
                pstmt.setString(2, language);
                pstmt.executeUpdate();
            }
            catch(SQLException ex)
            {
                System.out.println("Error when writing Set Name and Language to SetLanguage link table to the db - " + ex.getMessage());
            }
        });
    }
    
    public void writeQuestionInformation(String setName, String information)
    {
        String[] infoList = information.split("\n");
        String query = String.format("INSERT INTO RichTextData (`PageName`, `InfoText-English`) VALUES (?, ?)", setName);
        
        for(String info : infoList)
        {
            try(PreparedStatement pstmt = conn.prepareStatement(query)) 
            {    
                pstmt.setString(1, setName);
                pstmt.setString(2, info);
                pstmt.executeUpdate();
            }
            catch(SQLException ex)
            {
                System.out.println("Error when writing information data to the db - " + ex.getMessage());
            }
        }
    }
    
    public ArrayList<String> getSetLanguages(String setName)
    {
        ArrayList<String> setLanguages = new ArrayList<>();
        
        String query = "SELECT s.SetName, l.Language FROM QuestionSetLanguages sl " +
                       "JOIN QuestionSets s on sl.QuestionSetID = s.QuestionSetID " +
                       "JOIN QuestionLanguages l on sl.QuestionLanguageID = l.QuestionLanguageID " +
                       "WHERE s.SetName = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, setName);
            ResultSet results = pstmt.executeQuery();
            
            while(results.next())
            {
                String language = results.getString("Language");
                setLanguages.add(language);
            }
        }
        catch(SQLException ex)
        {
            System.out.println("Error when getting question set languages to the db - " + ex.getMessage());
        }
        return setLanguages;
    }
    
    public boolean getLanguageActive(String language)
    {
        String query = "SELECT s.SetName FROM QuestionSetLanguages sl " +
                       "JOIN QuestionSets s on sl.QuestionSetID = s.QuestionSetID " +
                       "JOIN QuestionLanguages l on sl.QuestionLanguageID = l.QuestionLanguageID " +
                       "WHERE l.Language = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, language);
            ResultSet results = pstmt.executeQuery();
            
            if(results.next())
                return true;
        }
        catch(SQLException ex)
        {
            System.out.println("Error when checking language active in the db - " + ex.getMessage());
        }
        return false;
    }
    
    public boolean checkSetNameExists(String setName)
    {
        String query = "SELECT * FROM QuestionSets WHERE SetName = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, setName);
            ResultSet results = pstmt.executeQuery();
            
            if(results.next())
                return true;
        }
        catch(SQLException ex)
        {
            System.out.println("Error when checking if set name exists in the db - " + ex.getMessage());
        }
        return false;
    }
    
    public void deleteAllSetData(String setName)
    {
        deleteQuestionInfo(setName);
        deleteQuestionSetTable(setName);
        deleteQuestionSetLanguage(setName);
        deleteQuestionSet(setName);
    }
    
    public void deleteQuestionSetTable(String setName)
    {
        String query = String.format("DROP TABLE IF EXISTS `%s`", setName);
        
        try(Statement stmt = conn.createStatement())
        {
            stmt.executeUpdate(query);
        }
        catch(SQLException ex)
        {
            System.out.println("Error when dropping Question Set table in the db - " + ex.getMessage());
        }
    }
    
    private void deleteQuestionSetLanguage(String setName)
    {
        String query = "DELETE FROM QuestionSetLanguages WHERE QuestionSetID = "
                     + "(SELECT QuestionSetID FROM QuestionSets WHERE SetName = ?)";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, setName);
            pstmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Error when deleting a Question Set from Set/Language link table in the db - " + ex.getMessage());
        }
    }
    
    private void deleteQuestionSet(String setName)
    {
        String query = "DELETE FROM QuestionSets WHERE SetName = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, setName);
            pstmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Error when deleting a Set Name from the db - " + ex.getMessage());
        }
    }
    
    public void deleteLanguage(String language)
    {
        String query = "DELETE FROM QuestionLanguages WHERE Language = ?";
        
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
    
    public void deleteQuestionInfo(String setName)
    {
        String query = "DELETE FROM RichTextData WHERE PageName = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(query)) 
        {    
            pstmt.setString(1, setName);
            pstmt.executeUpdate();
        }
        catch(SQLException ex)
        {
            System.out.println("Error when deleting question information from the db - " + ex.getMessage());
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
