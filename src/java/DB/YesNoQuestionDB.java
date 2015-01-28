
package DB;

import java.sql.*;
import Utils.*;
import java.util.*;
import Model.*;

public class YesNoQuestionDB {
    private Connection connection;

    public YesNoQuestionDB() {
        connection = DBUtil.getConnection();
    }

    public void AddQuestion(YesNoQuestion yesNoQuestion) 
    {
        try 
        {
            PreparedStatement pStatement;
            pStatement = connection.prepareStatement("insert into YES_NO_QUESTION (QUESTION, ANSWER, CATEGORY, LEVEL)"
                    + " values (?, ?, ?, ? )");
            pStatement.setString(1, yesNoQuestion.GetQuestion());
            pStatement.setBoolean(2, yesNoQuestion.GetAnswer());
            pStatement.setString(3, yesNoQuestion.GetCategory().name());
            pStatement.setString(4, yesNoQuestion.GetLevel().name());

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
            pStatement = connection.prepareStatement("delete from YES_NO_QUESTION where code=?");
            pStatement.setInt(1, code);
            pStatement.executeUpdate();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }

    public List<YesNoQuestion> getAllQuestion() 
    {
        List<YesNoQuestion> yesNoQuestions = new ArrayList<YesNoQuestion>();

        try 
        {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from YES_NO_QUESTION");

            while (rs.next()) 
            {
                YesNoQuestion yesNoQuestion = SetQuestion(rs);
                yesNoQuestions.add(yesNoQuestion);
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return yesNoQuestions;
    }

    private YesNoQuestion SetQuestion(ResultSet rs) throws SQLException {
        YesNoQuestion yesNoQuestion = new YesNoQuestion();
        yesNoQuestion.SetCode(rs.getInt("code"));
        yesNoQuestion.SetQuestion(rs.getString("question"));
        yesNoQuestion.SetAnswer(rs.getBoolean("answer"));
        yesNoQuestion.SetCategory(Category.valueOf(rs.getString("category")));
        yesNoQuestion.SetLevel(Level.valueOf(rs.getString("level")));
        return yesNoQuestion;
    }

    public List<YesNoQuestion> GetQuestionsByCategoryAndLevel(HashMap<String,String> categoryLevel) 
    {
        List<YesNoQuestion> yesNoQuestions = new ArrayList<YesNoQuestion>();

        try 
        {
            PreparedStatement pStatement;

            pStatement = connection.prepareStatement("select * from YES_NO_QUESTION where category=? And level=");
            Object[] keys = categoryLevel.keySet().toArray();
            
            for (int i = 0; i < categoryLevel.size(); i++) 
            {
                pStatement.setString(1, keys[i].toString());
                pStatement.setString(2, categoryLevel.get(keys[i].toString()));
                ResultSet rs = pStatement.executeQuery();
                if (rs.next()) 
                {
                    YesNoQuestion yesNoQuestion = SetQuestion(rs);
                    yesNoQuestions.add(yesNoQuestion);
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return yesNoQuestions;

    }
    
}
