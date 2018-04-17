<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="table" type="text/css" href="st.css"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="table.css"/>
    <title>Ваши задачи</title>
</head>

<body>
    <table>
        <tr class="hat">
            <td>ID</td>
            <td>Название</td>
            <td>Информация</td>
            <td>Контакты</td>
            <td>Дата и время</td>
        </tr>
        <jsp:useBean id="tasks" type="java.util.List<src.common.controller.TaskList>" scope="application"/>

        <c:forEach items="${tasks}" var="task">
        <tr>
            <td>${task.getId()}</td>
            <td>${task.getName()}</td>
            <td>${task.getInfo()}</td>
            <td>${task.getContacts()}</td>
            <td>${task.getFormattedDateTime()}</td>
        </tr>
        </c:forEach>

    </table>
    <br>

    <form name="getTasks" action="GetTasks">
        <button>Показать все</button>
        <button value="a" name="active" >Показать активные</button>
        <button value="na" name="active" >Показать не активные</button>
    </form>
    <br>
    <input type="submit" value="Добавить" name="add"/>
    <input type="submit" value="Удалить" name="del"/>
    <input type="submit" value="Изменить" name="edit"/>
</form>
</body>
</html>
