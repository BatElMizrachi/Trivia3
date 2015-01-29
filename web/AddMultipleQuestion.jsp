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
            alert("Question field must be filled out");
                    return false;
            }
            if (y == null || y == "") {
            alert("Answer field must be filled out");
                    return false;
            }
            if (isNaN(parseFloat(y))) {
            alert("Answer field must be numeric");
                    return false;
            }
            }
        </script>
    </head>    
    <body>
        
<jsp:useBean id="multpleToAdd" type="Model.MultiplePossibleQuestion" scope="session" />
<jsp:getProperty name="multpleToAdd" property="Level" />
      
    </body>
</html>
