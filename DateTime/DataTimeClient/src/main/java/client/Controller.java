package client;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;


public class Controller implements Initializable
{
    @FXML
    private BorderPane pane;

    @FXML
    private Label time;

    @FXML
    private Label date;

    private Socket socket;
    private boolean flag = true;
    private DataInputStream input;

    public Controller() throws IOException {
    }

    private void runTask(){
        Task taskTime = new Task() {
            @Override
            protected Void call() throws Exception {
                while (flag){
                    String[] date_time = input.readUTF().split("t");
                    updateMessage(date_time[0]);
                    date.setText(date_time[1]);
                }
                return null;
            }
        };

        Thread t = new Thread(taskTime);
        t.setDaemon(true);
        t.start();
        time.textProperty().bind(taskTime.messageProperty());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        pane.setStyle("-fx-background-color: bisque");
        time.setStyle("-fx-font-size: 40");
        date.setStyle("-fx-font-style: italic; -fx-font-size: 15");

        try {
            socket = new Socket("localhost",1234);
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        runTask();
    }
}
