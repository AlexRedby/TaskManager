package src.view;

import src.controller.TaskList;
import src.model.Task;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;


public class AddTaskFrame extends JFrame{
    private JPanel panel1;
    private JButton btCancel;
    private JButton btSaveTask;
    private JTextField tfName;
    private JTextArea taInfo;
    private JRadioButton rbActive;
    private JTextField tfContacts;
    private JRadioButton rbNotActive;
    private ButtonGroup radioButtonGroup;
    private JLabel lbName;
    private JLabel lbInfo;
    private JLabel lbDateTime;
    private JLabel lbContacts;
    private JLabel lbAction;
    private JLabel label;
    private JFormattedTextField ftfDate;

    private TaskList taskList;

    public AddTaskFrame(TaskList taskList){
        this.taskList = taskList;

        groupRadioButton(true);
        setContentPane(panel1);
        setTitle("AddTaskFrame");
        setSize(500, 300);
        setVisible(true);

        btCancel.addActionListener(event -> dispose());
        btSaveTask.addActionListener(event -> addTask());
        try {
            Calendar dateTime = Calendar.getInstance();
            dateTime.add(Calendar.MINUTE, 5);
            dateTime.set(Calendar.SECOND, 0);
            formatDateTime(dateTime);
        }
        catch (ParseException e ) {
        }

    }
    public AddTaskFrame(TaskList taskList, Task task){
        this.taskList = taskList;

        groupRadioButton(task.isActive());
        setContentPane(panel1);
        setTitle("EditTaskFrame");
        setSize(500, 300);
        setVisible(true);
        tfName.setText(task.getName());
        taInfo.setText(task.getInfo());
        tfContacts.setText(task.getContacts());

        btCancel.addActionListener(event -> dispose());
        btSaveTask.addActionListener(event -> {
            taskList.deleteTask(task);
            addTask();
        });
        try {
            formatDateTime(task.getDateTime());
        }
        catch (ParseException e ) {
        }

    }

    private void groupRadioButton(boolean active){
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(rbActive);
        radioButtonGroup.add(rbNotActive);
        rbActive.setSelected(active);
        rbNotActive.setSelected(!active);
    }

    private void addTask(){
        boolean active;
        if(rbActive.isSelected()) {
            active = true;
        } else {
            active = false;
        }

        try {
            Task task = new Task(tfName.getText(), taInfo.getText(), getDateTime(), tfContacts.getText(), active);
            //Если такой задачи не сущетвует, то добавляем
            if(!taskList.isExist(task)) {
                taskList.addTask(task);

                dispose();
            }
            //Иначе сообщение с оповищением
            else JOptionPane.showMessageDialog(this,
                    "Такая задача уже существует. Изменити название, описание или дату.");
        }
        catch (DateTimeException e){
            e.printStackTrace();
        }
    }

    private void formatDateTime(Calendar dateTime) throws ParseException{
        MaskFormatter dateFormatter = new MaskFormatter("##/##/####   ##:##");
        dateFormatter.setValidCharacters("0123456789");
        dateFormatter.setPlaceholderCharacter('_');
        dateFormatter.setValueClass(String.class);
        DefaultFormatterFactory dateFormatterFactory = new
                DefaultFormatterFactory(dateFormatter);
        ftfDate.setFormatterFactory(dateFormatterFactory);

        Formatter dataTimeString = new Formatter();
        dataTimeString.format("%td%tm%tY%tH%tM", dateTime, dateTime, dateTime, dateTime, dateTime);
        ftfDate.setText(dataTimeString.toString());
    }

    private Calendar getDateTime() throws DateTimeException{
        Date date = null;
        try {
            ftfDate.commitEdit();
            date = new SimpleDateFormat("dd/MM/yyyy   HH:mm")
                    .parse((String)ftfDate.getValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Сравниваем введённое время с текущим
        if(new Date().after(date)){
            JOptionPane.showMessageDialog(this,
                    "Невозможно отложить задачу в прошлое!");
            throw new DateTimeException("Ошибка с датой!");
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

}
