package Controller; 

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Model.*;
import javax.servlet.RequestDispatcher;

@WebServlet(urlPatterns = {"/StartGame"})
public class StartGame extends HttpServlet 
{
    private Manager manager;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException 
    {
        HttpSession session = request.getSession(true);
        manager = (Manager) session.getAttribute ("manager");
        if (manager==null) 
        {
            manager=new Manager();
            session.setAttribute ("manager", manager);
        }
        
        if(request.getParameter("endOrNot") != null && request.getParameter("endOrNot").equals("Continue")) // ask question
        {
            AskQuestionForm(request,response, session);
        }
        else if(request.getParameter("Check") != null) // Check if correct and ask if wants more
        {
            int index = (int)session.getAttribute("NumofQuestions");
            QuestionBase currentQuestion = ((Manager)session.getAttribute("manager")).getQuestionByIndex(index);
            CheckAnswer(request, currentQuestion, response);
        }
        else if(request.getParameter("endOrNot") != null && request.getParameter("endOrNot").equals("End game")) // show points
        {
            int numberOfQuestion = (int)session.getAttribute("NumofQuestions");
            int numberOfCorrectAnswers = (int)session.getAttribute("CorrectAnswers");
            int score = numberOfCorrectAnswers * 100 / numberOfQuestion;
            session.setAttribute("score", score);
            
            ShowScoreView(request,response,score);
        }
        else // calc questions
        {
            HashMap<String,String> CategoriesLevel = GetCategoriesLevelByUserChoose(request,response);
            ((Manager)session.getAttribute("manager")).CalculateQuestionList(CategoriesLevel, request.getRealPath("/"));
            
            session.setAttribute("CorrectAnswers", 0); 
            session.setAttribute("NumofQuestions", 0); 
            session.setAttribute("FoodCount", 0);
            session.setAttribute("HistoryCount", 0);
            session.setAttribute("SportCount", 0);
            session.setAttribute("OtherCount", 0);
            
            if(((Manager)session.getAttribute("manager")).IsQuestionIsEmpty())
            {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/NoQuestion.html");
                dispatcher.include(request, response);
            }
            else
            {
                AskQuestionForm(request,response, session);
            }
        }
    }
    
    private void CheckAnswer(HttpServletRequest request, QuestionBase currentQuestion, HttpServletResponse response)
            throws NumberFormatException, IOException, ServletException 
    {
        boolean isCorrect = false;
        
        if(currentQuestion.getQuestionType().equals(QuestionType.Open))
        {
            if(((OpenQuestion)currentQuestion).getAnswer().equals(request.getParameter("openAnswer")))
            {
                isCorrect = true;
            }
        }
        else if(currentQuestion.getQuestionType().equals(QuestionType.YesNo))
        {
            if((((YesNoQuestion)currentQuestion).getAnswer() && request.getParameter("yesNoAnswer").equals("Yes")) ||
                    (!((YesNoQuestion)currentQuestion).getAnswer() && request.getParameter("yesNoAnswer").equals("No")))
            {
                isCorrect = true;
            }
        }
        else if (currentQuestion.getQuestionType().equals(QuestionType.MultiplePossible))
        {
            if(((MultiplePossibleQuestion)currentQuestion).getAnswer() ==
                    Integer.parseInt(request.getParameter("answerNumber")))
            {
                isCorrect = true;
            }
        }
        HttpSession session = request.getSession(true);
        int index = (int)session.getAttribute("NumofQuestions");
        int correctAnswers = (int)session.getAttribute("CorrectAnswers");
        
        session.setAttribute("NumofQuestions", index+1);
        if(isCorrect)
        {
            session.setAttribute("CorrectAnswers", correctAnswers+1);
        }
        
        CheckView(request,response, index+1 < ((Manager)session.getAttribute("manager")).QuestionSize(), isCorrect);
        
    }

    private void CheckView(HttpServletRequest request,HttpServletResponse response, boolean lastNotQuestion, boolean isCorrect)
            throws IOException, ServletException 
    {
        String link = "";

        if (isCorrect) 
        {
            if (lastNotQuestion) 
            {
                link = "/CheckViewCorrect.html";
            } 
            else 
            {
                link = "/CheckViewCorrectLast.html";
            }
        } 
        else 
        {
            if (lastNotQuestion)
            {
                link = "/CheckViewNotCorrect.html";
            } 
            else 
            {
                link = "/CheckViewNotCorrectLast.html";
            }
        }
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(link);
        dispatcher.include(request, response);
    }

    private void ShowScoreView(HttpServletRequest request,HttpServletResponse response, int score) 
            throws IOException, ServletException 
    {
        String link = "";
        if(score <= 60)
        {
            link = "/ShowScoreBad.jsp";
        }
        else if (score < 90)
        {
            link = "/ShowScoreGood.jsp";
        }
        else
        {
            link = "/ShowScoreExcellent.jsp";
        }
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(link);
        dispatcher.include(request, response);
    }

    private void AskQuestionForm(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws ServletException, IOException 
    {
        int index = (int)session.getAttribute("NumofQuestions");
        QuestionBase currentQuestion = ((Manager)session.getAttribute("manager")).getQuestionByIndex(index);
        String link = "";

        UpdateCategoryCount(session, currentQuestion);
        
        if(currentQuestion.getQuestionType().equals(QuestionType.Open))
        {
            request.setAttribute("openAsk", currentQuestion);
            link = "/AskOpenQuestion.jsp";    
        }
        else if(currentQuestion.getQuestionType().equals(QuestionType.YesNo))
        {
           request.setAttribute("YesNoAsk", currentQuestion);
           link = "/AskYesNoQuestion.jsp";  
        }
        else if (currentQuestion.getQuestionType().equals(QuestionType.MultiplePossible))
        {
            request.setAttribute("MultipleAsk", currentQuestion);
            link = "/AskMultipleQuestion.jsp";
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(link);
        dispatcher.forward(request, response);
    }

    private void UpdateCategoryCount(HttpSession session, QuestionBase question)
    {
        if(question.getCategory() == Category.Food)
        {
            int count = (int)session.getAttribute("FoodCount");
            session.setAttribute("FoodCount", count+1);
        }
        else if(question.getCategory() == Category.History)
        {
            int count = (int)session.getAttribute("HistoryCount");
            session.setAttribute("HistoryCount", count+1);
        }
        else if(question.getCategory() == Category.Sport)
        {
            int count = (int)session.getAttribute("SportCount");
            session.setAttribute("SportCount", count+1);
        }
        else if(question.getCategory() == Category.Other)
        {
            int count = (int)session.getAttribute("OtherCount");
            session.setAttribute("OtherCount", count+1);
        }
    }
    
    protected HashMap<String,String> GetCategoriesLevelByUserChoose(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        HashMap<String,String> categoryLevelUser = new HashMap<String,String>();
        String[] category = request.getParameterValues("Category");
        String categoryLevel;
        
        for (int i = 0; i < category.length; i++) 
        {
            categoryLevel = "Level" + category[i];
            categoryLevelUser.put(category[i], request.getParameter(categoryLevel)) ;
        }
        
        return categoryLevelUser;
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
