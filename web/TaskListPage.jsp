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
    <script src="http://code.jquery.com/jquery-2.0.2.min.js"></script>

    <script>
        function makeCountdown(){
            var taskMilliseconds = ${nearestTask.getDateTime().getTimeInMillis()};
            var currentMilliseconds = new Date().getTime();

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
            makeCountdown();
        }
    </script>
</head>

<body>
    <div class="center">
        <p style="color: red;">${edit_error}</p>
        <%request.removeAttribute("edit_error");%>

        <table>
            <tr class= "title main">
                <td class="main"> </td>
                <td class="main">Название</td>
                <td class="main">Информация</td>
                <td class="main">Контакты</td>
                <td class="main">Дата и время</td>
            </tr>
            <c:forEach items="${tasks}" var="task">

                <tr class="main" id = "${task.getId()}">

                <c:if test="${!task.isActive()}" var="val" scope="request">
                    <script>
                        document.getElementById(${task.getId()}).classList.add("notActive");
                    </script>
                </c:if>

                <td class="main"> <input type="checkbox" id=${task.getId()} name="checkbox"></td>
                <td class="main">${task.getName()}</td>
                <td class="main">${task.getInfo()}</td>
                <td class="main">${task.getContacts()}</td>
                <td class="main">${task.getFormattedDateTime()}</td>

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
            <button disabled name="del">Удалить</button>
        </form>

        <form name="editTask" action="EditTask" class="inline">
            <button disabled name="edit">Изменить</button><br>
        </form>

        <br>
        <form action="CloseClient" class="exit">
            <button value="exit" name="edit">Выйти</button>
        </form>

    </div>

    <script>
        var allCheckboxes = document.querySelectorAll('input[type="checkbox"]');

        for(var i = 0; i < allCheckboxes.length; i++) {
            allCheckboxes[i].addEventListener('click', updateDisplay);
        }

        function updateDisplay() {
            var activeElements = [];
            var checkedCount;
            $( 'input[name="checkbox"]:checked' ).each(function() {activeElements.push($(this).attr("id"))});
            checkedCount = activeElements.length;
            document.getElementsByName("del")[0].value = activeElements;
            document.getElementsByName("edit")[0].value = activeElements;

            if(checkedCount === 1) {
                document.getElementsByName("del")[0].disabled = false;
                document.getElementsByName("edit")[0].disabled = false;
            } else if(checkedCount > 1) {
                document.getElementsByName("del")[0].disabled = false;
                document.getElementsByName("edit")[0].disabled = true;
            } else {
                document.getElementsByName("del")[0].disabled = true;
                document.getElementsByName("edit")[0].disabled = true;
            }
        }
    </script>

    <!-- PopUp Window -->
    <div class = "popup">
        <form action="EditTask">
        <div align="center"><h2>Задача наступила!</h2></div>
        <b>${nearestTask.getName()}</b><br><br>
        ${nearestTask.getInfo()}<br>
        ${nearestTask.getFormattedDateTime()}<br><br>
        <fieldset>
            <legend>Отложить на:</legend>

            <table>
                <tr><td>
                    <input type="radio" id="5Minute" name="postponeValue" value="5" checked/>
                    <label for="5Minute">5 Минут</label>
                </td><td>
                    <input type="radio" id="10Minute" name="postponeValue" value="10"/>
                    <label for="10Minute">10 Минут</label>
                </td></tr>
                <tr><td>
                    <input type="radio" id="30Minute" name="postponeValue" value="30"/>
                    <label for="30Minute">30 Минут</label>
                </td><td>
                    <input type="radio" id="1Hour" name="postponeValue" value="60"/>
                    <label for="1Hour">Час</label>
                </td></tr>
                <tr><td>
                    <input type="radio" id="1Day" name="postponeValue" value="1440"/>
                    <label for="1Day">День</label>
                </td></tr>
            </table>
            <br>
            <div align="center">
                <button value="postpone" name="postpone">Отложить</button>
            </div>
        </fieldset>
        <br>
        <div align="center">
            <button value="complete" name="complete">Завершить</button>
        </div>
        </form>
    </div>

    <!-- Затемнённый фон -->
    <div class = "dark-back"></div>

    <!-- Запускаем скрипт -->
    <script>
        begin();
    </script>

</body>
</html>
