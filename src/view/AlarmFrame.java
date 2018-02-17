package src.view;

import src.controller.TaskList;
import src.model.Task;

import javax.swing.*;
import javax.swing.text.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

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
    private JRadioButton rbManually;
    private ButtonGroup radioButtonGroup;

    private JLabel lTaskInfo;
    private JFormattedTextField formattedTextField;

    public AlarmFrame(Task task, TaskList taskList) {
        this.task = task;
        this.taskList = taskList;
        groupRadioButton();
        showTask();

        //Добавляем слушателей на RadioButton
        Enumeration<AbstractButton> buttons = radioButtonGroup.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton b = buttons.nextElement();
            if(b != rbManually)
                b.addActionListener(event -> {
                        formattedTextField.setEnabled(false);
                });
            else
                b.addActionListener(event -> {
                    formattedTextField.setEnabled(true);
                });
        }

        //TODO: Нужно придумать что-то получше для ввода даты, может JDataPicker(нужно скачивать)
        //Устанавливаем MaskFormatter на FormattedTextField
        MaskFormatter mf = null;
        try {
            mf = new MaskFormatter("##/##/####   ##:##");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mf.setPlaceholderCharacter('_');
        DefaultFormatterFactory dff = new DefaultFormatterFactory(mf);
        formattedTextField.setFormatterFactory(dff);

        //Завершаем Task
        btCompleteTask.addActionListener(event -> {
            taskList.complete(task);
            dispose();
        });

        //Откладываем Task
        btPostponeTask.addActionListener(event -> {
            boolean isCorrect = true;
            Calendar newDate = Calendar.getInstance();
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
            else if(rbManually.isSelected()) {
                Date date = null;
                try {
                    formattedTextField.commitEdit();
                    date = new SimpleDateFormat("dd/MM/yyyy   HH:mm")
                            .parse((String)formattedTextField.getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Сравниваем введённое время с текущим
                if(new Date().after(date)){
                    JOptionPane.showMessageDialog(this,
                            "Невозможно отложить задачу в прошлое!");
                    isCorrect = false;
                }
                newDate.setTime(date);
            }
            if(isCorrect) {
                newDate.add(Calendar.MINUTE, minutes);
                taskList.postpone(task, newDate);
                dispose();
            }
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
        radioButtonGroup.add(rbManually);
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