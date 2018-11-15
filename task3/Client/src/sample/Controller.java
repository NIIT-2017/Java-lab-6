package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Button btnConnect;

    @FXML
    TextArea taInfo;

    @FXML
    ComboBox<String> cbLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList("client1", "client2", "client3", "client4");

        cbLogin.setItems(list);

        taInfo.setEditable(false);

        btnConnect.setOnAction(event -> new Connect(taInfo, cbLogin, btnConnect));
    }
}
