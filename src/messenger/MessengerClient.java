package messenger;

import java.io.*;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MessengerClient {
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader incomingMessage;
    private BufferedWriter outcomingMessage;
    private int id;
    private String time;
    private ArrayList<String> times;
    private ArrayList<String> messages;
    private String newMessage;

    public MessengerClient (int id) {
        this.id = id;
    }

    public int getId (int id) {
        return id;
    }

    public ArrayList<String> getTimes () {
        return times;
    }

    public void setTime (String time) {
        this.time=time;
    }

    public void setNewMessage (String newMessage) {
        this.newMessage = newMessage;
    }

    public void addTime (String time) {
        times.add(time);
    }

    public void addNewMessageToMessages (String newMessage) {
        messages.add(newMessage);
    }


    public void contactToServer () throws IOException {
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
            newMessage = incomingMessage.readLine();
            System.out.println("We received " + newMessage);
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

    public String getNewMessage() {
        return newMessage;
    }

    public static void main(String[] args) throws IOException {
        MessengerClient messengerClient = new MessengerClient(111);
        messengerClient.contactToServer();
    }
}

