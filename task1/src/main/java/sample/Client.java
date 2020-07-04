package sample;

import java.io.*;
import java.net.*;

public class Client {
    private String message;
    private Socket clientSocket;

    public String getMessage() {
        return message;
    }

    public void startClient(){
        try {
            clientSocket = new Socket("127.0.0.1", 8000);
            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            message = reader.readLine();
            reader.close();
            clientSocket.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}