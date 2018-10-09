package gui.about;

import gui.Collector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable{

    @FXML
    public Label text;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collector.aboutController = this;
        text.setStyle("-fx-font-size: 14; -fx-font-style: italic");
    }
}
