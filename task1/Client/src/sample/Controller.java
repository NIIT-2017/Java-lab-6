package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Button btnTime;

    @FXML
    TextField tfShowTime;

    @FXML
    TextArea taInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnTime.setOnAction(event -> {
            Socket server = null;

            try {
                taInfo.setText("Соединение с сервером...");
                server = new Socket("127.0.0.1",1234);
                BufferedReader in  = new BufferedReader(new InputStreamReader(server.getInputStream()));
                taInfo.appendText("\nСоединение с сервером установлено.");
                String time = in.readLine();
                if (time != null){
                    tfShowTime.setText(time);
                }
                taInfo.appendText("\nИнформация о текущем времени получена.");
                in.close();
                server.close();
            } catch (IOException e) {
                taInfo.appendText("\nОшибка соединения с сервером.");
            }
        });
    }
}
