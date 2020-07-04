package task4.server;

import task4.CurrencyExchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CurrencyServer {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Currency server started");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                new Thread(() -> {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                        CurrencyExchange exchange = (CurrencyExchange) in.readObject();
                        double rate = CurrencyRates.getRate(exchange);
                        System.out.println("Found rate: " + rate);
                        if(rate == 0.0){
                            out.writeObject("Unknown currency exchange");
                        } else {
                            out.writeObject(rate);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
