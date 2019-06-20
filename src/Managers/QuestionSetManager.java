package Managers;

import Classes.QuestionSet;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestionSetManager 
{
    private static ArrayList<String> questionLanguages;
    private static ArrayList<String> questionSets;
    private static HashMap<String, QuestionSet> questionSetsMap;
    private static String currentEditSet;
    
    public static void setQuestionLanguages(ArrayList<String> languages) { questionLanguages = languages; }
    public static void setQuestionSets(ArrayList<String> qSets) { questionSets = qSets; }
    
    public static void setQuestionSetsMap(HashMap<String, QuestionSet> questions) { questionSetsMap = questions; }
    
    public static void setCurrentEditSet(String set) { currentEditSet = set; }
    
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
    
    public static ArrayList<String> getQuestionLanguages() { return questionLanguages; }
    public static HashMap<String, QuestionSet> getQuestionMap() { return questionSetsMap; }
    public static ArrayList<String> getQuestionSets() { return questionSets; }
    public static String getCurrentEditSet() { return currentEditSet; }
}
