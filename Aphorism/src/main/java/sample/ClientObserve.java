package sample;

import java.io.*;
import java.net.Socket;

public class ClientObserve {

    private Socket server;
    private BufferedReader in;

    public ClientObserve(Socket socket){
        this.server =socket;
        try{
            in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException ex){
            System.out.println("Thread initialization error");
        }
    }


    public String getMessage(){
        String message = null;
        try{
                message=in.readLine();
                System.out.println(message);

        }catch (IOException ex){
            System.out.println("data transfer error");
        }

        return message;
    }

}
