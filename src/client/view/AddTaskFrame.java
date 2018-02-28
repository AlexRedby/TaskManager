package src.client.view;

import src.server.controller.TaskList;
import src.model.Constants;
import src.model.Task;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;


public class AddTaskFrame extends JFrame {
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
    private MainFrame mainFrame;

    public AddTaskFrame(TaskList taskList, MainFrame mainFrame) {
        this.taskList = taskList;
        this.mainFrame = mainFrame;

        btCancel.addActionListener(event -> dispose());
        btSaveTask.addActionListener(event -> addTask());
        try {
            Calendar dateTime = Calendar.getInstance();
            dateTime.add(Calendar.MINUTE, Constants.FIVE_MINUTES);
            dateTime.set(Calendar.SECOND, 0);
            formatDateTime(dateTime);
        } catch (ParseException e) {
        }

        groupRadioButton(true);
        setContentPane(panel1);
        setTitle("AddTaskFrame");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = Constants.ADD_TASK_FRAME_WIDTH;
        int sizeHeight = Constants.ADD_TASK_FRAME_HEIGHT;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        setBounds(locationX, locationY, sizeWidth, sizeHeight);

        setVisible(true);
    }

    public AddTaskFrame(TaskList taskList, Task task, MainFrame mainFrame) {
        this.taskList = taskList;
        this.mainFrame = mainFrame;

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
        } catch (ParseException e) {
        }

        groupRadioButton(task.isActive());
        setContentPane(panel1);
        setTitle("EditTaskFrame");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = Constants.ADD_TASK_FRAME_WIDTH;
        int sizeHeight = Constants.ADD_TASK_FRAME_HEIGHT;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        setBounds(locationX, locationY, sizeWidth, sizeHeight);

        setVisible(true);
    }

    private void groupRadioButton(boolean active) {
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(rbActive);
        radioButtonGroup.add(rbNotActive);
        rbActive.setSelected(active);
        rbNotActive.setSelected(!active);
    }

    private void addTask() {
        boolean active;
        active = rbActive.isSelected();

        try {
            Task task = new Task(tfName.getText(), taInfo.getText(), getDateTime(), tfContacts.getText(), active);
            //Если такой задачи не сущетвует, то добавляем
            if (!taskList.isExist(task)) {
                taskList.addTask(task);

                mainFrame.update();
                dispose();
            }
            //Иначе сообщение с оповищением
            else JOptionPane.showMessageDialog(this,
                    "Такая задача уже существует. Измените название, описание или дату.");
        }
        //Если getDateTime() ввернёт ошибку
        catch (DateTimeException | ParseException e) {
            JOptionPane.showMessageDialog(this, "Введена некорректная дата",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void formatDateTime(Calendar dateTime) throws ParseException {
        MaskFormatter dateFormatter = new MaskFormatter(Constants.DATE_TIME_MASK);
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

    private Calendar getDateTime() throws DateTimeException, ParseException {
        ftfDate.commitEdit();

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_PATTERN);
        sdf.setLenient(false);
        Date date = sdf.parse((String) ftfDate.getValue());

        //Сравниваем введённое время с текущим
        if (new Date().after(date)) {
            throw new DateTimeException("Невозможно отложить задачу в прошлое!");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
