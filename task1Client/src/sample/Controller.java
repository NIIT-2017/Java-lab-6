package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    Client myClient;

    @FXML
    private void initialize() {
        myClient = new Client();
    }

    @FXML
    private TextField tfTime;

    @FXML
    private TextField tfDate;


    @FXML
    private Button btnStart;


    private Timer t;
    private TimerTask task;

    @FXML
    public void onClickGetTime() {
        btnStart.setDisable(true);
        t = new Timer();
        task = new TimerTask() {
            public void run() {
                // еще одна обертка нужна для корректного обновления формы
                Platform.runLater(new Runnable() {
                    public void run() {
                        myClient.goClient("127.0.0.1");
                        tfTime.setText(myClient.getTime());
                        tfDate.setText(myClient.getDate());
                    }
                });
            }
        };
        t.schedule(task, 0, 1000);
    }

}
