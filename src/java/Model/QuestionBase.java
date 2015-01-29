package Model;

import java.io.Serializable;
import java.util.Scanner;


public abstract class QuestionBase implements IQuestionBase, Serializable 
{
    protected static Scanner reader = new Scanner(System.in);
    private String question;
    private Category category;
    private Level level;
    private int code;
    
    public abstract QuestionType getQuestionType();
    
    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public int getCode() {
        return this.code;
    }
    
    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
    }
    
    @Override
    public void setCode(int code) {
        this.code = code;
    }
    
    public void setQuestion(String question)
    {
        this.question = question;
    }
    public String getQuestion()
    {
        return this.question;
    }
}
