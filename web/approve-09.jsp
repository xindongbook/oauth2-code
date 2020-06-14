
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Oauth Test</title>
</head>
<body>

<form action="/OauthServlet-ch09" method="post">
    <input type="hidden" name="reqid" value="<%=request.getAttribute("reqid")%>" />
    <input type="hidden" name="response_type" value="<%=request.getAttribute("response_type")%>" />
    <input type="hidden" name="redirect_uri" value="<%=request.getAttribute("redirect_uri")%>" />
    <input type="hidden" name="app_id" value="<%=request.getAttribute("app_id")%>" />

    <!--模拟 approve 动作-->
    <input type="hidden" name="reqType" value="approve" />

    Are you sure you want the authorization code？

    <br>
    appid: <%=request.getAttribute("app_id")%>

    <br>
    <input type="checkbox" value="today" name="rscope" checked/>today<br>
    <input type="checkbox" value="history" name="rscope"/>history<br>
    <%--<input type="checkbox" value="pic" name="rscope"/>pic<br>--%>

    <br>

    <input type="submit" value="approve"/> <input type="submit" value="refuse"/>
    <br>


</form>

</body>
</html>
