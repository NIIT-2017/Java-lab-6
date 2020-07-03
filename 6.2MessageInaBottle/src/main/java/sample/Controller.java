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
            System.out.println("Не удается соединиться с сервером!");
            return;
        }
        System.out.println("Соединение с сервером установлено!");


        client=new ClientObserve(socket);

        String newMessage = client.getMessage();
        socket.close();

        URL resource = sample.Controller.class.getResource("/1.mp3");
        media = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        tfMessage.setText(newMessage);
    }
}
