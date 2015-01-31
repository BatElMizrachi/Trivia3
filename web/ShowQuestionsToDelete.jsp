
<%@page import="Model.QuestionBase"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href=\"Style/delete.css\" rel=\"stylesheet\" type=\"text/css\"/>
        <link href=\"Style/appliction.css\" rel=\"stylesheet\" type=\"text/css\"/>
        <title>JSP Page</title>
        
    </head>
    <body>
        <div>List of question</div>
        
        <jsp:useBean id="allQuestions" type="java.util.ArrayList<Model.QuestionBase>" scope="request" />
            <%!int index = 1; %>
            
            <%if(allQuestions.size() > 0)
            {
             for (QuestionBase question : allQuestions) 
                {
                    %> 
                    <tr>    
                        <td> <%= question.getQuestion() %> </td>
                    
                        <td><a href="DeleteQuestion?action=delete&code=<%=question.getCode()%>
                               &type=<%=question.getCategory().name()%>">Delete</a></td>  
                    </tr> 
                   <% index++;
                }  
            }
                   else
            {%>
                <div class="alert alert-danger" role="alert">There are no questions</div>
            %<}%>
        
    </body>
</html>


              