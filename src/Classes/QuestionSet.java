package Classes;

import java.util.HashMap;

public class QuestionSet 
{
    HashMap<Integer, Question> questionSet;
    
    public QuestionSet(HashMap<Integer, Question> qSet) { questionSet = qSet; }
    
    public HashMap<Integer, Question> getQuestionSet() { return questionSet; }
    public void setQuestionSet(HashMap<Integer, Question> qSet) { questionSet = qSet; }
}
