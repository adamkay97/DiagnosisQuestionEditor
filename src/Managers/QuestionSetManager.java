package Managers;

import Classes.QuestionSet;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestionSetManager 
{
    public static final String DEFAULTSET = "M-CHAT-R/F";
    private static boolean inEdit = false;
    
    private static ArrayList<String> questionLanguages;
    private static ArrayList<String> questionSets;
    private static HashMap<String, QuestionSet> questionSetsMap;
    
    private static String currentEditSet;
    private static QuestionSet currentCreateSet;
    
    public static void setInEdit(boolean edit) { inEdit = edit; }
    public static void setQuestionLanguages(ArrayList<String> languages) { questionLanguages = languages; }
    public static void setQuestionSets(ArrayList<String> qSets) { questionSets = qSets; }
    public static void setQuestionSetsMap(HashMap<String, QuestionSet> questions) { questionSetsMap = questions; }
    public static void setCurrentEditSet(String set) { currentEditSet = set; }
    public static void setCurrentCreateSet(QuestionSet qSet) { currentCreateSet = qSet; }
    
    public static QuestionSet getQuestionSet(String setName)
    {
        //return questionSetsMap.get(setName).getQuestionSet();
         return questionSetsMap.get(setName);
    }
    
    public static ArrayList<String> getSetLanguages(String setName)
    {
        ArrayList<String> setLanguages = new ArrayList<>();
        
        DatabaseManager dbManager = new DatabaseManager();
        if(dbManager.connect())
        {
            setLanguages = dbManager.getSetLanguages(setName);
            dbManager.disconnect();
        }
        return setLanguages;
    }
    
    public static boolean getInEdit() { return inEdit; }
    public static ArrayList<String> getQuestionLanguages() { return questionLanguages; }
    public static HashMap<String, QuestionSet> getQuestionMap() { return questionSetsMap; }
    public static ArrayList<String> getQuestionSets() { return questionSets; }
    public static String getCurrentEditSet() { return currentEditSet; }
    public static QuestionSet getCurrentCreateSet() { return currentCreateSet; }
}
