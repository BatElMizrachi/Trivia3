<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Servlet StartGame</title>    
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/Question.css" rel="stylesheet" type="text/css"/>
        <script language="javascript">
            function validateForm()
            {
                var x = document.forms["AskForm"]["openAnswer"].value;
                if (x==null || x=="") 
                {
                    alert("Answer field must be filled out");
                    return false;
                }
            }
        </script>
    </head>
    <body>

        <jsp:useBean id="openAsk" type="Model.OpenQuestion" scope="request" />
        <form name="AskForm">
            <input type="hidden" name="Check" value="Yes">

            <h1>The question is:</h1>
            <h2><jsp:getProperty name="openAsk" property="question" /></h2>
            <h1>Your answer:</h1>
            <input type="text" name="openAnswer" class="Answer">
            <br>
            <button type="submit" class="btn btn-default btn-save" value="Send" onclick="validateForm()">Send</button>
        </form>           
    </body>
</html>