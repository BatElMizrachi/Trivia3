<%-- 
    Document   : AskMultipleQuestion
    Created on : Jan 30, 2015, 9:52:26 AM
    Author     : Bat-El
--%>

<%@page contentType="texthtml" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Servlet StartGame</title>    
        <link href="Styleappliction.css" rel="stylesheet" type="textcss">
        <link href="StyleQuestion.css" rel="stylesheet" type="textcss">
        <script>
            function validateForm()
            {
                var y = document.forms["AskForm"]["answerNumber"].value;
                if (y==null || y=="") 
                {
                   alert("Answer field must be filled out");
                   return false;
                }
                if (isNaN(parseFloat(y))) 
                {
                    alert("Answer field must be numeric");
                    return false;
                }
                if (y <1) 
                {
                    alert("Answer must be bigger then 0");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <jsp:useBean id="MultipleAsk" type="Model.MultiplePossibleQuestion" scope="request" />
        <form name = "AskForm">
            <input type = "hidden" name = "Check" value = "Yes">
            <h1> The question is: <h1>
            <h2><jsp:getProperty name="MultipleAsk" property="question" /></h2>
            <ol>
                AllAnswer
                <ol>
                Map <String, String> allAnswer = ((MultiplePossibleQuestion)currentQuestion).getAllAnswer();
                for (int i = 1; i <= allAnswer.size(); i++) {
            <li> " + allAnswer.get(Integer.toString(i)) + " <li>
            }

            </ol>
            <h1> Select answers number: <h1>
            <input type = "text" name = "answerNumber">
            <br>
            <input type = "submit" value = "Send" onsubmit = "return validateForm()">
        </form>
    </body>
</html>
