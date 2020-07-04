package client;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class ClientController {
    @FXML
    private Button btnConnect;
    @FXML
    private Button btnExit;
    @FXML
    private TextArea taDisplay;
    @FXML
    private TextField tfServerIP;
    @FXML
    private TextField tfState;

    Socket clientSocket = null;

    public void setBtnExit(){
        try {
            PrintWriter pwOut = new PrintWriter(this.clientSocket.getOutputStream(), true);
            pwOut.println("exit");
            System.exit(0);
        }
        catch (NullPointerException e){
            tfState.setText("Nothing for disconnect");
        }
        catch (IOException e){
            tfState.setText("Disconnection error");
        }
    }

    public void getConnect() throws IOException {
        String sServerIP = tfServerIP.getText();
        tfState.setText("Connecting with "+sServerIP+"...");
        try{
            this.clientSocket = new Socket(sServerIP,2020);
            tfState.setText("Connection with "+sServerIP+" success");
        }
        catch (UnknownHostException e){
            tfState.setText("Connection failed: Unknown host");
        }
        catch (NoRouteToHostException e){
            tfState.setText("Connection failed: No connection");
        }
        catch (ConnectException e){
            tfState.setText("Connection failed: Connection error");
        }
        catch (IOException e){
            tfState.setText("Connection failed: Input/output error");
        }
        ClientProcess process = new ClientProcess(this.clientSocket);
        Thread thread = new Thread(process);
        thread.setDaemon(true);
        thread.start();
        taDisplay.textProperty().bind(process.messageProperty());
    }
}
