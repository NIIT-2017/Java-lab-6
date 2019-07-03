package sample;

import java.io.*;
import java.net.Socket;

public class Client {
    Socket clientSocket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String input, output;

    public void clientStart() throws IOException {
        System.out.println("client is started");
        clientSocket = new Socket("localhost", 5555);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);
    }
    public String clientRequest() throws IOException {
        System.out.println("cend request");
        output = "request";
        out.println(output);
        input = in.readLine();
        System.out.println("server answer is: " + input);
        return input;
    }
    public void clientStop() throws IOException {
        System.out.println("client is stopped");
        in.close();
        out.close();
        clientSocket.close();
        clientSocket = null;
        in = null;
        out = null;
    }
}
