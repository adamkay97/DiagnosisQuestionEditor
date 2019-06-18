package Classes;

import java.util.ArrayList;

public class Language 
{
    private final String language;
    private final ArrayList<String> activeQuestionSets;
    
    public Language(String lang, ArrayList<String> activeSets)
    {
        language = lang;
        activeQuestionSets = activeSets;
    }
    
    public String getLanguage() { return language; }
    public ArrayList<String> getActiveQuestionSets() { return activeQuestionSets; }
}
