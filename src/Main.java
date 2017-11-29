package src;

import src.model.Task;

import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("Test", "It is test task",
                Calendar.getInstance(), "AlexRedby");

        System.out.println(task);

        Task task2 = new Task("Test2", "It is test task too",
                Calendar.getInstance(), "SvetlanaKlementeva");
        System.out.println(task2);
    }
}
