package Classes;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionSet 
{
    private final String setName;
    private final int numberOfQuestions;
    private HashMap<Integer, Question> questionSet;
    private final ArrayList<String> activeLanguages;
    private String information;
    private ScoringAlgorithm scoringAlgorithm;
    
    public QuestionSet(String name, int numOfQs, HashMap<Integer, Question> qSet, ArrayList<String> languages, 
            String info, ScoringAlgorithm sa) 
    { 
        setName = name;
        numberOfQuestions = numOfQs;
        questionSet = qSet; 
        activeLanguages = languages;
        information = info;
        scoringAlgorithm = sa;
    }
    
    public void addNewActiveLanguage(String language) { activeLanguages.add(language); }
    
    public String getSetName() { return setName; }
    public int getNumberOfQuestions() { return numberOfQuestions; }
    public HashMap<Integer, Question> getQuestionSet() { return questionSet; }
    public ArrayList<String> getActiveLanguages() { return activeLanguages; }
    public String getInformation() { return information; }
    public ScoringAlgorithm getScoringAlgorithm() { return scoringAlgorithm; }
    
    public void setQuestionSet(HashMap<Integer, Question> qSet) { questionSet = qSet; }
    public void setInformation(String info) { information = info; }
    public void setScoringAlgorithm(ScoringAlgorithm algorithm) { scoringAlgorithm = algorithm; }
}
