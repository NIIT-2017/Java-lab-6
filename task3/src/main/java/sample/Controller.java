package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private Label lbTime;
    @FXML
    private Label lblRemind;
    @FXML
    private Label lbId;
    private Client client;
    private int id;
    private DateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lbId.setStyle("-fx-background-color: OLDLACE");
        lbTime.setStyle("-fx-background-color: OLDLACE");
        lblRemind.setStyle("-fx-background-color: WHITE");

        id = new Random().nextInt(3) + 1;
        //id = 1;
        lbId.setText("Ваш ID равен " + id);
        client = new Client("127.0.0.1", 8000);
        client.start(id);

        new Thread(()->{
            while (true){
                Date date = new Date();
                String time = formatter.format(date);
                Platform.runLater(() -> lbTime.setText(time));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(()->{
            while (true){
                String temp = client.getRemind();
                if(!temp.isEmpty()){
                    Platform.runLater(() -> lblRemind.setText(temp));
                }
            }
        }).start();
    }

}
