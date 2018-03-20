package src.server;

import src.common.model.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class StartServer {

    //Запуск сервера осуществляется тут
    //Создаёт экземпляры серверов для клиентов(многопоточность)
    public static void main(String[] args) {
        System.out.println("ServerStart: Start");
        try (ServerSocket server = new ServerSocket(Constants.SERVER_PORT);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("ServerStart: Серверный сокет создан.");
            System.out.println("ServerStart: Входим в цикл ожидания запроса(выход - quit)...");

            while (!server.isClosed()) {

                if(consoleInput.ready()){
                    System.out.println("ServerStart: Получил сообщение из консоли. Посмотрим что в нём...");

                    if(consoleInput.readLine().equalsIgnoreCase("quit")){
                        System.out.println("ServerStart: Получили команду на завершение работы...");
                        server.close();
                        break;
                    }
                }

                new Thread(new Server(server.accept())).start();
                System.out.println("ServerStart: Создали новый сервер...");
            }

            System.out.println("ServerStart: Сервер завершил свою работу...");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ServerStart: FIN!");
        }
    }
}
