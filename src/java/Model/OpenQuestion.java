package Model;

import java.util.Scanner;

public class OpenQuestion extends QuestionBase
{
    private String answer;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.Open;
    }
    
    public void setAnswer(String answer)
    {
        this.answer = answer;
    }
    
    public String getAnswer()
    {
        return this.answer;
    }

    
}
