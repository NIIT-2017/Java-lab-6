package ExchangeClient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

;

public class Controller implements Initializable {
   ExchangeClient exchangeClient = new ExchangeClient();

    @FXML
    private Button show;
    @FXML
    private Button off;
    @FXML
    private Label display;
    @FXML
    private TextField field;
    @FXML
    private ImageView picture;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controllers is initialiaezed!");
        display.setWrapText(true);
    }

    @FXML
    private void clickShow () throws IOException {
        Timer t1 = new Timer();
        t1.schedule(new ExchangeClientTimerTask() {
            @Override
            public void run() {
                System.out.println("ExchangeClientTimerTask is started" + new Date());
                try {
                    exchangeClient.contactToServer();
                    ArrayList<String> exchangeRates = exchangeClient.getExchangeRates();
                    for (int i = 0; i<exchangeRates.size(); i++) {
                        String toSet = exchangeRates.get(i);
                        Platform.runLater(()-> display.setText(toSet));
                        Thread.sleep(2500);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0);

    }

    class ExchangeClientTimerTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("ExchangeClientTimerTask is started" + new Date());
            completeTask();
            System.out.println("ExchangeClientTimerTask is finished" + new Date());

        }
        private void completeTask() {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clickOff() throws IOException {
        display.setText("I hope you like it! Bye!");
    }
}
