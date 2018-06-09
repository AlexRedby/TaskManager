<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link rel="stylesheet" type="text/css" href="index.css"/>

        <script src="http://code.jquery.com/jquery-2.0.2.min.js"></script>
        <script>
            function makeRegistrationPage() {
                $("#login").addClass('hidden');
                $("#registration").removeClass('hidden');
                $("#registrationLegend").removeClass('hidden');
                $("#loginLegend").addClass('hidden');
            }
            
            function makeLoginPage() {
                $("#registration").addClass('hidden');
                $("#login").removeClass('hidden');
                $("#registrationLegend").addClass('hidden');
                $("#loginLegend").removeClass('hidden');
            }
        </script>
    </head>

    <body>

    <div class align="center">
        <form name="loginform" action="CheckUser" method="post">
            <fieldset>
                <legend id="loginLegend">Login page</legend>
                <legend id="registrationLegend" class="hidden">Registration page</legend>

                <div align="left">
                    <label for="loginText">Логин:</label><br>
                        <input type="text" id="loginText" name="login" value="" size="20" required/><br>

                    <label for="passwordText">Пароль:</label><br>
                    <input type="password" id="passwordText" name="password" value="" size="20" required/><br>
                </div>
            </fieldset>

            <div id="login">
                <p><input type="submit" value="Войти" name="log_in" /></p>
                <p><input type="button" value="Регистрация" onclick="makeRegistrationPage()" /></p>
            </div>

            <div id="registration" class = "hidden">
                <p><input type="submit" value="Создать аккаунт" name="register" /></p>
                <p><input type="button" value="Авторизация" onclick="makeLoginPage()" /></p>
            </div>
        </form>

        <%-- Проверка на ошибку --%>
        <c:if test="${newUser==false}" var="val" scope="request">
            <b>Вход не выполнен!!!</b><br>
            <%request.removeAttribute("newUser");%>
        </c:if>
        <c:if test="${newUser==true}" var="val" scope="request">
            <b>Регистрация не выполнена!!!</b><br>
            <%request.removeAttribute("newUser");%>
        </c:if>

        <p style="color: red;">${error}</p>
        <%request.removeAttribute("error");%>
    </div>

    </body>
</html>