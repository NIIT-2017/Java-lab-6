package timeserver;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;


public class Controller {
    Socket server = null;
    String address = "localhost";
    String fserver;

    @FXML
    TextField tf;

    @FXML
    Button ConnectionButton;

    public void connect()throws IOException {

        System.out.println("Start client");

        System.out.println("Connecting to server " + address);
        server = new Socket(address, 8888);
        BufferedReader in  = new BufferedReader(
                new  InputStreamReader(server.getInputStream()));

        tf.clear();
        fserver = in.readLine();
        System.out.println(fserver);
        tf.appendText(fserver);

        in.close();
        server.close();
    }
}
