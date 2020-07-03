package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField tfTime;
    @FXML
    private TextField tfDate;
    @FXML
    private Button btnGet;
    @FXML
    private Button btnStop;

    private Socket server;
    BufferedReader in=null;
    PrintWriter out=null;
    private String streamTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        streamTime=new String("go");
        tfTime.setText("HH:MM:SS");
        tfDate.setText("dd.mm.yyyy");
    }

    @FXML
    private void getTimeDate() throws IOException {

        try{
            server = new Socket("127.0.0.1", 2345);
        }catch (UnknownHostException e){
            System.out.println("Unknown host");
            System.exit(-1);}
        catch (NoRouteToHostException e){
            System.out.println("No connection");
            System.exit(-1);}
        catch (ConnectException e){
            System.out.println("Connection error");
            System.exit(-1); }
        catch (IOException e){
            System.out.println("Output-input error");
            System.exit(-1); }

        try {
            in=new BufferedReader(new InputStreamReader(server.getInputStream()));
            out=new PrintWriter(server.getOutputStream(),true);

        }catch (IOException e){
            System.out.println("Error creating threads");
            System.exit(-1); }

        new Thread(()->{
            String fserver;
            String time=new String("HH:mm:ss");
            String date=new String("dd.MM.yyyy");

            while(streamTime.equals("go")) {
                try {
                    out.println(streamTime);

                    fserver = in.readLine();
                    time = fserver.substring(0, 8);
                    date = fserver.substring(8);
                    System.out.println(time);
                    System.out.println(date);
                }catch (IOException e){
                    System.out.println(e);
                }

                String time1=time;
                String date1=date;
                Platform.runLater(()->setTimeDate(time1,date1));//change data to scene
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace(); }
            }
        }).start();
    }

    private void setTimeDate(String time, String date){ //for set time on scene
        tfTime.setText(time);
        tfDate.setText(date);
    }

    private void setStreamTime(String world){ //for stop of time
        this.streamTime=world;
    }

    @FXML
    private void stop() throws IOException {

        setStreamTime("stop");
        tfTime.setText("");
        tfDate.setText("");
        out.close();
        in.close();
        server.close();
    }
}
