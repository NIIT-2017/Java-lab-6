package sample;

import Clients.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Client timeClient;

    Client aphClient;

    Client notClient;
    @FXML
    ListView<Client> listeners;
    ObservableList<Client> data;

    Client infoClient;


    @FXML
    TextField tfHost_timeCl;
    @FXML
    TextField tfPort_timeCl;
    @FXML
    TextArea taResp_timeCl;
    @FXML
    TextArea taLogs_timeCl;
    @FXML
    Button btnCon_timeCl;
    @FXML
    Button btnDisCon_timeCl;
    @FXML
    Button btnReq_timeCl;

    @FXML
    TextField tfHost_aphCl;
    @FXML
    TextField tfPort_aphCl;
    @FXML
    TextArea taResp_aphCl;
    @FXML
    TextArea taLogs_aphCl;
    @FXML
    Button btnCon_aphCl;
    @FXML
    Button btnDisCon_aphCl;
    @FXML
    Button btnReq_aphCl;

    @FXML
    TextField tfHost_notCl;
    @FXML
    TextField tfPort_notCl;
    @FXML
    TextField tfReq_notCl;
    @FXML
    TextArea taResp_notCl;
    @FXML
    TextArea taLogs_notCl;
    @FXML
    Button btnCon_notCl;
    @FXML
    Button btnDisCon_notCl;
    @FXML
    Button btnReq_notCl;

    @FXML
    TextField tfHost_infoCl;
    @FXML
    TextField tfPort_infoCl;
    @FXML
    TextField tfReq_infoCl;
    @FXML
    TextArea taResp_infoCl;
    @FXML
    TextArea taLogs_infoCl;
    @FXML
    Button btnCon_infoCl;
    @FXML
    Button btnDisCon_infoCl;
    @FXML
    Button btnReq_infoCl;

    Thread buttonsState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfPort_timeCl.setText("1201");
        tfHost_timeCl.setText("localhost");
        tfPort_aphCl.setText("1202");
        tfHost_aphCl.setText("localhost");
        tfPort_notCl.setText("1203");
        tfHost_notCl.setText("localhost");
        tfPort_infoCl.setText("1204");
        tfHost_infoCl.setText("localhost");

        data = FXCollections.observableArrayList();
        listeners.setItems(data);
        //show dialog of the elected client
        listeners.getSelectionModel().selectedItemProperty().addListener((observableValue, old_client, new_client) -> {
            if (data.isEmpty()) {
                taResp_notCl.setText("");
            }
            else{
                if (old_client != null)
                    old_client.setSelected(false);
                taResp_notCl.setText(new_client.getDialog());
                new_client.setSelected(true);
            }
        });

        buttonsState = new Thread(() -> {
            while (true) {
                btn_state_if_timeClient(timeClient == null || !timeClient.isAlive());
                btn_state_if_aphClient(aphClient == null || !aphClient.isAlive());
                btn_state_if_notClient(data.isEmpty());
                btn_state_if_infoClient(infoClient == null || !infoClient.isAlive());
                Thread.onSpinWait();
            }
        });
        buttonsState.setDaemon(true);
        buttonsState.start();
    }
    private void btn_state_if_timeClient(boolean flag) {
        btnCon_timeCl.setDisable(!flag);
        btnDisCon_timeCl.setDisable(flag);
        btnReq_timeCl.setDisable(flag);
    }
    private void btn_state_if_aphClient(boolean flag) {
        btnCon_aphCl.setDisable(!flag);
        btnDisCon_aphCl.setDisable(flag);
        btnReq_aphCl.setDisable(flag);
    }
    private void btn_state_if_notClient(boolean flag) {
        btnCon_notCl.setDisable(false);
        btnDisCon_notCl.setDisable(flag);
        btnReq_notCl.setDisable(flag);
    }
    private void btn_state_if_infoClient(boolean flag) {
        btnCon_infoCl.setDisable(!flag);
        btnDisCon_infoCl.setDisable(flag);
        btnReq_infoCl.setDisable(flag);
    }
    private int whichPort(int port, TextField tfPort, TextArea taLogs) {
        try {
            port = Integer.parseInt(tfPort.getText());
        } catch (NumberFormatException e) {
            taLogs.appendText("WARNING: invalid format of the port field\n");
        }
        return port;
    }
    private void runClient(Client client) {
        if (client.isConnected())
            client.start();
    }

    @FXML
    void OnClick_btnCon_timeCl() {
        timeClient = Client.getInstance(tfHost_timeCl.getText(),
                whichPort(1201, tfPort_timeCl, taLogs_timeCl), new TimeClient(taResp_timeCl, taLogs_timeCl));
        runClient(timeClient);

    }
    @FXML
    void OnCLick_btnDisCon_timeCl() {
        timeClient.disconnect();
    }
    @FXML
    void OnClick_btnReq_timeCl() {
        timeClient.sendRequest();
    }

    @FXML
    void OnClick_btnCon_aphCl() {
        aphClient = Client.getInstance(tfHost_aphCl.getText(),
                whichPort(1202, tfPort_aphCl, taLogs_aphCl), new AphorismClient(taResp_aphCl, taLogs_aphCl));
        runClient(aphClient);
    }
    @FXML
    void OnCLick_btnDisCon_aphCl() {
        aphClient.disconnect();
    }
    @FXML
    void OnClick_btnReq_aphCl() {
        aphClient.sendRequest();
    }

    @FXML
    void OnClick_btnCon_notCl() {
        notClient = Client.getInstance(tfHost_notCl.getText(),
                whichPort(1203, tfPort_notCl, taLogs_notCl), new NoticeModule(taResp_notCl, taLogs_notCl, tfReq_notCl));
        if (notClient.isConnected()){
            data.add(notClient);
            notClient.start();
            if (notClient.isAlive()) {
                listeners.getSelectionModel().select(notClient);
            }
        }

    }
    @FXML
    void OnCLick_btnDisCon_notCl() {
        Client client = listeners.getSelectionModel().getSelectedItem();
        data.remove(client);
        client.disconnect();
    }
    @FXML
    void OnClick_btnReq_notCl() {
        listeners.getSelectionModel().getSelectedItem().sendRequest();
    }

    @FXML
    void OnClick_btnCon_infoCl() {

        infoClient = Client.getInstance(tfHost_infoCl.getText(),
                whichPort(1204, tfPort_infoCl, taLogs_infoCl), new InfoClient(taResp_infoCl, taLogs_infoCl, tfReq_infoCl));
        runClient(infoClient);
    }
    @FXML
    void OnCLick_btnDisCon_infoCl() {
        infoClient.disconnect();
    }
    @FXML
    void OnClick_btnReq_infoCl() {
        infoClient.sendRequest();
    }
}
