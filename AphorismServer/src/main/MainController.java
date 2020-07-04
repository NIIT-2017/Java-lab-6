package main;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import server.Server;

public class MainController {
    @FXML
    private Button btnServer;
    @FXML
    private Button btnClient;

    public void setBtnServer(){
        Server server = new Server();
        Stage stage = new Stage();
        try {
            server.start(stage);
        }
        catch (Exception e){}
    }

    public void setBtnClient(){
        Client client = new Client();
        Stage stage = new Stage();
        try {
            client.start(stage);
        }
        catch (Exception e){}
    }
}
