package src.server.db;

import src.common.model.User;

import java.sql.*;

public class UsersTable {

    public static boolean haveLogin(String login) throws SQLException {
        try(Connection connection = OracleUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Constants.SQL_CHECK_LOGIN);
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();

            boolean result = false;
            if (resultSet.next())
                result = true;

            //Закрывем всё
            resultSet.close();
            statement.close();

            return result;
        }
    }

    public static User get(User user) throws SQLException{
        try(Connection connection = OracleUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Constants.SQL_CHECK_USER);

            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());

            ResultSet resultSet = statement.executeQuery();

            User resultUser = null;
            if (resultSet.next()) {
                int id = resultSet.getInt(Constants.USER_COLUMN_ID);
                user.setId(id);
                resultUser = user;
            }

            //Закрывем всё
            resultSet.close();
            statement.close();

            return resultUser;
        }
    }

    //Возвращает id
    public static int add(User user) throws SQLException{
        try(Connection connection = OracleUtils.getConnection()) {
            CallableStatement statement = connection.prepareCall(Constants.CALL_ADD_USER_FUNCTION);

            //Возвращаемое значение функцией
            statement.registerOutParameter(1, Types.INTEGER);

            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());

            statement.executeUpdate();

            //Получаем id добавленной задачи
            int id = statement.getInt(1);

            //Закрываем всё
            statement.close();

            return id;
        }
    }
}
