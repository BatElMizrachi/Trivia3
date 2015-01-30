<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/Question.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <h2>hey, <%= request.getSession().getAttribute("user").toString()%>.</h2>
        <h3>You asked by categories</h3>
        <h3>Food: <%= request.getSession().getAttribute("FoodCount").toString()%> </h3>
        <h3>History:  <%= request.getSession().getAttribute("HistoryCount").toString()%></h3>
        <h3>Sport: <%= request.getSession().getAttribute("SportCount").toString()%></h3>
        <h3>Other: <%= request.getSession().getAttribute("OtherCount").toString()%></h3>
        <form name="scoreBad">
            <h1>Your score is: <%= request.getSession().getAttribute("score").toString()%></h1>
            <h1>Maybe next time you will have better luck</h1>
        </form>
    </body>
</html>



