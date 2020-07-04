package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {
    Client client;

    @FXML
    private TextField tfTime;

    @FXML
    private Button btnConnect;

    private Timer timer;
    private TimerTask task;

    @FXML
    public void onClickConnect() {
        btnConnect.setDisable(true);
        timer = new Timer();
        task = new TimerTask() {
            public void run() {
                Platform.runLater(()->{
                    client.startClient();
                    tfTime.setText(client.getMessage());
                });

            }
        };
        timer.schedule(task, 0, 1000);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client();
    }
}