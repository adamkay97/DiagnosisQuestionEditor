package Classes;

import java.util.HashMap;

public class Question 
{
    private final int questionID;
    private final String questionBehaviour;
    private final HashMap<String, String> questionText;
    private final HashMap<String, String> questionInstructions;
    private final String questionRiskResponse;
    
    public Question(int id, String behaviour, HashMap<String, String> qText, 
            HashMap<String, String> instruction, String riskResponse)
    {
        questionID = id;
        questionBehaviour = behaviour;
        questionText = qText;
        questionInstructions = instruction;
        questionRiskResponse = riskResponse;
    }
    
    public int getQuestionId() { return questionID; }
    public String getQuestionBehaviour() { return questionBehaviour; }
    
    //Getters for specific language
    public String getQuestionText(String language) { return questionText.get(language); }
    public String getQuestionInstructions(String language) { return questionInstructions.get(language); }
    
    public HashMap<String, String> getQuestionTextMap() { return questionText; }
    public HashMap<String, String> getQuestionInstructionsMap() { return questionInstructions; }
    
    public String getQuestionRiskResponse() { return questionRiskResponse; }
}