package src.server.db;

public class Constants {
    //--------------------------Общие константы------------------------------
    public static final String TASK_COLUMN_ID = "task_id";
    public static final String TASK_COLUMN_NAME = "name";
    public static final String TASK_COLUMN_INFO = "info";
    public static final String TASK_COLUMN_DATE = "date_time";
    public static final String TASK_COLUMN_CONTSACTS = "contacts";
    public static final String TASK_COLUMN_ACTIVE = "active";
    public static final String TASK_COLUMN_OWNER = "owner";

    public static final String USER_COLUMN_ID = "user_id";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_PASSWORD = "password";

    public static final String ORACLE_URL = "jdbc:oracle:thin:@//localhost:1521/XE";
    public static final String ORACLE_USER = "password";
    public static final String ORACLE_PASSWORD = "password";
    //-----------------------------------------------------------------------

    //--------------------------TaskTable constants--------------------------
    public static final String SQL_GET_ALL = "Select * from TASKS WHERE owner = ?";
    public static final String CALL_ADD_TASK_FUNCTION= "{ ? = call ADD_TASK(?, ?, ?, ?, ?, ?) }";
    public static final String SQL_UPDATE = "UPDATE TASKS SET name = ?, info = ?, date_time = ?, contacts = ?, active = ? WHERE task_id = ?";
    public static final String SQL_DELETE = "Delete From TASKS where TASK_ID = ?";
    //-----------------------------------------------------------------------

    //--------------------------UserTable constants--------------------------
    public static final String SQL_CHECK_LOGIN = "Select * from USERS where name = ?";
    public static final String SQL_CHECK_USER = "Select * from USERS where name = ? AND password = ? ";
    public static final String CALL_ADD_USER_FUNCTION = "{ ? = call ADD_USER(?, ?) }";
    //-----------------------------------------------------------------------
}
