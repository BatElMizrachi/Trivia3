package Utils;

import Model.Category;
import Model.Level;
import Model.QuestionType;
import java.util.Scanner;

public class Utils
{
    public static Level getLevelByUserChoose(String levelName)
    {
        if(levelName.equals("Medium"))
            return Level.Medium;
        else if(levelName.equals("Difficult"))
            return Level.Difficult;
        
        return Level.Easy;
    }
 
    public static Category getCategoryByUserChoose(String categoryName)
    {
        if(categoryName.equals("Food"))
            return Category.Food;
        else if(categoryName.equals("History"))
            return Category.History;
        else if(categoryName.equals("Sport"))
            return Category.Sport;
        
        return Category.Other;
    }
    
    public static QuestionType getQuestionTypeByUserChoose(String questionTypeName)
    {
        if(questionTypeName.equals("Yes/No"))
            return QuestionType.YesNo;
        else if(questionTypeName.equals("MultipleChoice"))
            return QuestionType.MultiplePossible;
        
        return QuestionType.Open;
    }
    
    public static boolean ChangeAnswerBoolean(String boolAnswer)
    {
        if (boolAnswer.equals("Yes"))
            return true;
        return false;
    }
}
