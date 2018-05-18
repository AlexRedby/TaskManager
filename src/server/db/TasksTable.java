package src.server.db;

import src.common.model.Task;
import src.common.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksTable {

    private static final String SQL_GET_ALL = "Select * from TASKS WHERE owner = ?";
    private static final String CALL_ADD_TASK_FUNCTION= "{ ? = call ADD_TASK(?, ?, ?, ?, ?, ?) }";
    private static final String SQL_UPDATE = "UPDATE TASKS SET name = ?, info = ?, date_time = ?, contacts = ?, active = ? WHERE task_id = ?";
    private static final String SQL_DELETE = "Delete From TASKS where TASK_ID = ?";

    public static List<Task> getAll(User user) throws SQLException {
        try(Connection connection = OracleUtils.getConnection()) {
            List<Task> tasks = new ArrayList<>();

            PreparedStatement pstm = connection.prepareStatement(SQL_GET_ALL);

            pstm.setInt(1, user.getId());

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Task currentTask = new Task();

                currentTask.setId(rs.getInt("task_id"));
                currentTask.setName(rs.getString("name"));
                currentTask.setInfo(rs.getString("info"));
                currentTask.setDateTime(OracleUtils.getCalendar(rs.getTimestamp("date_time")));
                currentTask.setContacts(rs.getString("contacts"));
                currentTask.setActive(OracleUtils.getBoolean(rs.getInt("active")));

                tasks.add(currentTask);
            }

            //Закрывем всё
            rs.close();
            pstm.close();

            return tasks;
        }
    }

    //Возвращает id
    public static int add(Task task, User user) throws SQLException{
        try(Connection connection = OracleUtils.getConnection()) {
            CallableStatement cstm = connection.prepareCall(CALL_ADD_TASK_FUNCTION);

            //Возвращаемое значение функцией
            cstm.registerOutParameter(1, Types.INTEGER);

            //Передаваемые параметры
            cstm.setString(2, task.getName());
            cstm.setString(3, task.getInfo());
            cstm.setTimestamp(4, OracleUtils.getTimestamp(task.getDateTime()));
            cstm.setString(5, task.getContacts());
            cstm.setInt(6, OracleUtils.getInt(task.isActive()));
            cstm.setInt(7, user.getId());

            cstm.executeUpdate();

            //Получаем id добавленной задачи
            int id = cstm.getInt(1);

            //Закрываем всё
            cstm.close();

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
            PreparedStatement pstm = connection.prepareStatement(SQL_UPDATE);

            //SET
            pstm.setString(1, task.getName());
            pstm.setString(2, task.getInfo());
            pstm.setTimestamp(3, OracleUtils.getTimestamp(task.getDateTime()));
            pstm.setString(4, task.getContacts());
            pstm.setInt(5, OracleUtils.getInt(task.isActive()));

            //WHERE
            pstm.setInt(6, task.getId());

            pstm.executeUpdate();

            //Закрывем всё
            pstm.close();
        }
    }

    public static void delete(Task task) throws SQLException {
        try(Connection connection = OracleUtils.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(SQL_DELETE);

            pstm.setInt(1, task.getId());

            pstm.executeUpdate();

            //Закрывем всё
            pstm.close();
        }
    }
}
