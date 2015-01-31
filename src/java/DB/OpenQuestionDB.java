
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
            pStatement.setString(1, openQuestion.getQuestion());
            pStatement.setString(2, openQuestion.getAnswer());
            pStatement.setString(3, openQuestion.getCategory().name());
            pStatement.setString(4, openQuestion.getLevel().name());

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
                OpenQuestion openQuestion = setQuestion(rs);
                openQuestions.add(openQuestion);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return openQuestions;
    }

    private OpenQuestion setQuestion(ResultSet rs) throws SQLException 
    {
        OpenQuestion openQuestion = new OpenQuestion();
        openQuestion.setCode(rs.getInt("code"));
        openQuestion.setQuestion(rs.getString("question"));
        openQuestion.setAnswer(rs.getString("answer"));
        openQuestion.setCategory(Category.valueOf(rs.getString("category")));
        openQuestion.setLevel(Level.valueOf(rs.getString("level")));
        return openQuestion;
    }

    public List<OpenQuestion> getQuestionsByCategoryAndLevel(HashMap<String,String> categoryLevel) 
    {
        List<OpenQuestion> openQuestions = new ArrayList<OpenQuestion>();

        try 
        {
            PreparedStatement pStatement;

            pStatement = connection.prepareStatement("select * from OPEN_QUESTION where category=? And level=?");
            Object[] keys = categoryLevel.keySet().toArray();
            
            for (int i = 0; i < categoryLevel.size(); i++) 
            {
                pStatement.setString(1, keys[i].toString());
                pStatement.setString(2, categoryLevel.get(keys[i].toString()));
                ResultSet rs = pStatement.executeQuery();
                while (rs.next()) 
                {
                    OpenQuestion openQuestion = setQuestion(rs);
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
