package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller{
    @FXML
    Button btnConnect;
    @FXML
    Label wisdom;
    private Client client;


    @FXML
    public void connect(){
        client = new Client("127.0.0.1", 1234);
        client.start();
        new Thread(()->{
            while (true){
                getWisdom();}
        }).start();

    }

    public void getWisdom(){
        String wisdom = client.getWisdom();
        Platform.runLater(() -> this.wisdom.setText(wisdom));
    }
}
