package server;

import java.io.IOException;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1234);
        System.out.println("Сервер стартовал");
        try {
            while (true) {
                Socket incoming = server.accept();
                System.out.println("Соединение установлено");
                Runnable r = new ThreadedHandler(incoming);
                Thread t = new Thread(r);
                t.start();
            }
        } finally {
            server.close();
        }

    }
}
