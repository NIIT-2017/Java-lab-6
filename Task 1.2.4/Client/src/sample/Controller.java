package sample;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label lbTime;
    @FXML
    private Label lbConnection;
    @FXML
    private Label lbCity;
    @FXML
    private TextArea taAphorism;
    @FXML
    private Button btSynchTime;
    @FXML
    private Button btWeatherRequest;
    @FXML
    private Button btConnection;
    @FXML
    private TextField tfIP;

    @FXML
    private ImageView ivWeather;

    @FXML
    private Pane pane;

    @FXML
    private TextField tfWeather;

    static private boolean zTime=true;
    static private boolean sTime=false;
    private Client client;
    private String id="id0";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lbTime.setStyle("-fx-text-fill: #FFEC73;");
        lbCity.setStyle("-fx-text-fill: #FFEC73;");
        lbTime.setFont(new Font("Agency FB", 30));
        zeroTime zeroTime = new zeroTime();
        zeroTime.start();
        weatherRequest();
        BackgroundFill bf = new BackgroundFill(Color.BLACK, new CornerRadii(0), null);
        pane.setBackground(new Background(bf));


    }

    //weather display on request
    public void weatherRequest(){
        try {
            URL img = new URL("http://wttr.in/"+tfWeather.getText()+"_0pq.png");
            Image image = new Image(String.valueOf(img));
            ivWeather.setImage(image);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(){
        Client client = new Client(tfIP.getText(), 10753);
        client.start();
        lbConnection.setText(client.sentMessage(id));
    }

    //displaying a random aphorism from server
    public void getAphorism(){
        client = new Client(tfIP.getText(), 10753);
        client.start();
        String temp = client.getAphorism();
        if (temp == "Server not found")
            lbConnection.setText(temp);
        else
            lbConnection.setText("the connection to the server is established");
            taAphorism.setText(temp);
    }

    //displaying synchronized time
    public void synchronizeTime(){

        if(sTime){
            sTime=false;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sTime=true;
        client = new Client(tfIP.getText(), 10753);
        client.start();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-H-m-s-S");
        Date synchdate = null;
        String temp = client.getDate();
        if (temp == "Server not found") {
            lbConnection.setText(temp);
            return;
        }
        lbConnection.setText("the connection to the server is established");
        zTime=false;
        try {
            synchdate = sdf.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(synchdate);
        final int[] seconds = {cal.get(Calendar.SECOND)};
        new Thread(()->{
            while (sTime){
                Platform.runLater(() -> lbTime.setText(dateFormat.format(cal.getTime())));
                seconds[0]++;
                if (seconds[0] == 61)
                    seconds[0]=1;
                cal.set(Calendar.SECOND, seconds[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //displaying time that is not synchronized
    class zeroTime extends Thread {
        @Override
        public void run() {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            int seconds = 0;
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            while (zTime){
                cal.set(Calendar.SECOND, seconds);
                Platform.runLater(() -> lbTime.setText(dateFormat.format(cal.getTime())));
                seconds++;
                if (seconds == 61)
                    seconds = 1;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}