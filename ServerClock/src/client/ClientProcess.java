package client;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess extends Task<Void>{

    private Socket clientSocket;

    public ClientProcess(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public Void call() {
        getTime();
        return null;
    }

    public void getTime(){
        BufferedReader brIn = null;
        PrintWriter pwOut = null;
        String sInput=null;
        try{
            brIn = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            pwOut = new PrintWriter(this.clientSocket.getOutputStream(),true);
        }
        catch (IOException e) {}

        while (true){
            pwOut.println("time");
            try {
                sInput = brIn.readLine();
                updateMessage(sInput);
            }
            catch (IOException e){}
            try {Thread.sleep(1000);}
            catch (InterruptedException e){}
        }
    }
}
