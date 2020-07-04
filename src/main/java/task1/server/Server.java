package task1.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String text = in.readLine();
                if(!"getDate".equals(text)){
                    System.out.println("Unknown message from client");
                    continue;
                }

                System.out.println("Incoming request: " + text);
                out.println(new Date().getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
