package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;

import java.io.IOException;

public class Controller {
    @FXML
    private Button btnGetDictum;
    @FXML
    private Button btnStartConection;
    @FXML
    private TextField tfData;
    @FXML
    private TextArea taDictum;

    Client client =new Client();
    boolean conection = false;

    public void onAction_btnStartConection (ActionEvent actionEvent){
     try {
      if (conection == false){
       client.clientStart();
       btnGetDictum.setDisable(false);
      }
      else{
       btnGetDictum.setDisable(true);
       client.clientStop();
      }
     } catch (IOException e) {
      e.printStackTrace();
     }
     conection=!conection;
     tfData.setText("Hello!");
    }
    public void onAction_btnGetDictum (ActionEvent actionEvent){
        JSONObject inputDictumJO = new JSONObject();
        String dictum=null;
        String author=null;
       try {
        inputDictumJO= client.clientRequest();
       } catch (IOException e) {
        e.printStackTrace();
       }
       dictum = (String) inputDictumJO.get("dictum");
       author = (String) inputDictumJO.get("author");

        tfData.setText(author);
        taDictum.setText(dictum);
    }
}
