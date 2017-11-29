import model.Task;

import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("Test", "It is test task",
                Calendar.getInstance(), "AlexRedby");

        System.out.println(task);
    }
}
