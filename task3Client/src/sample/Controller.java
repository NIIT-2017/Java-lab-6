package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {
    // int id;
    //Socket socket;
    //private ObservableList<ClientMessage> clientMessages = FXCollections.observableArrayList();
    //private BufferedReader in; // stream for getting messages from server
    //private PrintWriter out;  //stream for initial sending id to server
    Client myClient;

    @FXML
    Label lblId;

    @FXML
    Button btnStart;

    @FXML
    TableView<ClientMessage> tvMessages;

    @FXML
    TableColumn<ClientMessage, String> tcTime;

    @FXML
    TableColumn<ClientMessage, String> tcMessage;

    @FXML
    private void initialize() {
        myClient = new Client(101);
        tcTime.setCellValueFactory(new PropertyValueFactory<ClientMessage, String>("time"));
        tcMessage.setCellValueFactory(new PropertyValueFactory<ClientMessage, String>("mess"));
        //adding messages from observable list to the table
        tvMessages.setItems(myClient.getClientMessages());
        //id = 101;
        lblId.setText("Ваш Id: " + myClient.getId());
    }


    @FXML
    public void onClickStart() {
        btnStart.setDisable(true);
        myClient.setConnectionClient("127.0.0.1");
    }

    @FXML
    public void onClickExit() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                myClient.closeClient();
            }
        });
        t.start();
    }
}
