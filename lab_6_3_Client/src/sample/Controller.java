package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Controller extends Thread {

    @FXML
    Label ID;

    @FXML
    TableView<Message> tvMessage;

    @FXML
    TableColumn<Message, String> tcTime;

    @FXML
    TableColumn<Message, String> tcMessage;

    private ObservableList<Message> messages = FXCollections.observableArrayList();
    private String id;
    Socket server;
    BufferedReader in;

    @FXML
    private void initialize() {
        tcTime.setCellValueFactory(new PropertyValueFactory<Message, String>("timeOfMessage"));
        tcMessage.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
        tvMessage.setItems(messages);
    }

    @FXML
    public void request() throws IOException {
        if (id != null)
            return;
        server = new Socket("127.0.0.1", 1234);
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        String fserver = in.readLine();
        while (!("New".equals(fserver.substring(0, 3)))) {
            fserver = in.readLine();
        }
        id = fserver.substring(3);
        ID.setText("ID: "+id);
        start();
    }

    public void run() {

        while(true){
            try{
                Thread.sleep(100);
                int lengthOfId=id.length();
                String fserver=in.readLine();
                if (fserver.substring(0,lengthOfId).equals(id)){
                    String messageForTable=fserver.substring(lengthOfId);
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String timeOfMessage= sdf.format(cal.getTime()).toString();
                    System.out.println(timeOfMessage);
                    System.out.println(messageForTable);
                    messages.add(new Message(timeOfMessage,messageForTable));
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}