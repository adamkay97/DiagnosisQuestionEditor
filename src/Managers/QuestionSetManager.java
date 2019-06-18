package Managers;

import Classes.Language;
import Classes.Question;
import Classes.QuestionSet;
import java.util.ArrayList;
import java.util.HashMap;

public class QuestionSetManager 
{
    private static ArrayList<Language> languageList;
    private static ArrayList<String> questionSets;
    private static HashMap<String, QuestionSet> questionSetsMap;
    
    public static void setLanguageList(ArrayList<Language> languages) { languageList = languages; }
    public static void setQuestionSetsMap(HashMap<String, QuestionSet> questions) { questionSetsMap = questions; }
    public static void setQuestionSets(ArrayList<String> qSets) { questionSets = qSets; }
    
    public static HashMap<Integer, Question> getQuestionSet(String setName)
    {
        return questionSetsMap.get(setName).getQuestionSet();
    }
    
    public static ArrayList<String> getAllLanguages()
    {
        ArrayList<String> languages = new ArrayList<>();
        
        for(Language language : languageList)
            languages.add(language.getLanguage());
        
        return languages;
    }
    
    public static ArrayList<String> getSetLanguages(String setName)
    {
        ArrayList<String> setLanguage = new ArrayList<>();
        
        for(Language language : languageList)
        {
            if(language.getActiveQuestionSets().contains(setName))
                setLanguage.add(language.getLanguage());
        }
        return setLanguage;
    }
    
    public static ArrayList<Language> getLanguageList() { return languageList; }
    public static HashMap<String, QuestionSet> getQuestionMap() { return questionSetsMap; }
    public static ArrayList<String> getQuestionSets() { return questionSets; }
}
