package src.server.db;

import src.common.model.User;

import java.sql.*;

public class UsersTable {

    private static final String SQL_CHECK_LOGIN = "Select * from USERS where name = ?";
    private static final String SQL_CHECK_USER = "Select * from USERS where name = ? AND password = ? ";
    private static final String CALL_ADD_USER_FUNCTION = "{ ? = call ADD_USER(?, ?) }";

    public static boolean haveLogin(String login) throws SQLException {
        try(Connection connection = OracleUtils.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(SQL_CHECK_LOGIN);
            pstm.setString(1, login);

            ResultSet rs = pstm.executeQuery();

            boolean result = false;
            if (rs.next())
                result = true;

            //Закрывем всё
            rs.close();
            pstm.close();

            return result;
        }
    }

    public static User get(User user) throws SQLException{
        try(Connection connection = OracleUtils.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(SQL_CHECK_USER);

            pstm.setString(1, user.getName());
            pstm.setString(2, user.getPassword());

            ResultSet rs = pstm.executeQuery();

            User resultUser = null;
            if (rs.next()) {
                int id = rs.getInt("user_id");
                user.setId(id);
                resultUser = user;
            }

            //Закрывем всё
            rs.close();
            pstm.close();

            return resultUser;
        }
    }

    //Возвращает id
    public static int add(User user) throws SQLException{
        try(Connection connection = OracleUtils.getConnection()) {
            CallableStatement cstm = connection.prepareCall(CALL_ADD_USER_FUNCTION);

            //Возвращаемое значение функцией
            cstm.registerOutParameter(1, Types.INTEGER);

            cstm.setString(2, user.getName());
            cstm.setString(3, user.getPassword());

            cstm.executeUpdate();

            //Получаем id добавленной задачи
            int id = cstm.getInt(1);

            //Закрываем всё
            cstm.close();

            return id;
        }
    }
}
