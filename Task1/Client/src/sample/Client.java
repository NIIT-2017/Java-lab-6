package sample;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTime(){
        BufferedReader in  = null;
        String time = null;
            try {
                in = new BufferedReader(new InputStreamReader(server.getInputStream()));
                time = in.readLine();
                System.out.println("!!!" + time);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return time;
    }
}