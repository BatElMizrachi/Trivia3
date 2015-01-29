package Model;

import java.util.Scanner;

public class YesNoQuestion extends QuestionBase
{
    private boolean answer;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.YesNo;
    }

    public void setAnswer(boolean answer)
    {
        this.answer = answer;
    }
    
    public void setAnswer(String answer)
    {
        this.answer = answer.equals("Yes");
    }
    
    public boolean getAnswer()
    {
        return this.answer;
    }
    
}
