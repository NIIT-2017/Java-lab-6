package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {

    @FXML
    private Label lbInfo;

    @FXML
    private Button btConnect;

    @FXML
    private Button btDisconnect;

    Socket server = null;
    BufferedReader in = null;
    PrintWriter out = null;
    BufferedReader inu = null;
    String fuser = "ok";
    String fserver;

    @FXML
    void initialize() {
        btConnect.setOnAction(event-> startClient());
        btDisconnect.setOnAction(event-> stopClient());

    }

    void startClient() {
        System.out.println("Client started");

            try {
                server = new Socket("localhost", 1234);
                in = new BufferedReader(
                        new InputStreamReader(server.getInputStream()));
                out =
                        new PrintWriter(server.getOutputStream(), true);
                inu =
                        new BufferedReader(new InputStreamReader(System.in));
                out.println(fuser);
                fserver = in.readLine();
                System.out.println(fserver);
                lbInfo.setText(fserver);
                out.close();
                in.close();
                inu.close();
                server.close();
                server = null;


            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    void stopClient(){
        try {
            server = new Socket("localhost", 1234);
            out =
                    new PrintWriter(server.getOutputStream(), true);
            out.println("exit");
            out.close();
            server.close();
            server = null;


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Platform.exit();
    }

}

