package src.server.db;

import src.common.model.Task;
import src.common.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksTable {

    public static List<Task> getAll(User user) throws SQLException {
        try(Connection connection = OracleUtils.getConnection()) {
            List<Task> tasks = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement(Constants.SQL_GET_ALL);

            statement.setInt(1, user.getId());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Task currentTask = new Task();

                currentTask.setId(resultSet.getInt(Constants.TASK_COLUMN_ID));
                currentTask.setName(resultSet.getString(Constants.TASK_COLUMN_NAME));
                currentTask.setInfo(resultSet.getString(Constants.TASK_COLUMN_INFO));
                currentTask.setDateTime(OracleUtils.getCalendar(resultSet.getTimestamp(Constants.TASK_COLUMN_DATE)));
                currentTask.setContacts(resultSet.getString(Constants.TASK_COLUMN_CONSTACTS));
                currentTask.setActive(OracleUtils.getBoolean(resultSet.getInt(Constants.TASK_COLUMN_ACTIVE)));

                tasks.add(currentTask);
            }

            //Закрывем всё
            resultSet.close();
            statement.close();

            return tasks;
        }
    }

    //Возвращает id
    public static int add(Task task, User user) throws SQLException{
        try(Connection connection = OracleUtils.getConnection()) {
            CallableStatement statement = connection.prepareCall(Constants.CALL_ADD_TASK_FUNCTION);

            //Возвращаемое значение функцией
            statement.registerOutParameter(1, Types.INTEGER);

            if(task.getInfo() == null) task.setInfo("");
            if(task.getContacts() == null) task.setContacts("");

            //Передаваемые параметры
            statement.setString(2, task.getName());
            statement.setString(3, task.getInfo());
            statement.setTimestamp(4, OracleUtils.getTimestamp(task.getDateTime()));
            statement.setString(5, task.getContacts());
            statement.setInt(6, OracleUtils.getInt(task.isActive()));
            statement.setInt(7, user.getId());

            statement.executeUpdate();

            //Получаем id добавленной задачи
            int id = statement.getInt(1);

            //Закрываем всё
            statement.close();

            return id;
        }
    }

    public static void postpone(Task task, Calendar dateTime) throws SQLException {
        task.setDateTime(dateTime);
        update(task);
    }

    public static void complete(Task task) throws SQLException {
        task.setActive(false);
        update(task);
    }

    public static void update(Task task) throws SQLException {
        try(Connection connection = OracleUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Constants.SQL_UPDATE);

            //SET
            statement.setString(1, task.getName());
            statement.setString(2, task.getInfo());
            statement.setTimestamp(3, OracleUtils.getTimestamp(task.getDateTime()));
            statement.setString(4, task.getContacts());
            statement.setInt(5, OracleUtils.getInt(task.isActive()));

            //WHERE
            statement.setInt(6, task.getId());

            statement.executeUpdate();

            //Закрывем всё
            statement.close();
        }
    }

    public static void delete(Task task) throws SQLException {
        try(Connection connection = OracleUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Constants.SQL_DELETE);

            statement.setInt(1, task.getId());

            statement.executeUpdate();

            //Закрывем всё
            statement.close();
        }
    }
}
