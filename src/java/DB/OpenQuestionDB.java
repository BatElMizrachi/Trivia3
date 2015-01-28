
package DB;

import java.sql.*;
import Utils.*;
import java.util.*;
import Model.*;

public class OpenQuestionDB 
{
    private Connection connection;

    public OpenQuestionDB() {
        connection = DBUtil.getConnection();
    }

    public void AddQuestion(OpenQuestion openQuestion) 
    {
        try 
        {
            PreparedStatement pStatement;
            pStatement = connection.prepareStatement("insert into OPEN_QUESTION (QUESTION, ANSWER, CATEGORY, LEVEL)"
                    + " values (?, ?, ?, ? )");
            pStatement.setString(1, openQuestion.GetQuestion());
            pStatement.setString(2, openQuestion.GetAnswer());
            pStatement.setString(3, openQuestion.GetCategory().name());
            pStatement.setString(4, openQuestion.GetLevel().name());

            pStatement.executeUpdate();
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
            pStatement = connection.prepareStatement("delete from OPEN_QUESTION where code=?");
            pStatement.setInt(1, code);
            pStatement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public List<OpenQuestion> GetAllQuestion() 
    {
        List<OpenQuestion> openQuestions = new ArrayList<OpenQuestion>();

        try 
        {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from OPEN_QUESTION");

            while (rs.next()) 
            {
                OpenQuestion openQuestion = SetQuestion(rs);
                openQuestions.add(openQuestion);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return openQuestions;
    }

    private OpenQuestion SetQuestion(ResultSet rs) throws SQLException 
    {
        OpenQuestion openQuestion = new OpenQuestion();
        openQuestion.SetCode(rs.getInt("code"));
        openQuestion.SetQuestion(rs.getString("question"));
        openQuestion.SetAnswer(rs.getString("answer"));
        openQuestion.SetCategory(Category.valueOf(rs.getString("category")));
        openQuestion.SetLevel(Level.valueOf(rs.getString("level")));
        return openQuestion;
    }

    public List<OpenQuestion> GetQuestionsByCategoryAndLevel(HashMap<String,String> categoryLevel) 
    {
        List<OpenQuestion> openQuestions = new ArrayList<OpenQuestion>();

        try 
        {
            PreparedStatement pStatement;

            pStatement = connection.prepareStatement("select * from OPEN_QUESTION where category=? And level=");
            Object[] keys = categoryLevel.keySet().toArray();
            
            for (int i = 0; i < categoryLevel.size(); i++) 
            {
                pStatement.setString(1, keys[i].toString());
                pStatement.setString(2, categoryLevel.get(keys[i].toString()));
                ResultSet rs = pStatement.executeQuery();
                if (rs.next()) 
                {
                    OpenQuestion openQuestion = SetQuestion(rs);
                    openQuestions.add(openQuestion);
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return openQuestions;

    }
    
}
