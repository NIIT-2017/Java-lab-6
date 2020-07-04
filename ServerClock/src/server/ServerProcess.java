package server;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerProcess extends Task<Void> {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public ServerProcess(ServerSocket serverSocket, Socket clientSocket) {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
    }

    @Override
    public Void call(){
        giveTime();
        return null;
    }

    private String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(new Date());
    }

    private void giveTime() {
        BufferedReader brIn = null;
        PrintWriter pwOut = null;
        String sInput;
        updateMessage("Waiting connection");
        try {
            clientSocket = serverSocket.accept();
            updateMessage("Client has connected");
            brIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pwOut = new PrintWriter(clientSocket.getOutputStream(), true);
            while ((sInput = brIn.readLine()) != null) {
                if (sInput.equals("exit")) {
                    updateMessage("Client disconnected. Server stopped");
                    brIn.close();
                    pwOut.close();
                    serverSocket.close();
                    clientSocket.close();
                    break;
                } else if (sInput.equals("time")) {
                    pwOut.println(getNowTime());
                }
            }
        }
        catch (IOException e){
            updateMessage("Can't connect");
        }
    }
}
