package Controller;
        
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import Model.*;
import DB.*;

public class Manager 
{
    ArrayList<QuestionBase> questions;
    
    public QuestionBase getQuestionByIndex(int index)
    {
        return questions.get(index);
    }
    
    public boolean IsQuestionIsEmpty()
    {
        return questions.isEmpty();
    }
    
    public int QuestionSize()
    {
        return questions.size();
    }
    
    public void CalculateQuestionList(HashMap<String,String> CategoriesLevel, String path) 
            throws IOException, FileNotFoundException, ClassNotFoundException
    {
        // טעינת השאלות
        questions = new ArrayList<QuestionBase>();
        
        MultiplePossibleQuestionDB multiplePossibleQuestionDB = new MultiplePossibleQuestionDB();
        OpenQuestionDB openQuestionDB = new OpenQuestionDB();
        YesNoQuestionDB yesNoQuestionDB = new YesNoQuestionDB();
        
        questions.addAll(multiplePossibleQuestionDB.getQuestionsByCategoryAndLevel(CategoriesLevel));
        questions.addAll(openQuestionDB.getQuestionsByCategoryAndLevel(CategoriesLevel));
        questions.addAll(yesNoQuestionDB.getQuestionsByCategoryAndLevel(CategoriesLevel));
        
        //ערבוב נתונים
        Collections.shuffle(questions, new Random());
    }
}
