package aphorisms;

import java.io.*;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class AphorismClient {
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader incomingMessage;
    private BufferedWriter outcomingMessage;
    private String aphorism;


    public void contactToServer () throws IOException {
        try {
            System.out.println("The client is started!");
            clientSocket = new Socket("localhost", 3333);
            outcomingMessage = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            outcomingMessage.write("Hi, server!\n");
            outcomingMessage.flush();
            incomingMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            aphorism = incomingMessage.readLine();
            System.out.println("We received " + aphorism);
        } catch (UnknownHostException e) {
            System.out.println("The host isn`t found!");
        } catch (NoRouteToHostException e) {
            System.out.println("The server isn`t available!");
        } catch (IOException e) {
            System.out.println("The connection request is rejected!");
        }
        incomingMessage.close();
        clientSocket.close();
        System.out.println("The client is disconnected!");
    }

    public String getAphorism() {
        return aphorism;
    }

    public static void main(String[] args) throws IOException {
        AphorismClient aphorismClient = new AphorismClient();
        aphorismClient.contactToServer();
    }
}
