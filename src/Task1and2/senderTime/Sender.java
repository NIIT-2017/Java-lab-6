package Task1and2.senderTime;

import java.io.IOException;
import java.net.ServerSocket;

public class Sender {
    private static int PORT = 2000;
    private static ServerSocket server;
    public static void main(String[] args) {
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("Server didn't run");
        }

        System.out.println("sever is up");
        while (true) {
            try {
                new ServerSoket(server.accept());
                System.out.println("Task1and2.client is connect");
            } catch (IOException e) {
                System.out.println("cannot connecting with Task1and2.client");
            }
        }
    }
}
