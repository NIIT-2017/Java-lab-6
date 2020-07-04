package server;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController{

    @FXML
    private TextField tfState;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;

    public void setBtnStart(){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(2020);
        }
        catch (IOException e){
            tfState.setText("Connection error with port 2020");
        }
        ServerProcess process = new ServerProcess(serverSocket, clientSocket);
        Thread thread = new Thread(process);
        thread.setDaemon(true);
        thread.start();
        tfState.textProperty().bind(process.messageProperty());
    }

    public void setBtnStop(){
        System.exit(0);
    }
}
