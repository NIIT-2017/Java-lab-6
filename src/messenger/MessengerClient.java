package messenger;

import java.io.*;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class MessengerClient extends Thread {
    private Socket clientSocket;
    private BufferedReader incomingMessage;
    private BufferedWriter outcomingMessage;
    private int id;
    private  String newMessage;

    public MessengerClient (int id) {
        this.id = id;
    }
    public void setId (int id){
        this.id = id;
    }

    public void contactToServer () throws IOException {
        contactToServer(null);
    }

    public void contactToServer (Consumer<String> callback) throws IOException {
        try {
            System.out.println("The client is started!");
            clientSocket = new Socket("localhost", 1003);
            outcomingMessage = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String sendId = Integer.toString(id);
            outcomingMessage.write(sendId);
            outcomingMessage.write("\n");
            outcomingMessage.flush();
            System.out.println("The client " + id + " is connected!");
            incomingMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            while (true) {
                newMessage = incomingMessage.readLine();
                if (newMessage != null){
                    System.out.println("We received " + newMessage);
                    if (callback != null){
                        callback.accept(newMessage);
                    }
                } else {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("The host isn`t found!");
        } catch (NoRouteToHostException e) {
            System.out.println("The server isn`t available!");
        } catch (IOException e) {
            System.out.println("The connection request is rejected!");
        }
        if(incomingMessage != null){
            incomingMessage.close();
        }
        if (clientSocket != null){
            clientSocket.close();
        }
        System.out.println("The client is disconnected!");
    }

    public static void main(String[] args) throws IOException {
        MessengerClient messengerClient = new MessengerClient(111);
        messengerClient.contactToServer();
    }
}

