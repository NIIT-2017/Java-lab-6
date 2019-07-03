package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Controller {
   Client client =new Client();
   @FXML
   private Button btnGetData;
   @FXML
   private TextField tfData;

    public void onAction_btnGetData (ActionEvent actionEvent){
        String input=null;
        try {
            client.clientStart();
            input=client.clientRequest();
            client.clientStop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tfData.setText(input);
    }
}
