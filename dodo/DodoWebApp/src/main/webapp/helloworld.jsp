<%@ page import="service.base.Connection" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello World</title>
</head>
<body>
<h1>hahaha</h1>

<%
    String greeting = "hello";
    Connection conn = (Connection) session.getAttribute("connection");
    String userFirstName = conn.getUser().getFirstName();

//    String user_firstname = (String) session.getAttribute("user_firstname");
//    boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
//    boolean isVIP = (Boolean) session.getAttribute("isVIP");

    if (!userFirstName.equals(""))
        greeting = greeting + ", " + userFirstName;
%>
<b><%=greeting%></b>
<br>
details:<br>
<%=conn.toString()%>
<br>
<a href="<s:url action="Logout"/> ">logout</a>
</body>
</html>
