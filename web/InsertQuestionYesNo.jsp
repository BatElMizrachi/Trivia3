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
                if (x == null || x == "") {
                    alert("Question field must be filled out");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form name="showViewQuestionToAdd" Action="AddQuestion" onsubmit="return(validateForm());">
            <input type="hidden" name="Level" value=<%= request.getParameter("Level")%>>
            <input type="hidden" name="Category" value= <%= request.getParameter("Category")%>>
            <h1>Insert question:</h1>
            <input type="text" name="question" width="400" height="50">
            <br>
            <h1>Select answer:</h1>
            <div class="yesNoAnswer">
                <input type="radio" name="yesNoAnswer" value="Yes" checked>Yes
                <input type="radio" name="yesNoAnswer" value="No">No
                <input type="hidden" name="forSave" value="yes">
            </div>
            <br>
            <button type="submit" class="btn btn-default btn-save" value="Save">Save</button>
        </form>
    </body>
</html>