package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import srvs.*;

public class Controller implements Initializable {

    private Server timeServer;
    private Server aphServer;
    private Server notServer;
    private Server infoServer;

    @FXML
    TextField tfPort_timeMod;
    @FXML
    Button btnStart_timeMod;
    @FXML
    Button btnStop_timeMod;
    @FXML
    TextArea taLog_timeMod;


    @FXML
    TextField tfPort_aphMod;
    @FXML
    Button btnStart_aphMod;
    @FXML
    Button btnStop_aphMod;
    @FXML
    TextArea taLog_aphMod;

    @FXML
    TextField tfPort_notMod;
    @FXML
    Button btnStart_notMod;
    @FXML
    Button btnStop_notMod;
    @FXML
    TextArea taLog_notMod;

    @FXML
    TextField tfPort_infoMod;
    @FXML
    Button btnStart_infoMod;
    @FXML
    Button btnStop_infoMod;
    @FXML
    TextArea taLog_infoMod;

    private Thread buttonsState;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set default ports for servers
        tfPort_timeMod.setText("1201");
        tfPort_aphMod.setText("1202");
        tfPort_notMod.setText("1203");
        tfPort_infoMod.setText("1204");

        buttonsState = new Thread(() -> {
           while (true) {
               btn_state_if_timeServer(timeServer == null || !timeServer.isAlive());
               btn_state_if_aphServer(aphServer == null || !aphServer.isAlive());
               btn_state_if_notServer(notServer == null || !notServer.isAlive());
               btn_state_if_infoServer(infoServer == null || !infoServer.isAlive());
               Thread.onSpinWait();
           }
        });
        buttonsState.setDaemon(true);
        buttonsState.start();
    }
    private void btn_state_if_timeServer(boolean flag) {
        btnStart_timeMod.setDisable(!flag);
        btnStop_timeMod.setDisable(flag);
    }
    private void btn_state_if_aphServer(boolean flag) {
        btnStart_aphMod.setDisable(!flag);
        btnStop_aphMod.setDisable(flag);
    }
    private void btn_state_if_notServer(boolean flag) {
        btnStart_notMod.setDisable(!flag);
        btnStop_notMod.setDisable(flag);
    }
    private void btn_state_if_infoServer(boolean flag) {
        btnStart_infoMod.setDisable(!flag);
        btnStop_infoMod.setDisable(flag);
    }

    //Reads port from the textField and checks it. Return read port or default port.
    private int whichPort(int port, TextField tfPort, TextArea taLogs) {
        try {
            port = Integer.parseInt(tfPort.getText());
        } catch (NumberFormatException e) {
            taLogs.appendText("WARNING: invalid format of the port field\n");
        }
        return port;
    }
    private void stopServer(Server server) {
        server.module.doLog("send stop command to the server\n");
        server.terminateFlag = true;
        server.close();
    }

    @FXML
    void onClick_startBtn_timeMod() {
        timeServer = Server.getInstance(new TimeModule(taLog_timeMod), whichPort(1201, tfPort_timeMod, taLog_timeMod)).multiThread(false);
        timeServer.start();
    }
    @FXML
    void onClick_stopBtn_timeMod() {
        stopServer(timeServer);
    }

    @FXML
    void onClick_startBtn_aphMod() {
        aphServer = Server.getInstance(new AphorismModule(taLog_aphMod), whichPort(1202, tfPort_aphMod, taLog_aphMod)).multiThread(false);
        aphServer.start();
    }
    @FXML
    void onClick_stopBtn_aphMod() {
        stopServer(aphServer);
    }

    @FXML
    void onClick_startBtn_notMod() {
        notServer = Server.getInstance(new NoticeModule(taLog_notMod), whichPort(1203, tfPort_notMod, taLog_notMod)).multiThread(true);
        notServer.start();
    }
    @FXML
    void onClick_stopBtn_notMod() {
        stopServer(notServer);
    }

    @FXML
    void onClick_startBtn_infoMod() {
        infoServer = Server.getInstance(new InfoModule(taLog_infoMod), whichPort(1204, tfPort_infoMod, taLog_infoMod)).multiThread(false);
        infoServer.start();
    }
    @FXML
    void onClick_stopBtn_infoMod() {
        stopServer(infoServer);
    }
}
