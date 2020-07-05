package ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private String host;
    private int port;
    private Socket server = null;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connectClient (){
        try {
            server = new Socket(host,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getTime() throws IOException {
        BufferedReader in  = new BufferedReader(new InputStreamReader(server.getInputStream()));
        String time;
        time= in.readLine();
        return time;
    }
}
