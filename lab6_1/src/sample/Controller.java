package sample;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class Controller implements Initializable{


    @FXML
    private Label lbl_Date;
    @FXML
    private Label lbl_Time;
    @FXML
    private Button btn_Update;
    PublicServerTime p = new PublicServerTime();
    public void initialize(URL location, ResourceBundle resources) {
        DigitalClock();

        btn_Update.setOnAction(e -> {

            DigitalClock();

        });
    }

    public void DigitalClock(){

        List<String> list = new ArrayList<>();

        String s=String.valueOf(p.getNTPDate());
        String[] parts = s.split(" ");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] range = part.split(":");
                for (String i: range)
                    list.add(i);
            }
            else {
                String value = part;
                list.add(value);

            }
        }
        lbl_Date.setText(list.get(2)+" "+list.get(1)+" "+ list.get(7));
        lbl_Time.setText(list.get(3)+" : "+list.get(4) + " : " + list.get(5));

    }
}