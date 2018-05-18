package src.server.db;

import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import java.util.Locale;

public class OracleUtils {
    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        if(dataSource == null){
            Locale.setDefault(Locale.ENGLISH);

            OracleDataSource ds = new OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@//localhost:1521/XE");
            ds.setUser("TMUser");
            ds.setPassword("1234");

            dataSource = ds;
        }

        return dataSource.getConnection();
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
