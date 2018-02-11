package src.view;

import src.controller.TaskList;
import src.model.Task;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainFrame extends JFrame{
    private JPanel panelMain;
    private JLabel lbl;
    private JList<Task> list1;
    private JButton btShowAll;
    private JButton btAdd;
    private JButton btDelete;
    private JButton btEdit;
    private JButton btShowActive;
    private JButton btShowNotActive;
    private JScrollPane scrollPane;

    private TaskList taskList;

    public MainFrame(TaskList taskList){
        this.taskList = taskList;
        showTaskList();

        // Задаем актовность кнопок
        btDelete.setEnabled(false);
        btEdit.setEnabled(false);
        list1.addListSelectionListener(e -> {
            btDelete.setEnabled(true);
            btEdit.setEnabled(true);
        });

        setContentPane(panelMain);
        setTitle("MainFrame");
        setSize(600, 400);
        setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btShowActive.addActionListener(event -> showList(true) );
        btShowNotActive.addActionListener(event -> showList(false) );
        btShowAll.addActionListener(event -> showList());

        // TODO: 11.02.2018 Добавить действие на удаление таска
        // TODO: 11.02.2018 Добавить действие на Изменение/Добавление таска (сделать форму)

        // TODO: 11.02.2018 Как сделать, чтобы список задач обновлялся без нажатия на кнопки (и надо ли это?)

    }

    private void showTaskList(){
        String text = "Ваш список задач:";
        lbl.setText(text);
        showList();

    }

    // TODO: 11.02.2018 Как в этом случае не дублировать код?

    private void showList(){
        // Добавляем все задач из taskList'a в список
        // TODO: 11.02.2018 Можно итератор написать для forEach
        DefaultListModel dfm = new DefaultListModel();
        for (int i = 0; i < taskList.getTaskList().size(); i++) {
            dfm.addElement(taskList.getTaskList().get(i));
        }
        list1.setModel(dfm);
    }

    private void showList(boolean active){
        DefaultListModel dfm = new DefaultListModel();
        for (int i = 0; i < taskList.getTaskList(active).size(); i++) {
            dfm.addElement(taskList.getTaskList(active).get(i));
        }
        list1.setModel(dfm);

    }

}
