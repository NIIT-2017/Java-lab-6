package task2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AphorismsServer {

    private static AphorismSender aphorismSender = new AphorismSender();

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        System.out.println("Aphorism server started");
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                try {
                    aphorismSender.addClient(new ClientHolder(clientSocket));
                } catch (IOException e) {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
