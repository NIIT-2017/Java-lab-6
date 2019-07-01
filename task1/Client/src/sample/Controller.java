package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Controller {
    @FXML
    Label time1;

    @FXML
    public void reguest() throws IOException {
        Socket socketServer = new Socket("127.0.0.1", 5600);
        BufferedReader input = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
        String inputString = input.readLine();
        System.out.println(inputString);
        time1.setText(inputString);
        input.close();
        socketServer.close();

    }
}
