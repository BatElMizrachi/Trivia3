package Controller;

import Utils.Utils;
import DB.MultiplePossibleQuestionDB;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Model.*;
import DB.*;

@WebServlet(urlPatterns = {"/AddQuestion"})
public class AddQuestion extends HttpServlet {

    private String errorMessage;
    private QuestionBase addQuestion;
    private String link = "";
    private MultiplePossibleQuestionDB multiplePossibleQuestionDB;
    private OpenQuestionDB openQuestionDB;
    private YesNoQuestionDB yesNoQuestionDB;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try
        {
            if (request.getParameter("forSave") != null) // view to save question 
            {
                AddQuestionByType(request);
                link = "/SavedQuestion.html";
            } 
            else if (request.getParameter("count") != null) // view - for multiple
            {
                addQuestion = new MultiplePossibleQuestion();
                addQuestion.setQuestion(request.getParameter("question"));
                SetBasicFields(request);
                request.setAttribute("multpleToAdd", addQuestion);
                link = "/AddMultipleQuestion.jsp";
            }
            else // view - to insert question 
            {
                QuestionType questionType = Utils.getQuestionTypeByUserChoose((String) request.getParameter("QuestionType"));

                if (questionType.equals(QuestionType.Open))
                {
                    openQuestionDB  = new OpenQuestionDB();
                    addQuestion = new OpenQuestion();
                    request.setAttribute("openToAdd", addQuestion);
                    link = "/InsertQuestionOpen.jsp";
                } 
                else if (questionType.equals(QuestionType.YesNo))
                {
                    yesNoQuestionDB = new YesNoQuestionDB();
                    addQuestion = new YesNoQuestion();
                    request.setAttribute("yesNoToAdd", addQuestion);
                    link = "/InsertQuestionYesNo.jsp";
                } 
                else if (questionType.equals(QuestionType.MultiplePossible)) 
                {
                    multiplePossibleQuestionDB = new MultiplePossibleQuestionDB();
                    addQuestion = new MultiplePossibleQuestion();
                    request.setAttribute("multpleToAdd", addQuestion);
                    link = "/InsertQuestionMultiple.jsp";
                }
                
                SetBasicFields(request);
            }
        }
        catch (InvalidValueException ivEx) 
        {
            request.setAttribute("error", errorMessage);
            link = "/ErrorSavedQuestion.jsp";
        } 
        catch (Exception ex) 
        {
            request.setAttribute("error", "The question has not been saved");
            link = "/ErrorSavedQuestion.jsp";
        }
        finally
        {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(link);
            dispatcher.forward(request, response);
        }
    }

    private void SetBasicFields(HttpServletRequest request) {
        addQuestion.setLevel(Utils.getLevelByUserChoose((String) request.getParameter("Level")));
        addQuestion.setCategory(Utils.getCategoryByUserChoose((String) request.getParameter("Category")));
    }

    private void AddQuestionByType(HttpServletRequest request) throws Exception 
    {
        if (request.getParameter("openAnswer") != null)
        {
            OpenQuestion openQuestion = new OpenQuestion();
            openQuestion.setAnswer(request.getParameter("openAnswer"));
            openQuestion.setQuestion(request.getParameter("question"));
            openQuestion.setCategory(Utils.getCategoryByUserChoose(request.getParameter("Category")));
            openQuestion.setLevel(Utils.getLevelByUserChoose(request.getParameter("Level")));
            
            openQuestionDB.AddQuestion(openQuestion);
        } 
        else if (request.getParameter("yesNoAnswer") != null) 
        {
            YesNoQuestion yesNoQuestion = new YesNoQuestion();
            yesNoQuestion.setAnswer(request.getParameter("yesNoAnswer"));
            yesNoQuestion.setQuestion(request.getParameter("question"));
            yesNoQuestion.setCategory(Utils.getCategoryByUserChoose(request.getParameter("Category")));
            yesNoQuestion.setLevel(Utils.getLevelByUserChoose(request.getParameter("Level")));
            
            yesNoQuestionDB.AddQuestion(yesNoQuestion);
        }
        else if (request.getParameter("numberOfAnswer") != null) 
        {
            boolean cantSave = false;
            errorMessage = "The question will not save: <br>";

            MultiplePossibleQuestion multiplePossibleQuestion = new MultiplePossibleQuestion();
            multiplePossibleQuestion.setAnswer(Integer.parseInt(request.getParameter("numberOfAnswer")));
            multiplePossibleQuestion.setQuestion(request.getParameter("question"));
            int numberOfPossibleAnswer = 0;

            for (int i = 1; i <= Integer.parseInt(request.getParameter("count")); i++) {
                if (request.getParameter(Integer.toString(i)) != null
                        && request.getParameter(Integer.toString(i)).isEmpty()) {
                    cantSave = true;
                    errorMessage += "There are empty possible answers.<br>";
                    break;
                } else {
                    multiplePossibleQuestion.AddToAllAnswer(Integer.toString(i),
                            request.getParameter(Integer.toString(i)));
                    numberOfPossibleAnswer++;
                }
            }

            if (Integer.parseInt(request.getParameter("numberOfAnswer")) <= 0
                    || Integer.parseInt(request.getParameter("numberOfAnswer")) > numberOfPossibleAnswer) {
                cantSave = true;
                errorMessage += "The correct answer number is not valid - must be between 1 to " + numberOfPossibleAnswer + ".";
            }

            if (cantSave) 
            {
                throw new InvalidValueException();
            } 
            else 
            {
                multiplePossibleQuestion.setCategory(Utils.getCategoryByUserChoose(request.getParameter("Category")));
                multiplePossibleQuestion.setLevel(Utils.getLevelByUserChoose(request.getParameter("Level")));
                multiplePossibleQuestionDB.AddQuestion(multiplePossibleQuestion);
            }
        } 
        else 
        {
            throw new Exception();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AddQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AddQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
