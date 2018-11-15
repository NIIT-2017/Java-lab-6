package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Button btnShow;

    @FXML
    TextArea taInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnShow.setOnAction(event -> {
            Socket server = null;

            try {
                server = new Socket("127.0.0.1",1235);
                BufferedReader in  = new BufferedReader(new InputStreamReader(server.getInputStream()));
                String message = in.readLine();
                if (message != null){
                    taInfo.setText(message);
                }
                in.close();
                server.close();
            } catch (IOException e) {
                taInfo.setText("Ошибка соединения с сервером.");
            }
        });
    }
}
