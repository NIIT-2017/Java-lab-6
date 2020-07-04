package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Client client;

    @FXML
    private TextArea taAphorism;

    @FXML
    public void onClickGet() throws IOException {
        client.startClient("get");
        taAphorism.setText(client.getAphorism());
    }

    @FXML
    public void onClickExit() throws IOException {
        client.startClient("exit");
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            client = new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}