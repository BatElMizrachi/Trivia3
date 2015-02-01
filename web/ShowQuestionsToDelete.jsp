<%@page import="Model.QuestionBase"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="texthtml" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="Style/delete.css" rel="stylesheet" type="text/css"/>
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/Question.css" rel="stylesheet" type="text/css"/>
        <title>JSP Page</title>  
    </head>
    <body>   
        <div>List of question</div>
        <jsp:useBean id="allQuestions" type="java.util.ArrayList<Model.QuestionBase>" scope="request" />
        <%!int index = 1;%>
        <%
            if (allQuestions.size() > 0) 
            {
                for (QuestionBase question : allQuestions)
                {
                    %>
                     <nav class="headerContain">
                        <input type="hidden" name="code" value="<%=question.getCode()%>">
                        <input type="hidden" name="Type" value="<%=question.getQuestionType().name()%>">
                        <span> <%= question.getQuestion() %> </span>
                        <span> <a href="DeleteQuestion?action=delete&code=<%=question.getCode()%>&type=<%=question.getQuestionType().name()%>">Delete</a></span>
                        <br>
                    </nav>
                <%}
            } 
            else 
            { 
        %>
                <div class="alert alert-danger" role="alert">There are no questions</div>
        <%
            }
        %>
    </body>
</html>



