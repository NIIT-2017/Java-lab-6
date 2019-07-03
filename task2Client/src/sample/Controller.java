package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Controller {
    Client myClient = new Client();
    @FXML
    private TextArea taPhrase;

    @FXML
    public void onClickGetPhrase(){
        //calling go method to create new socket with server
        myClient.goClient("127.0.0.1");
        taPhrase.setText(myClient.getPhrase());
    }
}
