<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Ошибка</title>
</head>
<body>
<jsp:useBean id="newUser" type="java.lang.Boolean" scope="application"/>
<c:if test="${!newUser}" var="val" scope="request">
    <b>Вход не выполнен!!!!</b><br>
</c:if>
<c:if test="${newUser}" var="val" scope="request">
    <b>Регистрация не выполнена!!!!</b><br>
</c:if>

<jsp:useBean id="error" type="String" scope="application"/>
<br>Что не так: <%= error%><br>
</body>
</html>