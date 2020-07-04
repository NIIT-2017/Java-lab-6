package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class Controller {

    @FXML
    private TextField tfMessage;
    @FXML
    private Button btnGetMessage;

    private Socket socket;
    private ClientObserve client;
    static final int PORT=1234;
    static final String HOST="127.0.0.1";
    Media media;


    @FXML
    private void getMessage() throws IOException {

        try{
            socket=new Socket(HOST,PORT);
        }catch (Exception ee){
            System.out.println("No connection to server!");
            return;
        }
        System.out.println("Connection to server established!");


        client=new ClientObserve(socket);

        String newMessage = client.getMessage();
        socket.close();
        tfMessage.setText(newMessage);
    }
}
