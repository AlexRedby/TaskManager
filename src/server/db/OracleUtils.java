package src.server.db;

import java.sql.*;
import java.util.Calendar;
import java.util.Locale;

public class OracleUtils {
    public static Connection getOracleConnection()
            throws SQLException, ClassNotFoundException {

        //TO-DO: Изменить параметры соединения на свои.
        String hostName = "localhost";
        String sid = "XE";
        String userName = "TMUser";
        String password = "1234";

        return getOracleConnection(hostName, sid, userName, password);
    }

    public static Connection getOracleConnection(String hostName, String sid,
                                                 String userName, String password)
            throws SQLException, ClassNotFoundException {

        //Без этого не работает
        Locale.setDefault(Locale.ENGLISH);

        // jdbc:oracle:thin:@localhost:1521:XE
        String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;

        Connection connection = DriverManager.getConnection(connectionURL,
                userName, password);
        return connection;
    }

    public static int getInt(boolean b){
        return b ? 1 : 0;
    }

    public static boolean getBoolean(int i){
        return i != 0;
    }

    public static Timestamp getTimestamp(Calendar c) {
        return new Timestamp(c.getTimeInMillis());
    }

    public static Calendar getCalendar(Timestamp d){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(d.getTime());
        return c;
    }
}
