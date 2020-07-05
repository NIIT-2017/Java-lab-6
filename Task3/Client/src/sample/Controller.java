package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {
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
        tvMessages.setItems(myClient.getClientMessages());
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
