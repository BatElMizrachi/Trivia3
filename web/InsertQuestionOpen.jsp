
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
                var y = document.forms["showViewQuestionToAdd"]["openAnswer"].value;
                if (x == null || x == "") {
                    alert("Question field must be filled out");
                    return false;
                }
                if (y == null || y == "") {
                    alert("Answer field must be filled out");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <jsp:useBean id="openToAdd" type="Model.OpenQuestion" scope="request" />
        <form name="showViewQuestionToAdd" Action="AddQuestion" onsubmit="return(validateForm());">
            <input type="hidden" name="Level" value=<jsp:getProperty name="openToAdd" property="level" />>
            <input type="hidden" name="Category" value=<jsp:getProperty name="openToAdd" property="category" />>
            <h1>Insert question:</h1>
            <input type="text" name="question" width="400" height="50">
            <br>
            <h1>Insert answer:</h1>
            <input type="text" name="openAnswer" class="Answer">
            <input type="hidden" name="forSave" value="yes">
            <br>
            <button type="submit" class="btn btn-default btn-save" value="Save">Save</button>

        </form>
    </body>
</html>
