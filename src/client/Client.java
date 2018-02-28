package src.client;

import com.google.gson.Gson;
import src.model.Task;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Client{

    public static void main(String[] args) {
        System.out.println("Client: Start");

        try (Socket socket = new Socket("localhost", 777)) {
            System.out.println("Client: Установили соединение с сервером с портом 777");

            DataInputStream serverReader = new DataInputStream(socket.getInputStream());
            DataOutputStream serverWriter = new DataOutputStream(socket.getOutputStream());

            (new ObjectOutputStream(serverWriter)).writeObject("Svetlana");
            serverWriter.flush();

            String json;
            ArrayList<Task> tl = new ArrayList<>();
            int count = serverReader.readInt();
            for (int i = 0; i< count; i++){
                json = (String)(new ObjectInputStream(serverReader)).readObject();
                tl.add(new Gson().fromJson(json, Task.class));
            }
            System.out.println("Клиент приянл таски");


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
}
