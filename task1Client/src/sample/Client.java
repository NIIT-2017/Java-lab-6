package sample;

import java.io.*;
import java.net.*;

public class Client {
    private String time;
    private String date;
    private Socket server;
    private BufferedReader reader;

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void goClient(String ip){
        try {
            server = new Socket(ip, 1559);
            InputStreamReader inputStreamReader = new InputStreamReader(server.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            time = reader.readLine();
            date = reader.readLine();
            reader.close();
            server.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

