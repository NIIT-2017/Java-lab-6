package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client{
    private final String ip;
    private final int port;
    private Socket server;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        try {
            server = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWisdom(){
        BufferedReader in  = null;
        String wisdom = null;
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            wisdom = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wisdom;
    }
}