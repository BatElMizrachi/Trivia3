package Model;

import java.io.Serializable;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MultiplePossibleQuestion extends QuestionBase implements Serializable
{
    private int answer;
    private Map<String, String> allAnswers;

    public MultiplePossibleQuestion() {
        this.allAnswers = new HashMap<String, String>();
    }
    
    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MultiplePossible;
    }

    public void setAnswer(int answer)
    {
        this.answer = answer;
    }
    
    public int getAnswer()
    {
        return this.answer;
    }
    
    public void setAllAnswer(Map<String, String> allAnswers)
    {
        this.allAnswers.putAll(allAnswers);
    }
    
    public void setAllAnswer(HashMap<String, String> allAnswers)
    {
        this.allAnswers.putAll(allAnswers);
    }
    
    public void AddToAllAnswer(String key, String answer)
    {
        this.allAnswers.put(key, answer);
    }
    public Map<String, String> getAllAnswer()
    {
        return this.allAnswers;
    }
    
    public void ShowAllAnswers()
    {
        for (Map.Entry<String,String> currentAnswer : this.allAnswers.entrySet()) 
        {
            System.out.println(currentAnswer.getKey() + "." + currentAnswer.getValue());
        }
    }
    
    public String getCorrectAnswer()
    {
        return this.allAnswers.get(this.answer);
    }


}
