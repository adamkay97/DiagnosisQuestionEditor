package Classes;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestionSet 
{
    String setName;
    int numberOfQuestions;
    HashMap<Integer, Question> questionSet;
    ArrayList<String> activeLanguages;
    
    public QuestionSet(String name, int numOfQs, HashMap<Integer, Question> qSet, ArrayList<String> languages) 
    { 
        setName = name;
        numberOfQuestions = numOfQs;
        questionSet = qSet; 
        activeLanguages = languages;
    }
    
    public String getSetName() { return setName; }
    public int getNumberOfQuestions() { return numberOfQuestions; }
    public HashMap<Integer, Question> getQuestionSet() { return questionSet; }
    public ArrayList<String> getActiveLanguages() { return activeLanguages; }
    
    
    public void setQuestionSet(HashMap<Integer, Question> qSet) { questionSet = qSet; }
}
