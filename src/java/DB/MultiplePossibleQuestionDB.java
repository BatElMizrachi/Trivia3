
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
            pStatement = connection.prepareStatement("insert into multiple_Possible_Question (QUESTION, ANSWER, CATEGORY, LEVEL)"
                    + " values (?, ?, ?, ? )");
            pStatement.setString(1, multiplePossibleQuestion.getQuestion());
            pStatement.setInt(2, multiplePossibleQuestion.getAnswer());
            pStatement.setString(3, multiplePossibleQuestion.getCategory().name());
            pStatement.setString(4, multiplePossibleQuestion.getLevel().name());
            pStatement.executeUpdate();
            
            pStatement = connection.prepareStatement("select CODE from multiple_Possible_Question where QUESTION=? and ANSWER=?"
                                                        + "and CATEGORY=? and LEVEL=?");
            pStatement.setString(1, multiplePossibleQuestion.getQuestion());
            pStatement.setInt(2, multiplePossibleQuestion.getAnswer());
            pStatement.setString(3, multiplePossibleQuestion.getCategory().name());
            pStatement.setString(4, multiplePossibleQuestion.getLevel().name());
            ResultSet rs = pStatement.executeQuery();
            rs.next();
            int code = rs.getInt("CODE");
            
            Map<String,String> allAnswers =multiplePossibleQuestion.getAllAnswer();
            for (int i = 1; i <= multiplePossibleQuestion.getAllAnswer().size(); i++) 
            {
                pStatement = connection.prepareStatement("insert into multiple_Possible_Answers (ANSWER, QUESTION_CODE, ANSWER_CODE)"
                        + " values (?, ?, ?)");
                pStatement.setString(1, allAnswers.get(Integer.toString(i)));
                pStatement.setInt(2, code);
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
                MultiplePossibleQuestion multiplePossibleQuestion = setQuestion(rs);
                
                setAnswers(multiplePossibleQuestion);
                
                multiplePossibleQuestions.add(multiplePossibleQuestion);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return multiplePossibleQuestions;
    }

    private void setAnswers(MultiplePossibleQuestion multiplePossibleQuestion) throws SQLException 
    {
        PreparedStatement pStatement = connection.prepareStatement("select * from Multiple_Possible_Answers where Question_Code=?");
        pStatement.setInt(1, multiplePossibleQuestion.getCode());
        ResultSet rs2 = pStatement.executeQuery();
        
        while (rs2.next())
        {
            multiplePossibleQuestion.AddToAllAnswer(Integer.toString(rs2.getInt("ANSWER_CODE")),
                                                    rs2.getString("ANSWER"));
        }
    }

    private MultiplePossibleQuestion setQuestion(ResultSet rs) throws SQLException 
    {
        MultiplePossibleQuestion multiplePossibleQuestion = new MultiplePossibleQuestion();
        multiplePossibleQuestion.setCode(rs.getInt("code"));
        multiplePossibleQuestion.setQuestion(rs.getString("question"));
        multiplePossibleQuestion.setAnswer(rs.getInt("answer"));
        multiplePossibleQuestion.setCategory(Category.valueOf(rs.getString("category")));
        multiplePossibleQuestion.setLevel(Level.valueOf(rs.getString("level")));
        return multiplePossibleQuestion;
    }

    public List<MultiplePossibleQuestion> getQuestionsByCategoryAndLevel(HashMap<String,String> categoryLevel) 
    {
        List<MultiplePossibleQuestion> multiplePossibleQuestions = new ArrayList<MultiplePossibleQuestion>();

        try 
        {
            PreparedStatement pStatement;

            pStatement = connection.prepareStatement("select * from Multiple_Possible_Question where category=? And level=?");
            Object[] keys = categoryLevel.keySet().toArray();
            
            for (int i = 0; i < categoryLevel.size(); i++) 
            {
                pStatement.setString(1, keys[i].toString());
                pStatement.setString(2, categoryLevel.get(keys[i].toString()));
                ResultSet rs = pStatement.executeQuery();
                while (rs.next()) 
                {
                    MultiplePossibleQuestion multiplePossibleQuestion = setQuestion(rs);
                    setAnswers(multiplePossibleQuestion);
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
