package Controller; 

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
            try (PrintWriter out = response.getWriter()) 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet StartGame</title>");    
                out.println("<link href=\"Style/appliction.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/Question.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("</head>");
                out.println("<body>");
                
         RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/testBean.jsp");
        dispatcher.forward(request, response);
                
                AskQuestionForm(out, session);
                out.println("</body>");
                out.println("</html>");
            }
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
            
            int index = 0;
            
            try (PrintWriter out = response.getWriter()) 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet StartGame</title>");   
                
                if(!((Manager)session.getAttribute("manager")).IsQuestionIsEmpty())
                {
                    QuestionBase currentQuestion = ((Manager)session.getAttribute("manager")).getQuestionByIndex(index);
                    if(currentQuestion.getQuestionType() == QuestionType.YesNo)
                    {
                        out.println("<script language=\"javascript\">");
                        out.println("function validateForm()\n");
                        if(currentQuestion.getQuestionType() == QuestionType.Open)
                        {
                            out.println("    var x = document.forms[\"AskForm\"][\"openAnswer\"].value;\n" +
                                        "    if (x==null || x==\"\") {\n" +
                                        "        alert(\"Answer field must be filled out\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "}\n");
                        }
                        else if (currentQuestion.getQuestionType() == QuestionType.MultiplePossible)
                        {
                            out.println("    var y = document.forms[\"AskForm\"][\"answerNumber\"].value;\n" +
                                        "    if (y==null || y==\"\") {\n" +
                                        "        alert(\"Answer field must be filled out\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "    if (isNaN(parseFloat(y))) {\n" +
                                        "        alert(\"Answer field must be numeric\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "    if (y < 1) {\n" +
                                        "        alert(\"Answer must be bigger then 0\");\n" +
                                        "        return false;\n" +
                                        "    }\n" +
                                        "}\n" );
                        }

                        out.println("</script>");
                    }
                }
                
                out.println("<link href=\"Style/appliction.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("<link href=\"Style/Question.css\" rel=\"stylesheet\" type=\"text/css\"/>");
                out.println("</head>");
                out.println("<body>");
                
                if(((Manager)session.getAttribute("manager")).IsQuestionIsEmpty())
                {
                    out.println("<form name=\"Failure\">");
                    out.println("<h1>There are no questions thet meet the conditions</h1>");
                    out.println("</form>");
                }
                else
                {
                    AskQuestionForm(out, session);
                }
                
                out.println("</body>");
                out.println("</html>");
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

    private void AskQuestionForm(final PrintWriter out, HttpSession session) 
    {
        int index = (int)session.getAttribute("NumofQuestions");
        QuestionBase currentQuestion = ((Manager)session.getAttribute("manager")).getQuestionByIndex(index);
        //RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/testBean.jsp");
        //dispatcher.forward(request, response);
        out.println("<form name=\"AskForm\">");
        out.println("<input type=\"hidden\" name=\"Check\" value=\"Yes\">");
        UpdateCategoryCount(session, currentQuestion);
        
        if(currentQuestion.getQuestionType().equals(QuestionType.Open))
        {
            ShowQuestion(out, currentQuestion);
            out.println("<h1>Your answer:</h1>");
            out.println("<input type=\"text\" name=\"openAnswer\" class=\"Answer\">");
            
        }
        else if(currentQuestion.getQuestionType().equals(QuestionType.YesNo))
        {
            ShowQuestion(out, currentQuestion);
            
            out.println("<h1>Your answer:</h1>");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" class=\"list-answers\" value=\"Yes\" checked>Yes");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" class=\"list-answers\" value=\"No\">No");  
        }
        else if (currentQuestion.getQuestionType().equals(QuestionType.MultiplePossible))
        {
            ShowQuestion(out, currentQuestion);
            out.println("   <ol>");
            
            Map<String, String> allAnswer = ((MultiplePossibleQuestion)currentQuestion).getAllAnswer();
            for (int i = 1; i <= allAnswer.size(); i++) {
                out.println("       <li>" + allAnswer.get(Integer.toString(i)) + "</li>");
            }
            
            out.println("   </ol>");
            
            out.println("<h1>Select answers number:</h1>");
            out.println("<input type=\"text\" name=\"answerNumber\">");
        }
        
        out.println("<br>");
        out.println("<input type=\"submit\" value=\"Send\" onsubmit=\"return validateForm()\" >");
        out.println("</form>");
    }

    private void ShowQuestion(final PrintWriter out, QuestionBase currentQuestion) {
        out.println("<h1>The question is:</h1>");
        out.println("<h2>"+ currentQuestion.getQuestion() +"</h2>");
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
