package task2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class AphorismsClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            while (true) {
                String aphorism = reader.readLine();
                System.out.println("Received aphorism: " + aphorism);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
