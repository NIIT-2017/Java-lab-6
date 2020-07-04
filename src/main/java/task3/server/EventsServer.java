package task3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class EventsServer {
    private static EventsSender eventsSender = new EventsSender();

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        int clientId = 0;
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Events server started");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    System.out.println("New client with id: " + clientId + " connected");
                    eventsSender.addClient(new ClientHolder(clientSocket, clientId));
                    clientId++;
                } catch (IOException e) {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
