package src.server;

import com.google.gson.*;
import src.model.packet.*;
import src.server.controller.Controller;
import src.server.controller.TaskList;
import src.model.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private Socket socket;

    public Server(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try(DataInputStream reader = new DataInputStream(socket.getInputStream());
                DataOutputStream writer = new DataOutputStream(socket.getOutputStream())){

                String jsonInput = reader.readUTF();
                System.out.println("Server: Получили json.");

                //Как быть с различными пакетами?(или сделать один общий?)(или посылать сначало Aсtion?)
                PacketUser pu = new Gson().fromJson(jsonInput, PacketUser.class);
                System.out.println("Server: Преоброзовали в PC.");

                //В будущем заменить на switch
                if(pu.getAction() == Action.LOGIN) {

                    PacketServer ps = null;
                    //Проверять существует ли файл с таким именем
                    if(pu.getLogin().equals("Alex")) {
                        ps = new PacketServer(State.OK);
                    }
                    //Создавать новый файл если нет такового
                    else
                        ps = new PacketServer(State.LOGIN_ERROR);

                    String jsonOutput = new Gson().toJson(ps);
                    writer.writeUTF(jsonOutput);
                    writer.flush();
                }

            } catch (Exception e) {
                System.out.println("Server: Возникла ошибка: " + e.getMessage());
            }
        }
    }

}
