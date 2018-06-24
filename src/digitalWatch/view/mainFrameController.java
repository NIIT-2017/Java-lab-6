package digitalWatch.view;


import client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalTime;


public class mainFrameController {
    private Client client;
    @FXML
    Label hourLabel;
    @FXML
    Label minuteLabel;
    @FXML
    Label secondLabel;


    @FXML
    void initialize() {

        Thread thread = new Thread(() -> {
            while (true) {
                client = new Client("localhost", 2000);
                LocalTime time = client.receiveTime();
                Platform.runLater(() -> {
                    hourLabel.setText(addZero(String.valueOf(time.getHour())));
                    minuteLabel.setText(addZero(String.valueOf(time.getMinute())));
                    secondLabel.setText(addZero(String.valueOf(time.getSecond())));
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private String addZero(String str) {
        if (str.length()==1){
            str = "0".concat(str);
        }
        return str;
    }
}
