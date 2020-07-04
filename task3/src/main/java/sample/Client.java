package sample;
import java.io.*;
import java.net.Socket;

public class Client{
    private final String ip;
    private final int port;
    private Socket server;
    BufferedReader in;
    PrintWriter out;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start(int id) {
        try {
            server = new Socket(ip, port);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
            out.println(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRemind(){
        String remind="";
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            remind = in.readLine();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return remind;
    }


}