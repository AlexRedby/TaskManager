<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="table" type="text/css" href="st.css"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="table.css"/>
    <title>Ваши задачи</title>
</head>

<body>
    <div class="center">
        <table>
            <tr class= "title">
                <td>ID</td>
                <td>Название</td>
                <td>Информация</td>
                <td>Контакты</td>
                <td>Дата и время</td>
            </tr>
            <c:forEach items="${tasks}" var="task">

                <tr id = "${task.getId()}"  onfocus = "focusMethod()" onblur="setTimeout(blurMethod, 100)" tabindex=${task.getId()} >

                <c:if test="${!task.isActive()}" var="val" scope="request">
                    <script>
                        document.getElementById(${task.getId()}).classList.add("notActive");
                    </script>
                </c:if>
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

        <form name="addTask" action="AddTaskPage.jsp" target="_blank" class="inline">
            <button value="add" name="add">Добавить</button>
        </form>

        <form name="delTask" action="GetTasks" class="inline">
            <button disabled name="del" onclick="getFocusedTaskId()">Удалить</button>
        </form>
        <form name="editTask" action="GetTasks" class="inline">
            <button disabled name="edit">Изменить</button><br>
        </form>
        <script>
            var activeElement;
            function focusMethod() {
                document.getElementsByName("del")[0].disabled = false;
                document.getElementsByName("edit")[0].disabled = false;
                activeElement = document.activeElement.getAttribute("id");
            }
            function blurMethod() {
                document.getElementsByName("del")[0].disabled = true;
                document.getElementsByName("edit")[0].disabled = true;
                activeElement = "";
            }

            function getFocusedTaskId() {
                document.getElementsByName("edit")[0].value = activeElement;
                document.getElementsByName("del")[0].value = activeElement;
            }
        </script>
        <br>
        <form action="CloseClient" class="exit">
            <button value="exit" name="edit">Выйти</button>
        </form>


    </div>
</body>
</html>
