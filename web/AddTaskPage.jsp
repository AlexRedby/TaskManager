<%@ page import="src.common.model.Task" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="index.css"/>
    <link rel="stylesheet" type="text/css" href="table.css"/>
    <title>Добвление задачи</title>
</head>
<body>
    <div class="center">
            <form name="addForm" action="SaveTask">
                <fieldset class="fildsetAdd">
                    <legend>Add task page</legend>

                    <label for="name"> Название:</label>
                         <input type="text" id="name" name="name" value="${oldTask.getName()}" size="20" class="filds" required/><br>


                    <label for="info">Описание:</label>
                        <textarea id="info" name="info" class="filds" style="resize: none" required>${oldTask.getInfo()}</textarea>


                    <label for="dateTime">Дата и время:</label>
                    <input class="inline filds" name="date" type="datetime-local" id="dateTime" ><br>
                    <script>

                        function checkDate() {
                            document.getElementById("dateTime").min = getFormattedDate(new Date());
                        }

                        function getFormattedDate(date) {
                            var day = date.getDate();
                            var month = date.getMonth() + 1;
                            var year = date.getFullYear();
                            var hour = date.getHours();
                            var min = date.getMinutes() + 5;

                            if (month < 10) month = "0" + month;
                            if (day < 10) day = "0" + day;
                            if (hour < 10) hour = "0" + hour;
                            if (min < 10) min = "0" + min;
                            if (min >= 60) min = "0" + (min-60);

                            var today = year + "-" + month + "-" + day + "T" + hour + ":" + min;
                            return today;
                        }

                        var currentDate = new Date();
                        var date = new Date(${oldTask.getDateTime().getTimeInMillis()});

                        if(date < currentDate)
                            date = currentDate;

                        document.getElementById("dateTime").value =  getFormattedDate(date);
                    </script>


                    <label for="contacts">Контакты:</label>
                    <input type="text" id="contacts" name="contacts" value="${oldTask.getContacts()}" size="20" class="filds" required/><br>


                    <label>Активность:</label>

                        <input type="radio" id = "active" name="active" value="a" checked/><label for="active">активная</label>
                        <input type="radio" id = "notActive" name="active" value="na"/><label for="notActive">не активная</label>
                    <script>
                        document.getElementById("active").checked = ${oldTask.isActive()};
                        document.getElementById("notActive").checked = ${!oldTask.isActive()};
                    </script>


                </fieldset>

                <button name="save" onclick="checkDate()">Сохранить</button>
                <script>
                    document.getElementsByName("save")[0].value = ${oldTask.getId()};
                </script>
                <%request.removeAttribute("oldTask");%>
            </form>
        </div>
</body>
</html>
