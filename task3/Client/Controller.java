package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {
    Socket server = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String fuser, fserver;
    Thread con;

    @FXML
    Label userInfo;

    @FXML
    TextArea login;

    @FXML
    TextArea toDoList;

    public void request() throws IOException {
        server = new Socket("127.0.0.1", 1234);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
        fuser = login.getText();
        out.println(fuser);
        System.out.println(fuser);
        userInfo.setText("Your login is " + fuser);
        toDoList.setText("Your tasks:\n");


        con = new Thread(() -> {
            while (true) {
                try {
                    fserver = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (fserver.equalsIgnoreCase("end"))
                    break;
                System.out.println(fserver);
                Platform.runLater(() -> toDoList.appendText(fserver + "\n"));
            }
            try {
                in.close();
                out.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        con.start();
    }
}