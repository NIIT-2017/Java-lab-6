package timeserver;

import java.io.*;
import java.net.*;
import java.util.Calendar;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Start server");

        ServerSocket server = null;
        PrintWriter out = null;
        Socket client = null;

        try {
            server = new ServerSocket(8888);
        } catch (IOException e) {
            System.out.println("Error connection to port: 8080");
            System.exit(-1);
        }

        try {
            while (true) {
                try {
                    System.out.print("Waiting for connection");
                    client = server.accept();
                    System.out.println("\n" + "Client connected");
                } catch (IOException e) {
                    System.out.println("Connection error");
                    System.exit(-1);
                }


                out = new PrintWriter(client.getOutputStream(), true);

                Calendar data = Calendar.getInstance();
                out.println(data.getTime());

                out.close();
                client.close();
            }
        } finally {
            server.close();
        }
    }
}
