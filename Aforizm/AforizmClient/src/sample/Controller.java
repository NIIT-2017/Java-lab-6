package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller{
    @FXML
    Button btnConnect;
    @FXML
    Label aforizm;
    private Client client;


    @FXML
    public void connect(){
        client = new Client("127.0.0.1", 1234);
        client.start();
        new Thread(()->{
            while (true){
                getAforizm();}
        }).start();

    }

    public void getAforizm(){
        String aforizm = client.getAforizm();
        Platform.runLater(() -> this.aforizm.setText(aforizm));
    }
}