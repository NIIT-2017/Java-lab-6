package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientElectricClock {
    private  Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader incomingMessage;
    private String actualTime;


    public void contactToServer () throws IOException {
        try {
            System.out.println("The client is started!");
            clientSocket = new Socket("localhost", 1111);
            reader = new BufferedReader(new InputStreamReader(System.in));
            incomingMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            actualTime = incomingMessage.readLine();
            System.out.println("We received " + actualTime);
        } catch (UnknownHostException e) {
            System.out.println("The host isn`t found!");
        } catch (NoRouteToHostException e) {
            System.out.println("The server isn`t available!");
        } catch (IOException e) {
            System.out.println("The connection request is rejected!");
        }
        reader.close();
        incomingMessage.close();
        clientSocket.close();
        System.out.println("The client is disconnected!");
    }

    public String getActualTime() {
        return actualTime;
    }

    public static void main(String[] args) throws IOException {
        ClientElectricClock clientElectricClock = new ClientElectricClock();
        clientElectricClock.contactToServer();
    }
}
