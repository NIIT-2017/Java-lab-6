package messenger;

import aphorisms.AphorismClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

;


public class Controller implements Initializable {
    private AphorismClient aphorismClient = new AphorismClient();

    @FXML
    private Button show;
    @FXML
    private Button off;
    @FXML
    private Label display;
    @FXML
    private ImageView picture;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controllers is initialiaezed!");
        display.setWrapText(true);
    }

    @FXML
    private void clickShow () throws IOException {
        aphorismClient.contactToServer();
        String toSet = aphorismClient.getAphorism();
        display.setText(toSet);
    }

    @FXML
    private void clickOff() throws IOException {
        display.setText("I hope you like it! Bye!");
        aphorismClient.contactToServer();
        System.out.println("OFF");
    }
}
