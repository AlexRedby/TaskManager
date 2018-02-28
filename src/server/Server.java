package src.server;

import com.google.gson.*;
import src.controller.Controller;
import src.controller.TaskList;
import src.model.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private Socket socket;

    private Server(Socket socket){
        this.socket = socket;
    }

    public static void main(String[] args)  {
        System.out.println("Server: Start");
        try (ServerSocket server = new ServerSocket(777)) {
            System.out.println("Server: Серверный сокет с портом 777 создан.");
            System.out.println("Server: Входим в бесконечный цикл ожидания...");
            while (true) {
                new Thread(new Server(server.accept())).start();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Server: FIN!");
        }
    }


    @Override
    public void run() {
        try {
            System.out.println("Client: Установили соединение с сервером с портом 777");
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            String login = (String)(new ObjectInputStream(reader)).readObject();
            System.out.println(login);
            if (login.equals("Svetlana")){
                TaskList taskList = Controller.readTaskList();
                writer.writeInt(taskList.getTaskList().size());
                System.out.println(taskList.getTaskList().size());
                writer.flush();
                System.out.println("Отправляем клиенту таски");
                for (Task task : taskList.getTaskList()) {
                    Gson gson = new Gson();
                    String outputStr = gson.toJson(task);
                    System.out.println(outputStr);
                    (new ObjectOutputStream(writer)).writeObject(outputStr);
                    writer.flush();
                }

            }



            System.out.println("Server: Работа с данным клиентом завершена(закрываем Streams and socket).");
            reader.close();
            writer.close();
            socket.close();
        }
        catch (Exception e){
            System.out.println("Server: Возникла ошибка: " + e.getMessage());
        }
    }

}
