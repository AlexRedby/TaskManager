package src.server.db;

import src.common.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersTable {
    private Connection connection;

    public UsersTable() throws SQLException, ClassNotFoundException {
        connection = OracleUtils.getOracleConnection();
    }

    public boolean haveLogin(String login) throws SQLException {
        String sql = "Select * from USERS where name = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, login);

        ResultSet rs = pstm.executeQuery();

        if (rs.next())
            return true;
        return false;
    }

    public User get(User user) throws SQLException{
        String sql = "Select * from USERS where name = ? AND password = ? ";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, user.getName());
        pstm.setString(2, user.getPassword());

        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            //String password = rs.getString("Password");
            int id = rs.getInt("user_id");
            user.setId(id);
            return user;
        }
        return null;
    }

    //Возвращает id
    public int add(User user) throws SQLException{
        String sql = "Insert into Users(name, password) values (?,?)";

        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, user.getName());
        pstm.setString(2, user.getPassword());

        pstm.executeUpdate();

        //Похоже на костыль...
        user = get(user);
        return user.getId();
    }
}
