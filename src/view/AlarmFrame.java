package src.view;

import src.controller.TaskList;
import src.model.Task;

import javax.swing.*;
import java.util.Calendar;

public class AlarmFrame extends JFrame{
    private Task task;
    private TaskList taskList;

    private JPanel panelMain;
    private JButton btCompleteTask;
    private JButton btPostponeTask;

    private JRadioButton rb5Minute;
    private JRadioButton rb10Minute;
    private JRadioButton rb30Minute;
    private JRadioButton rbHour;
    private JRadioButton rbDay;
    private ButtonGroup radioButtonGroup;

    private JLabel lTaskInfo;

    public AlarmFrame(Task task, TaskList taskList){
        this.task = task;
        this.taskList = taskList;
        groupRadioButton();
        showTask();

        //Завершаем Task
        btCompleteTask.addActionListener(event -> {
            taskList.complete(task);
            dispose();
        });

        //Откладываем Task
        btPostponeTask.addActionListener(event -> {
            int minutes = 0;
            if(rb5Minute.isSelected())
                minutes = 5;
            else if(rb10Minute.isSelected())
                minutes = 10;
            else if(rb30Minute.isSelected())
                minutes = 30;
            else if(rbHour.isSelected())
                minutes = 60;
            else if(rbDay.isSelected())
                minutes = 1440;

            Calendar newDate = Calendar.getInstance();
            newDate.add(Calendar.MINUTE, minutes);

            taskList.postpone(task, newDate);
            dispose();
        });

        setContentPane(panelMain);
        setTitle("AlarmFrame");
        setSize(300, 400);
        setVisible(true);
    }


    private void groupRadioButton(){
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(rb5Minute);
        radioButtonGroup.add(rb10Minute);
        radioButtonGroup.add(rb30Minute);
        radioButtonGroup.add(rbHour);
        radioButtonGroup.add(rbDay);
    }

    private void showTask(){
        String text = "<html>"
                + task.getName() + "<br>"
                + task.getInfo() + "<br>"
                + task.getDateTime().getTime() + "<br><br>"
                + "Отложить на:"
                + "</html>";
        lTaskInfo.setText(text);
    }
}
