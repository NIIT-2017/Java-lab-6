package electricClock;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerElectricClock {
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static BufferedWriter outgoingMessage;

    public void receiveClient() throws IOException {
        try {
            serverSocket = new ServerSocket(1111);
            System.out.println("The server is started!");
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Connection is accepted from: " + clientSocket.getRemoteSocketAddress());
                try {
                    outgoingMessage = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
                    Date actualDateAndTime = new Date();
                    outgoingMessage.write(String.valueOf(dateFormat.format(actualDateAndTime)));
                    outgoingMessage.flush();
                } finally {
                    outgoingMessage.close();
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerElectricClock serverElectricClock = new ServerElectricClock();
        serverElectricClock.receiveClient();
    }
}
