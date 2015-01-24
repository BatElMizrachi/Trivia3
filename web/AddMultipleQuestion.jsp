<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <title></title>  
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/Question.css" rel="stylesheet" type="text/css"/>

        <script>
            function validateForm()
            {
            var x = document.forms["showViewQuestionToAdd"]["question"].value;
                    var y = document.forms["showViewQuestionToAdd"]["numberOfAnswer"].value;
                    if (x == null || x == "") {
            alert("Question field must be filled out
                    return false;
            }
            if (y == null || y == "") {
            alert("Answer field must be filled out
                    return false;
            }
            if (isNaN(parseFloat(y))) {
            alert("Answer field must be numeric
                    return false;
            }
            }
        </script>
    </head>    
    <body>
        <jsp:useBean id="u" class="model.MultiplePossibleQuestion" scope="request"/>
        Record:<br>
        <jsp:getProperty property="name" name="u"/><br>
        <jsp:getProperty property="password" name="u"/>
        <form name="showViewQuestionToAdd" Action="AddQuestion" onsubmit="return(validateForm());">
            <input type="hidden" name="Level" value=<%= (String)request.getParameter("Level")%>>
            <input type="hidden" name="Category" value= <%= (String)request.getParameter("Category")%>>
            <input type="hidden" name="count" value= <%= (String)request.getParameter("count")%>>
            <h1>Insert question:</h1>
            <input type="text" name="question" value=<%= (String)request.getParameter(("question")%> width="400" height="50">
            <br>
            <div class="possible-answers">
                <h1>Insert possible answers:</h1>
                <ol>
                    <%
                    for (int i = 1; i <= Integer.parseInt(request.getParameter("count")); i++) {
                        %>
                    <li class="list-answers">
                        <input type="text" name=<%= i %> width="400" height="50">
                    </li>
                    <% } %>

                </ol>
            </div>

            <input type="hidden" name="forSave" value="yes">
            <h1>Select answers number:</h1>
            <input type="text" name="numberOfAnswer">
        </form>
    </body>
</html>
