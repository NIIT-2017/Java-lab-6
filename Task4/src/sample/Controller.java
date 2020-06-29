package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Client client;
    @FXML
    Label lblName;
    @FXML
    Label lblOwner;
    @FXML
    Label lblDate;
    @FXML
    Label lblTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client();
        client.connect();
        client.getResponse();
        lblName.setText(client.getName());
        lblOwner.setText(client.getOwner());
        lblDate.setText(client.getDate());
        lblTime.setText(client.getTime());
    }
}
