
package DB;

import java.sql.*;
import Utils.*;
import java.util.*;
import Model.*;

public class MultiplePossibleQuestionDB {
    
    private Connection connection;

    public MultiplePossibleQuestionDB() {
        connection = DBUtil.getConnection();
    }

    public void AddQuestion(MultiplePossibleQuestion multiplePossibleQuestion) 
    {
        try 
        {
            PreparedStatement pStatement;
            pStatement = connection.prepareStatement("insert into multiple_Possible_Questions (QUESTION, ANSWER, CATEGORY, LEVEL)"
                    + " values (?, ?, ?, ? )");
            pStatement.setString(1, multiplePossibleQuestion.GetQuestion());
            pStatement.setInt(2, multiplePossibleQuestion.GetAnswer());
            pStatement.setString(3, multiplePossibleQuestion.GetCategory().name());
            pStatement.setString(4, multiplePossibleQuestion.GetLevel().name());
            pStatement.executeUpdate();
            
            Map<String,String> allAnswers =multiplePossibleQuestion.GetAllAnswer();
            for (int i = 1; i <= multiplePossibleQuestion.GetAllAnswer().size(); i++) 
            {
                pStatement = connection.prepareStatement("insert into multiple_Possible_Answers (ANSWER, QUESTION_CODE, ANSWER_CODE)"
                        + " values (?, ?, ?)");
                pStatement.setString(1, allAnswers.get(i));
                pStatement.setInt(2, multiplePossibleQuestion.GetCode());
                pStatement.setInt(3, i);
                pStatement.executeUpdate();
            }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public void DeleteQuestion(int code) 
    {
        try 
        {
            PreparedStatement pStatement;
            
            pStatement = connection.prepareStatement("delete from Multiple_Possible_Answers where Question_Code=?");
            pStatement.setInt(1, code);
            pStatement.executeUpdate();
            
            pStatement = connection.prepareStatement("delete from Multiple_Possible_Question where code=?");
            pStatement.setInt(1, code);
            pStatement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public List<MultiplePossibleQuestion> GetAllQuestion() 
    {
        List<MultiplePossibleQuestion> multiplePossibleQuestions = new ArrayList<MultiplePossibleQuestion>();

        try 
        {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Multiple_Possible_Question");

            while (rs.next()) 
            {
                MultiplePossibleQuestion multiplePossibleQuestion = SetQuestion(rs);
                
                SetAnswers(multiplePossibleQuestion);
                
                multiplePossibleQuestions.add(multiplePossibleQuestion);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return multiplePossibleQuestions;
    }

    private void SetAnswers(MultiplePossibleQuestion multiplePossibleQuestion) throws SQLException 
    {
        PreparedStatement pStatement = connection.prepareStatement("select * from Multiple_Possible_Answers where Question_Code=?");
        pStatement.setInt(1, multiplePossibleQuestion.GetCode());
        ResultSet rs2 = pStatement.executeQuery();
        
        while (rs2.next())
        {
            multiplePossibleQuestion.AddToAllAnswer(Integer.toString(rs2.getInt("ANSWER_CODE")),
                                                    rs2.getString("ANSWER"));
        }
    }

    private MultiplePossibleQuestion SetQuestion(ResultSet rs) throws SQLException 
    {
        MultiplePossibleQuestion multiplePossibleQuestion = new MultiplePossibleQuestion();
        multiplePossibleQuestion.SetCode(rs.getInt("code"));
        multiplePossibleQuestion.SetQuestion(rs.getString("question"));
        multiplePossibleQuestion.SetAnswer(rs.getInt("answer"));
        multiplePossibleQuestion.SetCategory(Category.valueOf(rs.getString("category")));
        multiplePossibleQuestion.SetLevel(Level.valueOf(rs.getString("level")));
        return multiplePossibleQuestion;
    }

    public List<MultiplePossibleQuestion> GetQuestionsByCategoryAndLevel(HashMap<String,String> categoryLevel) 
    {
        List<MultiplePossibleQuestion> multiplePossibleQuestions = new ArrayList<MultiplePossibleQuestion>();

        try 
        {
            PreparedStatement pStatement;

            pStatement = connection.prepareStatement("select * from Multiple_Possible_Question where category=? And level=");
            Object[] keys = categoryLevel.keySet().toArray();
            
            for (int i = 0; i < categoryLevel.size(); i++) 
            {
                pStatement.setString(1, keys[i].toString());
                pStatement.setString(2, categoryLevel.get(keys[i].toString()));
                ResultSet rs = pStatement.executeQuery();
                if (rs.next()) 
                {
                    MultiplePossibleQuestion multiplePossibleQuestion = SetQuestion(rs);
                    SetAnswers(multiplePossibleQuestion);
                    multiplePossibleQuestions.add(multiplePossibleQuestion);
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return multiplePossibleQuestions;

    }
    
    
}
