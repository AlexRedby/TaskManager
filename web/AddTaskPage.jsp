<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="index.css"/>
    <link rel="stylesheet" type="text/css" href="table.css"/>
    <title>Добвление задачи</title>
</head>
<body>

    <form name="addform" class="center">
        <fieldset class="fildsetAdd">
            <legend>Add task page</legend>

            <label for="name"> Название:</label>
                <input type="text" id="name" name="name" value="" size="20" class="filds"/><br>


            <label for="info">Описание:</label>
                <textarea id="info" name="info" class="filds" style="resize: none"></textarea>


            <label for="dateTime">Дата и время:</label>
            <input class="inline, filds" type="datetime-local" id="dateTime" ><br>
            <script>
                var date = new Date();

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
                document.getElementById("dateTime").value = today;
            </script>


            <label for="contacts">Контакты:</label>
            <input type="text" id="contacts" value="" size="20" class="filds"/><br>


            <label>Активность:</label>

                <input type="radio" id = "active" name="active" checked/><label for="active">активная</label>
                <input type="radio" id = "notActive" name="active"/><label for="notActive">не активная</label>

        </fieldset>
        <input type="submit" value="Сохранить" name="save" />
        <input type="submit" value="Отмена" name="cancel" />
    </form>

</body>
</html>
