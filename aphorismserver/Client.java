package aphorismserver;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Start client");

        Socket server = null;
        String address = "localhost";
        String fserver;

        System.out.println("Connecting to server " + address);
        server = new Socket(address, 1515);
        BufferedReader in  = new BufferedReader(
                new InputStreamReader(server.getInputStream()));

        fserver = in.readLine();
        System.out.println(fserver);

        in.close();
        server.close();
    }
}

