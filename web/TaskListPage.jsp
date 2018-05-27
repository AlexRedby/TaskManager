<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
%>

<link rel="table" type="text/css" href="st.css"/>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="table.css"/>
    <title>Ваши задачи</title>
    <script src="http://code.jquery.com/jquery-2.0.2.min.js">
    </script>
    <script>
        var activeElement;
        function focusMethod() {
            document.getElementsByName("del")[0].disabled = false;
            document.getElementsByName("edit")[0].disabled = false;
            activeElement = document.activeElement.getAttribute("id");
        }
        // TODO: Сделать, чтобы кнопки "Удалить" И "Изменить" были не активны, если не выделена ни одна задача
        // function blurMethod() {
        //     document.getElementsByName("del")[0].disabled = true;
        //     document.getElementsByName("edit")[0].disabled = true;
        // }

        function getFocusedTaskId() {
            document.getElementsByName("edit")[0].value = activeElement;
            document.getElementsByName("del")[0].value = activeElement;
        }

        function findNearestTask(){
            var taskMilliseconds = ${nearestTask.getDateTime().getTimeInMillis()};

            var ms = new Date();
            var currentMilliseconds = ms.getTime();

            var n = taskMilliseconds - currentMilliseconds;
            if(n > 0){
                setTimeout(popup,n);
            }
            else {
                popup();
            }
        }

        function popup(){
            $(".dark-back").addClass('active');
            $(".popup").addClass('active');
        }

        function begin(){
            //По клику на затемнённый фон, он и popup снова скрываются
            $(".dark-back").click(function () {
                $(".popup").removeClass('active');
                $(".dark-back").removeClass('active');
            });

            findNearestTask();
        }

    </script>
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

                <tr id = "${task.getId()}"  onfocus = "focusMethod()" tabindex=${task.getId()} >

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

        <form name="addTask" action="EditTask" class="inline" method="post">
            <button value="add" name="add">Добавить</button>
        </form>

        <form name="delTask" action="EditTask" class="inline">
            <button disabled name="del" onclick="getFocusedTaskId()">Удалить</button>
        </form>

        <form name="editTask" action="EditTask" class="inline">
            <button disabled name="edit" onclick="getFocusedTaskId()">Изменить</button><br>
        </form>

        <br>
        <form action="CloseClient" class="exit">
            <button value="exit" name="edit">Выйти</button>
        </form>


    </div>

    <!-- PopUp Window -->
    <div class = "popup">
        Я формочка, которая откладывает или завершает задачу - ${nearestTask}
    </div>

    <!-- Затемнённый фон -->
    <div class = "dark-back"></div>

    <!-- Запускаем скрипт -->
    <script> begin(); </script>

</body>
</html>
