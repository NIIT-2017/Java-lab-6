package sample;

import ClientServer.Client;
import ClientServer.Server;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

    @FXML
    private Button btConnect;
    @FXML
    private TextField tfIP;
    @FXML
    private TextField tfPort;
    @FXML
    private Label lbTime;
    @FXML
    private Button btDisConnect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfIP.setText("127.0.0.1");
        tfPort.setText("1235");
    }
    private Client client = null;
    private Timer t;
    private TimerTask task;
    private Timer t1;
    private TimerTask task1;

    private Server server = null;

    @FXML
    public void connect(){
        new Thread(()->{
            server = new Server(Integer.parseInt(tfPort.getText()));
            try {
                server.createServer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        t=new Timer();
        task = new TimerTask() {
            @Override public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        client = new Client (tfIP.getText(),Integer.parseInt(tfPort.getText()));
                        client.connectClient();
                    }
                });
            }
        };
        t.schedule(task,200);
        t1=new Timer();
        task1 = new TimerTask() {
            @Override public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            lbTime.setText(client.getTime());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        t1.schedule(task1,300,1000);
    }

    @FXML
    public void disconnect() throws IOException {
        task.cancel();
        task1.cancel();
        server.disconnect();
        lbTime.setText("Disconnect");
    }
}
