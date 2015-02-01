package Controller; 

import DB.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Model.*;
import javax.servlet.RequestDispatcher;

@WebServlet(urlPatterns = {"/DeleteQuestion"})
public class DeleteQuestion extends HttpServlet {

    
    private String link = "";
    private MultiplePossibleQuestionDB multiplePossibleQuestionDB;
    private OpenQuestionDB openQuestionDB;
    private YesNoQuestionDB yesNoQuestionDB;
    ArrayList<QuestionBase> allQuestions;
 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
 
        response.setContentType("text/html;charset=UTF-8");
        
        if(request.getParameter("action") != null && request.getParameter("action").equals("delete"))
        {
            try
            {
                if(request.getParameter("type").equals(QuestionType.MultiplePossible.name()))
                {
                    multiplePossibleQuestionDB.DeleteQuestion(Integer.parseInt(request.getParameter("code")));
                }
                else if(request.getParameter("type").equals(QuestionType.Open.name()))
                {
                    openQuestionDB.DeleteQuestion(Integer.parseInt(request.getParameter("code")));
                }
                else if(request.getParameter("type").equals(QuestionType.YesNo.name()))
                {
                    yesNoQuestionDB.DeleteQuestion(Integer.parseInt(request.getParameter("code")));
                }

                link = "/DeleteQuestionSucceeded.html";
            }
            catch (Exception ex)
            {
                link = "/DeleteQuestionFailed.html";
            }

        }
        else
        {
            allQuestions = new ArrayList<QuestionBase>();
            multiplePossibleQuestionDB = new MultiplePossibleQuestionDB();
            openQuestionDB = new OpenQuestionDB();
            yesNoQuestionDB = new YesNoQuestionDB();
            allQuestions.addAll(multiplePossibleQuestionDB.GetAllQuestion());
            allQuestions.addAll(openQuestionDB.GetAllQuestion());
            allQuestions.addAll(yesNoQuestionDB.getAllQuestion());

            request.setAttribute("allQuestions", allQuestions);
            link = "/ShowQuestionsToDelete.jsp";
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(link);
        dispatcher.forward(request, response);
        
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
        processRequest(request, response);
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
        processRequest(request, response);
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
