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
            <jsp:useBean id="tasks" type="java.util.List<src.common.controller.TaskList>" scope="application"/>

            <c:forEach items="${tasks}" var="task">
                <tr id = "tr"  onfocus = "focusMethod()" onblur="blurMethod()" tabindex=${task.getId()} >

                <c:if test="${task.isActive()==false}" var="val" scope="request">
                    <script>
                        tr.classList.add("notActive")
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
        <button disabled value="del" name="del">Удалить</button>
        <button disabled value="edit" name="edit">Изменить</button><br>

        <script>
            function focusMethod() {
                document.getElementsByName("del")[0].disabled = false;
                document.getElementsByName("edit")[0].disabled = false;
            }
            function blurMethod() {
                document.getElementsByName("del")[0].disabled = true;
                document.getElementsByName("edit")[0].disabled = true;
            }
        </script>
        <br>
        <form action="CloseClient" class="exit">
            <button value="exit" name="edit">Выйти</button>
        </form>


    </div>
</body>
</html>
