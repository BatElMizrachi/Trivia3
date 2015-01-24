<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/Question.css" rel="stylesheet" type="text/css"/>
        <script>
            function validateForm() {
                var x = document.forms["showViewQuestionToAdd"]["question"].value;
                var y = document.forms["showViewQuestionToAdd"]["count"].value;
                if (x == null || x == "") {
                    alert("Question field must be filled out");
                    return false;
                }
                if (y == null || y == "") {
                    alert("Possible answer count field must be filled out");
                    return false;
                }
                if (isNaN(parseFloat(y))) {
                    alert("Possible answer count field must be numeric");
                    return false;
                }
                if (parseInt(y) < 2 || parseInt(y) > 20) {
                    alert("Possible answer count must be bigger the 1 and smaller than 20");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form name="showViewQuestionToAdd" Action="AddQuestion" onsubmit="return(validateForm());">
            <h1>Insert question:</h1>
            <input type="text" name="question" width="400" height="50">
            <br>
            <h1>Insert count of possible answers:</h1>
            <input type="text" name="count" class="count">      
            <button type="submit" class="btn btn-default btn-continue" value="Continue">Continue</button>
            <input type="hidden" name="Level" value=<%= request.getParameter("Level")%>>
            <input type="hidden" name="Category" value=<%= request.getParameter("Category")%>>
        </form>
    </body>
</html>