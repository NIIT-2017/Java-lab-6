package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class Controller {

    Socket server = null;
    BufferedReader reader = null;

    @FXML
    Label myLabel;

    @FXML
    public void request() throws IOException, InterruptedException {

        this.server = new Socket("127.0.0.1", 1234);
        reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
        String s = reader.readLine();
        System.out.println(s);
        myLabel.setWrapText(true);
        myLabel.setText(s);
    }

    @FXML
    public void newString() throws IOException {
        String s = reader.readLine();
        if(s == null || s.length() == 0) {
            myLabel.setText("Афоризмы кончились");
            reader.close();
        } else {
            System.out.println(s);
            myLabel.setWrapText(true);
            myLabel.setText(s);
        }

    }

}
