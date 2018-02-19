package src.controller;

import java.io.*;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Controller {

    public static void writeTaskList(TaskList taskList) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("TaskLog.bin"));
        out.writeObject(taskList);
        out.close();
    }

    public static TaskList readTaskList() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("TaskLog.bin"));
        TaskList readedTaskList = (TaskList) in.readObject();
        in.close();
        return readedTaskList;
    }

    public static Calendar getDateTime(String strDate) throws DateTimeException {
        String strValue[] = strDate
                .trim()
                .replaceAll("\\s+", " ")
                .split("[/: ]");

        int intValue[] = new int[strValue.length];
        for (int i = 0; i < strValue.length; i++)
            intValue[i] = Integer.valueOf(strValue[i]);

        //Месяц
        if (intValue[1] > 12 || intValue[2] < 1)
            throw new DateTimeException("Месяц должен лежать в интервале = [1,12]");
        //День
        else if (intValue[0] > 0 && !checkDayInMonth(intValue[0], intValue[1], intValue[2]))
            throw new DateTimeException("День в " + strValue[1] + "/"
                    + strValue[2] + " не может быть = " + strValue[0]);
        //Час
        else if (intValue[3] > 23 || intValue[3] < 0)
            throw new DateTimeException("Часы должны лежать в интервале = [0,23]");
        //Минута
        else if (intValue[4] > 59 || intValue[4] < 0)
            throw new DateTimeException("Часы должны лежать в интервале = [0,59]");

        return new GregorianCalendar(intValue[2], intValue[1], intValue[0], intValue[3], intValue[4]);
    }

    private static boolean checkDayInMonth(int day, int month, int year) {
        int daysInMonths[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (month == 2 && (((year % 4 == 0) && (year % 100) != 0) || (year % 400 == 0)))
            daysInMonths[1]++;

        return daysInMonths[month - 1] >= day;
    }
}
