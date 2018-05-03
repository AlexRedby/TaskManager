package src.server.db;

import src.common.model.Task;
import src.common.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksTable {
    private Connection connection;
    private User user;

    public TasksTable(User user) throws SQLException, ClassNotFoundException {
        connection = OracleUtils.getOracleConnection();
        this.user = user;
    }

    public Task get(Task task) throws SQLException{
        String sql = "Select * from TASKS where " +
                "name = ? AND info = ? AND date_time = ? AND owner = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, task.getName());
        pstm.setString(2, task.getInfo());
        pstm.setTimestamp(3, OracleUtils.getTimestamp(task.getDateTime()));
        pstm.setInt(4, user.getId());

        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            //String password = rs.getString("Password");
            int id = rs.getInt("task_id");
            task.setId(id);
            return task;
        }
        return null;
    }

    public List<Task> getAll() throws SQLException {
        String sql = "Select * from TASKS WHERE owner = ?";
        List<Task> tasks = new ArrayList<>();

        PreparedStatement pstm = connection.prepareStatement(sql);

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
        return tasks;
    }

    public int add(Task task) throws SQLException{
        String sql = "Insert into Tasks(name, info, date_time, contacts, active, owner) " +
                "values (?,?,?,?,?,?)";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, task.getName());
        pstm.setString(2, task.getInfo());
        pstm.setTimestamp(3, OracleUtils.getTimestamp(task.getDateTime()));
        pstm.setString(4, task.getContacts());
        pstm.setInt(5, OracleUtils.getInt(task.isActive()));
        pstm.setInt(6, user.getId());

        pstm.executeUpdate();

        //Похоже на костыль...
        return get(task).getId();
    }

    public void postpone(Task task, Calendar dateTime) throws SQLException {
        task.setDateTime(dateTime);
        update(task);
    }

    public void complete(Task task) throws SQLException {
        task.setActive(false);
        update(task);
    }

    public void update(Task task) throws SQLException {
        String sql = "UPDATE TASKS " +
                "SET name = ?, info = ?, date_time = ?, contacts = ?, active = ? " +
                "WHERE task_id = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);

        //SET
        pstm.setString(1, task.getName());
        pstm.setString(2, task.getInfo());
        pstm.setTimestamp(3, OracleUtils.getTimestamp(task.getDateTime()));
        pstm.setString(4, task.getContacts());
        pstm.setInt(5, OracleUtils.getInt(task.isActive()));

        //WHERE
        pstm.setInt(6, task.getId());

        pstm.executeUpdate();
    }

    public void delete(Task task) throws SQLException {
        String sql = "Delete From TASKS where TASK_ID = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setInt(1, task.getId());

        pstm.executeUpdate();
    }
}
