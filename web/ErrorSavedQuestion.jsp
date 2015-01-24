<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title></title>
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/Question.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <form name=\"Failure\">
            <nav class="headerContain">
                <h1 class="h1-m"><%= ((String)request.getAttribute("error"))%> </h1>
                <span><img src="Pic/wrong.jpg" alt="" class="alert_pic"></span>
            </nav>
            </form>
    </body>
</html>
              