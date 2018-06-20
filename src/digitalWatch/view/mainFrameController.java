package digitalWatch.view;


import digitalWatch.model.ClientTime;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalTime;


public class mainFrameController {
    private ClientTime clientTime;
    @FXML
    Label hourLabel;
    @FXML
    Label minuteLabel;
    @FXML
    Label secondLabel;


    @FXML
    void initialize() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    clientTime = new ClientTime("localhost", 2000);
                    LocalTime time = clientTime.reciveTime();
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
