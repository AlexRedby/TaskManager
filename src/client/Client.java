package src.client;

import com.google.gson.Gson;
import src.model.Task;
import src.model.packet.*;
import src.model.packet.Action;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Client {
    // TODO: 02.03.2018 Нужно все манипуляции сделать функциями, чтобы вызывать их из фреймов (наверное??!)

    private Socket socket;

    public Client(String login) throws Exception {
        socket = new Socket("localhost", 777);

        try (DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverReader = new DataInputStream(socket.getInputStream())) {
            //Логинимся
            String jsonOutput = new Gson().toJson(Action.LOGIN);

            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();

            serverWriter.writeUTF(login);
            serverWriter.flush();

            String jsonInput = serverReader.readUTF();
            State answerFromServer = new Gson().fromJson(jsonInput, State.class);
            if (answerFromServer != State.OK) {
                throw new Exception("Client: Не удалось залогиниться!");
            }
            System.out.println("Client: Регистрация прошла, сервер ответил OK");


            //Залогинились, теперь получаем таски
            jsonOutput = new Gson().toJson(Action.GET_ALL_TASKS);
            System.out.println("Client: Посылаем запрос на все таски");
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();
            jsonInput = serverReader.readUTF();
            answerFromServer = new Gson().fromJson(jsonInput, State.class);
            if (answerFromServer == State.NO_TASKS) {

                jsonOutput = new Gson().toJson(Action.EXIT);
                System.out.println("Client: Сматываем удочки");
                serverWriter.writeUTF(jsonOutput);
                serverWriter.flush();
                socket.close();
                throw new Exception("Client: Тасков нет!!!");
            }
            // Если есть таски, принимаем их
            List<Task> tasks = new ArrayList<>();
            int count = serverReader.readInt();
            for (int i = 0; i < count; i++) {
                jsonInput = serverReader.readUTF();
                tasks.add(new Gson().fromJson(jsonInput, Task.class));
            }
            System.out.println("Client: Таски приняты");

            //Давай попробуем добавить таску
            jsonOutput = new Gson().toJson(Action.ADD_TASK);
            System.out.println("Client: Посылаем запрос на добавление");
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();

            jsonOutput = new Gson().toJson(new Task());
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();
            jsonInput = serverReader.readUTF();
            answerFromServer = new Gson().fromJson(jsonInput, State.class);
            if (answerFromServer != State.OK) {
                jsonOutput = new Gson().toJson(Action.EXIT);
                System.out.println("Client: Сматываем удочки");
                serverWriter.writeUTF(jsonOutput);
                serverWriter.flush();
                socket.close();
                throw new Exception("Client: Таск не добавился!!!");
            }
            System.out.println("Client: Таск добавился");

            //Теперь удалим!!
            jsonOutput = new Gson().toJson(Action.DELETE_TASK);
            System.out.println("Client: Посылаем запрос на удаление");
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();

            jsonOutput = new Gson().toJson(tasks.get(1));
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();

            jsonInput = serverReader.readUTF();
            answerFromServer = new Gson().fromJson(jsonInput, State.class);
            if (answerFromServer == State.OK) {
                System.out.println("Client: Удалили таск" + tasks.get(1).toString());
            }

            //И обновим что нибудь
            jsonOutput = new Gson().toJson(Action.UPDATE_TASK);
            System.out.println("Client: Посылаем запрос на обновление таска");
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();

            jsonOutput = new Gson().toJson(tasks.get(0));
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();
            jsonOutput = new Gson().toJson(new Task());
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();

            jsonInput = serverReader.readUTF();
            answerFromServer = new Gson().fromJson(jsonInput, State.class);
            if (answerFromServer == State.OK) {
                System.out.println("Client: Заменили таск " + tasks.get(0).toString() + " на " + new Task().toString());
            }

            jsonOutput = new Gson().toJson(Action.EXIT);
            System.out.println("Client: Сматываем удочки");
            serverWriter.writeUTF(jsonOutput);
            serverWriter.flush();
        }
        socket.close();
    }
}
