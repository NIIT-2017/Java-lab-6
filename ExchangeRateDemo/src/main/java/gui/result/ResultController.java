package gui.result;

import gui.Collector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;


public class ResultController implements Initializable {

    @FXML
    private Label result;

    @FXML
    private StackPane window;

    @FXML
    private Button ok;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Collector.resultController = this;

    }
    @FXML
    public void closeRes(ActionEvent actionEvent) {
        Stage stage = (Stage)ok.getScene().getWindow();
        stage.close();
    }
    public void setResult(String txt){
        result.setText(txt);
    }
}
