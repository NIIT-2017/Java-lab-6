package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class Controller implements Initializable{
    @FXML
    private Label lblDateTime;
    @FXML
    private Label lblMessage;
    @FXML
    private ComboBox<Integer> cbId;
    private Client client;
    private int id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComboBox();
    }

    public void setComboBox(){
        ObservableList<Integer> idList = FXCollections.observableArrayList(1,2,3);
        cbId.setItems(idList);
    }

    public void getId(){
        try {
            id = cbId.getValue();
        }
        catch (java.lang.NullPointerException e){};
        client = new Client("127.0.0.1", 1234, id);
        client.start(id);
        new Thread(()->{
            while (true){
                getTime();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while (true){
                getMessage();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }}
        }).start();
    }

    public void getTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        Platform.runLater(() -> lblDateTime.setText(time));
    }

    public void getMessage(){
        String message = client.getMessage();
        if(!message.equals("")){
            Platform.runLater(() -> lblMessage.setText(message));
        }
    }
}

