package messenger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button show;
    @FXML
    private Button off;
    @FXML
    private Label display;
    @FXML
    private Label ID;
    @FXML
    private TextField id;
    @FXML
    private ImageView picture;

    MessengerClient messengerClient = new MessengerClient(111);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controllers is initialiaezed!");
        display.setWrapText(true);
        id.setEditable(true);

    }

    @FXML
    private void getMessage () throws IOException {
        messengerClient.setId(Integer.parseInt(id.getText()));
        id.setDisable(true);
        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    messengerClient.contactToServer((String stringToSet) -> {
                        Platform.runLater(()-> display.setText(stringToSet));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }

    @FXML
    private void clickOff() throws IOException {
        display.setText("I hope you like it! Bye!");
        System.out.println("OFF");
        id.setDisable(false);
        id.setEditable(true);
    }
}
