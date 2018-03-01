package src.client;

import com.google.gson.Gson;
import src.model.Task;
import src.model.packet.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Client{

    //Как закрывать socket?
    private Socket socket;

    public Client(String login) throws Exception {
            socket = new Socket("localhost", 777);

            try(DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());
                DataInputStream serverReader = new DataInputStream(socket.getInputStream())){

                String jsonOutput = new Gson().toJson(Action.LOGIN);

                serverWriter.writeUTF(jsonOutput);
                serverWriter.flush();

                String jsonInput = serverReader.readUTF();
                PacketServer answerFromServer = new Gson().fromJson(jsonInput, PacketServer.class);
                if(answerFromServer.getState() != State.OK)
                    throw new Exception("Не удалось залогиниться!");
            }

    }

    //Убрать
    public static void main(String[] args) {
        System.out.println("Client: Start");

        try (Socket socket = new Socket("localhost", 777)) {
            System.out.println("Client: Установили соединение с сервером с портом 777");

            DataInputStream serverReader = new DataInputStream(socket.getInputStream());
            DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());

            List<Task> taskList = getTasks(serverWriter, serverReader);
            System.out.println("Client: Отсоединяемся от сервера и закрываем Streams");

            serverReader.close();
            serverWriter.close();
            socket.close();
        }
        catch(Exception e) {
            System.out.println("Client: Возникла ошибка: ");
            System.out.print(e.getMessage());
        }
        System.out.println("Client: FIN!");
    }

    //Переписать и сделать публичным
    private static List<Task> getTasks(DataOutputStream serverWriter, DataInputStream serverReader){
        try {
            (new ObjectOutputStream(serverWriter)).writeObject("Svetlana");
            serverWriter.flush();

            String json;
            List<Task> tl = new ArrayList<>();
            int count = serverReader.readInt();
            for (int i = 0; i < count; i++) {
                json = (String) (new ObjectInputStream(serverReader)).readObject();
                tl.add(new Gson().fromJson(json, Task.class));
            }
            System.out.println("Клиент приянл таски");
            return tl;
        }
            catch(Exception e) {
                System.out.println("Client: Возникла ошибка: ");
                System.out.print(e.getMessage());
                return null;
        }
    }
}
