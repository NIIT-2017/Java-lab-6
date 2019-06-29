package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {
    boolean flag = true;
    Thread con;

    Socket server = null;
    BufferedReader in = null;
    PrintWriter out = null;
    String fuser, fserver;

    {
        try {
            server = new Socket("127.0.0.1", 1234);
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            out = new PrintWriter(server.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    TextArea dateAndTime;

    @FXML
    public void connection() {

        con = new Thread(() -> {
            while (flag) {
                fuser = "time";
                out.println(fuser);
                try {
                    dateAndTime.clear();
                    fserver = in.readLine();
                    System.out.println(fserver);
                    Platform.runLater(() -> dateAndTime.appendText(fserver));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        con.start();
    }

    @FXML
    public void exit() throws IOException {
        flag = false;
        fuser = "exit";
        out.println(fuser);

        in.close();
        out.close();
        server.close();
        Platform.exit();
    }

}
