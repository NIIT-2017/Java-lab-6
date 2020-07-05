package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.ConnectIOException;

public class Client{
    private final String ip;
    private final int port;
    private Socket server;
    private String error=null;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        try {
            server = new Socket(ip, port);
        } catch (RuntimeException e) {
            error="Server not found";
        } catch (IOException e){
            error="Server not found";
        }
    }

    public String getAphorism(){
        BufferedReader in  = null;
        String aphorism = null;
        if (error != null){
            return error;
        }
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("aphorism");
            aphorism = in.readLine();
        } catch (ConnectIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aphorism;
    }
    public String getDate(){
        BufferedReader in  = null;
        String stringDate = null;
        if (error != null){
            return error;
        }
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("time");
            stringDate = in.readLine();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return stringDate;
    }
    public String sentMessage(String id){
        BufferedReader in  = null;
        String stringMessage = null;
        if (error != null){
            return error;
        }
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF(id);
            stringMessage = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringMessage;
    }
}