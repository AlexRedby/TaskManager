package src.server;

import java.net.ServerSocket;

public class StartServer {

    //Запуск сервера осуществляется тут
    //Создаёт экземпляры серверов для клиентов(многопоточность)
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
}
