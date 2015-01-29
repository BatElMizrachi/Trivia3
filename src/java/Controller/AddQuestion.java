package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Model.*;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/AddQuestion"})
public class AddQuestion extends HttpServlet {

    private String errorMessage;
    private QuestionBase addQuestion;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getParameter("forSave") != null) // view to save question 
        {
            PrintWriter out = response.getWriter();
            String saveQuestionResult = null;

            try {
                AddQuestionByType(request, out);
                saveQuestionResult = "/SavedQuestion.html";
            } catch (InvalidValueException ivEx) {
                request.setAttribute("error", errorMessage);
                saveQuestionResult = "/ErrorSavedQuestion.jsp";
            } catch (Exception ex) {
                request.setAttribute("error", "The question has not been saved");
                saveQuestionResult = "/ErrorSavedQuestion.jsp";
            } finally {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(saveQuestionResult);
                dispatcher.forward(request, response);
            }
        } 
        else if (request.getParameter("count") != null) // view - for multiple
        {
            HttpSession session = request.getSession();
            MultiplePossibleQuestion multpleToAdd = new MultiplePossibleQuestion();
            multpleToAdd.setLevel(Utils.getLevelByUserChoose((String) request.getParameter("Level")));
            multpleToAdd.setCategory(Utils.getCategoryByUserChoose((String) request.getParameter("Category")));
            session.setAttribute("multpleToAdd", multpleToAdd);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AddMultipleQuestion.jsp");
            dispatcher.forward(request, response);
        }
        else // view - to insert question 
        {

            // להכניס את השטות לBEAN
            Level level = Utils.getLevelByUserChoose((String) request.getParameter("Level"));
            Category category = Utils.getCategoryByUserChoose((String) request.getParameter("Category"));
            QuestionType questionType = Utils.getQuestionTypeByUserChoose((String) request.getParameter("QuestionType"));
            String insertQuestion = "";
            
            try 
            {
                if (questionType.equals(QuestionType.Open))
                {
                    insertQuestion = "/InsertQuestionOpen.jsp";
                } 
                else if (questionType.equals(QuestionType.YesNo))
                {
                    insertQuestion = "/InsertQuestionYesNo.jsp";
                } 
                else if (questionType.equals(QuestionType.MultiplePossible)) 
                {
                    insertQuestion = "/InsertQuestionMultiple.jsp";
                }
            } 
            finally 
            {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(insertQuestion);
                dispatcher.include(request, response);
            }
        }
    }

    private void AddQuestionByType(HttpServletRequest request, final PrintWriter out) throws Exception {

        ArrayList<QuestionBase> allQuestions = new ArrayList<QuestionBase>();
        allQuestions = FileHandler.ReadQuestions(request.getRealPath("/"));

        if (request.getParameter("openAnswer") != null)
        {
            OpenQuestion openQuestion = new OpenQuestion();
            openQuestion.setAnswer(request.getParameter("openAnswer"));
            openQuestion.setQuestion(request.getParameter("question"));
            openQuestion.setCategory(Utils.getCategoryByUserChoose(request.getParameter("Category")));
            openQuestion.setLevel(Utils.getLevelByUserChoose(request.getParameter("Level")));
            allQuestions.add(openQuestion);
            FileHandler.WriteQuestions(allQuestions, request.getRealPath("/"));
        } 
        else if (request.getParameter("yesNoAnswer") != null) 
        {
            YesNoQuestion yesNoQuestion = new YesNoQuestion();
            yesNoQuestion.setAnswer(request.getParameter("yesNoAnswer"));
            yesNoQuestion.setQuestion(request.getParameter("question"));
            yesNoQuestion.setCategory(Utils.getCategoryByUserChoose(request.getParameter("Category")));
            yesNoQuestion.setLevel(Utils.getLevelByUserChoose(request.getParameter("Level")));
            allQuestions.add(yesNoQuestion);
            FileHandler.WriteQuestions(allQuestions, request.getRealPath("/"));
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

            if (cantSave) {
                throw new InvalidValueException();
            } else {
                multiplePossibleQuestion.setCategory(Utils.getCategoryByUserChoose(request.getParameter("Category")));
                multiplePossibleQuestion.setLevel(Utils.getLevelByUserChoose(request.getParameter("Level")));
                allQuestions.add(multiplePossibleQuestion);
                FileHandler.WriteQuestions(allQuestions, request.getRealPath("/"));
            }
        } else {
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
