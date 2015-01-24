package Controller; 

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/Connect"})
public class Connect extends HttpServlet 
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String link = "";
        HttpSession session = request.getSession(true);
        
        if (request.getParameter("AfterFill") != null)
        {
            if( request.getParameter("remember-me") != null )
            {
                Cookie c = SaveToCookie(request,response);
                session.putValue("user", c.getValue());
                link = "/UserConnect.jsp";
            }
            else
            {
                String user = request.getParameter("First") + " " + request.getParameter("Last");
                session.putValue("user", user);
                link = "/UserConnect.jsp";
            }
        }
        else
        {
            String user = CheckRegisteredUser(request);
            if (user.equals("New user"))
            {
                link = "/SignIn.html";
            }
            else
            {
                session.putValue("user", user);
                link = "/UserConnect.jsp";
            }
        }
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(link);
        dispatcher.include(request, response);
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
            throws ServletException, IOException 
    {
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

    private String CheckRegisteredUser (HttpServletRequest request)
    {
        Cookie[] cookies = null;
        cookies = request.getCookies();
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals("user"))
                {
                    return cookie.getValue();
                }
            }
        }
        return "New user";
    }
    
    private Cookie SaveToCookie(HttpServletRequest request,HttpServletResponse response)
    {
        String cookieString = request.getParameter("First") + " " + request.getParameter("Last");
        Cookie cookie = new Cookie("user", cookieString);
        cookie.setMaxAge(15 * 365 * 24 * 60 * 60);
        response.addCookie(cookie);
        
        return cookie;
    }
}
