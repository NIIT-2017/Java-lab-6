package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class Controller {
    Client myClient = new Client("127.0.0.1");

    @FXML
    private TextArea taPhrase;

    @FXML
    public void onClickGetPhrase(){
        //calling go method to get phrase from server
        myClient.goClient("get");
        taPhrase.setText(myClient.getPhrase());
    }

    @FXML
    public void onClickExit(){
        //calling go method to close server
        myClient.goClient("exit");
        System.out.println("Клиент нажал кнопку выход, программа закрывается");
        System.exit(0);
    }
}
