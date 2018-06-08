package src.server.db;

import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import java.util.Locale;

public class OracleUtils {
    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        if(OracleUtils.dataSource == null){
            Locale.setDefault(Locale.ENGLISH);

            OracleDataSource dataSource = new OracleDataSource();
            dataSource.setURL(Constants.ORACLE_URL);
            dataSource.setUser(Constants.ORACLE_USER);
            dataSource.setPassword(Constants.ORACLE_PASSWORD);

            OracleUtils.dataSource = dataSource;
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
