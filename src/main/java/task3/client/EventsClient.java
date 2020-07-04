package task3.client;

import task3.Event;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class EventsClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             InputStream input = socket.getInputStream();
             ObjectInputStream reader = new ObjectInputStream(input)) {
            while (true) {
                try {
                    Event event = (Event) reader.readObject();
                    System.out.println("Received event for client: " + event.getId() + ", date: " + event.getDate() + " message: " + event.getMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
