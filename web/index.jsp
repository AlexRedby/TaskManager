<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="index.css"/>
    </head>

    <body>
    <c:if test="${newUser==false}" var="val" scope="request">
        <b>Вход не выполнен!!!!</b><br>
    </c:if>
    <c:if test="${newUser==true}" var="val" scope="request">
        <b>Регистрация не выполнена!!!!</b><br>
    </c:if>

    <p style="color: red;">${error}</p>

        <form name="loginform" action="CheckUser" method="post">
            <fieldset>
                <legend>Login page</legend>

                <label>Логин:</label>
                <input type="text" name="login" value="" size="20" /><br>

                <label>Пароль:</label>
                <input type="password" name="password" value="" size="20" /><br>
            </fieldset>
            <p><input type="submit" value="Войти" name="log_in" /></p>
            <p><input type="submit" value="Регистрация" name="register" /></p>
        </form>





    </body>
</html>