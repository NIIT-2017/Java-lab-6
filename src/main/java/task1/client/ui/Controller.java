package task1.client.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import task1.client.Client;

public class Controller {

    @FXML
    private DigitalClock digitalClock;

    @FXML
    private Button button;

    private Client client = new Client();


    public void click() {
        digitalClock.setTime(client.getDateFromServer());
    }
}
