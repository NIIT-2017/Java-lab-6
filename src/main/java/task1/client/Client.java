package task1.client;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Client {

    public Date getDateFromServer() {
        String date = "";
        try (Socket socket = new Socket("localhost", 8080);
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);
             InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            System.out.println();
            writer.println("getDate");
            date = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Date(Long.parseLong(date));
    }
}
