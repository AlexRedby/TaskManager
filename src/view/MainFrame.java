package src.view;

import src.controller.TaskList;
import src.model.Task;

import javax.swing.*;
import java.awt.*;

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

        //Чтобы активные задачи отличались от не активных
        list1.setCellRenderer(new DefaultListCellRenderer () {
            @Override
            public Component getListCellRendererComponent(JList list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);

                Task task = (Task) value;
                if (!task.isActive()) {
                    setBackground(Color.GRAY);
                }
                return this;
            }
        });

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

        //Удаление Task
        btDelete.addActionListener(event -> {
            Task selectedTask = list1.getSelectedValue();
            int selectedIdx = list1.getSelectedIndex();
            taskList.deleteTask(selectedTask);
            ((DefaultListModel)list1.getModel()).remove(selectedIdx);
        });
        btAdd.addActionListener(event -> new AddTaskFrame(taskList));
        btEdit.addActionListener(event -> new AddTaskFrame(taskList, list1.getSelectedValue()));


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

        btDelete.setEnabled(false);
        btEdit.setEnabled(false);
    }

    private void showList(boolean active){
        DefaultListModel dfm = new DefaultListModel();
        for (int i = 0; i < taskList.getTaskList(active).size(); i++) {
            dfm.addElement(taskList.getTaskList(active).get(i));
        }
        list1.setModel(dfm);

        btDelete.setEnabled(false);
        btEdit.setEnabled(false);
    }

}
