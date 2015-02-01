<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Servlet ServletLogin</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="Style/appliction.css" rel="stylesheet" type="text/css"/>
        <link href="Style/signin.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
    <left> Welcome, <%= session.getValue("user").toString() %> </left>
</html>
