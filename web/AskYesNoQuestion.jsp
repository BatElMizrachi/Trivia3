<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Servlet StartGame</title>    
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/Question.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:useBean id="YesNoAsk" type="Model.YesNoQuestion" scope="request" />
        <form name="AskForm">
            <input type="hidden" name="Check" value="Yes">
            <h1>The question is:</h1>
            <h2><jsp:getProperty name="YesNoAsk" property="question" /></h2>
            <h1>Your answer:</h1>
            <input type="radio" name="yesNoAnswer" class="list-answers" value="Yes" checked>Yes
            <input type="radio" name="yesNoAnswer" class="list-answers" value="No">No  
            <br>
            <input type="submit" value="Send">
        </form>
    </body>
</html>